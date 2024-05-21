<template>
  <a-carousel
      :style="{
      width: '100%',
      height: '100%',
    }"
      :auto-play="true"
      indicator-type="dot"
      show-arrow="hover"
  >
    <a-carousel-item v-for="(image, index) in images" :key="index">
      <div class="image-container">
        <CustomImage
            :src="image"
            :fallbackSrc="'college.jpg'"
            alt="carousel image"
            class="carousel-image"
        />
      </div>
    </a-carousel-item>
  </a-carousel>
</template>

<script setup>
import { defineProps, defineExpose } from 'vue'
import CustomImage from './CustomImage.vue';

const props = defineProps({
  images: {
    type: Array,
    required: true
  }
})

defineExpose({ images: props.images })
// return { CustomImage, props}
</script>

<style scoped>
.image-container {
  width: 100%; 
  height: auto; 
  justify-content: center;
  align-items: center;
  overflow: hidden; 
}

.carousel-image {
  width: 100%;
  height: auto;
  object-fit: cover; 
  border-radius: 0px; 
  transition: transform 0.5s ease; 
}

.a-carousel {
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2); 
  border-radius: 10px; 
  overflow: hidden; 
}
</style>
