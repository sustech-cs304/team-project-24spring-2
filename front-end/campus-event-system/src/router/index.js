import { createRouter, createWebHistory } from 'vue-router'

import Homepage from "@/views/Homepage.vue";
import Layout from "@/layout/Layout.vue";
import events from "@/views/Events.vue";
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/homepage',
      name: 'homepage',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: Layout,
      children: [
          {
            path: '/events',
            name: 'events',
            component: events
          }
      ]
    }
  ]
})

export default router
