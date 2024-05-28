<script>
import {
  IconHeartFill,
  IconStarFill,
} from '@arco-design/web-vue/es/icon';
import { reactive, ref, onMounted } from 'vue';
import axios from 'axios';
import CustomImage from '@/components/CustomImage.vue';
import { Message } from '@arco-design/web-vue';
import { IconEdit } from '@arco-design/web-vue/es/icon';
import OrderCard from '@/components/OrderCard.vue';
import TicketCard from '@/components/TicketCard.vue';
import utils from '@/api/utils.ts';
import { useRouter } from 'vue-router';
import RecommendCard from '@/components/RecommendCard.vue';



export default {
  name: "UserInfo",
  components: {
    CustomImage,
    IconEdit,
    OrderCard,
    TicketCard,
    RecommendCard,
    IconHeartFill,
    IconStarFill,
  },
  setup() {

    const router = useRouter();

    const user = ref({});

    const form = reactive({
      nickname: '',
      email: '',
      phone: '',
      realName: '',
      birthday: '',
      description: '',
      gender: '',
    })

    const password = ref('');
    const confirmPassword = ref('');
    const orders = ref([]);
    const tickets = ref([]);
    const starEvents = ref([]);

    onMounted(async () => {
      if (!utils.verifyLoginState(true)) {
        router.push('/').then(() => {
          window.location.reload();
        });
      }
      user.value = await fetchUser();
      if (user.value.avatar_url === null) {
        user.value.avatar_url = 'error.png';
      }
      form.realName = user.value.real_name;
      form.nickname = user.value.nickname;
      form.email = user.value.email;
      form.phone = user.value.phone;
      form.birthday = user.value.birthday;
      form.description = user.value.description;
      form.gender = user.value.gender;

      orders.value = await fetchOrders();
      tickets.value = await fetchTickets();
    
      await fetchStarEvents();
    });

    async function fetchOrders() {
      let response = await axios.post(`/api/order/get-orders`, {},
        {
          headers: {
            'Authorization': localStorage.getItem('token_type') + ' ' + localStorage.getItem('access_token')
          }
        }
      );
      let ordersData = response.data;
      // sort by order_create_time
      ordersData.sort((a, b) => {
        return b.order_create_time - a.order_create_time;
      });
      return ordersData;
    }


    async function fetchTickets() {
      let response = await axios.post(`/api/user/get-tickets`, {},
        {
          headers: {
            'Authorization': localStorage.getItem('token_type') + ' ' + localStorage.getItem('access_token')
          }
        }
      );
      let ticketData = response.data;
      for (let i = 0; i < ticketData.length; i++) {
        let response = await axios.post(`/api/ticket/get-ticket?ticketId=${ticketData[i].ticket_id}`);
        ticketData[i].ticketInfo = response.data;
        let event_response = await axios.post(`/api/event/get-event?eventId=${ticketData[i].ticketInfo.event_id}`);
        ticketData[i].eventInfo = event_response.data;
      }
      ticketData.sort((a, b) => {
        if (a.checked_in && !b.checked_in) {
          return 1;
        } else if (!a.checked_in && b.checked_in) {
          return -1;
        } else {
          return b.eventInfo.end_time - a.eventInfo.end_time;
        }

      });
      return ticketData;
    }


    const fetchUser = async () => {
      const response = await axios.post(`/api/user/profile`, {},
        {
          headers: {
            'Authorization': localStorage.getItem('token_type') + ' ' + localStorage.getItem('access_token')
          }
        }
      );
      return response.data;
    };

    
    async function updateUser() {
      let changeForm = JSON.parse(JSON.stringify(form));
      if (password.value !== '') {
        if(password.value!= confirmPassword.value){
          Message.error('两次密码不一致');
          return;
        }
        changeForm.password = password.value;
      }
      const response = await axios.post(`/api/user/update-profile`, changeForm,
        {
          headers: {
            'Authorization': localStorage.getItem('token_type') + ' ' + localStorage.getItem('access_token')
          }
        }
      );
      if (response.status === 200) {
        Message.success('更新成功');
        user.value = await fetchUser();
        if (user.value.avatar_url === null) {
          user.value.avatar_url = 'error.png';
        }
        form.data = user.value;
      } else {
        Message.error('更新失败');
      }
      return response.data;
    }

    async function uploadAvatar(){
      const formData = new FormData();
      const file = await new Promise((resolve) => {
        const input = document.createElement('input');
        input.type = 'file';
        input.accept = 'image/*';
        input.onchange = () => {
          resolve(input.files[0]);
        };
        input.click();
      });
      formData.append('file', file);
      const response = await axios.post(`/api/file/upload?usage=avatar`, 
        formData,
        {
          headers: {
            'Authorization': localStorage.getItem('token_type') + ' ' + localStorage.getItem('access_token')
          }
        }
      );
      if (response.status === 200) {
        Message.success('上传成功');
        user.value.avatar_url = response.data;
        window.location.reload();
      } else {
        Message.error('上传失败');
      }
      return response.data;

    }

    async function fetchStarEvents() {
      let response = await axios.post(`/api/recommend/get-ratings`, {},
        {
          headers: {
            'Authorization': localStorage.getItem('token_type') + ' ' + localStorage.getItem('access_token')
          }
        }
      );
      let starEventIds = response.data;

      for (let i = 0; i < starEventIds.length; i++) {
        let response = await axios.post(`/api/event/get-event?eventId=${starEventIds[i]}`);
        starEvents.value.push(response.data);
      }
      
      return response.data;
    }



    return {
      form,
      user,
      orders,
      tickets,
      password,
      confirmPassword,
      starEvents,
      updateUser,
      uploadAvatar,
    }
  },
}
</script>

