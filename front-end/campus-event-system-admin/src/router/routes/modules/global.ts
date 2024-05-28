import { basicPerm, auditPerm, superPerm } from '@/store/modules/user/types';
import { DEFAULT_LAYOUT } from '../base';
import { AppRouteRecordRaw } from '../types';

const EVENTS: AppRouteRecordRaw = {
  path: '/global',
  name: 'global',
  component: DEFAULT_LAYOUT,
  meta: {
    locale: 'menu.global',
    requiresAuth: true,
    icon: 'icon-settings',
    order: 1,
  },
  children: [
    {
      path: 'settings',
      name: 'GlobalSettings',
      component: () => import('@/views/global/settings/index.vue'),
      meta: {
        locale: 'menu.global.settings',
        requiresAuth: true,
        roles: auditPerm,
      },
    },
  ],
};

export default EVENTS;
