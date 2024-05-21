<script>
import { reactive, ref, onMounted } from 'vue';
import axios from 'axios';
import CustomImage from '@/components/CustomImage.vue';
import { Message } from '@arco-design/web-vue';
import { IconEdit } from '@arco-design/web-vue/es/icon';

export default {
  name: "UserInfo",
  components: {
    CustomImage,
    IconEdit,
  },
  setup() {

    const user = ref({});

    const form = reactive({
      nickname: '', // Username
      email: '', // New field for Email
      phone: '', // New field for Telephone Number
      realName: '', // New field for Real Name
      birthday: '', // New field for Birthday
      description: '', // New field for Description
      gender: '', // New
    })

    const password = ref('');
    const confirmPassword = ref('');

    onMounted(async () => {
      user.value = await fetchUser();
      form.realName = user.value.real_name;
      form.nickname = user.value.nickname;
      form.email = user.value.email;
      form.phone = user.value.phone;
      form.birthday = user.value.birthday;
      form.description = user.value.description;
      form.gender = user.value.gender;
    });


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
      console.log(response);
      if (response.status === 200) {
        Message.success('上传成功');
        user.value.avatar_url = response.data;
        window.location.reload();
      } else {
        Message.error('上传失败');
      }
      return response.data;

    }


    return {
      form,
      user,
      password,
      confirmPassword,
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
            <a-form-item label="Real Name">
              <a-input v-model="form.realName" :disabled=true />
            </a-form-item>
            <a-form-item label="Nickname">
              <a-input v-model="form.nickname" :disabled=true  />
            </a-form-item>
            <a-form-item label="Gender">
              <a-radio-group v-model="form.gender">
                <a-radio value="MALE">Male</a-radio>
                <a-radio value="FEMALE">Female</a-radio>
                <a-radio value="OTHER">Other</a-radio>
              </a-radio-group>
            </a-form-item>
            <a-form-item label="Birthday">
              <a-date-picker v-model="form.birthday" />
            </a-form-item>

            <a-form-item label="Email">
              <a-input v-model="form.email" placeholder="Please enter your email..." />
            </a-form-item>
            <a-form-item label="Telephone">
              <a-input v-model="form.phone" placeholder="Please enter your telephone number..." />
            </a-form-item>
            <a-form-item label="Description">
              <a-input v-model="form.description" placeholder="Please enter your description..." />
            </a-form-item>
            <a-form-item label="Password">
              <a-input-password v-model="password" placeholder="Please enter your description..." />
            </a-form-item>
            <a-form-item label="confirmPassword">
              <a-input-password v-model="confirmPassword" placeholder="Please enter your description..." />
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
              收藏
            </template>
            <div class="Information">
              这里是收藏
            </div>
          </a-tab-pane>
          <a-tab-pane key="2">
            <template #title>
              订单
            </template>
            <div class="Information">
              这里是订单
            </div>
          </a-tab-pane>
          <a-tab-pane key="3">
            <template #title>
              票务
            </template>
            <div class="Information">
              这里是票务
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