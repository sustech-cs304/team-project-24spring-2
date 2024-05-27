<template>
  <a-card :bordered="false">
    <a-space :size="54">
      <a-upload
        :custom-request="customRequest"
        list-type="picture-card"
        :file-list="fileList"
        :show-upload-button="true"
        :show-file-list="false"
        @change="uploadChange"
      >
        <template #upload-button>
          <a-avatar v-if="userStore.avatar_url" :size="100" class="info-avatar">
            <template #trigger-icon>
              <icon-camera />
            </template>
            <img :src="userStore.avatar_url" />
          </a-avatar>
          <a-avatar
            :style="{ backgroundColor: '#3370ff' }"
            :size="100"
            class="avatar"
            v-else
          >
            <IconUser />
            <template #trigger-icon>
              <icon-camera />
            </template>
          </a-avatar>
        </template>
      </a-upload>
      <a-descriptions
        :data="renderData"
        :column="2"
        align="right"
        layout="inline-horizontal"
        :label-style="{
          width: '140px',
          fontWeight: 'normal',
          color: 'rgb(var(--gray-8))',
        }"
        :value-style="{
          width: '200px',
          paddingLeft: '8px',
          textAlign: 'left',
        }"
      >
        <template #label="{ label }">{{ $t(label) }} :</template>
        <template #value="{ value, data }">
          <a-tag
            v-if="data.label === 'userSetting.label.certification'"
            color="green"
            size="small"
          >
            已认证
          </a-tag>

          <div v-else-if="data.label === 'User.info.permission_group'">
            {{ $t(`User.permission.group.${value}`) }}</div
          >
          <span v-else>{{ value }}</span>
        </template>
      </a-descriptions>
    </a-space>
  </a-card>
</template>

<script lang="ts" setup>
  import { ref, computed } from 'vue';
  import type {
    FileItem,
    RequestOption,
  } from '@arco-design/web-vue/es/upload/interfaces';
  import { useUserStore } from '@/store';
  import { uploadFile } from '@/api/file';
  import { updateUserForm, updateUser } from '@/api/user';
  import type { DescData } from '@arco-design/web-vue/es/descriptions/interface';
  import { Notification } from '@arco-design/web-vue';
  import router from '@/router';

  const userStore = useUserStore();

  const file = {
    uid: '-2',
    name: 'avatar.png',
    url: userStore.avatar_url,
  };
  const renderData = computed<DescData[]>(() => {
    return [
      {
        label: 'User.info.realname',
        value: userStore.real_name,
      },
      {
        label: 'User.info.nickname',
        value: userStore.nickname,
      },
      {
        label: 'User.info.email',
        value: userStore.email,
      },
      {
        label: 'User.info.phone',
        value: userStore.phone,
      },
      {
        label: 'User.info.permission_group',
        value: userStore.permission_group,
      },
    ];
  });

  const fileList = ref<FileItem[]>([file]);
  const uploadChange = (fileItemList: FileItem[], fileItem: FileItem) => {
    fileList.value = [fileItem];
  };

  const updateAvatar = async (url: string) => {
    const submition = ref<updateUserForm>({} as updateUserForm);
    submition.value.avatar_url = url;
    try {
      const res = await updateUser(submition.value);
      if (res) {
        Notification.success({
          title: '更新成功',
          content: '头像更新成功',
        });
        userStore.info();
    }
    } catch (error) {
      Notification.error({
        title: '更新失败',
        content: '头像更新失败',
      });
    }
  };

  const customRequest = (options: RequestOption) => {
    // docs: https://axios-http.com/docs/cancellation
    const controller = new AbortController();

    (async function requestWrap() {
      const {
        onProgress,
        onError,
        onSuccess,
        fileItem,
        name = 'file',
      } = options;
      onProgress(20);
      const formData = new FormData();
      formData.append(name as string, fileItem.file as Blob);
      const onUploadProgress = (event: ProgressEvent) => {
        let percent;
        if (event.total > 0) {
          percent = (event.loaded / event.total) * 100;
        }
        onProgress(parseInt(String(percent), 10), event);
      };

      try {
        // https://github.com/axios/axios/issues/1630
        // https://github.com/nuysoft/Mock/issues/127

        const res = await uploadFile(
          formData,
          {
            usage: 'avatar',
          },
          controller,
          onUploadProgress
        );
        onSuccess(res);
        updateAvatar(res.data);
      } catch (error) {
        onError(error);
      }
    })();
    return {
      abort() {
        controller.abort();
      },
    };
  };
  defineExpose({
    userStore,
  });
</script>

<style scoped lang="less">
  .arco-card {
    padding: 14px 0 4px 4px;
    border-radius: 4px;
  }
  :deep(.arco-avatar-trigger-icon-button) {
    width: 32px;
    height: 32px;
    line-height: 32px;
    background-color: #e8f3ff;
    .arco-icon-camera {
      margin-top: 8px;
      color: rgb(var(--arcoblue-6));
      font-size: 14px;
    }
  }
</style>
