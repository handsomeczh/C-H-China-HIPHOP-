<template>
  <div class="common-layout">
    <el-container>
      <el-container>
        <el-aside width="200px">Aside</el-aside>
        <el-container>
          <el-header class="header">
            <SearchMusic />
          </el-header>
          <el-main class="main">
            <Playlist />
          </el-main>
        </el-container>
      </el-container>
      <el-footer class="footer"><MusicPlayer/></el-footer>
    </el-container>
  </div>
</template>

<script setup>
import SearchMusic from '@/components/music/SearchMusic.vue'
import Playlist from '@/components/music/Playlist.vue'
import MusicPlayer from '@/components/music/MusicPlayer.vue'
</script>

<style scoped>
.common-layout {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.el-container {
  flex: 1;
  display: flex;
}

.el-aside {
  background-color: #f2f2f2;
}

.header {
  height: 20%;
  background-color: #f8f8f8;
  display: flex;
  align-items: center;
  justify-content: center;
}

.main {
  height: 60%;
  overflow: auto;
  background: url("https://gw.alipayobjects.com/zos/rmsportal/FfdJeJRQWjEeGTpqgBKj.png") 0 0 / 100% 100%;
}

.footer {
  height: 20%;
  background-color: #f8f8f8;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
