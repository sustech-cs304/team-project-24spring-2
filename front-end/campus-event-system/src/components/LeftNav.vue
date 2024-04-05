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
  </div>
</template>

<style scoped>
.menu-demo {
  box-sizing: border-box;
  width: 100%;
  height: calc(100% - 50px);
  padding: 0;
  background-color: var(--color-neutral-2);
}

</style>