<template>
  <div class="main_container">
    <div class="profile">
      <div class="avatar">
        <a-avatar :size="200" trigger-type="mask" @click="uploadAvatar">
          <CustomImage
            alt="avatar"
            :src="user.avatar_url"
            fallbackSrc="test1.jpeg"

          />
          <template #trigger-icon>
            <IconEdit />
          </template>
        </a-avatar>
      </div>
      <div class="details">
        <a-space direction="vertical" size="large" :style="{width: '600px'}" >
          <a-form :model="form" :layout=horizontal>
            <a-form-item label="真实姓名">
              <a-input v-model="form.realName" :disabled=true />
            </a-form-item>
            <a-form-item label="昵称">
              <a-input v-model="form.nickname" :disabled=true  />
            </a-form-item>
            <a-form-item label="性别">
              <a-radio-group v-model="form.gender">
                <a-radio value="MALE">男</a-radio>
                <a-radio value="FEMALE">女</a-radio>
                <a-radio value="OTHER">未指定</a-radio>
              </a-radio-group>
            </a-form-item>
            <a-form-item label="生日">
              <a-date-picker v-model="form.birthday" />
            </a-form-item>

            <a-form-item label="邮箱">
              <a-input v-model="form.email" placeholder="" :disabled=true />
            </a-form-item>
            <a-form-item label="电话">
              <a-input v-model="form.phone" placeholder="" />
            </a-form-item>
            <a-form-item label="个人简介">
              <a-input v-model="form.description" placeholder="" />
            </a-form-item>
            <a-form-item label="修改密码">
              <a-input-password v-model="password" placeholder="" />
            </a-form-item>
            <a-form-item label="重复密码">
              <a-input-password v-model="confirmPassword" placeholder="" />
            </a-form-item>
            <a-form-item>
              <a-button type="primary" @click="updateUser">保存</a-button>
            </a-form-item>
          </a-form>
        </a-space>
      </div>
    </div>
    <div class="bottom_container">
      <a-tabs>
          <a-tab-pane key="1">
            <template #title>
              <!-- <IconStarFill /> -->
              收藏
            </template>
            <div class="Information">
              <RecommendCard v-for="(item, index) in starEvents" :key="index" :event="item" style="margin-bottom: 15px;" />
            </div>
          </a-tab-pane>
          <a-tab-pane key="2">
            <template #title>
              订单
            </template>
            <OrderCard v-for="(item, index) in orders" :key="index" :order="item" />
          </a-tab-pane>
          <a-tab-pane key="3">
            <template #title>
              票务
            </template>
            <div class="Information">
              <!-- 这里是票务 -->
              <TicketCard v-for="(item, index) in tickets" :key="index" :ticket="item" />
            </div>
          </a-tab-pane>
        </a-tabs>
    </div>
  </div>



</template>
<style scoped>
.main_container {
  margin-top: 2vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 70vw;
  flex: 1;
}
.profile {
  width: 100%;
  height: auto;
  display: flex;
  justify-content: center; /* Center horizontally */
}
.avatar {
  flex: 1;
  width: 100%;
  height: auto;
  display: flex;
  justify-content: center; /* Center horizontally */
}
.details {
  flex: 3;
}

.bottom_container {
  /* margin-top: 2vh; */
  width: 100%;
  height: auto;
}



</style>