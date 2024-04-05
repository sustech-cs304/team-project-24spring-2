<script>
import { ref } from 'vue';
import { Message } from '@arco-design/web-vue';
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
  name: "LeftNavigate",
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
    return {
      onCollapse(val, type) {
        const content = type === 'responsive' ? '触发响应式收缩' : '点击触发收缩';
        Message.info({
          content,
          duration: 2000,
        });
      }
    };
  }
};
</script>

<template>
  <div class="menu-demo">
    <a-menu
        :style="{ width: '180px', height: '100%' }"
        :default-selected-keys="['0']"
        show-collapse-button
        breakpoint="xl"
        @collapse="onCollapse"
    >
      <a-menu-item key="0">
        <template #icon><icon-home/></template>
        Home
      </a-menu-item>
      <a-sub-menu key="1">
        <template #icon><icon-sort/></template>
        <template #title>Classification</template>
        <a-menu-item key="1_0">Menu 1</a-menu-item>
        <a-menu-item key="1_1">Menu 2</a-menu-item>
        <a-menu-item key="1_2">Menu 3</a-menu-item>
      </a-sub-menu>
      <a-sub-menu key="2">
        <template #icon><icon-settings></icon-settings></template>
        <template #title>Settings</template>
        <a-menu-item key="2_0">
          <template #icon><icon-user /></template>
          User
        </a-menu-item>
        <a-menu-item key="2_0">
          <template #icon><icon-export /></template>
          Logout
        </a-menu-item>
        <a-menu-item key="2_1">
          <template #icon><icon-question-circle /></template>
          Help
        </a-menu-item>
      </a-sub-menu>
    </a-menu>
    <div class="white-box">
      <router-view/>
    </div>
  </div>
</template>

<style scoped>
.menu-demo {
  display: flex; /* 添加Flexbox布局 */
  box-sizing: border-box;
  height: calc(100% - 52px);
  padding: 0;
  background-color: var(--color-neutral-2);
}
.white-box {
  border: 1px solid #e1e1e1; /* 淡色边框比纯白色更可见 */
  background-color: #FFFFFF;
  flex-grow: 1;
  margin: 0.1%;
  padding: 20px; /* 添加一些内边距 */
  //border-radius: 10px; /* 圆角边框 */
  box-shadow: 0 2px 4px rgba(0,0,0,0.1); /* 轻微的阴影效果 */
  transition: box-shadow 0.3s ease; /* 鼠标悬停时阴影效果的平滑过渡 */
}

.white-box:hover {
  box-shadow: 0 5px 15px rgba(0,0,0,0.2); /* 鼠标悬停时增加阴影 */
}

</style>