
<script>
import { ref, toRefs, onMounted} from 'vue';

import {
  IconHeart,
  IconMessage,
  IconStar,
  IconStarFill,
  IconHeartFill,
  IconDelete
} from '@arco-design/web-vue/es/icon';
import CustomImage from './CustomImage.vue';
import axios from 'axios';
import Pictures from './Pictures.vue';
import { Message } from '@arco-design/web-vue';

export default {
  name: 'Comment',
  components: {
    IconHeart,
    IconMessage,
    IconStar,
    IconStarFill,
    IconHeartFill,
    IconDelete,
    CustomImage,
    Pictures,
  },
  props: {
    comment: {
      required: true,
    },
    delete: {
      required: false,
      default: false,
    }
  },
  emits: ['delete'],
  setup(props, { emit }) {
    const propsRef = toRefs(props);
    const comment = propsRef.comment;
    const canDelete = propsRef.delete;
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
      let response = await axios.post(`/api/user/get-user?userId=${userId}`);
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
          let suffix = attachment.value[0].split('.').pop().toLowerCase();
          isPicture.value = !videoExtensions.has(suffix);
        }
      } catch (error) {
        console.error('An error occurred:', error);
      }
    });


    async function getAttachment(commentId) {
      let response = await axios.post(`/api/comment/get-comment-attachments?commentId=${commentId}`);
      return response.data;
    }

    async function deleteComment(commentId) {
      try {
        let response = await axios.post(`/api/comment/delete-comment?commentId=${commentId}`, {}, {
          headers: {
            'Authorization': localStorage.getItem('token_type') + ' ' + localStorage.getItem('access_token')
          }
        });
        if (response.status === 200) {
          Message.success('删除成功');
          emit('delete');
        } else {
          Message.error('删除失败');
        }
      } catch (error) {
        Message.error('删除失败');
        console.error('An error occurred:', error);
      }
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
      onStarChange,
      canDelete,
      deleteComment
    }
  },
};
</script>

<template>
  <a-comment
    :author="user.nickname"
    :content="comment.content"
    :datetime="$formatDateTime(comment.create_time)"
    :style="{ font: '16px Arial, sans-serif' }"
  >
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
  <div v-if="hasAttachment&&isPicture" class="comment-pics">
    <a-image-preview-group v-if="hasAttachment&&isPicture">
      <a-image v-for="(img, index) in attachment" :key="index" :src="img" width="100" height="100" class="comment-picture"/>
    </a-image-preview-group>
  </div>
  <div v-if="hasAttachment&&!isPicture" >
    <video  controls :style="{ width: '400px', height: '300px' }" >
      <source :src="attachment[0]">
      Your browser does not support the video tag.
    </video>
  </div>
  <!-- <template #actions> -->
      <!-- <span class="action" key="heart" @click="onLikeChange">
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
      </span> -->
    <div class="actions">
      <span v-if="canDelete" class="action" key="delete" @click="deleteComment(comment.id)">
        <IconDelete />
        删除
      </span>
    </div>
    <!-- </template> -->
  <a-divider />
</template>


<style scoped>

.actions {
  display: flex;
  justify-content: space-between;
  margin-top: 0.5vh;
  margin-left: 4.5vh;
}

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

.comment-pics {
  margin-left: 4.5vh;
  margin-top: 0.1vh;
}

.comment-picture {
  margin-top: 0.2vh;
  cursor: pointer;
}

.comment-picture:hover {
  filter: brightness(70%);
}

</style>
