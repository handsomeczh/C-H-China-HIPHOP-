<!-- src/components/music/MusicPlayer.vue -->
<template>
  <n-card title="Music Player">
    <n-input v-model:value="inputMusicId" placeholder="Enter music ID" clearable @keyup.enter="fetchMusicData" />
    <n-button @click="fetchMusicData" type="primary" style="margin-top: 10px;">加载音乐</n-button>
    <audio ref="audioPlayer" controls :src="musicUrl"></audio>
    <div id="lyrics"></div>
  </n-card>
</template>

<script setup>
import { ref } from 'vue';
import { useMessage } from 'naive-ui';
import axios from 'axios';

// 引入Naive UI组件
import { NInput, NButton, NCard } from 'naive-ui';

// 定义响应式变量
const musicUrl = ref('');
const wordUrl = ref('');
const audioPlayer = ref(null);
const inputMusicId = ref('');  // 用于输入的musicId

const message = useMessage();

// 获取音乐和歌词URL数据
const fetchMusicData = async () => {
  if (!inputMusicId.value) {
    message.error('请输入音乐ID');
    return;
  }

  try {
    const response = await axios.get(`http://localhost:8101/api/music/url/${inputMusicId.value}`);
    const data = response.data.data;
    console.log("后端响应的数据", data);
    musicUrl.value = data.musicUrl;
    wordUrl.value = data.wordUrl || ''; // 如果没有歌词文件，则将 wordUrl 设为空

    // 清空歌词显示区域
    clearLyrics();

    if (wordUrl.value) {
      await fetchLyrics(wordUrl.value);
    } else {
      displayNoLyricsMessage();
    }

    // 清空输入框
    inputMusicId.value = '';

    audioPlayer.value.oncanplaythrough = () => {
      audioPlayer.value.play().catch(error => {
        console.error('Error playing the audio:', error);
      });
    };

    audioPlayer.value.load();
  } catch (error) {
    message.error('获取音乐数据时出错：' + error.message);
  }
};

// 获取并解析歌词文件
const fetchLyrics = async (url) => {
  try {
    const response = await axios.get(url, { responseType: 'text' });
    const lyricsText = response.data;
    const lyrics = parseLyrics(lyricsText);

    displayLyrics(audioPlayer.value, lyrics);
  } catch (error) {
    message.error('获取歌词文件时出错：' + error.message);
  }
};

// 清空歌词显示区域
const clearLyrics = () => {
  const lyricsDiv = document.getElementById('lyrics');
  lyricsDiv.innerHTML = '';
};

// 显示“暂无歌词”消息
const displayNoLyricsMessage = () => {
  const lyricsDiv = document.getElementById('lyrics');
  lyricsDiv.innerHTML = '<div class="no-lyrics">暂无歌词</div>';
};

// 解析歌词文件
const parseLyrics = (lyricsText) => {
  const lines = lyricsText.split('\n');
  const lyrics = [];
  const timeReg = /\[(\d{2}):(\d{2})\.(\d{2})\]/;

  for (let line of lines) {
    const match = line.match(timeReg);
    if (match) {
      const minutes = parseInt(match[1], 10);
      const seconds = parseInt(match[2], 10);
      const milliseconds = parseInt(match[3], 10);
      const time = minutes * 60 + seconds + milliseconds / 100;
      const text = line.replace(timeReg, '').trim();
      lyrics.push({ time, text });
    }
  }
  return lyrics;
};

// 显示歌词
const displayLyrics = (audioPlayer, lyrics) => {
  const lyricsDiv = document.getElementById('lyrics');
  lyricsDiv.innerHTML = '';

  lyrics.forEach((lyric, index) => {
    const lyricLine = document.createElement('div');
    lyricLine.className = 'lyric-line';
    lyricLine.id = 'lyric-' + index;
    lyricLine.innerText = lyric.text;
    lyricsDiv.appendChild(lyricLine);
  });

  let currentIndex = 0;

  const updateLyrics = () => {
    const currentTime = audioPlayer.currentTime;
    if (currentIndex < lyrics.length && currentTime >= lyrics[currentIndex].time) {
      if (currentIndex > 0) {
        document.getElementById('lyric-' + (currentIndex - 1)).classList.remove('active');
      }
      const currentLyric = document.getElementById('lyric-' + currentIndex);
      currentLyric.classList.add('active');
      currentLyric.scrollIntoView({ behavior: 'smooth', block: 'center' });
      currentIndex++;
    }
    requestAnimationFrame(updateLyrics);
  };

  updateLyrics();
};
</script>

<style>
#lyrics {
  white-space: pre-wrap;
  font-family: monospace;
  margin-top: 20px;
  max-height: 200px;
  overflow-y: auto;
  position: relative;
}
.lyric-line {
  transition: color 0.3s;
}
.active {
  color: red;
  font-weight: bold;
}
.no-lyrics {
  color: gray;
  font-style: italic;
}
</style>
