<template>
  <a-card class="general-card">
    <template #title>
      {{ $t('Event.Publisher.info') }}
    </template>
    <div class="avatar-content">
      <a-avatar
        :size="180"
        class="avatar"
        v-if="props.userInfo.avatar_url != null"
      >
        <img :src="props.userInfo.avatar_url" />
      </a-avatar>
      <a-avatar
        :style="{ backgroundColor: '#3370ff' }"
        :size="180"
        class="avatar"
        v-else
      >
        <IconUser />
      </a-avatar>
    </div>

    <a-descriptions
      :data="(renderData as any)"
      :column="1"
      layout="horizontal"
      :label-style="{
        width: '20%',
        fontWeight: 'normal',
        color: 'rgb(var(--gray-8))',
        marginBottom: '36px',
        fontSize: '20px',
      }"
      :value-style="{
        width: '100px',
        paddingLeft: '8px',
        textAlign: 'center',
        fontSize: '20px',
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
        <span v-else-if="data.label === 'User.info.gender'">
          <div v-if="value === 'MALE'">
            <icon-man /> {{ $t('User.info.gender.male') }}
          </div>
          <div v-else-if="value == 'FEMALE'">
            <icon-woman />{{ $t('User.info.gender.female') }}
          </div>
          <div v-else-if="value == 'OTHER'">
            <icon-user /> {{ $t('User.info.gender.other') }}
          </div>
          <div v-else-if="value == ''">
            <icon-robot />
            {{ $t('User.info.gender.wierd') }}
          </div>
        </span>
        <span v-else-if="data.label === 'User.info.description'">
          <div v-if="value === null || value === ''">
            {{ $t('User.info.description.empty') }}
          </div>
          <div class="description" v-else>{{ value }}</div>
        </span>
        <span v-else>{{ value }}</span>
      </template>
    </a-descriptions>
  </a-card>
</template>

<script lang="ts" setup>
  import { computed, defineProps } from 'vue';
  import { UserState } from '@/store/modules/user/types';
  import { DescData } from '@arco-design/web-vue';

  const props = defineProps<{
    loading: boolean;
    userInfo: UserState;
  }>();

  const renderData = computed(() => {
    return [
      {
        label: 'User.info.nickname',
        value: props.userInfo.nickname,
      },
      {
        label: 'User.info.realname',
        value: props.userInfo.real_name,
      },
      {
        label: 'User.info.gender',
        value: props.userInfo.gender,
      },

      {
        label: 'User.info.email',
        value: props.userInfo.email,
      },
      {
        label: 'User.info.phone',
        value: props.userInfo.phone,
      },
    ];
  });
</script>

<style scoped lang="less">
  .avatar-content {
    align-items: center;
    text-align: center;
    margin-bottom: 32px;
  }
  .description {
    text-align: left;
  }
</style>
