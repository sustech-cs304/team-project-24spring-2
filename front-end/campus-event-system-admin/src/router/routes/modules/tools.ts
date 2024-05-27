import { basicPerm, auditPerm, superPerm } from '@/store/modules/user/types';
import { DEFAULT_LAYOUT } from '../base';
import { AppRouteRecordRaw } from '../types';

const TOOLS: AppRouteRecordRaw = {
  path: '/tools',
  name: 'tools',
  component: DEFAULT_LAYOUT,
  meta: {
    locale: 'menu.tools',
    requiresAuth: true,
    icon: 'icon-tool',
    order: 5,
  },
  children: [
    {
      path: 'ticket-service',
      name: 'ticketService',
      component: () => import('@/views/tools/ticket-service/index.vue'),
      meta: {
        locale: 'menu.tools.ticketService',
        requiresAuth: true,
        roles: basicPerm,
      },
    },
    
  ],
};

export default TOOLS;
