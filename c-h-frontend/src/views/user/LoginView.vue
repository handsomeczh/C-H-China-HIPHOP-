<template>
  <div class="container">
    <div class="phone-input">
      <el-input
        v-model="phone"
        placeholder="输入电话号码"
        clearable
      />
      <el-button type="success" style="margin-left: 2px" @click="getCode">获取验证码</el-button>
    </div>
    <div class="code-input">
      <el-input
        v-model="code"
        placeholder="输入验证码"
        clearable
      />
      <el-button type="success" style="margin-left: 60px" :icon="Check" circle @click="login" />
    </div>
    <el-button type="primary" style="margin-top: 60px" @click="goToRegis">注册</el-button>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Check } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessageBox } from 'element-plus'

const router = useRouter()

const phone = ref('18520282569')
const code = ref('')
const rightCode = ref('')
// 注册按钮点击事件
const goToRegis = () => {
  router.push({ name: 'regis' })
}
// 获取验证码按钮点击事件
const getCode = async () => {
  try {
    const response = await axios.get(`http://localhost:8101/api/user/login/${phone.value}`)
    rightCode.value = response.data
  } catch (error) {
    await ElMessageBox.confirm('获取验证码失败', '错误', {
      type: 'error' // 可选的类型：success、warning、info、error
    })
  }
}

// 登录按钮点击事件
const login = async () => {
  if (code.value === rightCode.value.data) {
    try {
      const response = await axios.post(`http://localhost:8101/api/user/loginByCode`, {
          code: code.value,
          userIphone: phone.value
        }
      )
      // todo 接收jwt令牌
      console.log('LoginView从后端接收数据', response.data)
      // 跳转MusicView
      router.push({name:'music'})
    } catch (error) {
      await ElMessageBox.confirm('登录失败', '错误', {
        type: 'error' // 可选的类型：success、warning、info、error
      })
    }
  } else {
    await ElMessageBox.confirm('请输入正确的验证码', '错误', {
      type: 'error' // 可选的类型：success、warning、info、error
    })
  }
}


</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh; /* Adjust as needed */
  background: url("https://gw.alipayobjects.com/zos/rmsportal/FfdJeJRQWjEeGTpqgBKj.png") 0 0 / 100% 100%;
}

.phone-input {
  display: flex;
  align-items: center;
  margin-bottom: 20px; /* Adjust spacing */
}

.code-input {
  display: flex;
  align-items: center;
}
</style>
