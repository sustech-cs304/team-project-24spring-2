<script>
import {
  IconMenuFold,
  IconMenuUnfold,
  IconApps,
  IconBug,
  IconBulb,
  IconSettings,
  IconUser,
  IconQuestionCircle,
  IconHome,
  IconExport,
  IconSort,
} from '@arco-design/web-vue/es/icon';
import { useRoute, useRouter } from 'vue-router';
import utils from '../api/utils.ts';

export default {
  name: "TopNav",
  components: {
    IconMenuFold,
    IconMenuUnfold,
    IconApps,
    IconBug,
    IconBulb,
    IconSettings,
    IconUser,
    IconQuestionCircle,
    IconHome,
    IconExport,
    IconSort,
  },
  setup() {
    const router = useRouter();

    const navigate = (path) => {
      router.push(path);
    }

    function verifyLoginState(post) {
      return utils.verifyLoginState(post);
    }

    function logout() {
      utils.logout();
      router.push('/').then(() => {
        window.location.reload();
      });
    }

    return { navigate, verifyLoginState, logout };
  }
}
</script>

<template>
  <div class="top-navbar">
    <div class="brand" @click="navigate('/')">
      <img src="@/assets/LOGO.png" alt="Logo" class="logo-image">
      <span class="logo">Campus Events and Entertainment Center</span>
    </div>
    <div class="navigation-icons">
      <a href="#" class="nav-item" @click="navigate('/')">
        <icon-home />主页 
      </a>
      <a href="#" class="nav-item" @click="navigate('/events')">
        <icon-apps />更多 
      </a>
      <a v-if="verifyLoginState(false)" href="#" class="nav-item" @click="navigate('/userinfo')">
        <icon-user />个人信息 
      </a>
      <a v-if="verifyLoginState(false)" href="#" class="nav-item" @click="logout()">
        <icon-export />登出
      </a>
      <a v-else href="#" class="nav-item" @click="navigate('/login')">
        <icon-user />登录
      </a>
    </div>
  </div>
</template>

<style scoped>
.top-navbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 20px;
  height: 60px;
  background-color: "--color-background";
  color: "--color-heading"; /* 设置文字颜色 */
}

.brand {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.logo-image {
  margin-right: 15px;
  height: auto;
  width: 200px;
}

.logo {
  font-family: 'Roboto', sans-serif;
  font-weight: bold;
}

.navigation-icons {
  display: flex;
  gap: 20px; /* 调整每个导航项之间的间距 */
}

.nav-item {
  display: inline-flex;
  align-items: center;
  gap: 5px; /* 调整图标和文字之间的间距 */
  cursor: pointer; /* 鼠标悬停时显示手形光标 */
  text-decoration: none; /* 移除链接的下划线 */
  color: inherit; /* 继承父元素的颜色 */
}

.nav-item:hover {
  color: #007bff; /* 可选：设置悬停时的文本颜色 */
}
</style>
