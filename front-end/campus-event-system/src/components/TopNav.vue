<script>
import {
  IconMenuFold,
  IconMenuUnfold,
  IconApps,
  IconBug,
  IconBulb,
  IconSettings,
  IconUser,
  IconQuestionCircle,
  IconHome,
  IconExport,
  IconSort,
} from '@arco-design/web-vue/es/icon';
import { useRoute, useRouter } from 'vue-router';  // Import routing composable

export default {
  name: "TopNav",
  components: {
    IconMenuFold,
    IconMenuUnfold,
    IconApps,
    IconBug,
    IconBulb,
    IconSettings,
    IconUser,
    IconQuestionCircle,
    IconHome,
    IconExport,
    IconSort,
  },
  setup() {
    const router = useRouter();  // Use the useRouter composable for navigation

    // Method to navigate to different routes
    const navigate = (path) => {
      router.push(path);
    }

    return { navigate };
  }
}
</script>


<template>
  <div class="top-navbar">
    <div class="brand" @click="navigate('/')">
      <img src="@/assets/LOGO.png" alt="Logo" class="logo-image">
      <span class="logo">Campus Events and Entertainment Center</span>
    </div>
    <div class="navigation-icons">
      <icon-home @click="navigate('/homepage')"/>
      <icon-apps @click="navigate('/events')"/>
      <icon-user @click="navigate('/userinfo')"/>
      <icon-settings @click="navigate('/settings')"/>
      <icon-bug @click="navigate('/report-issue')"/>
      <icon-question-circle @click="navigate('/faq')"/>
      <icon-export @click="navigate('/logout')"/>
    </div>
  </div>
</template>


<style scoped>
.top-navbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 20px;
  height: 60px; /* Increased to accommodate a larger logo */
  background-color: #fff;
}

.brand {
  display: flex;
  align-items: center;
  cursor: pointer; /* Make the logo clickable */
}

.logo-image {
  margin-right: 15px; /* Slightly more spacing to handle a larger image */
  height: 50px; /* Increased height from 40px to 50px */
  width: auto; /* Maintain aspect ratio */
}

.logo {
  font-family: 'Roboto', sans-serif;
  font-weight: bold;
}

.navigation-icons > * {
  margin-right: 20px; /* Space out icons */
  cursor: pointer; /* Indicate icons are clickable */
  vertical-align: middle; /* Align icons vertically in the middle */
}
</style>
