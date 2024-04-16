<script setup>
import Pictures from "@/components/Pictures.vue";
</script>

<template>
  <div class="description-contain">
    <div class="pic">
      <Pictures/>
    </div>
    <h1>
      Event Description
    </h1>
    <p>
      This is the event description page. Here you can find all the information about the event.
    </p>
  </div>
</template>

<style scoped>
.pic {
  height: 50%;
  width: 50%;
}
.description-contain{
  height: 100%;
  width: 100%;
  justify-content: center;
}
</style>