import { reactive } from 'vue'
import axios from 'axios'

export const store = reactive({
  data: [],
  async search(songName, artist) {
    try {
      const response = await axios.get('http://localhost:8101/api/music/music/search', {
        params: {
          songName: songName.value,
          artist: artist.value
        }
      })
      this.data = response.data.data.records
      console.log('后端返回数据', this.data)
    } catch (error) {
      console.error('搜索音乐数据时出错：', error)
    }
  },
  song: []
})