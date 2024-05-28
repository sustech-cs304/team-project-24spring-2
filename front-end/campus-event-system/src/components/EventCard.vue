<script>
import {
  IconThumbUp,
  IconShareInternal,
  IconMore,
  IconLocation,
  IconSchedule,
} from '@arco-design/web-vue/es/icon';
import { ref, onMounted, toRefs, computed } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';
import CustomImage from './CustomImage.vue';



export default {
  name: "EventCard",
  ticket_info: {},
  components: {IconThumbUp, IconShareInternal, IconMore, IconLocation, IconSchedule, CustomImage},
  props: {
    event: {
      type: Object,
      required: true,
    }
  },
  setup(props) {

    const tickets = ref([]);

    const router = useRouter();

    const { event } = toRefs(props);

    const priceRange = computed(() => {
      if (tickets.value.length === 0) {
        return "0";
      }
      let min = tickets.value[0].price;
      let max = tickets.value[0].price;
      tickets.value.forEach(ticket => {
        if (ticket === undefined) return;
        if (ticket.price < min) {
          min = ticket.price;
        }
        if (ticket.price > max) {
          max = ticket.price;
        }
      });
      if (min === max)
        return `${min}`;
      return `${min} - ${max}`;
    });

    function navigateToDetail() {
      router.push({path:`/eventInfo`, query: {"id": event.value.id}});
    };

    async function getTicketInfo(ticket_id) {
      const response = await axios.post(`/api/ticket/get-ticket?ticketId=${ticket_id}`);
      return response.data;
    };


    onMounted(async () => {
      const ticketPromises = event.value.tickets.map(ticket_id => {
          return getTicketInfo(ticket_id);
      });
      tickets.value = await Promise.all(ticketPromises);
    });

    return { tickets, priceRange, navigateToDetail, getTicketInfo };

  }
}


</script>

<template>
  <!-- <div :style="{ width: '800px' }">
    <div>
      <div
          class="icon"
          @click="navigateToDetail()"
      >
        <img
            :src="event.imgUrl"
            :style="{ width: '100%', transform: 'translateY(-20px)' }"
            alt="event image"
        />
      </div>
    </div>
  </div> -->

  <a-card :style="{ width: '800px' }">
    <!-- <template #actions> -->
      <!-- <span class="icon-hover"> <IconThumbUp/> </span> -->
      <!-- <span class="icon-hover"> <IconShareInternal/> </span> -->
      <!-- <span class="icon-hover"> <IconMore/> </span> -->
    <!-- </template> -->
    <template #cover>
      <div
          class="icon"
          @click="navigateToDetail()"
      >
        <CustomImage
            :src="event.image_url"
            :fallbackSrc="'error.png'"
            :style="{ width: '100%', transform: 'translateY(-20px)' }"
            alt="event image"
        />
      </div>
    </template>
    <div class="description-box">
      <div class="description-box-title" @click="navigateToDetail()">
        {{ event.category + " | " + event.title }}
      </div>
      <div class="description-box-row-box">
        <span class="icon-hover"> <IconLocation/> </span>
        {{ event.location_name }}
      </div>
      <div class="description-box-row-box">
        <span class="icon-hover"><icon-schedule /> </span>
        {{ this.$formatDateTime(event.start_time) }} - {{ this.$formatDateTime(event.end_time)}}
      </div>
      <div class="description-box-price">
        ¥{{ priceRange }}
      </div>
    </div>

  </a-card>

</template>

<style scoped>


.icon {
  overflow: hidden;
  height: 150px;
  display: flex;
  align-items: center;
  justify-content: center;
  /* border-radius: 8px; */
  cursor: pointer;
}

.description-box-row-box {
  display: flex;
  align-items: flex-start;
  justify-content: center;
  flex-direction: row;
}

.description-box-row-box:hover {
  color: var(--vt-c-text-hover);
  cursor: default;
}

.description-box-title {
  font-size: 20px;
  font-weight: 500;
  color: black;
  user-select: none;
  
}

.description-box-title:hover {
  color: var(--vt-c-text-hover);
  cursor: pointer;

}

.description-box-price {
  margin-top: 10px;
  font-size: 16px;
  font-weight: 500;
  color: var(--vt-c-text-hover);
}

.description-box {
  padding: 10px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.icon:hover {
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  transition: all 0.1s;
}

.icon-hover {
  display: flex;
  align-items: center;
  flex-direction: row;
  justify-content: center;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  transition: all 0.1s;
}
</style>
