<template>
    <img
      :src="src"
      @error="handleImageError"
      :alt="alt"
    />
  </template>
  
  <script>

  export default {
    props: {
      src: {
        type: String,
        required: true
      },
      fallbackSrc: {
        type: String,
        required: true
      },
      alt: {
        type: String,
        default: ''
      }
    },
    methods: {
      handleImageError(event) {
        console.log('Failed to load image:', event.target.src);
        event.target.src = this.fallbackSrc;
      },
    }
  };
  </script>
  