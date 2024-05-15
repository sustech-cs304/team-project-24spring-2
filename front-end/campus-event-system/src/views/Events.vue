<script>
import EventCard from "@/components/EventCard.vue";
import { ref } from 'vue';
import axios from 'axios';
import { onMounted } from "vue";


export default {
  name: 'Events',
  components: {
    EventCard,
  },

  setup() {
    const eventsData = ref([]);
    const totalEvents = ref(0);

    onMounted(() => {
      loadEventsData(1);
    });

    async function loadEventsData(current) {
      current -= 1;
      loadTotalEventSize();
      axios.post(`/api/event/explore-events?page=${current}`)
        .then(response => {
          eventsData.value = response.data;
        })
        .catch(error => {
          console.error(error);
        });
    }

    async function loadTotalEventSize() {
      axios.post(`/api/event/explore-events-size`)
        .then(response => {
          totalEvents.value = response.data;
        })
        .catch(error => {
          console.error(error);
        });
    }

    return { eventsData, totalEvents, loadEventsData, loadTotalEventSize };
  }
}
</script>

<template>
  <div class="page-container">
    <!-- 设置 a-scrollbar 的高度以适应页面布局，这里假设为页面布局允许的最大高度 -->
    <a-scrollbar style="height: 100%; max-height: 770px; overflow: auto;">
      <div class="cards-container">
        <EventCard v-for="(item, index) in eventsData" :key="index" :event="item" />
      </div>
    </a-scrollbar>
    <div class="pagination">
      <a-pagination :total="totalEvents" :page-size=10 @change="loadEventsData"/>
    </div>
  </div>
</template>

<style scoped>
.page-container {
  height: 100%;
}

.cards-container {
  padding: 20px;
  display: flex;
  align-items: center;
  flex-direction: column;
  gap: 20px;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>
