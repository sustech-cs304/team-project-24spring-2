<template>
  <div class="container">
    <a-card class="card">
      <div class="cover-image-area">
        <crop-image-modal
          ref="crop-image"
          :visible="modalVisible"
          :img="strImg"
          :options="cropOptions"
          @closeModal="onCloseCrop"
          @confirm="onConfirmCrop"
        />
        <a-upload
          action="/"
          :fileList="coverImage as any ? [coverImage  as any] : []"
          :show-file-list="false"
          :custom-request="fakeUpload"
          @change="onSelectedImage"
          @progress="onProgress"
          class="cover-upload"
        >
          <template #upload-button>
            <div class="cover-upload-done" v-if="coverImage.url != ''">
              <img :src="coverImage.url" class="cover-upload-list-image" />
              <div class="cover-upload-list-mask">
                <IconEdit
                  style="
                    position: absolute;
                    top: 0;
                    left: 0;
                    right: 0;
                    bottom: 0;
                    margin: auto;
                    width: 50px;
                    height: 50px;
                  "
                />
              </div>
            </div>
            <div class="cover-before-upload" v-else>
              <div class="cover-before-upload-text">
                <IconPlus style="margin: 20 auto 20" />
                <div style="font-weight: 600; font-size: large; margin: auto"
                  >Upload</div
                >
              </div>
            </div>
          </template>
        </a-upload>
      </div>

      <div id="vditor" class="markdown-form"></div>

      <div id="image-form" class="image-form">
        <a-card class="image-form-card">
          <template #title>
            {{ $t('eventEdit.imageForm') }}
          </template>
          <a-list :max-height="550" @reach-bottom="pushNewData(10)">
            <img-card
              v-for="(element, index) in renderImages"
              :image="element"
              :key="index"
              :is="element"
              class="img-cards"
              @delete-image="deleteImage(element.url)"
            />
          </a-list>
          <div class="uploading">
            <a-upload
              :custom-request="customUploadUser"
              :show-file-list="false"
              accept="image/*"
              multiple
              draggable
              auto-upload
              v-model="fileList"
            />
          </div>
        </a-card>
      </div>
    </a-card>
  </div>
</template>

