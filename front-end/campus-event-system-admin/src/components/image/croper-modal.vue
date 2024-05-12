<template>
  <a-modal
    :title="props.title"
    :visible="props.visible"
    :on-before-cancel="exit"
    append-to-body
  >
    <a-row>
      <vue-picture-cropper
        :img="props.img"
        :options="props.options"
        @realTime="realTime"
        v-if="visible"
      />
    </a-row>
  </a-modal>
</template>

<script lang="ts" setup>
  import { ref } from 'vue';
  import VuePictureCropper, { cropper } from 'vue-picture-cropper';

  import { defineProps, defineEmits } from 'vue';

  const previews = ref({});

  const emits = defineEmits(['closeModal']);

  const optDefault = {
    autoCrop: true,
    autoCropWidth: 200,
    autoCropHeight: 200,
    fixedBox: true,
  };

  const props = defineProps({
    title: {
      type: String,
      default: '图片裁剪',
    },
    visible: {
      type: Boolean,
      default: false,
    },
    img: {
      type: String,
      default: '',
    },
    options: {
      type: Object,
      default: () => {
        return {
          aspectRatio: 16 / 5,
        };
      },
    },
  });

  //   const cropper = ref();

  const realTime = (data: any) => {
    previews.value = data;
  };

  const rotateLeft = () => {
    // cropper.rotateLeft();
  };
  const rotateRight = () => {
    // cropper.rotateRight();
  };
  // 图片缩放
  const changeScale = (num: number) => {
    num = num || 1;
    // cropper.value.changeScale(num);
  };

  const exit = () => {
    emits('closeModal');
    return true;
  };

  //   watch(
  //     () => props.visible,
  //     (val) => {
  //       if (val) {
  //         options.img = props.img;
  //       }
  //     }
  //   );
</script>
