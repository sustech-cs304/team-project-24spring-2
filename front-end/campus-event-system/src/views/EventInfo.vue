<script>
import {
  IconLocation,
  IconStar,
  IconStarFill,
} from '@arco-design/web-vue/es/icon';
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';
import Comment from '@/components/Comment.vue';
import CustomImage from '@/components/CustomImage.vue';

import { Message } from '@arco-design/web-vue';

import utils from '../api/utils.ts';

import MarkdownIt from 'markdown-it';


export default {
  name: 'Events',
  components: {
    Comment,
    IconStar,
    IconStarFill,
    IconLocation,
    CustomImage
  },
  setup() {
    const eventInfo = ref({});
    const route = useRoute();
    const tickets = ref([]);
    const selectedPrice = ref(null);
    const comments = ref([]);
    const totalComments = ref(0);
    const commentAttchments = ref([]);
    const uploadType = ref("image");
    const uploadProperty = ref({});
    const commentText = ref("");
    const star = ref(false);
    const markdownContent = ref("");
    uploadTypeChange();

    const markdown = new MarkdownIt()

    const customRequest = (option) => {
      const {onProgress, onError, onSuccess, fileItem, name} = option
      commentAttchments.value.push(fileItem);
      onSuccess('ok');
    };

    onMounted(async () => {
      // loadMarkdown();
      await loadEventsInfo(route.query.id);
      if (eventInfo.value.tickets) {
        const ticketPromises = eventInfo.value.tickets.map(ticket_id => getTicketInfo(ticket_id));
        tickets.value = await Promise.all(ticketPromises);
      }
      await getComments(route.query.id);
      console.log(eventInfo.value);  
      markdownContent.value = getMarkdownConetent(eventInfo.value.document_url);
    });

    async function getTicketInfo(ticket_id) {
      const response = await axios.post(`/api/ticket/get-ticket?ticketId=${ticket_id}`);
      return response.data;
    }

    function onStarChange(){
      star.value = !star.value;
    }

    function getMarkdownConetent(url){
      axios.get(url).then((response) => {
        markdownContent.value = String(response.data);
      });
    }



    async function loadEventsInfo(eventId) {
      try {
        const response = await axios.post(`/api/event/get-event?eventId=${eventId}&page=1`);
        eventInfo.value = response.data;
      
      } catch (error) {
        console.error(error);
      }
    }

    function uploadTypeChange() {
      commentAttchments.value = [];
      if (uploadType.value === 'image') {
        uploadProperty.value = {
          listType: "picture-card",
          limit: 9,
          accept: "image/*",
          draggable: false
        }
      }
      if (uploadType.value === 'video') {
        uploadProperty.value = {
          listType: "text",
          limit: 1,
          accept: "video/*",
          draggable: true
        }
      }
    }

    function openAMap(latitude, longitude) {
      const url = `https://www.amap.com/search?query=${latitude},${longitude}`;
      window.open(url, '_blank');
    }

    async function makeComment() {
      if (!utils.verifyLoginState(false)) {
        Message.error('请先登录');
        return;
      }
      if ((!commentText.value) || commentText.value.trim() === '') {
        Message.error('评论不能为空');
        return;
      }
      if (commentText.value.length > 200) {
        Message.error('评论不能超过200字');
        return;
      }

      let text = commentText.value;
      try {
        const response = await axios.post(`/api/comment/make-comment`, {
            event_id: route.query.id,
            content: text,
          },
          {
            headers: {
              'Authorization': localStorage.getItem('token_type') + ' ' + localStorage.getItem('access_token')
            }
          }
        );
        if (response.status === 200) {
          commentText.value = '';
          let commentId = response.data.id;
          try {
            for (let i = 0; i < commentAttchments.value.length; i++) {
              let file = commentAttchments.value[i].file;
              let formData = new FormData();
              formData.append('file', file);
              let uploadResponse = await axios.post(`/api/file/upload?usage=comment&commentId=${commentId}`, formData, {
                headers: {
                  'Authorization': localStorage.getItem('token_type') + ' ' + localStorage.getItem('access_token')
                }
              });
              console.log(uploadResponse);
            }
            commentAttchments.value = [];
          } catch (error) {
            console.error(error);
            Message.error('评论成功，但附件上传失败');
            await getComments(route.query.id);
            return;
          }
          Message.success('评论成功');
          await getComments(route.query.id);
        } else {
          Message.error('评论失败');
        }
      } catch (error) {
        console.error(error);
        Message.error('评论失败');
      }
    }

    async function getComments(eventId) {
      try {
        const response = await axios.post(`/api/comment/get-comments?eventId=${eventId}`);
        comments.value = response.data;
      } catch (error) {
        console.error(error);
      }
    }

    return { markdown, eventInfo, tickets, selectedPrice, comments, totalComments, commentAttchments, uploadType, uploadProperty, star, commentText, markdownContent, loadEventsInfo , openAMap, uploadTypeChange, customRequest, makeComment, onStarChange };
  }
};
</script>






