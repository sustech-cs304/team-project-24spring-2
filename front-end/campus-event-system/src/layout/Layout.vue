<script setup>
import {ref} from 'vue';
import TopNav from "@/components/TopNav.vue";
import {Message} from '@arco-design/web-vue';
import {useRouter} from 'vue-router';

const router = useRouter();

function navigateToEvents() {
  router.push({name: 'events'});
}

function navigateToHome() {
  router.push({name: 'homepage'});
}

function navigateToUserInfo() {
  router.push({name: 'userinfo'});
}


</script>

<template>
  <div class="main" id="main_contain">
    <div class="top-nav">
      <TopNav/>
    </div>
    <div class="white-box">
      <router-view/>
    </div>
  </div>
</template>


<style scoped>
.main {
  height: 100vh;
}

.top-nav {
  border-bottom: ghostwhite 2px solid;
}

.menu-demo {
  display: flex; /* 添加Flexbox布局 */
  box-sizing: border-box;
  height: calc(100% - 52px);
  padding: 0;
  background-color: var(--color-neutral-2);
}

.white-box {
  border: 1px solid "--color-border"; /* 淡色边框比纯白色更可见 */
  background-color: "--color-background";
  flex-grow: 1;
  margin: 0.1%;
  padding: 10px; /* 添加一些内边距 */
  height: auto;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 轻微的阴影效果 */
  transition: box-shadow 0.3s ease; /* 鼠标悬停时阴影效果的平滑过渡 */
}

</style>
