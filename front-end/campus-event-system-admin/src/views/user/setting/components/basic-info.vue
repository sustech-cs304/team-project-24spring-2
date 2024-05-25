<template>
  <a-form
    ref="formRef"
    :model="formData"
    class="form"
    :label-col-props="{ span: 8 }"
    :wrapper-col-props="{ span: 16 }"
  >
    <a-form-item
      field="nickname"
      :label="$t('User.info.nickname')"
      :rules="[
        {
          required: true,
          message: $t('userSetting.form.error.nickname.required'),
        },
      ]"
      disabled
    >
      <a-input
        v-model="formData.nickname"
        :placeholder="$t('userSetting.basicInfo.placeholder.nickname')"
      />
    </a-form-item>

    <a-form-item field="realname" :label="$t('User.info.realname')" disabled>
      <a-input v-model="formData.nickname" />
    </a-form-item>

    <a-form-item field="gender" :label="$t('User.info.gender')">
      <a-select
        v-model="formData.gender"
        :placeholder="$t('userSetting.basicInfo.placeholder.area')"
        disabled
      >
        <a-option value="MALE"> {{ $t('User.info.gender.male') }}</a-option>
        <a-option value="FEMALE"> {{ $t('User.info.gender.female') }}</a-option>
        <a-option value="OTHER"> {{ $t('User.info.gender.other') }}</a-option>
      </a-select>
    </a-form-item>
    <a-form-item field="email" :label="$t('User.info.email')" disabled>
      <a-input
        v-model="formData.email"
        :placeholder="$t('userSetting.basicInfo.placeholder.email')"
      />
    </a-form-item>

    <a-form-item
      field="phone"
      :label="$t('User.info.phone')"
      :rules="[
        {
          match: /^[0-9]{11}$/,
          message: $t('userSetting.basicInfo.phone.error'),
        },
      ]"
    >
      <a-input
        v-model="formData.phone"
        :placeholder="$t('userSetting.basicInfo.placeholder.phone')"
      >
        <template #prepend> +86 </template>
      </a-input>
    </a-form-item>

    <a-form-item
      field="description"
      :label="$t('userSetting.basicInfo.form.label.description')"
      :rules="[
        {
          maxLength: 200,
          message: $t('userSetting.form.error.description.maxLength'),
        },
      ]"
      row-class="keep-margin"
    >
      <a-textarea
        v-model="formData.description"
        :placeholder="$t('userSetting.basicInfo.placeholder.description')"
      />
    </a-form-item>
    <a-form-item>
      <a-space>
        <a-button type="primary" @click="onSave">
          {{ $t('userSetting.save') }}
        </a-button>
        <a-button type="secondary" @click="reset">
          {{ $t('userSetting.reset') }}
        </a-button>
      </a-space>
    </a-form-item>
  </a-form>
</template>

<script lang="ts" setup>
  import { ref } from 'vue';
  import { FormInstance } from '@arco-design/web-vue/es/form';
  import { BasicInfoModel } from '@/api/user-center';
  import { UserState } from '@/store/modules/user/types';

  const formRef = ref<FormInstance>();
  const formData = defineModel<UserState>({
    default: {} as UserState,
  });

  const emits = defineEmits(['saveBasic']);

  const onSave = async () => {
    const res = await formRef.value?.validate();
    if (!res) {
      emits('saveBasic');
    }
  };
  const reset = async () => {
    await formRef.value?.resetFields();
  };
</script>

<style scoped lang="less">
  .form {
    width: 540px;
    margin: 0 auto;
  }
</style>