<template>
  <div class="outline_container">
    <div class="main_container">
      <div class="upper_container">
        <div class="post_container">
          <CustomImage :src="eventInfo.image_url" :fallbackSrc="'college.jpg'" alt="event image" class="event_post"/>
        </div>
        <div class="details_container">
          <div class="title_container">
            <h1>{{ eventInfo.title }}</h1>
            <span class="action" key="star" @click="onStarChange">
              <span v-if="star">
                <IconStarFill :style="{ color: '#ffb400' }" />
              </span>
              <span v-else>
                <IconStar />
              </span>
              {{ 0 + (star ? 1 : 0) }}
            </span>
          </div>
   
          <p class="details_item">时间：{{ $formatDateTime(eventInfo.start_time) }} - {{ $formatDateTime(eventInfo.end_time) }}</p>
          <p class="details_item"> 
            地点：{{ eventInfo.location_name }}
            <ALink @click="openAMap(eventInfo.latitude, eventInfo.longitude)">  <IconLocation/>  </ALink>

          </p>

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
            <div class="description_container">
              <!-- 这里是项目详情 -->
              <div v-html="markdown.render(markdownContent)" class="markdown-content"></div>
            </div>
          </a-tab-pane>
          <!-- <a-tab-pane key="2">
            <template #title>
              购票须知
            </template>
            <div class="Information">
              这里是购票须知
            </div>
          </a-tab-pane> -->
          <a-tab-pane key="2">
            <template #title>
              评论
            </template>
            <div class="comment">
              <Comment v-for="(item, index) in comments" :key="index" :comment="item" />
              <div class="my_comment">
                <a-input placeholder="" v-model="commentText"/>
                <a-button type="primary" @click="makeComment">评论</a-button>
              </div>
              <div style="margin-top: 10px;">
                <a-space>
                  <a-radio-group v-model="uploadType" @change="uploadTypeChange">
                    <a-radio value="image">上传照片</a-radio>
                    <a-radio value="video">上传视频</a-radio>
                  </a-radio-group>
                </a-space>
                <a-upload 
                  :auto-upload="true"
                  :show-retry-button="false"
                  :show-cancel-button="false"
                  :custom-request="customRequest" 
                  :list-type="uploadProperty.listType"
                  :file-list="commentAttchments"
                  :limit="uploadProperty.limit"
                  :accept="uploadProperty.accept"
                  :draggable="uploadProperty.draggable"
                >
                <template #start_icon>
                </template>
              
              </a-upload>
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




<style scoped>

.description_container {
  width: 80vh;
}

.outline_container {
  margin-top: 2vh;
  /* border: 1px solid #ccc; */
  display: flex;
  align-items: center;
  width: 86vw;
}

.main_container {
  flex: 1;

  /* border-right: 1px solid #ccc; */
  /* border-left: 1px solid #ccc; */
}

.right_container {
  width: 25%;
  display: flex;
  flex-direction: column;
  align-items: center;

  /* border-right: 1px solid #ccc; */
}

.upper_container {
  width: 100%;
  height: auto;
  display: flex;
  /* border-bottom: 1px solid #ccc; */
}

.post_container {
  flex: 1;

}

.details_container {


  width: 70%;
}
.lower_conatiner {
  margin-top: 2vh;
  width: 100%;
  height: auto;
}
.comment{
  margin: 10px;
}

.my_comment {
  margin-top: 1vh;
  display: flex;
  align-items: center; 
  gap: 10px; 
}
.details_item {
  margin-bottom: 20px;
}
.event_post {
  width: 80%;
  height: 100%;
  
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
.title_container {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
/* .markdown-content {

width: 20vw;
overflow: hidden;
} */
</style>