<script>
import EventCard from "@/components/EventCard.vue";
import { ref } from 'vue';
import axios from 'axios';
import { onMounted } from "vue";
import RecommendCard from "@/components/RecommendCard.vue";
import Recommends from "@/components/Recommends.vue";

export default {
  name: 'Events',
  components: {
    EventCard,
    RecommendCard,
    Recommends
  },

  setup() {
    const eventsData = ref([]);
    const totalEvents = ref(0);
    const categories = ref([]);
    const selectedCategory = ref("all");
    const recommendEvents = ref([]);

    onMounted(async () => {
      loadEventsData(1);
      fetchCategory();
      recommendEvents.value = await fetchRecommendEvents();
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

    async function fetchCategory() {
      axios.post(`/api/global/get-setting?key=categories`)
        .then(response => {
          let sting = response.data;
          // spilt string by ,
          categories.value = sting.split(',');
          console.log(categories.value);
        })
        .catch(error => {
          console.error(error);
        });
    }

    async function changeCategory(val) {
      console.log(val);
      selectedCategory.value = val;
      if (val === "all") {
        loadEventsData(1);
      } else {
        axios.post(`/api/event/explore-events?category=${val}`)
          .then(response => {
            eventsData.value = response.data;
          })
          .catch(error => {
            console.error(error);
          });
      }
    }

    async function fetchRecommendEvents() {
      try {
        let response = await axios.post(`/api/recommend/get-recommendation`,{},
          {
            headers: {
              'Authorization': localStorage.getItem('token_type') + ' ' + localStorage.getItem('access_token')
            }
          });
        let events = Object.keys(response.data);
        let return_events = [];
        if(events.length == 0){
          // using expore events to fill the recommend events
          console.log("explore events")
          let response = await axios.post(`/api/event/explore-events`);
          let explore_event = response.data;
          for(let i = 0; i < 3; i++){
            console.log(explore_event[i])
            return_events.push(explore_event[i]);
          }
        }else{
          for(let i = 0; i < events.length; i++){
            let response = await axios.post(`/api/event/get-event?eventId=${events[i]}`);
            return_events.push(response.data);
          }
        }
        return return_events;
      } catch (error) {
        let return_events = [];
        let response = await axios.post(`/api/event/explore-events`);
          let explore_event = response.data;
          for(let i = 0; i < 3; i++){
            console.log(explore_event[i])
            return_events.push(explore_event[i]);
          }
        return return_events;
      }
    }

    return { eventsData, totalEvents, categories, selectedCategory, loadEventsData, loadTotalEventSize, changeCategory, recommendEvents };
  }
}
</script>

<template>
  <div class="main-container">
    <div class="page-container">
      <div style="display: flex; align-items: center; margin-top: 20px; margin-left: 30px;">
        <p style="margin: 0;"><strong>分类: </strong></p>
        <a-radio-group type="button" :model-value="selectedCategory" @change="(val) => changeCategory(val)"
          style="margin-left: 10px;">
          <a-radio value="all" style="width: 100px;">全部</a-radio>
          <a-radio v-for="(categorie, index) in categories" :key="categorie" :value="categorie" style="width: 100px;">
            {{ categorie }}
          </a-radio>
        </a-radio-group>
      </div>

      <a-scrollbar>
        <div class="cards-container">
          <EventCard v-for="(item, index) in eventsData" :key="index" :event="item" />
        </div>
      </a-scrollbar>
      <div class="pagination">
        <a-pagination :total="totalEvents" :page-size=10 @change="loadEventsData" />
      </div>
    </div>

    <Recommends :eventsData="recommendEvents" style="margin-top: 3.5vh" />
  </div>
</template>

<style scoped>
.main-container {
  display: flex;
  justify-content: center;
  flex-direction: row;
  width: 80%;
}

.page-container {
  height: 100%;
}

.cards-container {
  padding: 20px;
  display: flex;
  align-items: center;
  flex-direction: column;
  gap: 20px;
  min-width: 100vh;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>
