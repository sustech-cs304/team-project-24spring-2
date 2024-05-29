<template>
  <!-- 这里需要注意一下，官网上给的引入方式需要改一下，应该是没有更新导致的；还有一点就是modelValue需要添加括号，否则找不到这个值 -->
  <VueMarkdownEditor
    v-model="model"
    mode="preview"
  ></VueMarkdownEditor>
</template>

<script setup>
  import VueMarkdownEditor from '@kangc/v-md-editor';
  import '@kangc/v-md-editor/lib/style/base-editor.css';
  import '@kangc/v-md-editor/lib/theme/style/vuepress.css';
  import vuepressTheme from '@kangc/v-md-editor/lib/theme/vuepress';

  import Prism from 'prismjs';

  VueMarkdownEditor.use(vuepressTheme, {
    Prism,
  });

  const model = defineModel({
    type: String,
    default: '',
  });
</script>

<style></style>
