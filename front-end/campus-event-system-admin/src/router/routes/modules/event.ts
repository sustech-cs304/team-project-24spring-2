import { DEFAULT_LAYOUT } from '../base';
import { AppRouteRecordRaw } from '../types';

const DASHBOARD: AppRouteRecordRaw = {
  path: '/event',
  name: 'event',
  component: DEFAULT_LAYOUT,
  meta: {
    locale: 'menu.event',
    requiresAuth: true,
    icon: 'icon-dashboard',
    order: 1,
  },
  children: [
    {
      path: 'manage',
      name: 'manage',
      component: () => import('@/views/event/manage/index.vue'),
      meta: {
        locale: 'menu.event.manage',
        requiresAuth: true,
        roles: ['admin'],
      },
    },
    {
      path: 'create',
      name: 'create',
      component: () => import('@/views/event/create/index.vue'),
      meta: {
        locale: 'menu.event.create',
        requiresAuth: true,
        roles: ['admin'],
      },
    },
    {
        path: 'audit',
        name: 'audit',
        component: () => import('@/views/event/audit/index.vue'),
        meta: {
          locale: 'menu.event.audit',
          requiresAuth: true,
          roles: ['admin'],
        },
      },
  ],
};

export default DASHBOARD;
