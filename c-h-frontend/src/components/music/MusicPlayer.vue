<template>
  <audio ref="audioPlayer" controls :src="musicUrl"></audio>
</template>

<script setup>
import { ref, watch } from 'vue'
import { store } from '@/js/store.js'
import axios from 'axios'

const song = ref()
// 同步监听数据
watch(() => store.song, (newData) => {
  song.value = newData
  console.log('MusicPlayer接收到的数据', song.value)
  // 监听到数据变化时调用播放
  fetchMusicData()
})

// 获取歌曲的临时url
// 定义响应式变量
const musicUrl = ref('')
const audioPlayer = ref(null)

// 获取音乐和歌词URL数据
const fetchMusicData = async () => {
  if (!song.value.id) {
    console.log('获取音乐数据时出错：')
    return
  }
  try {
    const response = await axios.get(`http://localhost:8101/api/music/url/${song.value.id}`)
    const data = response.data.data
    console.log('后端响应的数据', data)
    musicUrl.value = data.musicUrl

    audioPlayer.value.oncanplaythrough = () => {
      audioPlayer.value.play().catch(error => {
        console.error('Error playing the audio:', error)
      })
    }

    audioPlayer.value.load()
  } catch (error) {
    console.log('获取音乐数据时出错：' + error.message)
  }
}
</script>

<style>

</style>
