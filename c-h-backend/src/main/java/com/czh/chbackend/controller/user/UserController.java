package com.czh.chbackend.controller.user;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.czh.chbackend.aliyun.SendSmsUtils;
import com.czh.chbackend.common.PageRequest;
import com.czh.chbackend.common.PageResult;
import com.czh.chbackend.common.Result;
import com.czh.chbackend.controller.music.PlaylistController;
import com.czh.chbackend.mapper.FollowMapper;
import com.czh.chbackend.model.dto.user.UserLoginRequest;
import com.czh.chbackend.model.dto.user.UserRegisterRequest;
import com.czh.chbackend.model.dto.user.UserUpdateRequest;
import com.czh.chbackend.model.entity.Follow;
import com.czh.chbackend.model.entity.User;
import com.czh.chbackend.model.vo.FanOrFollowVo;
import com.czh.chbackend.service.IFollowService;
import com.czh.chbackend.service.IUserService;
import com.czh.chbackend.utils.JwtUtil;
import com.czh.chbackend.utils.PasswordUtil;
import com.czh.chbackend.utils.UserContext;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.czh.chbackend.common.ErrorCode.*;
import static com.czh.chbackend.common.CommonConstant.*;

/**
 * 用户
 *
 * @author Lenovo
 */
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*") // 解决跨域问题
@Slf4j
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private FollowMapper followMapper;

    @Autowired
    private IFollowService followService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Resource
    private PlaylistController playlistController;

    /**
     * 注册用户:获取验证码
     */
    @GetMapping("/getCode/{phone}")
    public Result getCode(@PathVariable(value = "phone") String phone) {
        if (phone == null) {
            return Result.error(PARAMS_ERROR, "电话号码不能为空");
        }
        // 正则表达式，用于匹配中国大陆手机号码
        if (!PHONE_NUMBER_PATTERN.matcher(phone).matches()) {
            return Result.error(PARAMS_ERROR, "电话号码不正确");
        }
        // 判断手机号是否被注册
        if (userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUserIphone, phone)) != null) {
            return Result.error(OPERATION_ERROR, "电话已被注册");
        }
        // 发送短信验证码
        String code;
        try {
            code = SendSmsUtils.sendMessage(phone);
            // 保存验证码到redis
            redisTemplate.opsForValue().set(phone, code, 2L, TimeUnit.MINUTES);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // todo 上线后，不需要向前端传递code
        return Result.success(code);
    }

    /**
     * 注册用户：验证验注册
     */
    @PostMapping("/register")
    public Result register(@RequestBody UserRegisterRequest newUser) {
        // 接收短信验证信息
        String newPhone = newUser.getUserIphone();
        String newCode = newUser.getCode();
        if (newUser == null || newPhone == null || newCode == null) {
            return Result.error(PARAMS_ERROR, "缺少手机号或者验证码");
        }
        // 判断用户是否存在
        if (userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUserIphone, newPhone)) != null) {
            return Result.error(OPERATION_ERROR, "电话已被注册");
        }
        // 从Redis中获取验证码
        String code = redisTemplate.opsForValue().get(newPhone);
        if (code == null) {
            return Result.error(PARAMS_ERROR, "手机号码不正确或未注册");
        }
        // 判断验证码
        if (!newCode.equals(code)) {
            return Result.error(PARAMS_ERROR, "验证码错误");
        }
        //新建用户
        User user = new User();
        user.setUserIphone(newPhone);
        boolean save = userService.save(user);
        if (!save) {
            return Result.error(OPERATION_ERROR, "创建失败");
        }
        // 删除验证码缓存
        redisTemplate.delete(newPhone);
        // 创建收藏歌单+下载歌单
        playlistController.createInitPlaylist(user.getId());
        return Result.success();
    }

    /**
     * 用户登录:密码登录
     */
    @GetMapping("/login")
    public Result<String> login(@RequestBody UserLoginRequest loginUser) {
        String userPassword = loginUser.getUserPassword();
        String userIphone = loginUser.getUserIphone();
        if (loginUser == null || userPassword == null || userIphone == null) {
            return Result.error(PARAMS_ERROR, "电话或密码不能为空");
        }
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUserIphone, userIphone));
        if (user == null) {
            return Result.error(NOT_REGISTER_ERROR, "用户未注册");
        }

        String password = user.getUserPassword();
        if (!PasswordUtil.verifyWithoutSalt(userPassword, password)) {
            return Result.error(PASSWORD_ERROR, "密码错误");
        }
        // 将用户存入线程中
        Long userId = user.getId();
        String jwt = JwtUtil.generateToken(userId, user.getUserIphone());
        return Result.success(jwt);
    }

    /**
     * 用户登录：获取验证码
     */
    @GetMapping("/login/{phone}")
    public Result<String> getLoginCode(@PathVariable String phone) {
        if (phone == null) {
            return Result.error(PARAMS_ERROR, "电话号码不能为空");
        }
        // 正则表达式，用于匹配中国大陆手机号码
        if (!PHONE_NUMBER_PATTERN.matcher(phone).matches()) {
            return Result.error(PARAMS_ERROR, "电话号码不正确");
        }
        // 判断手机号是否被注册
        if (userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUserIphone, phone)) == null) {
            return Result.error(NOT_REGISTER_ERROR, "未注册");
        }
        // 发送短信验证码
        String code;
        try {
            code = SendSmsUtils.sendMessage(phone);
            // 保存验证码到redis
            redisTemplate.opsForValue().set(phone, code, 200L, TimeUnit.MINUTES);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // todo 上线后，不需要向前端传递code
        return Result.success(code);
    }

    /**
     * 用户登录：验证码登录
     */
    @PostMapping("/loginByCode")
    public Result loginByCode(@RequestBody UserLoginRequest loginUser) {
        String loginCode = loginUser.getCode();
        String loginPhone = loginUser.getUserIphone();
        if (loginCode == null || loginPhone == null) {
            return Result.error(PARAMS_ERROR, "电话或密码不能为空");
        }
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUserIphone, loginPhone));
        if (user == null) {
            return Result.error(NOT_REGISTER_ERROR, "用户未注册");
        }
        // 获取验证码
        String code = redisTemplate.opsForValue().get(loginPhone);
        if (code == null) {
            return Result.error(PARAMS_ERROR, "手机号码不正确或未注册");
        }
        // 判断验证码
        if (!loginCode.equals(code)) {
            return Result.error(PARAMS_ERROR, "验证码错误");
        }
        // 将用户存入线程中
        Long userId = user.getId();
        String jwt = JwtUtil.generateToken(userId, loginPhone);
        // 删除验证码缓存
        redisTemplate.delete(loginPhone);
        return Result.success(jwt);
    }

    /**
     * 用户信息修改
     */
    @PutMapping("/update")
    public Result updateUser(@RequestBody UserUpdateRequest updateUser) {
        if (updateUser == null) {
            throw new RuntimeException(PARAMS_ERROR.getMessage());
        }
        User user = new User();
        BeanUtils.copyProperties(updateUser, user);
        // 如果存在密码对密码进行加密
        String password = user.getUserPassword();
        if (password != null) {
            String pw = PasswordUtil.encryptWithoutSalt(password);
            user.setUserPassword(pw);
        }
        boolean result = userService.updateById(user);
        if (result) {
            throw new RuntimeException(OPERATION_ERROR.getMessage());
        }
        return Result.success();
    }

    /**
     * 获取用户关注列表
     */
    @GetMapping("/follow")
    public Result<PageResult> getFollow(@RequestBody PageRequest pageRequest) {
        Long userId = UserContext.getCurrentId();
        // key
        String key = REDIS_FOLLOW_KEY + userId;
        HashOperations<String, Object, Object> forHash = redisTemplate.opsForHash();
        // 从Redis中获取数据
        List<FanOrFollowVo> list = forHash.values(key).stream().map(obj -> JSONUtil.toBean((String) obj, FanOrFollowVo.class)).collect(Collectors.toList());
        if (list != null && !list.isEmpty()) {
            return Result.success(new PageResult(list.size(), list));
        }
        // 缓存不存在从数据库获取
        PageResult pageResult = followService.getFollow(pageRequest, userId);
        List<Follow> records = pageResult.getRecords();
        // 数据处理
        List<FanOrFollowVo> follows = records.stream().map(entity -> {
                    FanOrFollowVo vo = new FanOrFollowVo();
                    vo.setId(entity.getFollowId());
                    vo.setUserName(entity.getFollowName());
                    vo.setUserAvatar(entity.getFollowAvatar());
                    // 存入Redis
                    forHash.put(key, String.valueOf(entity.getFollowId()), JSONUtil.toJsonStr(vo));
                    return vo;
                })
                .collect(Collectors.toList());

        // 设置Redis键的过期时间为2小时
        redisTemplate.expire(key, 2, TimeUnit.HOURS);
        pageResult.setRecords(follows);
        return Result.success(pageResult);
    }

    /**
     * 关注用户
     */
    @PostMapping("/follow/{followId}")
    public Result follow(@PathVariable Long followId) {
        if (followId == null) {
            throw new RuntimeException(PARAMS_ERROR.getMessage());
        }
        // 判断用户是否存在
        User other = userService.getById(followId);
        if (other == null) {
            return Result.error(PARAMS_ERROR, "用户不存在");
        }
        // 判断是否已经关注
        if (followMapper.exists(new LambdaQueryWrapper<Follow>().eq(Follow::getFollowId, other.getId()))) {
            return Result.error(OPERATION_ERROR, "用户已关注");
        }
        // 获取当前用户
        Long userId = UserContext.getCurrentId();
        User fan = userService.getById(userId);
        // 添加用户关联表
        Follow follow = new Follow(fan, other);
        int insert = followMapper.insert(follow);
        if (insert != 1) {
            throw new RuntimeException(OPERATION_ERROR.getMessage());
        }
        // key
        String key = REDIS_FOLLOW_KEY + userId;
        redisTemplate.delete(key);
        return Result.success();
    }

    /**
     * 取消关注
     */
    @DeleteMapping("/cancelFollow/{followId}")
    public Result cancelFollow(@PathVariable Long followId) {
        if (followId == null) {
            throw new RuntimeException(PARAMS_ERROR.getMessage());
        }
        // 获取当前用户id
        Long fanId = UserContext.getCurrentId();
        // 删除
        int delete = followMapper.delete(new LambdaQueryWrapper<Follow>()
                .eq(Follow::getFanId, fanId)
                .eq(Follow::getFollowId, followId));
        // key
        String key = REDIS_FOLLOW_KEY + fanId;
        redisTemplate.delete(key);
        return Result.success();
    }

    /**
     * 获取用户粉丝列表
     */
    @GetMapping("/fan")
    public Result<PageResult> getFan(@RequestBody PageRequest pageRequest) {
        Long userId = UserContext.getCurrentId();
        // key
        String key = REDIS_FAN_KEY + userId;
        HashOperations<String, Object, Object> forHash = redisTemplate.opsForHash();
        // 从Redis中获取数据
        List<FanOrFollowVo> list = forHash.values(key).stream().map(obj -> JSONUtil.toBean((String) obj, FanOrFollowVo.class)).collect(Collectors.toList());
        if (list != null && !list.isEmpty()) {
            return Result.success(new PageResult(list.size(), list));
        }
        // 从数据库获取
        PageResult pageResult = followService.getFan(pageRequest, userId);
        List<Follow> records = pageResult.getRecords();
        // 数据处理
        List<FanOrFollowVo> fans = records.stream().map(entity -> {
                    FanOrFollowVo vo = new FanOrFollowVo();
                    vo.setId(entity.getFanId());
                    vo.setUserName(entity.getFanName());
                    vo.setUserAvatar(entity.getFanAvatar());
                    // 存入Redis
                    forHash.put(key, String.valueOf(entity.getFanId()), JSONUtil.toJsonStr(vo));
                    return vo;
                })
                .collect(Collectors.toList());

        // 设置Redis键的过期时间为2小时
        redisTemplate.expire(key, 2, TimeUnit.HOURS);
        pageResult.setRecords(fans);
        return Result.success(pageResult);
    }

    /**
     * 删除粉丝
     */
    @DeleteMapping("/cancelFan/{fanId}")
    public Result cancelFan(@PathVariable Long fanId) {
        if (fanId == null) {
            throw new RuntimeException(PARAMS_ERROR.getMessage());
        }
        // 获取当前用户id
        Long followId = UserContext.getCurrentId();
        // 删除
        int delete = followMapper.delete(new LambdaQueryWrapper<Follow>()
                .eq(Follow::getFanId, fanId)
                .eq(Follow::getFollowId, followId));
        // key
        String key = REDIS_FOLLOW_KEY + followId;
        redisTemplate.delete(key);
        return Result.success();
    }


}






























