<script>
import {
  IconLocation,
  IconStar,
  IconStarFill,
} from "@arco-design/web-vue/es/icon";
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import axios from "axios";
import Comment from "@/components/Comment.vue";
import CustomImage from "@/components/CustomImage.vue";

import { Message } from "@arco-design/web-vue";

import utils from "../api/utils.ts";

import MarkdownIt from "markdown-it";
import Recommends from "@/components/Recommends.vue";

import VueMarkdownEditor from "@kangc/v-md-editor";
import "@kangc/v-md-editor/lib/style/base-editor.css";
import "@kangc/v-md-editor/lib/theme/style/vuepress.css";
import vuepressTheme from "@kangc/v-md-editor/lib/theme/vuepress";

import Prism from "prismjs";

VueMarkdownEditor.use(vuepressTheme, {
  Prism,
});

export default {
  name: "Events",
  components: {
    Comment,
    IconStar,
    IconStarFill,
    IconLocation,
    CustomImage,
    Recommends,
    VueMarkdownEditor,
  },
  setup() {
    const eventInfo = ref({});
    const route = useRoute();
    const tickets = ref([]);
    const selectedPrice = ref(0);
    const comments = ref([]);
    const totalComments = ref(0);
    const commentAttchments = ref([]);
    const uploadType = ref("image");
    const uploadProperty = ref({});
    const commentText = ref("");
    const star = ref(false);
    const markdownContent = ref("");
    const loadingComments = ref(false);
    const recommendEvents = ref([]);
    uploadTypeChange();

    const markdown = new MarkdownIt({
      html: true,
    });

    const customRequest = (option) => {
      let { onProgress, onError, onSuccess, fileItem, name } = option;
      commentAttchments.value.push(fileItem);
      onSuccess("ok");
    };

    onMounted(async () => {
      await loadEventsInfo(route.query.id);
      if (eventInfo.value.tickets) {
        let ticketPromises = eventInfo.value.tickets.map((ticket_id) =>
          getTicketInfo(ticket_id)
        );
        tickets.value = await Promise.all(ticketPromises);
        selectedPrice.value = 0;
        while (
          tickets.length > 0 &&
          tickets.value[selectedPrice.value].total_amount <=
            tickets.value[selectedPrice.value].lock_amount
        ) {
          selectedPrice.value++;
        }
      }
      await fetchComments(route.query.id);
      getMarkdownConetent(eventInfo.value.document_url);
      recommendEvents.value = await fetchRecommendEvents();
      await fetchStarState();
    });

    async function fetchRecommendEvents() {
      try {
        let response = await axios.post(
          `/api/recommend/get-recommendation`,
          {},
          {
            headers: {
              Authorization:
                localStorage.getItem("token_type") +
                " " +
                localStorage.getItem("access_token"),
            },
          }
        );
        let events = Object.keys(response.data);
        let return_events = [];
        if (events.length == 0) {
          let response = await axios.post(`/api/event/explore-events`);
          let explore_event = response.data;
          for (let i = 0; i < 3; i++) {
            return_events.push(explore_event[i]);
          }
        } else {
          for (let i = 0; i < events.length; i++) {
            let response = await axios.post(
              `/api/event/get-event?eventId=${events[i]}`
            );
            return_events.push(response.data);
          }
        }
        return return_events;
      } catch (error) {
        let return_events = [];
        let response = await axios.post(`/api/event/explore-events`);
        let explore_event = response.data;
        for (let i = 0; i < 3; i++) {
          return_events.push(explore_event[i]);
        }
        return return_events;
      }
    }

    async function getTicketInfo(ticket_id) {
      let response = await axios.post(
        `/api/ticket/get-ticket?ticketId=${ticket_id}`
      );
      return response.data;
    }

    async function fetchStarState() {
      if (!utils.verifyLoginState()) {
        star.value = false;
        return;
      }
      try {
        let response = await axios.post(
          `/api/recommend/get?eventId=${route.query.id}`,
          {},
          {
            headers: {
              Authorization:
                localStorage.getItem("token_type") +
                " " +
                localStorage.getItem("access_token"),
            },
          }
        );
        if (response.data == 0) {
          star.value = false;
        } else {
          star.value = true;
        }
      } catch (error) {
        console.error(error);
      }
    }

    async function onStarChange() {
      if (!utils.verifyLoginState()) {
        Message.error("请先登录");
        return;
      }
      if (!star.value) {
        try {
          let response = await axios.post(
            `/api/recommend/add?eventId=${route.query.id}`,
            {},
            {
              headers: {
                Authorization:
                  localStorage.getItem("token_type") +
                  " " +
                  localStorage.getItem("access_token"),
              },
            }
          );
          if (response.status === 200) {
            Message.success("收藏成功");
          } else {
            Message.error("收藏失败");
          }
          star.value = !star.value;
        } catch (error) {
          console.error(error);
          Message.error("收藏失败");
        }
      } else {
        try {
          let response = await axios.post(
            `/api/recommend/delete?eventId=${route.query.id}`,
            {},
            {
              headers: {
                Authorization:
                  localStorage.getItem("token_type") +
                  " " +
                  localStorage.getItem("access_token"),
              },
            }
          );
          if (response.status === 200) {
            Message.success("取消收藏");
          } else {
            Message.error("取消收藏失败");
          }
          star.value = !star.value;
        } catch (error) {
          console.error(error);
          Message.error("取消收藏失败");
        }
      }
    }

    function getMarkdownConetent(url) {
      axios.get(url).then((response) => {
        markdownContent.value = String(response.data);
      });
    }

    async function loadEventsInfo(eventId) {
      try {
        let response = await axios.post(
          `/api/event/get-event?eventId=${eventId}&page=1`
        );
        eventInfo.value = response.data;
      } catch (error) {
        console.error(error);
      }
    }

    function uploadTypeChange() {
      commentAttchments.value = [];
      if (uploadType.value === "image") {
        uploadProperty.value = {
          listType: "picture-card",
          limit: 9,
          accept: "image/*",
          draggable: false,
        };
      }
      if (uploadType.value === "video") {
        uploadProperty.value = {
          listType: "text",
          limit: 1,
          accept: "video/*",
          draggable: true,
        };
      }
    }

    function openAMap(latitude, longitude) {
      let url = `https://uri.amap.com/marker?position=${longitude},${latitude}`;
      window.open(url, "_blank");
    }

    async function makeComment() {
      if (!utils.verifyLoginState()) {
        Message.error("请先登录");
        return;
      }
      if (!commentText.value || commentText.value.trim() === "") {
        Message.error("评论不能为空");
        return;
      }
      if (commentText.value.length > 200) {
        Message.error("评论不能超过200字");
        return;
      }

      let text = commentText.value;
      try {
        let response = await axios.post(
          `/api/comment/make-comment`,
          {
            event_id: route.query.id,
            content: text,
          },
          {
            headers: {
              Authorization:
                localStorage.getItem("token_type") +
                " " +
                localStorage.getItem("access_token"),
            },
          }
        );
        if (response.status === 200) {
          commentText.value = "";
          let commentId = response.data.id;
          try {
            for (let i = 0; i < commentAttchments.value.length; i++) {
              let file = commentAttchments.value[i].file;
              let formData = new FormData();
              formData.append("file", file);
              let uploadResponse = await axios.post(
                `/api/file/upload?usage=comment&commentId=${commentId}`,
                formData,
                {
                  headers: {
                    Authorization:
                      localStorage.getItem("token_type") +
                      " " +
                      localStorage.getItem("access_token"),
                  },
                }
              );
            }
            commentAttchments.value = [];
          } catch (error) {
            console.error(error);
            Message.error("评论成功，但附件上传失败");
            await fetchComments(route.query.id);
            return;
          }
          Message.success("评论成功");
          await fetchComments(route.query.id);
        } else {
          Message.error("评论失败");
        }
      } catch (error) {
        console.error(error);
        Message.error("评论失败");
      }
    }

    async function fetchComments(eventId) {
      loadingComments.value = true;
      try {
        let response = await axios.post(
          `/api/comment/get-comments?eventId=${eventId}`
        );
        let commentData = response.data;
        commentData.sort((a, b) => {
          return b.create_time - a.create_time;
        });
        comments.value = commentData;
      } catch (error) {
        console.error(error);
      } finally {
        loadingComments.value = false;
      }
    }

    // function renderMarkdown(markdownContent) {
    //   try {
    //     return markdown.render(markdownContent);
    //   } catch (error) {
    //     return "";
    //   }
    // }

    function checkDeletePermission(comment) {
      if (!utils.verifyLoginState()) {
        return false;
      }
      let permission = localStorage.getItem("permission_group");
      if (permission === "INSTITUTE_ADMIN" || permission === "SUPER_ADMIN") {
        return true;
      }
      if (comment.user_id === localStorage.getItem("uuid")) {
        return true;
      }
      return false;
    }

    async function refreshComments() {
      await fetchComments(route.query.id);
    }

    async function purchase() {
      if (!utils.verifyLoginState()) {
        Message.error("请先登录");
        return;
      }
      let ticket = tickets.value[selectedPrice.value];
      if (ticket.total_amount <= ticket.lock_amount) {
        Message.error("该票档已售罄");
        return;
      }
      try {
        let response = await axios.post(
          `/api/order/make-order`,
          {
            ticketId: ticket.id,
          },
          {
            headers: {
              Authorization:
                localStorage.getItem("token_type") +
                " " +
                localStorage.getItem("access_token"),
            },
          }
        );
        if (response.status === 200) {
          Message.success("发起订单成功");
          let payResponse = await axios.post(
            `/api/pay/pay-order?orderId=${response.data.id}&purchaseMethod=ALIPAY`,
            {},
            {
              headers: {
                Authorization:
                  localStorage.getItem("token_type") +
                  " " +
                  localStorage.getItem("access_token"),
              },
            }
          );
          if (payResponse.status !== 200) {
            throw new Error("支付订单失败");
          }
          if (payResponse.data === "Order paid successfully") {
            Message.success("购票成功，请前往后台查看");
            return;
          }
          let payWindow = window.open("", "_blank");
          payWindow.document.write(payResponse.data);
        } else {
          Message.error("发起订单失败");
        }
      } catch (error) {
        if (error.response.data.includes("unpaid")) {
          Message.error("您有未支付订单，请先支付");
        } else {
          Message.error("购票失败");
        }
      }
    }

    return {
      markdown,
      eventInfo,
      tickets,
      selectedPrice,
      comments,
      totalComments,
      commentAttchments,
      uploadType,
      uploadProperty,
      star,
      commentText,
      markdownContent,
      loadEventsInfo,
      openAMap,
      uploadTypeChange,
      customRequest,
      makeComment,
      onStarChange,
      //   renderMarkdown,
      checkDeletePermission,
      refreshComments,
      loadingComments,
      purchase,
      recommendEvents,
    };
  },
};
</script>

