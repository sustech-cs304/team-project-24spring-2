import { DEFAULT_LAYOUT } from '../base';
import { AppRouteRecordRaw } from '../types';

const DASHBOARD: AppRouteRecordRaw = {
  path: '/activity',
  name: 'activity',
  component: DEFAULT_LAYOUT,
  meta: {
    locale: 'menu.activity',
    requiresAuth: true,
    icon: 'icon-dashboard',
    order: 1,
  },
  children: [
    {
      path: 'manage',
      name: 'manage',
      component: () => import('@/views/activity/manage/index.vue'),
      meta: {
        locale: 'menu.activity.manage',
        requiresAuth: true,
        roles: ['admin'],
      },
    },
    {
      path: 'create',
      name: 'create',
      component: () => import('@/views/activity/create/index.vue'),
      meta: {
        locale: 'menu.activity.create',
        requiresAuth: true,
        roles: ['admin'],
      },
    },
  ],
};

export default DASHBOARD;
