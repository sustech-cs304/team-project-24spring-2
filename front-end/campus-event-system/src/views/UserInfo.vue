<script>
import { reactive, ref } from 'vue';
export default {
  name: "UserInfo",
  setup() {
    const layout = ref('horizontal')
    const form = reactive({
      name: '', // Username
      post: '',
      isRead: false,
      email: '', // New field for Email
      telephone: '', // New field for Telephone Number
      realName: '', // New field for Real Name
      SID: '', // New field for SID (Student ID, assuming)
    })

    // Make SID a read-only field
    const isSIDEditable = ref(false);

    return {
      layout,
      form,
      isSIDEditable,
    }
  },
}
</script>

<template>
  <a-space direction="vertical" size="large" :style="{width: '600px'}">
    <a-form :model="form" :layout="layout">
      <a-form-item label="Username">
        <a-input v-model="form.name" placeholder="Please enter your username..." />
      </a-form-item>
      <a-form-item label="Post">
        <a-input v-model="form.post" placeholder="Please enter your post..." />
      </a-form-item>
      <a-form-item label="Email">
        <a-input v-model="form.email" placeholder="Please enter your email..." />
      </a-form-item>
      <a-form-item label="Telephone">
        <a-input v-model="form.telephone" placeholder="Please enter your telephone number..." />
      </a-form-item>
      <a-form-item label="Real Name">
        <a-input v-model="form.realName" placeholder="Please enter your real name..." />
      </a-form-item>
      <a-form-item label="SID">
        <a-input v-model="form.SID" :disabled="!isSIDEditable" placeholder="Your SID..." />
      </a-form-item>
      <a-form-item>
        <a-checkbox v-model="form.isRead">
          I have read the manual
        </a-checkbox>
      </a-form-item>
      <a-form-item>
        <a-button>Submit</a-button>
      </a-form-item>
    </a-form>
  </a-space>
</template>

<style scoped>
</style>
