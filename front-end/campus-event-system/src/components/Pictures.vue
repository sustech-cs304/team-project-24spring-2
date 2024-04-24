<script setup>
import { ref } from 'vue'

const images = ref([
  'https://p1-arco.byteimg.com/tos-cn-i-uwbnlip3yd/a20012a2d4d5b9db43dfc6a01fe508c0.png~tplv-uwbnlip3yd-webp.webp',
  'https://p1-arco.byteimg.com/tos-cn-i-uwbnlip3yd/6480dbc69be1b5de95010289787d64f1.png~tplv-uwbnlip3yd-webp.webp',
  'https://p1-arco.byteimg.com/tos-cn-i-uwbnlip3yd/0265a04fddbd77a19602a15d9d55d797.png~tplv-uwbnlip3yd-webp.webp',
]);

defineExpose({ images })
</script>

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
        <img
            :src="image"
            class="carousel-image"
        />
      </div>
    </a-carousel-item>
  </a-carousel>
</template>
<style scoped>
.image-container {
  width: 100%; /* Viewport width */
  height: 100%; /* Viewport height */
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden; /* Ensures no part of the image spills out */
}

.carousel-image {
  max-width: 100%;
  max-height: 100%;
  object-fit: cover; /* Ensures the image covers the area without distortion */
  border-radius: 10px; /* Adds rounded corners to the images */
  transition: transform 0.5s ease; /* Smooth transition for hover effects */
}

.image-container:hover .carousel-image {
  transform: scale(1.05); /* Slightly enlarges the image on hover */
}

.a-carousel {
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2); /* Adds a shadow for depth */
  border-radius: 10px; /* Rounds the corners of the carousel container */
  overflow: hidden; /* Ensures no overflow from the rounded image corners */
}
</style>
