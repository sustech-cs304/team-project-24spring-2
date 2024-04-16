<template>
  <a-form
    ref="formRef"
    :model="formData"
    class="form"
    :label-col-props="{ span: 6 }"
    :wrapper-col-props="{ span: 18 }"
  >
    <a-form-item
      field="eventName"
      :label="$t('event.form.label.eventName')"
      :rules="[
        {
          required: true,
          message: $t('event.form.error.eventName.required'),
        },
        {
          match: /^[a-zA-Z0-9\u4e00-\u9fa5]{1,20}$/,
          message: $t('event.form.error.eventName.pattern'),
        },
      ]"
    >
      <a-input
        v-model="formData.eventName"
        :placeholder="$t('event.placeholder.eventName')"
      />
    </a-form-item>
    <a-form-item
      field="eventType"
      :label="$t('event.form.label.eventType')"
      :rules="[
        {
          required: true,
          message: $t('event.form.error.eventType.required'),
        },
      ]"
    >
      <a-select
        v-model="formData.eventType"
        :placeholder="$t('event.placeholder.eventType')"
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
      field="eventTime"
      :label="$t('event.form.label.eventTime')"
      :rules="[
        {
          required: true,
          message: $t('event.form.error.eventTime.required'),
        },
      ]"
    >
      <a-range-picker v-model="formData.eventTime" />
    </a-form-item>
    <a-form-item
      field="eventAddress"
      :label="$t('event.form.label.eventAddress')"
      :rules="[
        {
          required: true,
          message: $t('event.form.error.eventAddress.required'),
        },
        {
          type: 'url',
          message: $t('event.form.error.eventAddress.pattern'),
        },
      ]"
      row-class="keep-margin"
    >
      <a-input
        v-model="formData.eventAddress"
        :placeholder="$t('event.placeholder.eventAddress')"
      />
      <template #help>
        <span>{{ $t('event.form.tip.eventAddress') }}</span>
      </template>
    </a-form-item>
    <a-form-item>
      <a-button type="primary" @click="onNextClick">
        {{ $t('event.button.next') }}
      </a-button>
    </a-form-item>
  </a-form>
</template>

<script lang="ts" setup>
  import { ref } from 'vue';
  import { FormInstance } from '@arco-design/web-vue/es/form';
  import { BaseInfoModel } from '@/api/event';

  const emits = defineEmits(['changeStep']);
  const formRef = ref<FormInstance>();
  const formData = ref<BaseInfoModel>({
    eventName: '',
    eventType: '',
    eventTime: [],
    eventAddress: 'https://arco.design',
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
