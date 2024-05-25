import { basicPerm, auditPerm, superPerm } from '@/store/modules/user/types';
import { DEFAULT_LAYOUT } from '../base';
import { AppRouteRecordRaw } from '../types';

const EVENTS: AppRouteRecordRaw = {
  path: '/event',
  name: 'event',
  component: DEFAULT_LAYOUT,
  meta: {
    locale: 'menu.event',
    requiresAuth: true,
    icon: 'icon-fire',
    order: 1,
  },
  children: [
    {
      path: 'manage',
      name: 'EventManage',
      component: () => import('@/views/event/manage/index.vue'),
      meta: {
        locale: 'menu.event.manage',
        requiresAuth: true,
        roles: basicPerm,
      },
    },
    {
      path: 'create',
      name: 'EventCreate',
      component: () => import('@/views/event/create/index.vue'),
      meta: {
        locale: 'menu.event.create',
        requiresAuth: true,
        roles: basicPerm,
      },
    },
    {
      path: 'audit-manage',
      name: 'EventAuditManage',
      component: () => import('@/views/event/audit-manage/index.vue'),
      meta: {
        locale: 'menu.event.audit',
        requiresAuth: true,
        roles: basicPerm,
      },
    },

    {
      path: 'edit',
      name: 'EventEdit',
      component: () => import('@/views/event/edit-page/index.vue'),
      meta: {
        locale: 'menu.event.edit',
        requiresAuth: true,
        hideInMenu: true,
        roles: basicPerm,
      },
    },

    {
      path: 'audit',
      name: 'EventAudit',
      component: () => import('@/views/event/audit-page/index.vue'),
      meta: {
        locale: 'menu.event.audit',
        requiresAuth: true,
        hideInMenu: true,
        roles: auditPerm,
      },
    },
    {
      path: 'view',
      name: 'EventView',
      component: () => import('@/views/event/view/index.vue'),
      meta: {
        locale: 'menu.event.audit',
        requiresAuth: true,
        hideInMenu: true,
        roles: basicPerm,
      },
    },

    {
      path: 'settings',
      name: 'EventSettings',
      component: () => import('@/views/event/settings/index.vue'),
      meta: {
        locale: 'menu.event.settings',
        requiresAuth: true,
        roles: superPerm,
      },
    },
  ],
};

export default EVENTS;
