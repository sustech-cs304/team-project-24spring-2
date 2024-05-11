<template>
  <div class="container">
    <a-card class="card">
      <div id="vditor" class="markdown-form"></div>

      <div id="image-form" class="image-form">
        <a-card class="image-form-card">
          <template #title >
            {{ $t('eventEdit.imageForm') }}
          </template>
          <img-card
            v-for="(element, index) in images"
            :image="element"
            :key="index"
            :is="element"
            class="img-cards"
          />
          <!-- <template #item="{ element }"> -->
          <!-- <img-card :image="element" class="img-cards" /> -->
          <!-- </template> -->
        </a-card>
      </div>
    </a-card>
  </div>
</template>

<script lang="ts" setup>
  // 1.1 引入Vditor 构造函数
  import Vditor from 'vditor';
  import 'vditor/dist/index.css';
  import imgCard from '@/components/image-card/image-card.vue';
  import draggable from 'vuedraggable'; // 页面引入

  import { ref, onMounted } from 'vue';

  // 2. 获取DOM引用
  const vditor = ref();
  const images = ref([
    {
      name: 'image1',
      url: 'https://www.oscar-referencement.com/wp-content/uploads/2019/01/1472132976url-parameters.jpg',
    },
    {
      name: 'image2',
      url: 'https://p1-arco.byteimg.com/tos-cn-i-uwbnlip3yd/a8c8cdb109cb051163646151a4a5083b.png~tplv-uwbnlip3yd-webp.webp',
    },
    {
      name: 'image3',
      url: 'https://th.bing.com/th/id/OIP.gJkmBFK8shWJRa41tQRsngHaFj?w=188&h=180&c=7&r=0&o=5&dpr=1.7&pid=1.7',
    },
    {
      name: 'image4',
      url: 'https://th.bing.com/th/id/OIP.i9n79q1sI-UcBOrfKnQegwAAAA?rs=1&pid=ImgDetMain',
    },
  ]);
  // 3. 在组件初始化时，就创建Vditor对象，并引用
  onMounted(() => {
    vditor.value = new Vditor('vditor', {
      height: 600,
      width: '70%',
      fullscreen: {
        index: 100,
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
    width: 28%;
  }

  .image-form-card {
    height: 100%;
    min-height: 600px;
    overflow: hidden;
    border-radius: 5px;
  }

  :deep(.img-cards) {
    border-radius: 4px;
    height: 33%;
    width: 50%;

    max-width: 1500px;
    align-items: center;
    text-align: center;
    padding: 5px;
    font-size: 14px;
    color: #666;

    float: left;
    // margin-right: calc(0%);
    .arco-card-body {
      padding: 5px;
      margin: auto;
    }
  }

  //   .img-cards:nth-of-type(2n) {
  //     /* 每行第5个不需要列间隔 */
  //     margin-right: 0;
  //     /* 每行第5个设置行间隔=>不足5个即为最后一行，不需要行间隔 */
  //     margin-bottom: 20px;
  //   }
</style>
