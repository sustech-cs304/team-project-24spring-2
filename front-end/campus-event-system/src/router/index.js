import { createRouter, createWebHistory } from 'vue-router'

import Homepage from "@/views/Homepage.vue";
import Layout from "@/layout/Layout.vue";
import events from "@/views/Events.vue";
import userInfo from "@/views/UserInfo.vue";
import EventInfo from "@/views/EventInfo.vue";
import Login from "@/views/Login.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/homepage'
    },
    {
      path: '/login',
      name: 'login',
      component: Login
    },
    {
      path: '/layout',
      name: 'layout',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: Layout,
      children: [
          {
            path: '/events',
            name: 'events',
            component: events
          },
          {
            path: '/homepage',
            name: 'homepage',
            component: Homepage
          },
          {
            path: '/userinfo',
            name: 'userinfo',
            component: userInfo
          },
          {
            path: '/eventInfo',
            name: 'eventInfo',
            component: EventInfo
          },
      ]
    }
  ]
})

export default router;
