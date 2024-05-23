<template>
  <div class="container">
    <a-card class="card" hoverable draggable="">
      <div class="img-body">
        <a-image
          class="img"
          footer-position="outer"
          :src="props.image.url"
          :title="props.image.name"
          :on-error="ImageInterceptor"
          width="100%"
        >
          <template #extra>
            <a-space>
              <a-tooltip content="删除">
                <span
                  class="action actions-outer"
                  @click.prevent="deleteImage(props.image.url)"
                  ><icon-delete
                /></span>
              </a-tooltip>
            </a-space>
          </template>
        </a-image>
      </div>
    </a-card>
  </div>
</template>

<script lang="ts" setup>
  import { useI18n } from 'vue-i18n';
  import { Notification } from '@arco-design/web-vue';
  import { ImageInterceptor, deteleFile } from '@/api/file';

  const emits = defineEmits(['deleteImage']);

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

  const deleteImage = async (url: string) => {
    emits('deleteImage', url);
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
    min-height: 50px;
  }
  .card:hover {
    transform: translateY(-1px);
  }

  .img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    margin: 0 auto;
    font-size: px;
  }

  .actions {
    display: flex;
    align-items: center;
  }
  .action {
    padding: 5px 4px;
    font-size: 14px;
    margin-left: 12px;
    border-radius: 2px;
    line-height: 1;
    cursor: pointer;
  }
  .action:first-child {
    margin-left: 0;
  }

  .action:hover {
    background: rgba(0, 0, 0, 0.5);
  }
  .actions-outer {
    .action {
      &:hover {
        color: #ffffff;
      }
    }
  }
</style>
