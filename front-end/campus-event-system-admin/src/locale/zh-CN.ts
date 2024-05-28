import localeMessageBox from '@/components/message-box/locale/zh-CN';
import localeLogin from '@/views/login/locale/zh-CN';

import localeWorkplace from '@/views/dashboard/workplace/locale/zh-CN';

import localeMonitor from '@/views/dashboard/monitor/locale/zh-CN';

import localeEventTable from '@/views/event/manage/locale/zh-CN';
import localeEventCreate from '@/views/event/create/locale/zh-CN';
import localeEventAuditMange from '@/views/event/audit-manage/locale/zh-CN';
import localeEventSettings from '@/views/global/settings/locale/zh-CN';
import localeEventEdit from '@/views/event/edit-page/locale/zh-CN';
import localeEventAudit from '@/views/event/audit-page/locale/zh-CN';

// import localeSearchTable from '@/views/list/search-table/locale/zh-CN';

import localeUsersManage from '@/views/users/manage/locale/zh-CN';

import localeGlobalSettings from '@/views/global/settings/locale/zh-CN';
// import localeCardList from '@/views/list/card/locale/zh-CN';

// import localeStepForm from '@/views/form-backup/step/locale/zh-CN';
// import localeGroupForm from '@/views/form-backup/group/locale/zh-CN';

import localeBasicProfile from '@/views/profile/basic/locale/zh-CN';


import localeSuccess from '@/views/result/success/locale/zh-CN';
import localeError from '@/views/result/error/locale/zh-CN';

import locale403 from '@/views/exception/403/locale/zh-CN';
import locale404 from '@/views/exception/404/locale/zh-CN';
import locale500 from '@/views/exception/500/locale/zh-CN';



import localeUserSetting from '@/views/user/setting/locale/zh-CN';

import localeSettings from './zh-CN/settings';
import localeCommon from './zh-CN/common';



export default {
  'menu.dashboard': '仪表盘',
  'menu.server.dashboard': '仪表盘-服务端',
  'menu.server.workplace': '工作台-服务端',
  'menu.server.monitor': '实时监控-服务端',
  'menu.list': '列表页',
  'menu.tools': '工具页',
  'menu.result': '结果页',
  'menu.exception': '异常页',
  'menu.global': '全局设置',
  'menu.event': '活动管理页',
  'menu.users': '用户管理页',
  'menu.user': '个人中心',
  'menu.faq': '常见问题',
  'navbar.docs': '文档中心',
  'navbar.action.locale': '切换为中文',
  'menu.event.info': '活动详情',

  'menu.aboutCES': '关于CES',
  'CSE.Console': '校园活动管理系统 - 控制台',
  ...localeCommon,
  ...localeSettings,
  ...localeMessageBox,
  ...localeLogin,
  ...localeWorkplace,
  ...localeEventTable,
  ...localeEventCreate,
  ...localeMonitor,
//   ...localeStepForm,
//   ...localeGroupForm,
  ...localeBasicProfile,
  ...localeSuccess,
  ...localeError,
  ...locale403,
  ...locale404,
  ...locale500,
  ...localeUserSetting,
  ...localeEventAudit,
  ...localeEventSettings,
  ...localeUsersManage,
  ...localeEventEdit,
  ...localeEventAuditMange,
  ...localeGlobalSettings
};
