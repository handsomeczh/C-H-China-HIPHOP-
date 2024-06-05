<template>
  <el-main>
    <ul v-infinite-scroll="load" class="infinite-list" style="overflow: auto">
      <li v-for="song in songs" :key="song.id" class="infinite-list-item">
        <el-row :gutter="20" align="middle" class="song-row">
          <el-col :span="2" class="image-col">
            <el-image :src="song.songImagePath || defaultImage" fit="cover" class="song-image"></el-image>
          </el-col>
          <el-col :span="16" class="info-col">
            <div class="song-info">
              <div class="song-name">{{ song.songName }}</div>
              <div class="song-artist">{{ song.artist }}</div>
            </div>
          </el-col>
          <el-col :span="4" class="button-col">
            <el-button type="primary" @click="playSong(song)">播放</el-button>
          </el-col>
        </el-row>
      </li>
    </ul>
  </el-main>
</template>

<script setup>
import { ref,watch } from 'vue'
import { store } from '@/js/store.js'

// 获取搜索得到的数据,并监听
const songs = ref([]) // 初始化为一个空数组
watch(() => store.data, (newData) => {
  songs.value = newData
  console.log('Playlist接收到的数据', songs.value)
})

const defaultImage = 'https://via.placeholder.com/60'

// 同步数据
const playSong = (song) => {
  console.log('播放歌曲', song)
  store.song = song;
}

const load = () => {
  // 在这里可以添加加载更多歌曲的逻辑
}
</script>

<style>
.el-header {
  background-color: #b3c0d1;
  color: #333;
  text-align: center;
  line-height: 60px;
}

.el-main {
  padding: 20px;
}

.infinite-list {
  height: 450px;
  padding: 0;
  margin: 0;
  list-style: none;
}

.infinite-list .infinite-list-item {
  display: flex;
  align-items: center;
  height: 80px;
  background: var(--el-color-primary-light-9);
  margin: 10px 0;
  color: var(--el-color-primary);
  padding: 10px;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.song-row {
  width: 100%;
  display: flex;
  align-items: center;
}

.image-col {
  display: flex;
  align-items: center;
}

.song-image {
  width: 60px;
  height: 60px;
}

.info-col {
  display: flex;
  flex-direction: column;
  justify-content: center;
  text-align: center;
}

.song-name {
  font-weight: bold;
}

.song-artist {
  color: #666;
}

.button-col {
  display: flex;
  justify-content: flex-end;
  align-items: center;
}
</style>
