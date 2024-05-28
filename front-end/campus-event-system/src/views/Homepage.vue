<script>

import { ref } from 'vue'
import Pictures from "@/components/Pictures.vue";
import EventCard from "@/components/EventCard.vue";
import BottomNav from "@/components/BottomNav.vue";
import axios from 'axios';
import { onMounted } from "vue";
import Recommends from "@/components/Recommends.vue"; 

export default {
  name: 'Homepage',
  components: {
    Pictures,
    EventCard,
    BottomNav,
    Recommends,
  },
  setup() {
    const data = ref([]);
    const image_urls = ref([]);
    const recommendEvents = ref([]);


    onMounted(async () => {
      try {
        await loadEventsData(1);
        let first2data = [];
        for (let i = 0; i < 2; i++) {
          if (data.value[i] && data.value[i].image_url) {
            image_urls.value.push(data.value[i].image_url);
            first2data.push(data.value[i]);
          } else {
            console.warn(`data.value[${i}] is undefined or has no image_url`);
          }
        }
        data.value = first2data;
        
        recommendEvents.value = await fetchRecommendEvents();
      } catch (error) {
        console.error('Error loading events data:', error);
      }
    });

    async function loadEventsData(current) {
      current -= 1;
      await axios.post(`/api/event/explore-events?page=${current}`)
        .then(response => {
          data.value = response.data;
        })
        .catch(error => {
          console.error(error);
        });
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
          let response = await axios.post(`/api/event/explore-events`);
          let explore_event = response.data;
          for(let i = 0; i < 3; i++){
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
            return_events.push(explore_event[i]);
          }
        return return_events;
      }
    }
    return { data, image_urls, loadEventsData, recommendEvents};
  }
}

</script>

<template>
  <div class="container">
    <a-divider/>
    <div class="picture">
      <Pictures
          :images=image_urls
      />
    </div>
    <a-divider/>
    <div class="main-container">
      <div class="main-left-container">
          <a-typography-title class="title">
          时下热门
        </a-typography-title>
        <div class="cards-container">
          <EventCard
              v-for="(item, index) in data"
              :key="index"
              :event="item"
          />
        </div>
      </div>
      <div class="main-right-container">
        <Recommends
            :eventsData="recommendEvents"
        />
      </div>


    </div>



    <a-divider/>
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
  margin: auto;
  height: 40vh;
  width: 80vw;
  display: block;
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

.main-container{
  display: flex;
  align-items: baseline;
  justify-content: center;
  flex-direction: row;
}
.main-left-container{
  width: 70%;
}
.main-right-container{
  width: 30%;
}
</style>