<script lang="ts" setup>
  import Vditor from 'vditor';
  import 'vditor/dist/index.css';
  import imgCard from '@/components/image/image-card.vue';
  import { Notification } from '@arco-design/web-vue';
  import { uploadFile, getFile, deteleFile } from '@/api/file';
  import { originalEventCreationModel, Tickets } from '@/api/event';
  import { getUploadImages } from '@/api/user';
  import { ref, onMounted, defineProps } from 'vue';
  import { IconEdit, IconPlus } from '@arco-design/web-vue/es/icon';
  import cropImageModal from '@/components/image/croper-modal.vue';
  import { watch } from 'vue';
  import { onUnmounted } from 'vue';
  import { cloneDeep } from 'lodash';

  const coverImage = ref({
    url: '',
    blob: null as any,
  });
  const vditor = ref();
  const setted = false;
  const originText = ref('');
  const originCover = ref({
    url: '',
    blob: null as any,
  });

  // 1.1 引入Vditor 构造函数

  // 2. 获取DOM引用
  const renderImages = ref<any>([]);

  const fileList = ref([]);

  const userImages = [] as any[];

  const strImg = ref<string>('');

  const imgIndex = ref(0);

  const formData = defineModel<originalEventCreationModel>('form', {
    default: {} as originalEventCreationModel,
  });

  const modification = defineModel('mod');

  const modalVisible = ref(false);
  const cropOptions = {
    viewMode: 2,
    aspectRatio: 16 / 4,
    dragMode: 'move',
  };

  const updateCover = async () => {
    const { blob, url } = coverImage.value;
    if (url && !blob) {
      return '';
    }
    if (blob == null) {
      return '';
    }
    const controller = new AbortController();

    const newCover = new File([blob], 'cover.png', {
      type: 'image/png',
    });
    const coverData = new FormData();
    coverData.append('file', newCover);

    const resCover = await uploadFile(
      coverData,
      {
        usage: 'event',
        eventId: formData.value.uuid,
      },
      controller
    );
    coverImage.value.blob = null;
    return resCover.data;
  };

  const onExceed = (file: any) => {
    console.log('exceed');
    Notification.error({
      title: 'Error',
      content: '最多上传9张图片',
    });
  };

  const fetchData = async () => {
    const res = await getUploadImages();
    userImages.splice(0, userImages.length);
    for (let i = 0; i < res.data.length; i += 1) {
      userImages.push({
        name: 'image',
        url: `${res.data[i]}`,
      });
    }
    renderImages.value.splice(0, renderImages.value.length);
    console.log('fetchData', res);
  };

  const pushNewData = async (time: number) => {
    for (let i = 0; i < time && userImages.length > 0; i += 1)
      renderImages.value.push(userImages.pop());
  };

  const fakeUpload = (option: any) => {
    return {
      abort() {
        console.log('customUpload4');
      },
    };
  };

  const getDiffMkd = () => {
    const text = vditor.value.getValue();
    if (text !== originText.value) return text;
    return '';
  };

  const customUploadUser = (option: any) => {
    const { onProgress, onError, onSuccess, fileItem, name } = option;
    const controller = new AbortController();
    const form = new FormData();
    form.append('file', fileItem.file);
    uploadFile(
      form,
      {
        usage: 'user',
      },
      controller,
      (e: any) => {
        onProgress({ percent: e.loaded / e.total }, e);
      }
    )
      .then((res: any) => {
        console.log('res', res);
        Notification.success({
          title: 'Success',
          content: '上传成功',
        });
        onSuccess(res, fileItem);
        renderImages.value.push({
          name: 'image',
          url: `${res.data}`,
        });
      })
      .catch((err: any) => {
        console.log('err', err);
        Notification.error({
          title: 'Error',
          content: '上传失败',
        });
        onError(err, fileItem);
      });
    return {
      abort() {
        console.log('customUpload4');
      },
    };
  };
  const deleteImage = async (url: string) => {
    console.log('deleting', url);
    try {
      await deteleFile(url, {
        usage: 'user',
      });
      Notification.success({
        title: 'Success',
        content: '图片删除成功',
      });
    } catch (error) {
      console.error(error);
    } finally {
      for (let i = 0; i < renderImages.value.length; i += 1) {
        if (renderImages.value[i].url === url) {
          renderImages.value.splice(i, 1);
          break;
        }
      }
      pushNewData(1);
    }
  };
  const setVditor = async () => {
    if (formData.value.document_url !== '') {
      const mkd = await getFile(formData.value.document_url);
      originText.value = mkd.data;
      vditor.value.setValue(originText.value);
    } else {
      vditor.value.setValue('');
    }
  };

  const onSelectedImage = (_: any, currentFile: any) => {
    strImg.value = URL.createObjectURL(currentFile.file);
    modalVisible.value = true;
  };

  const onCloseCrop = () => {
    console.log('close');
    modalVisible.value = false;
  };
  const onConfirmCrop = (currentFile: any) => {
    modalVisible.value = false;

    coverImage.value = {
      blob: currentFile,
      url: URL.createObjectURL(currentFile),
    };
    console.log(coverImage);
  };
  const onChange = (_: any, currentFile: any) => {};
  const onProgress = (currentFile: any) => {
    coverImage.value = currentFile;
  };

  const reset = () => {
    coverImage.value = originCover.value;
    vditor.value.setValue(originText.value);
    console.log('reset', originText.value);
  };

  onMounted(async () => {});

  onUnmounted(() => {
    vditor.value.destroy();
  });

  watch(
    () => formData.value,
    async (val) => {
      if (val.image_url !== '' && val.image_url) {
        coverImage.value.url = cloneDeep(val.image_url);
        originCover.value = cloneDeep(coverImage.value);
      }
      vditor.value = new Vditor('vditor', {
        height: 800,
        width: '70%',
        fullscreen: {
          index: 100,
        },
        after() {
          setVditor();
        },
      });
      const res = await fetchData();

      pushNewData(20);
    }
  );

  defineExpose({
    coverImage,
    getDiffMkd,
    updateCover,
    reset,
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
    height: 100%;
    overflow: hidden;
    padding-top: 20px;
    padding-bottom: 20px;
    background-color: white;
  }

  .markdown-form {
    float: left;
    margin: auto;
    height: 100%;
  }

  .image-form {
    float: right;
    margin: auto;
    width: 28%;
    height: 800px;
  }

  .image-form-card {
    height: 100%;
    overflow: hidden;
    border-radius: 5px;
    height: 100%;
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

  .uploading {
    flex: auto;
    position: absolute;
    align-self: flex-end;

    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    border-radius: 8px;
    background-color: #f5f5f5;
  }

  .cover-image-area {
    width: 100%;
    overflow: hidden;
    margin-bottom: 20px;
    border-radius: 8px;
  }

  .cover-upload {
    cursor: pointer;
    border-radius: 8px;
    align-items: center;
    align-content: center;
    width: 100%;
    height: inherit;
  }

  .cover-before-upload {
    border: 2px solid #d9d9d9;
    background-color: #fafafa;
    border-radius: 8px;
    cursor: pointer;
    justify-content: center;
    align-items: center;
  }

  .cover-before-upload-text {
    width: 100px;
    height: 100px;
    align-items: center;
    text-align: center;
    margin: auto;
    margin-top: 30px;
  }

  .cover-upload-done {
    position: relative;
    border-radius: 8px;
    width: 100%;
    height: 100%;
    margin: 0 auto;
    background-color: #fafafa;
    cursor: pointer;
  }
  .cover-upload-list-image {
    position: relative;
    margin: auto;
    width: 100%;
    height: 100%;
  }

  .cover-upload-list-mask {
    position: absolute;
    left: 0;
    top: 0;
    opacity: 0;
    background-color: rgb(122, 122, 122);
    transition: opacity 0.2s;
    align-items: center;

    width: 100%;
    height: 100%;
  }

  .cover-upload-list-mask:hover {
    opacity: 0.5;
  }
</style>
