<script>
import RecommendCard from './RecommendCard.vue';
import { ref, toRefs } from 'vue';
import axios from 'axios';

export default {
    name: "Recommends",
    components: {
        RecommendCard
    },
    props: {
        eventsData: {
            required: true
        }
    },

    setup(props) {
        const { eventsData } = toRefs(props);
        return {
            eventsData
        }
    }
    
}


</script>

<template>
    <a-card title="您可能感兴趣">
    <div class="recommends">
      <RecommendCard v-for="(item, index) in eventsData" :key="index" :event="item" />
    </div>
  </a-card>
</template>
    


<style scoped>
.recommends {
  display: flex;
  align-items: center;
  flex-direction: column;
  gap: 20px;
}

</style>