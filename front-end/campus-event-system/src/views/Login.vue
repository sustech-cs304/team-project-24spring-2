<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <h2>SUSTech Campus Events and Entertainment Center</h2>
      </div>
      <a-form @submit="handleSubmit" layout="vertical" :model="loginForm" :rules="rules" ref="form">
        <a-form-item label="用户名" prop="username">
          <a-input v-model="loginForm.user_input" placeholder="请输入用户名" />
        </a-form-item>
        <a-form-item label="密码" prop="password">
          <a-input-password v-model="loginForm.password" placeholder="请输入密码" />
        </a-form-item>
        <a-form-item>
          <a-row :gutter="16">
            <a-col :span="12">
              <a-button type="primary" html-type="submit" block class="login-button">登录</a-button>
            </a-col>
            <a-col :span="12">
              <a-button type="outline" @click="showRegister" block class="register-button">没有账户？注册</a-button>
            </a-col>
          </a-row>
        </a-form-item>
      </a-form>
      <a-modal v-model:visible="isRegisterVisible" title="注册" @ok="handleRegister" @cancel="handleCancel">
        <a-form layout="vertical" :model="registerForm"  ref="register">
          <a-form-item label="用户名" prop="nickname">
            <a-input v-model="registerForm.nickname" placeholder="请输入用户名" />
          </a-form-item>
          <a-form-item label="真名" prop="realName">
            <a-input v-model="registerForm.realName" placeholder="请输入真名" />
          </a-form-item>
          <a-form-item label="密码" prop="password">
            <a-input-password v-model="registerForm.password" placeholder="请输入密码" />
          </a-form-item>
          <a-form-item label="确认密码" prop="confirmPassword">
            <a-input-password v-model="confirmPassword" placeholder="请确认密码" />
          </a-form-item>
          <a-form-item label="邮箱" prop="email">
            <a-input-group compact>
              <a-input v-model="registerForm.email" placeholder="请输入邮箱" style="width: 320px;" />
              <a-button type="primary" @click="sendVerificationCode" style="position: absolute; right: 20px; width: 30%;">发送验证码</a-button>
            </a-input-group>
          </a-form-item>
          <a-form-item label="验证码" prop="verifyCode">
            <a-input v-model="registerForm.verifyCode" placeholder="请输入验证码" />
          </a-form-item>
          <a-form-item label="电话号码" prop="phone">
            <a-input v-model="registerForm.phone" placeholder="请输入电话号码" />
          </a-form-item>
        </a-form>
      </a-modal>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { Message } from '@arco-design/web-vue';
import { onMounted } from 'vue';


export default {


  setup() {
    const router = useRouter();
    async function handleSubmit() {
      try {
        const response = await axios.post(`/api/user/login`, loginForm.value);
        localStorage.setItem('access_token', response.data.access_token);
        localStorage.setItem('token_type', response.data.token_type);
        localStorage.setItem('expire_time', response.data.expire_time);
        localStorage.setItem('uuid', response.data.user.id);
        localStorage.setItem('permission_group', response.data.user.permission_group);
        router.push('/').then(() => {
          window.location.reload();
          Message.success('登录成功');
        });
      } catch (error) {
        Message.error('登录失败');
      }
    }

    async function showRegister() {
      isRegisterVisible.value = true;
    }

    const confirmPassword = ref('');

    async function handleRegister() {
      if (registerForm.value.password !== confirmPassword.value) {
        Message.error('密码和确认密码不匹配');
        return false;
      }

      try {
        console.log(registerForm.value);
        // console.log({ ...registerForm.value }); // 展开运算符
        // console.log(JSON.parse(JSON.stringify(toRaw(registerForm.value)))); // 深拷贝


        const response = await axios.post('/api/user/register', registerForm.value);
        console.log('注册成功:', response.data);
        Message.success('注册成功');
        // registerForm.resetFields();
        // isRegisterVisible.value = false;
      } catch (error) {
        console.log('注册失败:', error);
        Message.error('注册失败');
      }
      
    }
    onMounted(() => {
      localStorage.clear();
    });

    async function sendVerificationCode() {
      if (!registerForm.value.email) {
        Message.error('请输入邮箱');
        return false;
      }
      try {
        console.log(registerForm.value.email)
        const response = await axios.post(`/api/user/fetch-register-code?email=${registerForm.value.email}`);
        Message.success('发送验证码成功');

      } catch (error) {
        if (error.response.status === 429) {
          Message.error('发送验证码过于频繁，请稍后再试');
        }else {
        Message.error('发送验证码失败');
        }
      }
    }




    function handleCancel() {
      isRegisterVisible.value = false;
    }

    const loginForm = ref({
      user_input: '',
      password: ''
    });

    const registerForm = ref({
      nickname: '',
      password: '',
      verifyCode: '',
      email: '',
      phone: '',
      realName: '',
    });

    const isRegisterVisible = ref(false);

    const rules = ref({
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' }
      ]
    });



    return {
      loginForm,
      registerForm,
      isRegisterVisible,
      rules,
      handleSubmit,
      showRegister,
      handleRegister,
      handleCancel,
      sendVerificationCode,
      confirmPassword
    };

  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: auto;
  height: 50vh;
}

.login-card {
  width: 400px;
  padding: 40px;
  background-color: rgba(255, 255, 255, 0.9);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}

.login-header {
  text-align: center;
  margin-bottom: 20px;
}

.login-header h2 {
  font-size: 24px;
  color: #333;
}

a-form-item {
  margin-bottom: 20px;
}

a-input,
a-input-password {
  height: 40px;
  font-size: 16px;
}

.row-buttom-item {
  display: flex;
  justify-content: space-between;

}

.login-button,
.register-button {
  
  height: 40px;
  font-size: 16px;
  border-radius: 4px;
}

a-button[type="link"] {
  font-size: 14px;
  color: #1890ff;
  border: 1px solid transparent;
  padding: 0 15px;
  height: 40px;
  line-height: 40px;
  display: block;
  text-align: center;
  border-radius: 4px;
}
</style>
