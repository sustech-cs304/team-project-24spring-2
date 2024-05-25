import { basicPerm, auditPerm, superPerm } from '@/store/modules/user/types';
import { DEFAULT_LAYOUT } from '../base';
import { AppRouteRecordRaw } from '../types';

const USER: AppRouteRecordRaw = {
  path: '/user',
  name: 'user',
  component: DEFAULT_LAYOUT,
  meta: {
    locale: 'menu.user',
    icon: 'icon-user',
    requiresAuth: true,
    order: 8,
    roles: basicPerm,
  },
  children: [
    // {
    //   path: 'info',
    //   name: 'Info',
    //   component: () => import('@/views/user/info/index.vue'),
    //   meta: {
    //     locale: 'menu.user.info',
    //     requiresAuth: true,
    //     roles: ['*'],
    //   },
    // },
    {
      path: 'setting',
      name: 'Setting',
      component: () => import('@/views/user/setting/index.vue'),
      meta: {
        locale: 'menu.user.setting',
        requiresAuth: true,
        roles: basicPerm,
      },
    },
  ],
};

export default USER;
