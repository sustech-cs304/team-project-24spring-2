<script>
import { ref } from 'vue'
import Pictures from "@/components/Pictures.vue";
import EventCard from "@/components/EventCard.vue";
import BottomNav from "@/components/BottomNav.vue";

export default {
  name: 'Homepage',
  components: {
    Pictures,
    EventCard,
    BottomNav,
  },
  setup() {
    const data = ref([
      {
        title: "Card Title 1",
        description: "This is the description of card 1.",
        imgUrl: "https://p1-arco.byteimg.com/tos-cn-i-uwbnlip3yd/a20012a2d4d5b9db43dfc6a01fe508c0.png~tplv-uwbnlip3yd-webp.webp",
        username: "Username1",
      },
      {
        title: "Card Title 2",
        description: "This is the description of card 2.",
        imgUrl: "https://p1-arco.byteimg.com/tos-cn-i-uwbnlip3yd/a20012a2d4d5b9db43dfc6a01fe508c0.png~tplv-uwbnlip3yd-webp.webp",
        username: "Username2",
      },
      {
        title: "Card Title 3",
        description: "This is the description of card 3.",
        imgUrl: "https://p1-arco.byteimg.com/tos-cn-i-uwbnlip3yd/a20012a2d4d5b9db43dfc6a01fe508c0.png~tplv-uwbnlip3yd-webp.webp",
        username: "Username3",
      },
      {
        title: "Card Title 4",
        description: "This is the description of card 4.",
        imgUrl: "https://p1-arco.byteimg.com/tos-cn-i-uwbnlip3yd/a20012a2d4d5b9db43dfc6a01fe508c0.png~tplv-uwbnlip3yd-webp.webp",
        username: "Username4",
      },
      {
        title: "Card Title 5",
        description: "This is the description of card 5.",
        imgUrl: "https://p1-arco.byteimg.com/tos-cn-i-uwbnlip3yd/a20012a2d4d5b9db43dfc6a01fe508c0.png~tplv-uwbnlip3yd-webp.webp",
        username: "Username5",
      },
      {
        title: "Card Title 6",
        description: "This is the description of card 6.",
        imgUrl: "https://p1-arco.byteimg.com/tos-cn-i-uwbnlip3yd/a20012a2d4d5b9db43dfc6a01fe508c0.png~tplv-uwbnlip3yd-webp.webp",
        username: "Username6",
      },

    ]);

    return {data};
  }
}

</script>

<template>
  <div class="container">
    <div class="picture">
      <Pictures/>
    </div>
    <!-- <a-divider/> -->
    <a-typography-title class="title">
      HOT EVENTS
    </a-typography-title>
    <div class="cards-container">
      <EventCard
          v-for="(item, index) in data"
          :key="index"
          :event="item"
      />
    </div>
    <a-divider/>
    <!-- <BottomNav/> -->
  </div>
</template>

<style scoped>
.container {
  width: 88vw;
  height: auto;
  margin: auto; 
  display: block; 
}
.picture {
  height: 40vh;
  width: 80vw;
  margin: auto; /* 将图片水平和垂直居中 */
  display: block; /* 确保图片的显示类型为块级元素 */
}

.cards-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 20px;
  width: 100%;
}
.title {
  text-align: center;
  color: var(--color-heading);
}
</style>
