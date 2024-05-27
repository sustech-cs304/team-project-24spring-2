<template>
  <div class="container">
    <Breadcrumb :items="['menu.user', 'menu.user.setting']" />
    <a-row style="margin-bottom: 16px">
      <a-col :span="24">
        <UserPanel v-model="userInfo" />
      </a-col>
    </a-row>
    <a-row class="wrapper">
      <a-col :span="24">
        <a-tabs default-active-key="1" type="rounded">
          <a-tab-pane key="1" :title="$t('userSetting.tab.basicInformation')">
            <BasicInfo v-model="userInfo" @save-basic="onSaveBasic" @reset-info="onResetInfo" />
          </a-tab-pane>
          <!-- <a-tab-pane key="2" :title="$t('userSetting.tab.securitySettings')">
            <SecuritySettings />
          </a-tab-pane> -->
          <!-- <a-tab-pane key="3" :title="$t('userSetting.tab.certification')">
            <Certification />
          </a-tab-pane> -->
        </a-tabs>
      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts" setup>
  import { UserState } from '@/store/modules/user/types';
  import { useUserStore } from '@/store';
  import { updateUser, updateUserForm } from '@/api/user';
  import { Notification } from '@arco-design/web-vue';
  import { keys } from 'lodash';
  import { ref,onBeforeMount } from 'vue';

  import useLoading from '@/hooks/loading';
  import UserPanel from './components/user-panel.vue';
  import BasicInfo from './components/basic-info.vue';
  import SecuritySettings from './components/security-settings.vue';
  //   import Certification from './components/certification.vue';

  const { loading, setLoading } = useLoading(true);
  const userStore = useUserStore();
  const userInfo = ref<UserState>({} as UserState);
  const userInfoOrigin = ref<UserState>({} as UserState);
  const fetchData = async () => {
    try {
      setLoading(true);
      await userStore.info();
      userInfo.value = { ...userStore.userInfo };
      userInfoOrigin.value = { ...userStore.userInfo };

    } catch (err) {
      console.log(err);
    } finally {
      setLoading(false);
    }
  };

  const onSaveBasic = async () => {
    const submition = ref<updateUserForm>({} as updateUserForm);

    if (userInfo.value.phone !== userInfoOrigin.value.phone) {
      submition.value.phone = userInfo.value.phone;
    }
    if (userInfo.value.description !== userInfoOrigin.value.description) {
      submition.value.description = userInfo.value.description;
    }
    if (userInfo.value.gender !== userInfoOrigin.value.gender) {
      submition.value.gender = userInfo.value.gender;
    }

    const updateKey = keys(submition.value);
    if (updateKey.length === 0) {
      Notification.info({
        title: '没有更新',
        content: '用户信息没有更新',
      });
    } else {
      const res = await updateUser(submition.value);
      Notification.success({
        title: '更新成功',
        content: '用户信息更新成功',
      });
      fetchData();
    }
  };


  const onResetInfo = () => {
    userInfo.value = { ...userInfoOrigin.value };
  };
  onBeforeMount(() => {
    fetchData();
  });
</script>

<script lang="ts">
  export default {
    name: 'Setting',
  };
</script>

<style scoped lang="less">
  .container {
    padding: 0 20px 20px 20px;
  }

  .wrapper {
    padding: 20px 0 0 20px;
    min-height: 580px;
    background-color: var(--color-bg-2);
    border-radius: 4px;
  }

  :deep(.section-title) {
    margin-top: 0;
    margin-bottom: 16px;
    font-size: 14px;
  }
</style>
