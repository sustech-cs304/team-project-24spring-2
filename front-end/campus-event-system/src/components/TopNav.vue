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
}

</script>

<template>
  <div class="top-navbar">
    <div class="brand">
      <img src="@/assets/LOGO.png" alt="Logo" class="logo-image">
      <span class="logo">Campus Events and Entertainment Center</span>
    </div>


  </div>
</template>

<style scoped>
.top-navbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0px;
  height: 50px;
  background-color: #fff;
  /* 其他样式 */
}

.brand {
  display: flex;
  align-items: center;
}

.logo-image {
  /* 这里可以添加一些CSS来调整图片的大小和间距 */
  margin-top: 5px;
  height: 140px;
}

.logo {
  text-align: center;
  font-family: 'Roboto', sans-serif; /* 更改为Roboto字体 */
  font-weight: bold; /* 加粗 */
  /* 其他样式保持不变 */
}

</style>