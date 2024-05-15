<script>
import { reactive, ref } from 'vue';
export default {
  name: "StudentActivityRegistration",
  setup() {
    const layout = ref('horizontal')
    const registration = reactive({
      SUSTechID: '', // 学生学号
      Name: '', // 学生姓名
      telephone: '', // 电话号码
      hasReadInstructions: false, // 是否已阅读参加须知
    })

    return {
      layout,
      registration,
    }
  },
}
</script>

<template>
  <a-space direction="vertical" size="large" :style="{width: '600px'}" class="activity-registration">
    <a-form :model="registration" :layout="layout">
      <a-form-item label="SUSTech ID">
        <a-input v-model="registration.SUSTechID" placeholder="Enter your Student ID..."/>
      </a-form-item>
      <a-form-item label="Name">
        <a-input v-model="registration.Name" placeholder="Enter your name..."/>
      </a-form-item>
      <a-form-item label="Telephone">
        <a-input v-model="registration.telephone" placeholder="Enter your telephone number..."/>
      </a-form-item>
      <a-form-item>
        <a-checkbox v-model="registration.hasReadInstructions">
          I have read and understood the participation instructions
        </a-checkbox>
      </a-form-item>
      <a-form-item>
        <a-button type="primary">Submit</a-button>
      </a-form-item>
    </a-form>
  </a-space>
</template>

<style scoped>
.activity-registration {
  justify-content: center;
  margin-top: 10%;
  margin-left: 20%;
}

</style>
