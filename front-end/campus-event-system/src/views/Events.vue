<script>
import EventCard from "@/components/EventCard.vue";
import { ref } from 'vue';

export default {
  name: 'Events',
  components: {
    EventCard,
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
      {
        title: "Card Title 7",
        description: "This is the description of card 7.",
        imgUrl: "https://p1-arco.byteimg.com/tos-cn-i-uwbnlip3yd/a20012a2d4d5b9db43dfc6a01fe508c0.png~tplv-uwbnlip3yd-webp.webp",
        username: "Username7",
      },
      {
        title: "Card Title 8",
        description: "This is the description of card 8.",
        imgUrl: "https://p1-arco.byteimg.com/tos-cn-i-uwbnlip3yd/a20012a2d4d5b9db43dfc6a01fe508c0.png~tplv-uwbnlip3yd-webp.webp",
        username: "Username8",
      },
      {
        title: "Card Title 9",
        description: "This is the description of card 9.",
        imgUrl: "https://p1-arco.byteimg.com/tos-cn-i-uwbnlip3yd/a20012a2d4d5b9db43dfc6a01fe508c0.png~tplv-uwbnlip3yd-webp.webp",
        username: "Username9",
      },

      // 假设这里有更多卡片数据
    ]); // 这里是您的数据源

    return {data};
  }
}
</script>

<template>
  <div class="page-container">
    <!-- 设置 a-scrollbar 的高度以适应页面布局，这里假设为页面布局允许的最大高度 -->
    <a-scrollbar style="height: 100%; max-height: 770px; overflow: auto;">
      <div class="cards-container">
        <EventCard
            v-for="(item, index) in data"
            :key="index"
            :event="item"
        />
      </div>
    </a-scrollbar>
  </div>
</template>

<style scoped>
.page-container {
  height: 100%;
}

.cards-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 20px;
}
</style>
