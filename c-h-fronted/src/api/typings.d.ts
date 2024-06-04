declare namespace API {
  type cancelFanUsingDELETEParams = {
    /** fanId */
    fanId: number;
  };

  type cancelFollowUsingDELETEParams = {
    /** followId */
    followId: number;
  };

  type CommentAddRequest = {
    content?: string;
    songId?: number;
  };

  type CommentPageRequest = {
    current?: number;
    isNew?: boolean;
    pageSize?: number;
    songId?: number;
    sortField?: string;
    sortOrder?: string;
  };

  type deleteCommentUsingDELETEParams = {
    /** id */
    id: number;
  };

  type deleteUsingDELETE1Params = {
    /** id */
    id: number;
  };

  type deleteUsingDELETEParams = {
    /** id */
    id: number;
  };

  type downloadedUsingGETParams = {
    /** path */
    path?: string;
  };

  type DownloadMusicVo = {
    artist?: string;
    path?: string;
    playlistId?: number;
    songImage?: string;
    songName?: string;
  };

  type downloadUsingPOSTParams = {
    /** id */
    id: number;
  };

  type followUsingPOSTParams = {
    /** followId */
    followId: number;
  };

  type getCodeUsingGETParams = {
    /** phone */
    phone: string;
  };

  type getLoginCodeUsingGETParams = {
    /** phone */
    phone: string;
  };

  type getUrlUsingGETParams = {
    /** id */
    id: number;
  };

  type likeCommentUsingPOSTParams = {
    /** commentId */
    commentId: number;
  };

  type MusicSearchRequest = {
    /** 歌手 */
    artist?: string;
    current?: number;
    pageSize?: number;
    /** 歌曲名 */
    songName?: string;
    sortField?: string;
    sortOrder?: string;
  };

  type MusicUploadRequest = {
    /** 专辑名称 */
    album?: string;
    /** 歌手 */
    artist?: string;
    /** 音乐类型 */
    genre?: string;
    /** 语种 */
    language?: string;
    /** 歌词文件在OSS存储系统中的路径 */
    lyricsFilePath?: string;
    /** 歌词文件大小，字节为单位 */
    lyricsFileSize?: number;
    /** MP3文件在OSS存储系统中的路径 */
    mp3FilePath?: string;
    /** MP3文件大小，字节为单位 */
    mp3FileSize?: number;
    /** 发行时间 */
    releaseDate?: string;
    /** 歌曲图片 */
    songImagePath?: string;
    /** 歌曲名 */
    songName?: string;
  };

  type PageRequest = {
    current?: number;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
  };

  type PageResult = {
    records?: Record<string, any>[];
    total?: number;
  };

  type PlaylistAddRequest = {
    artist?: string;
    playlistId?: number;
    songId?: number;
    songImage?: string;
    songName?: string;
  };

  type PlaylistCancelRequest = {
    playlistId?: number;
    songId?: number;
  };

  type PlaylistCreateRequest = {
    description?: string;
    listImage?: string;
    name?: string;
  };

  type PlaylistSearchRequest = {
    createTime?: string;
    current?: number;
    name?: string;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
  };

  type PlaylistSongGetRequest = {
    current?: number;
    pageSize?: number;
    playlistId?: number;
    sortField?: string;
    sortOrder?: string;
  };

  type PlaylistUpdateRequest = {
    description?: string;
    id?: number;
    listImage?: string;
    name?: string;
  };

  type ReplyAddRequest = {
    commentId?: number;
    content?: string;
  };

  type ReplyPageRequest = {
    commentId?: number;
    current?: number;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
  };

  type Result = {
    code?: number;
    data?: Record<string, any>;
    message?: string;
  };

  type ResultList_ = {
    code?: number;
    data?: Record<string, any>[];
    message?: string;
  };

  type ResultListDownloadMusicVo_ = {
    code?: number;
    data?: DownloadMusicVo[];
    message?: string;
  };

  type ResultPageResult_ = {
    code?: number;
    data?: PageResult;
    message?: string;
  };

  type ResultString_ = {
    code?: number;
    data?: string;
    message?: string;
  };

  type ResultUrlVo_ = {
    code?: number;
    data?: UrlVo;
    message?: string;
  };

  type shareUsingGETParams = {
    /** id */
    id: number;
  };

  type unlikeCommentUsingDELETEParams = {
    /** commentId */
    commentId: number;
  };

  type UrlVo = {
    /** 歌曲id */
    id?: number;
    /** musicUrl */
    musicUrl?: string;
    /** 歌词文件Url */
    wordUrl?: string;
  };

  type User_ = {
    /** 验证码 */
    code?: string;
    /** id */
    id?: number;
    /** 手机号 */
    userIphone?: string;
    /** 密码 */
    userPassword?: string;
  };

  type UserRegisterRequest = {
    /** 验证码 */
    code?: string;
    /** 手机号 */
    userIphone?: string;
  };

  type UserUpdateRequest = {
    /** id */
    id?: number;
    /** 用户地区 */
    userArea?: string;
    /** 用户头像 */
    userAvatar?: string;
    /** 用户生日 */
    userBirthday?: string;
    /** 性别0.其他1.男2.女 */
    userGender?: number;
    /** 手机号 */
    userIphone?: string;
    /** 用户昵称 */
    userName?: string;
    /** 密码 */
    userPassword?: string;
    /** 用户简介 */
    userProfile?: string;
  };
}
