<script setup>
import TopNav from "@/components/TopNav.vue";
import LeftNavigate from "@/components/LeftNav.vue";
import EventCard from "@/components/EventCard.vue";
</script>

<template>
  <div class="main">
    <div class="top-nav">
      <TopNav/>
    </div>
    <LeftNavigate/>
<!--    <EventCard/>-->
  </div>
</template>


<style scoped>
.main {
  height: 100vh;
}
.top-nav{
    border-bottom: ghostwhite 2px solid;
}
</style>
