<template>
  <a-form
    ref="formRef"
    :model="formData"
    class="form"
    :label-col-props="{ span: 6 }"
    :wrapper-col-props="{ span: 18 }"
  >
    <a-form-item
      field="title"
      :label="$t('event.label.eventName')"
      :rules="[
        {
          required: true,
          message: $t('event.error.eventName.required'),
        },
        {
          match: /^.{1,20}$/,
          message: $t('event.error.eventName.pattern'),
        },
      ]"
    >
      <a-input
        v-model="formData.title"
        :placeholder="$t('event.placeholder.eventName')"
      />
    </a-form-item>
    <a-form-item
      field="category"
      :label="$t('event.label.eventType')"
      :rules="[
        {
          required: true,
          message: $t('event.error.eventType.required'),
        },
      ]"
    >
      <a-select
        v-model="formData.category"
        :placeholder="$t('event.placeholder.eventType')"
        :options="categoryOptions"
      >
      </a-select>
    </a-form-item>
    <a-form-item
      field="time_range"
      :label="$t('event.label.eventTime')"
      :rules="[
        {
          required: true,
          message: $t('event.error.eventTime.required'),
        },
      ]"
    >
      <a-range-picker
        show-time
        v-model="formData.time_range"
        format="YYYY-MM-DD HH:mm"
        type="datetime"
        :disabledDate="
          (current) => {
            return false;
            // dayjs(current).isBefore(dayjs())
          }
        "
      />
    </a-form-item>
    <a-form-item
      field="address"
      :label="$t('event.label.eventAddress')"
      :rules="[
        {
          required: true,
          message: $t('event.error.eventAddress.required'),
        },
      ]"
      row-class="keep-margin"
    >
      <a-input
        v-model="formData.address"
        :placeholder="$t('event.placeholder.address')"
      />
      <MyMAP
        @confirm="onSelectedAddress"
        :address="formData.address"
        :lat="formData.lat"
        :lng="formData.lng"
      />

      <template #help>
        <span>{{ $t('event.tip.eventAddress') }}</span>
      </template>
    </a-form-item>
    <a-form-item>
      <a-button type="primary" @click="onNextClick">
        {{ $t('button.next') }}
      </a-button>
    </a-form-item>
  </a-form>
</template>

<script lang="ts" setup>
  import { ref, onBeforeMount } from 'vue';
  import { FormInstance } from '@arco-design/web-vue/es/form';
  import { getSetting } from '@/api/global';
  import { useI18n } from 'vue-i18n';
  import { EventBaseInfoModel } from '@/api/event';
  import type { SelectOptionData } from '@arco-design/web-vue/es/select/interface';
  import MyMAP from '@/components/map/select-map.vue';
  import { Notification } from '@arco-design/web-vue';
  import dayjs from 'dayjs';

  const { t } = useI18n();
  const emits = defineEmits(['changeStep']);
  const formRef = ref<FormInstance>({} as FormInstance);
  const formData = ref<EventBaseInfoModel>({
    title: '',
    category: '',
    time_range: [],
    address: '',
    lng: NaN,
    lat: NaN,
  });

  const categoryOptions = ref<SelectOptionData[]>([]);

  const getCategories = async () => {
    const categories = await getSetting('categories');
    categories.data.split(',').forEach((element: string) => {
      categoryOptions.value.push({
        label: element,
        // label: t(`Event.Category.${element}`),
        value: element,
      });
    });
  };
  const onNextClick = async () => {
    const res = await formRef.value?.validate();
    if (res) return;
    if (Number.isNaN(formData.value.lng) || Number.isNaN(formData.value.lat)) {
      Notification.warning({
        title: '请选择活动地点',
        content: '请选择活动地点',
      });
      return;
    }
    emits('changeStep', 'forward', { ...formData.value });
  };

  const onSelectedAddress = (form: any) => {
    formData.value.address = form.address;
    formData.value.lng = form.lng;
    formData.value.lat = form.lat;
  };
  onBeforeMount(() => {
    getCategories();
  });
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
