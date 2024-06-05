<!-- SearchMusic.vue -->
<template>
  <el-input
    v-model="songName"
    style="width: 240px"
    placeholder="请输入歌曲名"
    clearable
  />
  <el-input
    v-model="artist"
    style="width: 240px"
    placeholder="请输入歌手"
    clearable
  />
  <el-button type="primary" :icon="Search" @click="searchMusic">Search</el-button>
</template>

<script setup>
import { ref } from 'vue'
import { Search } from '@element-plus/icons-vue'
import { store } from '@/js/store.js'

const songName = ref('')
const artist = ref('')

const searchMusic = async () => {
  try {
    await store.search(songName, artist)
  } catch (error) {
    console.error('搜索音乐数据时出错：', error)
  }
}
</script>
