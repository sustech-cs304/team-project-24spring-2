<script>

import { ref } from 'vue'
import Pictures from "@/components/Pictures.vue";
import EventCard from "@/components/EventCard.vue";
import BottomNav from "@/components/BottomNav.vue";
import axios from 'axios';
import { onMounted } from "vue";

export default {
  name: 'Homepage',
  components: {
    Pictures,
    EventCard,
    BottomNav,
  },
  setup() {
    const data = ref([]);
    const image_urls = ref([]);


    onMounted(async () => {
      try {
        await loadEventsData(1);        
        for (let i = 0; i < 2; i++) {
          if (data.value[i] && data.value[i].image_url) {
            image_urls.value.push(data.value[i].image_url);
          } else {
            console.warn(`data.value[${i}] is undefined or has no image_url`);
          }
        }
      } catch (error) {
        console.error('Error loading events data:', error);
      }
    });

    async function loadEventsData(current) {
      current -= 1;
      await axios.post(`/api/event/explore-events?page=${current}`)
        .then(response => {
          data.value = response.data;
          console.log(data.value);
        })
        .catch(error => {
          console.error(error);
        });
    }
    return { data, image_urls, loadEventsData};
  }
}

</script>

<template>
  <div class="container">
    <div class="picture">
      <Pictures
          :images=image_urls
      />
    </div>
    <!-- <a-divider/> -->
    <a-typography-title class="title">
      当下热门
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
