<template>
  <div class="container">
    <a-card class="card">
      <div id="vditor" class="markdown-form" />
      <div id="vditor2" class="image-form" />
    </a-card>
  </div>
</template>

<script lang="ts" setup>
  // 1.1 引入Vditor 构造函数
  import Vditor from 'vditor';
  import 'vditor/dist/index.css';

  import { ref, onMounted } from 'vue';

  // 2. 获取DOM引用
  const vditor = ref();

  // 3. 在组件初始化时，就创建Vditor对象，并引用
  onMounted(() => {
    vditor.value = new Vditor('vditor', {
      height: '100%',
      width: '70%',
      fullscreen: {
        index: 100
      },
    //   toolbar: ['emoji', 'br', 'bold', '|', 'line', 'quote', 'list', 'check'],
    });
  });
</script>

<style scoped lang="less">
  .container {
    padding: 0 20px 40px 20px;
    overflow: hidden;
    align-items: center;
  }

  .card {
    border-radius: 8px;
    width: 100%;
    margin: auto;
    max-width: 1500px;
    overflow: hidden;
    padding-top: 20px;
    padding-bottom: 20px;
    background-color: white;
  }

  .markdown-form {
    float: left;
    margin: auto;
    min-height: 600px;
  }

  .image-form {
    float: right;
    margin: auto;
    min-height: 500px;
  }
</style>
