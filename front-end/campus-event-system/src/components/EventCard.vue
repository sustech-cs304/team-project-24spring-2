<script>
import {
  IconThumbUp,
  IconShareInternal,
  IconMore,
} from '@arco-design/web-vue/es/icon';

export default {
  name: "EventCard",
  components: {IconThumbUp, IconShareInternal, IconMore},
  props: {
    event: {
      type: Object,
      required: true,
    }
  },
  methods: {
    navigateToDetail() {
      // 使用 `this.event.id` 来访问当前卡片的 ID
      this.$router.push(`/eventInfo`);
    }
  }
}
</script>

<template>
  <a-card :style="{ width: '360px' }" @click="navigateToDetail()">
    <template #actions>
      <span class="icon-hover"> <IconThumbUp/> </span>
      <span class="icon-hover"> <IconShareInternal/> </span>
      <span class="icon-hover"> <IconMore/> </span>
    </template>
    <template #cover>
      <div
          :style="{
          height: '204px',
          overflow: 'hidden',
        }"
      >
        <img
            :src="event.imgUrl"
            :style="{ width: '100%', transform: 'translateY(-20px)' }"
            alt="event image"
        />
      </div>
    </template>
    <a-card-meta :title="event.title" :description="event.description">
      <template #avatar>
        <div
            :style="{ display: 'flex', alignItems: 'center', color: '#1D2129' }"
        >
          <a-avatar :size="24" :style="{ marginRight: '8px' }">
            {{ event.username[0].toUpperCase() }}
          </a-avatar>
          <a-typography-text>{{ event.username }}</a-typography-text>
        </div>
      </template>
    </a-card-meta>
  </a-card>
</template>

<style scoped>
.icon-hover {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  transition: all 0.1s;
}

.icon-hover:hover {
  background-color: rgb(var(--gray-2));
}
</style>
