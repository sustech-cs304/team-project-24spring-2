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
import { useRoute, useRouter } from 'vue-router';  // Import routing composable

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
    const router = useRouter();  // Use the useRouter composable for navigation

    const navigate = (path) => {
      router.push(path);
    }

    return { navigate };
  }
}
</script>


<template>
  <div class="top-navbar">
    <div class="brand" @click="navigate('/homepage')">
      <img src="@/assets/LOGO.png" alt="Logo" class="logo-image">
      <span class="logo">Campus Events and Entertainment Center</span>
    </div>
    <div class="navigation-icons">
      <icon-home @click="navigate('/homepage')"/>
      <icon-apps @click="navigate('/events')"/>
      <icon-user @click="navigate('/userinfo')"/>
      <icon-export @click="navigate('/logout')"/>
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

.navigation-icons > * {
  margin-right: 20px;
  cursor: pointer;
  vertical-align: middle;
  color: inherit; /* 图标继承父元素的颜色设置 */
}
</style>

