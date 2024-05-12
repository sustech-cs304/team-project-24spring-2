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
          :fileList="file ? [file] : []"
          :show-file-list="false"
          :custom-request="fakeUpload"
          @change="onSelectedImage"
          @progress="onProgress"
          class="cover-upload"
        >
          <template #upload-button>
            <div class="cover-upload-done" v-if="file && file.url">
              <img :src="file.url" class="cover-upload-list-image" />
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
          <a-list :max-height="550">
            <img-card
              v-for="(element, index) in renderImages"
              :image="element"
              :key="index"
              :is="element"
              class="img-cards"
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
  // 1.1 引入Vditor 构造函数
  import Vditor from 'vditor';
  import 'vditor/dist/index.css';
  import imgCard from '@/components/image/image-card.vue';
  import { Notification } from '@arco-design/web-vue';
  import { uploadFile } from '@/api/file';
  import { ref, onMounted } from 'vue';
  import { IconEdit, IconPlus } from '@arco-design/web-vue/es/icon';
  import cropImageModal from '@/components/image/croper-modal.vue';

  // 2. 获取DOM引用
  const vditor = ref();
  const renderImages = ref([
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

  const fileList = ref([]);
  const userImages = ref([]);
  const cropImage = ref();
  const strImg = ref<string>('');

  const onExceed = (file: any) => {
    console.log('exceed');
    Notification.error({
      title: 'Error',
      content: '最多上传9张图片',
    });
  };

  const fetchData = () => {
    // fetch user images
  };
  const fakeUpload = (option: any) => {
    return {
      abort() {
        console.log('customUpload4');
      },
    };
  };

  const customUploadUser = (option: any) => {
    const { onProgress, onError, onSuccess, fileItem, name } = option;
    const formData = new FormData();
    formData.append('file', fileItem.file);
    uploadFile(formData, 'user', (e: any) => {
      onProgress({ percent: e.loaded / e.total }, e);
    })
      .then((res: any) => {
        console.log('res', res);
        Notification.success({
          title: 'Success',
          content: '上传成功',
        });
        onSuccess(res, fileItem);
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

  const file = ref();
  const modalVisible = ref(false);
  const cropOptions = {
    viewMode: 2 ,
    aspectRatio: 16 / 4,
    dragMode:'move',


  }
  const onSelectedImage = (_: any, currentFile: any) => {
    console.log(currentFile);
    // file.value = {
    //   ...currentFile,
    //   url: URL.createObjectURL(currentFile.file),
    // };

    strImg.value = URL.createObjectURL(currentFile.file);
    modalVisible.value = true;
  };

  const onCloseCrop = () => {
    console.log('close');
    modalVisible.value = false;
  };
  const onConfirmCrop = (currentFile: any) => {
    modalVisible.value = false;

    file.value = {
      ...currentFile,
      url: URL.createObjectURL(currentFile),
    };
    console.log(currentFile);
  };
  const onChange = (_: any, currentFile: any) => {};
  const onProgress = (currentFile: any) => {
    file.value = currentFile;
  };

  onMounted(() => {
    vditor.value = new Vditor('vditor', {
      height: 800,
      width: '70%',
      fullscreen: {
        index: 100,
      },
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
