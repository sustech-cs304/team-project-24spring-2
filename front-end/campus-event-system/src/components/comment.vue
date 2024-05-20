
<script>
import { ref } from 'vue';
import {
  IconHeart,
  IconMessage,
  IconStar,
  IconStarFill,
  IconHeartFill,
} from '@arco-design/web-vue/es/icon';
import CustomImage from './CustomImage.vue';

export default {
  name: 'Comment',
  components: {
    IconHeart,
    IconMessage,
    IconStar,
    IconStarFill,
    IconHeartFill,
    CustomImage,
  },
  setup() {
    const like = ref(false);
    const star = ref(false);
    const onLikeChange = () => {
      like.value = !like.value;
    };
    const onStarChange = () => {
      star.value = !star.value;
    };

    return {
      like,
      star,
      onLikeChange,
      onStarChange
    }
  },
};
</script>

<template>
  <a-comment
    author="Socrates"
    content="Comment body content."
    datetime="1 hour"
  >
    <template #actions>
      <span class="action" key="heart" @click="onLikeChange">
        <span v-if="like">
          <IconHeartFill :style="{ color: '#f53f3f' }" />
        </span>
        <span v-else>
          <IconHeart />
        </span>
        {{ 0 + (like ? 1 : 0) }}
      </span>
      <span class="action" key="star" @click="onStarChange">
        <span v-if="star">
          <IconStarFill :style="{ color: '#ffb400' }" />
        </span>
        <span v-else>
          <IconStar />
        </span>
        {{ 0 + (star ? 1 : 0) }}
      </span>
      <span class="action" key="reply">
        <IconMessage /> Reply
      </span>
    </template>
    <template #avatar>
      <a-avatar>
        <CustomImage
          alt="avatar"
          :src="'error'"
          :fallbackSrc="'public/test1.jpeg'"
        />
      </a-avatar>
    </template>
  </a-comment>
</template>


<style scoped>
.action {
  display: inline-block;
  padding: 0 4px;
  color: var(--color-text-1);
  line-height: 24px;
  background: transparent;
  border-radius: 2px;
  cursor: pointer;
  transition: all 0.1s ease;
}
.action:hover {
  background: var(--color-fill-3);
}
</style>
