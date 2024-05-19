<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <h2>SUSTech Campus Events and Entertainment Center</h2>
      </div>
      <a-form @submit="handleSubmit" layout="vertical" :model="loginForm" :rules="rules" ref="form">
        <a-form-item label="用户名" prop="username">
          <a-input v-model="loginForm.username" placeholder="请输入用户名" />
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
              <a-button type="link" @click="showRegister" block class="register-button">没有账户？注册</a-button>
            </a-col>
          </a-row>
        </a-form-item>
      </a-form>
      <a-modal v-model:visible="isRegisterVisible" title="注册" @ok="handleRegister" @cancel="handleCancel">
        <a-form layout="vertical" :model="registerForm" :rules="registerRules" ref="registerForm">
          <a-form-item label="用户名" prop="username">
            <a-input v-model="registerForm.username" placeholder="请输入用户名" />
          </a-form-item>
          <a-form-item label="密码" prop="password">
            <a-input-password v-model="registerForm.password" placeholder="请输入密码" />
          </a-form-item>
          <a-form-item label="确认密码" prop="confirmPassword">
            <a-input-password v-model="registerForm.confirmPassword" placeholder="请确认密码" @input="validateConfirmPassword" />
          </a-form-item>
          <a-form-item label="邮箱" prop="email">
            <a-input v-model="registerForm.email" placeholder="请输入邮箱" />
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

export default {
  data() {
    return {
      loginForm: {
        user_input: '',
        password: ''
      },
      registerForm: {
        user_input: '',
        email: '',
        phone: '',
        password: '',
        confirmPassword: ''
      },
      isRegisterVisible: false,
      rules: {
        user_input: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ]
      },
      registerRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请确认密码', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { type: 'email', message: '请输入有效的邮箱地址', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入电话号码', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入有效的电话号码', trigger: 'blur' }
        ]
      }
    };
  },
  methods: {
    async handleSubmit() {
      try {
        const response = await axios.post(`http://localhost:8080/api/user/login`, this.loginForm);
        console.log('登录成功:', response.data);
      } catch (error) {
        console.log('登录失败:', error);
      }
    },
    showRegister() {
      this.isRegisterVisible = true;
    },
    async handleRegister() {  
      if (this.registerForm.password !== this.registerForm.confirmPassword) {
        this.$message.error('密码和确认密码不匹配');
        return false;
      }

      try {
        const response = await axios.post('/api/user/register', this.registerForm);
        console.log('注册成功:', response.data);
        // 处理成功逻辑
        this.$refs.registerForm.resetFields();
        this.isRegisterVisible = false;
      } catch (error) {
        console.log('注册失败:', error);
        // 处理失败逻辑
      }
      
    },

    validateConfirmPassword() {
      this.$refs.registerForm.validateField('confirmPassword');
    },
    handleCancel() {
      this.isRegisterVisible = false;
    },
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: url('college.jpg') no-repeat center center;
  background-size: cover;
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
