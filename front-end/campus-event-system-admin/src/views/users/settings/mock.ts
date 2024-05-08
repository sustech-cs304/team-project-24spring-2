import Mock from 'mockjs';
import setupMock, { successResponseWrap } from '@/utils/setup-mock';
import { UsersGroup } from '@/api/users';

const userPermissionGroups: UsersGroup[] = [
  {
    id: 0,
    icon: 'code',
    title: '活动管理员',
    name: 'manager',
    description:
      '高级管理员一般为校方所有，拥有审核、发布、删除等权限。',
  },
  {
    id: 1,
    icon: 'code',
    title: '高级用户',
    name: 'advance_user',
    description:
      '高级用户一般为校组织所有，拥有申请，编辑活动等权限。',
  },
  {
    id: 2,
    icon: 'code',
    title: '普通用户',
    name: 'normal_user',
    description:
      '普通用户一般为学生所有，拥有查看活动，报名等权限。',
  },
  {
    id: 3,
    icon: 'code',
    title: '游客',
    name: 'visitor',
    description:
      '游客一般为未登录用户，拥有查看活动等权限。',
  },
];

setupMock({
  setup() {
    // the service
    Mock.mock(new RegExp('/api/users/permission-group'), () => {
      return successResponseWrap(
        userPermissionGroups.map((_, index) => ({
          ...userPermissionGroups[index % userPermissionGroups.length],
          id: Mock.Random.guid(),
        }))
      );
    });
  },
});
