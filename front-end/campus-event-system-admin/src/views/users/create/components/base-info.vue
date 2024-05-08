<template>
  <a-form
    ref="formRef"
    :model="formData"
    class="form"
    :label-col-props="{ span: 6 }"
    :wrapper-col-props="{ span: 18 }"
  >
    <a-form-item
      field="usersName"
      :label="$t('users.form.label.usersName')"
      :rules="[
        {
          required: true,
          message: $t('users.form.error.usersName.required'),
        },
        {
          match: /^[a-zA-Z0-9\u4e00-\u9fa5]{1,20}$/,
          message: $t('users.form.error.usersName.pattern'),
        },
      ]"
    >
      <a-input
        v-model="formData.usersName"
        :placeholder="$t('users.placeholder.usersName')"
      />
    </a-form-item>
    <a-form-item
      field="usersType"
      :label="$t('users.form.label.usersType')"
      :rules="[
        {
          required: true,
          message: $t('users.form.error.usersType.required'),
        },
      ]"
    >
      <a-select
        v-model="formData.usersType"
        :placeholder="$t('users.placeholder.usersType')"
      >
        <a-option>社交</a-option>
        <a-option>兴趣交流</a-option>
        <a-option>体育运动</a-option>
        <a-option>学习分享</a-option>
        <a-option>党政学习</a-option>
        <a-option>其他</a-option>
      </a-select>
    </a-form-item>
    <a-form-item
      field="usersTime"
      :label="$t('users.form.label.usersTime')"
      :rules="[
        {
          required: true,
          message: $t('users.form.error.usersTime.required'),
        },
      ]"
    >
      <a-range-picker v-model="formData.usersTime" />
    </a-form-item>
    <a-form-item
      field="usersAddress"
      :label="$t('users.form.label.usersAddress')"
      :rules="[
        {
          required: true,
          message: $t('users.form.error.usersAddress.required'),
        },
        {
          type: 'url',
          message: $t('users.form.error.usersAddress.pattern'),
        },
      ]"
      row-class="keep-margin"
    >
      <a-input
        v-model="formData.usersAddress"
        :placeholder="$t('users.placeholder.usersAddress')"
      />
      <template #help>
        <span>{{ $t('users.form.tip.usersAddress') }}</span>
      </template>
    </a-form-item>
    <a-form-item>
      <a-button type="primary" @click="onNextClick">
        {{ $t('users.button.next') }}
      </a-button>
    </a-form-item>
  </a-form>
</template>

<script lang="ts" setup>
  import { ref } from 'vue';
  import { FormInstance } from '@arco-design/web-vue/es/form';
  import { BaseInfoModel } from '@/api/users';

  const emits = defineEmits(['changeStep']);
  const formRef = ref<FormInstance>();
  const formData = ref<BaseInfoModel>({
    usersName: '',
    usersType: '',
    usersTime: [],
    usersAddress: 'https://arco.design',
  });

  const onNextClick = async () => {
    const res = await formRef.value?.validate();
    if (!res) {
      emits('changeStep', 'forward', { ...formData.value });
    }
  };
</script>

<style scoped lang="less">
  .container {
    padding: 20px;
    .keep-margin {
      margin-bottom: 20px;
    }
  }

  .wrapper {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 64px 0;
    background-color: var(--color-bg-2);
  }

  .steps {
    margin-bottom: 36px;
  }

  .form {
    width: 500px;
  }

  .form-content {
    padding: 8px 50px 0 30px;
  }
</style>
