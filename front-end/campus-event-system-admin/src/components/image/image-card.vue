<template>
  <div class="container">
    <a-card class="card" hoverable>
      <div class="img-body">
        <a-image
          :src="props.image.url"
          :description="props.image.name"
          :on-error="ImageInterceptor"
          width="100%"
        />
      </div>
    </a-card>
  </div>
</template>

<script lang="ts" setup>
  import { useI18n } from 'vue-i18n';
  import { Notification } from '@arco-design/web-vue';
  import { ImageInterceptor } from '@/api/file';

  const props = defineProps({
    image: {
      type: Object,
      default: () => ({
        name: '',
        url: '',
      }),
    },
  });

  const copy = async () => {
    try {
      const img = document.createElement('img');
      img.removeAttribute('style');
      img.src = props.image.url;
      document.body.appendChild(img);
      const range = document.createRange();
      range.selectNode(img);
      const selection = window.getSelection();
      selection?.removeAllRanges();
      selection?.addRange(range);
      document.execCommand('copy');
      document.body.removeChild(img);
    } catch (error) {
      console.error(error);
    } finally {
      Notification.success({
        title: 'Success',
        content: '图片复制成功',
      });
    }
  };
</script>

<style scoped lang="less">
  .container {
    overflow: hidden;
    align-items: center;
  }

  .card {
    transition-property: all;
    -webkit-user-select: none;
    -moz-user-select: none; /*火狐*/
    -ms-user-select: none; /*IE10*/
    user-select: none;
  }
  .card:hover {
    transform: translateY(-1px);
  }

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
</style>