<template>
  <div class="outline_container">
    <div class="main_container">
      <div class="upper_container">
        <div class="post_container">
          <CustomImage
            :src="eventInfo.image_url"
            :fallbackSrc="'error.png'"
            alt="event image"
            class="event_post"
          />
        </div>
        <div class="details_container">
          <div class="title_container">
            <h1>{{ eventInfo.category + " | " + eventInfo.title }}</h1>
            <span class="action" key="star" @click="onStarChange">
              <span v-if="star">
                <IconStarFill :style="{ color: '#ffb400' }" />
              </span>
              <span v-else>
                <IconStar />
              </span>
            </span>
          </div>

          <p class="details_item">
            <strong>时间:</strong>
            <a-tag
              >{{ $formatDateTime(eventInfo.start_time) }} -
              {{ $formatDateTime(eventInfo.end_time) }}</a-tag
            >
          </p>
          <p class="details_item">
            <strong>地点:</strong> <a-tag>{{ eventInfo.location_name }}</a-tag>
            <ALink @click="openAMap(eventInfo.latitude, eventInfo.longitude)">
              <IconLocation />
            </ALink>
          </p>

          <div class="details_item" v-if="tickets.length > 0">
            <a-radio-group
              type="button"
              :model-value="selectedPrice"
              @change="(val) => (selectedPrice = val)"
            >
              <a-radio
                v-for="(ticket, index) in tickets"
                :key="ticket.id"
                :value="index"
                :disabled="ticket.total_amount <= ticket.lock_amount"
                >{{ ticket.description + " " + ticket.price }} 元</a-radio
              >
            </a-radio-group>
          </div>

          <a-button
            v-if="selectedPrice < tickets.length"
            type="primary"
            class="details_item"
            @click="purchase()"
          >
            购票
          </a-button>
          <a-button
            v-if="(tickets.length > 0) & (selectedPrice >= tickets.length)"
            type="secondary"
            class="details_item"
            :disabled="true"
          >
            售罄
          </a-button>
          <a-button
            v-if="tickets.length === 0"
            type="secondary"
            class="details_item"
            :disabled="true"
          >
            暂无票务信息
          </a-button>
        </div>
      </div>
      <div class="lower_conatiner">
        <a-tabs>
          <a-tab-pane key="1">
            <template #title> 项目详情 </template>
            <div class="description_container">
              <!-- 这里是项目详情 -->
              <!-- <div
                v-html="renderMarkdown(markdownContent)"
                class="markdown-content"
              ></div> -->
              <div class="markdown-html">
                <VueMarkdownEditor
                  v-model="markdownContent"
                  mode="preview"
                ></VueMarkdownEditor>
              </div>
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
            <template #title> 评论 </template>
            <div class="comment">
              <div v-if="!loadingComments">
                <Comment
                  v-for="(item, index) in comments"
                  :key="index"
                  :comment="item"
                  :delete="checkDeletePermission(item)"
                  v-on:delete="refreshComments()"
                />
              </div>
              <div class="my_comment">
                <a-input placeholder="" v-model="commentText" />
                <a-button type="primary" @click="makeComment">评论</a-button>
              </div>
              <div style="margin-top: 10px">
                <a-space>
                  <a-radio-group
                    v-model="uploadType"
                    @change="uploadTypeChange"
                  >
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
                  <template #start_icon> </template>
                </a-upload>
              </div>
            </div>
          </a-tab-pane>
        </a-tabs>
      </div>
    </div>
    <div class="right_container">
      <Recommends :eventsData="recommendEvents" />
    </div>
  </div>
