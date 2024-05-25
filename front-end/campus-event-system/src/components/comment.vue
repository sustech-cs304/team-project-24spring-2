
<script>
import { ref , toRefs, onMounted} from 'vue';
import {
  IconHeart,
  IconMessage,
  IconStar,
  IconStarFill,
  IconHeartFill,
} from '@arco-design/web-vue/es/icon';
import CustomImage from './CustomImage.vue';
import axios from 'axios';
import Pictures from './Pictures.vue';

export default {
  name: 'Comment',
  components: {
    IconHeart,
    IconMessage,
    IconStar,
    IconStarFill,
    IconHeartFill,
    CustomImage,
    Pictures,
  },
  props: {
    comment: {
      required: true,
    }
  },
  setup(props) {
    const { comment } = toRefs(props);
    const user = ref({});
    const attachment = ref([]);
    const hasAttachment = ref(false);
    const isPicture = ref(false);

    const like = ref(false);
    const star = ref(false);
    const onLikeChange = () => {
      like.value = !like.value;
    };
    const onStarChange = () => {
      star.value = !star.value;

    };


    const fetchUser = async (userId) => {
      const response = await axios.post(`/api/user/get-user?userId=${userId}`);
      return response.data;
    };

    const videoExtensions = new Set([
      'mp4', 'avi', 'mov', 'rmvb', 'flv', '3gp', 'wmv', 'mkv', 'rm', 'asf', 'ts',
      'mpg', 'mpeg', 'm4v', 'vob', 'webm', 'swf', 'f4v', 'ogg', 'dat', 'mts',
      'm2ts', 'm2t', 'm2v', 'm2p', 'm2a'
    ]);

    onMounted(async () => {
      try {
        user.value = await fetchUser(comment.value.user_id);
        attachment.value = await getAttachment(comment.value.id);

        if (attachment.value.length > 0) {
          hasAttachment.value = true;
          const suffix = attachment.value[0].split('.').pop().toLowerCase();
          isPicture.value = !videoExtensions.has(suffix);
        }
      } catch (error) {
        console.error('An error occurred:', error);
      }
    });


    async function getAttachment(commentId) {
      const response = await axios.post(`/api/comment/get-comment-attachments?commentId=${commentId}`);
      console.log(response.data);
      return response.data;
    }

    return {
      like,
      star,
      comment,
      user,
      attachment,
      isPicture,
      hasAttachment,
      onLikeChange,
      onStarChange
    }
  },
};
</script>

<template>
  <a-comment
    :author="user.nickname"
    :content="comment.content"
    :datetime="$formatDateTime(comment.create_time)"
  >




    <!-- <template #actions>
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
    </template> -->
    <template #avatar>
      <a-avatar>
        <CustomImage
          alt="avatar"
          :src="'user.avatar_url'"
          fallbackSrc="test1.jpeg"
        />
      </a-avatar>
    </template>
  </a-comment> 
  <div  v-if="hasAttachment&&isPicture" class="attach">
    <pictures   :images="attachment" />
  </div>
  <div v-if="hasAttachment&&!isPicture" >
    <video  controls :style="{ width: '400px', height: '300px' }" >
      <source :src="attachment[0]">
      Your browser does not support the video tag.
    </video>
  </div>


  <a-divider />
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
.attach {
  height: 30vh;
  width: 30vw;
  margin-top: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
}
</style>
