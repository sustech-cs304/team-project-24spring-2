<script>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';
import Comment from '@/components/comment.vue';

export default {
  name: 'Events',
  components: {
    Comment,
  },
  setup() {
    const eventInfo = ref({});
    const route = useRoute();
    const tickets = ref([]);
    const selectedPrice = ref(null);

    onMounted(async () => {
      await loadEventsInfo(route.query.id);
      if (eventInfo.value.tickets) {
        const ticketPromises = eventInfo.value.tickets.map(ticket_id => getTicketInfo(ticket_id));
        tickets.value = await Promise.all(ticketPromises);
      }
    });

    async function getTicketInfo(ticket_id) {
      const response = await axios.post(`/api/ticket/get-ticket?ticketId=${ticket_id}`);
      return response.data;
    }

    async function loadEventsInfo(eventId) {
      try {
        const response = await axios.post(`/api/event/get-event?eventId=${eventId}`);
        eventInfo.value = response.data;
      } catch (error) {
        console.error(error);
      }
    }

    return { eventInfo, tickets, selectedPrice, loadEventsInfo };
  }
};
</script>






<template>
  <div class="outline_container">
    <div class="main_container">
      <div class="upper_container">
        <div class="post_container">
          <img :src="eventInfo.image_url" alt="event image" style="width: 100%; height: 100%">
        </div>
        <div class="Details_container">
          <h1>{{ eventInfo.title }}</h1>
          <p class="details_item">时间：{{ $formatDateTime(eventInfo.start_time) }} - {{ $formatDateTime(eventInfo.end_time) }}</p>
          <p class="details_item"> 地点：{{ eventInfo.location_name }}</p>
          <div class="details_item">
            票档
            <a-radio-group type="button" v-model:checked="selectedPrice" >
              <a-radio v-for="ticket in tickets" :key="ticket.id" :value="ticket">{{ ticket.description  + " " + ticket.price }} 元</a-radio>
            </a-radio-group>
          </div>
          <a-button type="primary" class="details_item">
            购票
          </a-button>
        </div>
      </div>
      <div class="lower_conatiner">
        <a-tabs>
          <a-tab-pane key="1">
            <template #title>
              项目详情
            </template>
            <div class="Description">
              这里是项目详情
            </div>
          </a-tab-pane>
          <a-tab-pane key="2">
            <template #title>
              购票须知
            </template>
            <div class="Information">
              这里是购票须知
            </div>
          </a-tab-pane>
          <a-tab-pane key="3">
            <template #title>
              评论
            </template>
            <div class="comment">
              <Comment />
              <Comment />
              <Comment />
              <div class="my_comment">
                <a-input placeholder="Here is your content." />
                <a-button type="primary">评论</a-button>
              </div>
            </div>
          </a-tab-pane>
        </a-tabs>
      </div>
    </div>
    <div class="right_container">
      推荐内容
    </div>
  </div>
</template>
r



<style scoped>
.outline_container {
  display: flex;
  width: 86vw;
  margin: auto;
}

.main_container {
  flex: 1;

  border-right: 1px solid #ccc;
  border-left: 1px solid #ccc;
}

.right_container {
  width: 25%;

  border-right: 1px solid #ccc;
}

.upper_container {
  width: 100%;
  height: auto;
  display: flex;
  border-bottom: 1px solid #ccc;
}

.post_container {
  flex: 1;
}

.Details_container {

  width: 70%;
}
.lower_conatiner {
  width: 100%;
  height: auto;
}
.outline_container {
  display: flex;
  width: 86vw;
  margin: auto;
}
.comment{
  margin: 10px;
}

.my_comment {
  display: flex;
  align-items: center; 
  gap: 10px; 
}
.details_item {
  margin-bottom: 20px;
}
</style>