</template>

<style scoped>
.description_container {
  align-items: center;
  width: 80vh;
  margin: auto;
}

.markdown-html {
  padding: 20px;
  margin: auto;
  max-width: 1000px;
  background-color: #ffffff;
}

.outline_container {
  margin-top: 2vh;
  /* border: 1px solid #ccc; */
  display: flex;
  align-items: flex-start;
  justify-content: center;
  flex-direction: row;
  width: 90vw;
}

.main_container {
  flex: 1;
  /* width: 60vw; */
  /* border-right: 1px solid #ccc; */
  /* border-left: 1px solid #ccc; */
}

.right_container {
  /* width: 25%; */
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-left: 10px;
  /* border-right: 1px solid #ccc; */
}

.upper_container {
  display: flex;
  justify-content: flex-start;
}

.post_container {
  height: 50%;
  width: 50%;
}

.lower_conatiner {
  margin-top: 2vh;
  width: 100%;
  height: auto;
}

.comment {
  margin: 10px;
}

.my_comment {
  margin-top: 1vh;
  display: flex;
  align-items: center;
  gap: 10px;
}

.details_item {
  display: flex;
  align-items: center;
  justify-items: center;
  gap: 1vh;
  margin-bottom: 20px;
}

.event_post {
  width: 100%;
  height: 100%;
  padding: 3vh;
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
</style>
