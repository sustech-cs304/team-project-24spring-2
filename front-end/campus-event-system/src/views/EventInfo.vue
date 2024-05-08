<script>



import { ref, onMounted, toRefs, computed } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

export default {
  name: 'Events',
  components: {},
  props: {
    eventId: {
      type: String,
      required: true,
    }
  },
  setup(props) {
    const { eventId } = toRefs(props);

    const eventInfo = ref(Object);

    onMounted(() => {
      loadEventsInfo(eventId);
    });

    async function loadEventsInfo(eventId) {
      axios.post(`/api/event/get-event?eventId=${eventId}`)
        .then(response => {
          eventInfo.value = response.data;
          console.log(eventInfo.value)
        })
        .catch(error => {
          console.error(error);
        });
    }
    return { eventInfo, loadEventsInfo};
  }
}
</script>


<template>
  <div class="outline_container">
    <div class="main_container">
      <div class="upper_container">
        <div class="post_container">
            picture
        </div>
        <div class="Details_container">
          <h1> This is title</h1>
          <p>时间：</p>
          <p>地点：</p>
          <div >
            票档 
            <a-radio-group v-model:checked="selectedPrice">
              <a-radio v-for="price in eventInfo.prices" :key="price" :value="price">{{ price }}</a-radio>
            </a-radio-group>
          </div>
          <a-button type="primary">
              确定
          </a-button>
        </div>
      </div>
      <div class="lower_conatiner">
        <a-tabs>
          <a-tab-pane key="1">
            <template #title>
              <icon-calendar/> 项目详情
            </template>
            <div class="Description">
              这里是项目详情
            </div>
          </a-tab-pane>
          <a-tab-pane key="2">
            <template #title>
              <icon-clock-circle/>购票须知
            </template>
            <div class="Information">
              这里是购票须知
            </div>
          </a-tab-pane>
          <a-tab-pane key="3">
            <template #title>
              <icon-user/> 评论
            </template>
              这里是评论
          </a-tab-pane>
        </a-tabs>

      </div>
    </div>
    <div class="right_container">
      推荐内容
    </div>
  </div>
</template>


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


</style>