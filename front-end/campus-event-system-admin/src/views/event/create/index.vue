<template>
  <div class="container">
    <Breadcrumb :items="['menu.event', 'menu.event.create']" />
    <a-spin :loading="loading" style="width: 100%">
      <a-card class="general-card">
        <template #title>
          {{ $t('event.create.title') }}
        </template>
        <div class="wrapper">
          <a-steps
            v-model:current="step"
            style="width: 580px"
            line-less
            class="steps"
          >
            <a-step :description="$t('event.create.subTitle.baseInfo')">
              {{ $t('event.create.title.baseInfo') }}
            </a-step>
            <a-step :description="$t('event.create.subTitle.advance')">
              {{ $t('event.create.title.channel') }}
            </a-step>
            <a-step :description="$t('event.create.subTitle.finish')">
              {{ $t('event.create.title.finish') }}
            </a-step>
          </a-steps>
          <keep-alive>
            <BaseInfo v-if="step === 1" @change-step="changeStep" />
            <ChannelInfo v-else-if="step === 2" @change-step="changeStep" />
            <Success v-else-if="step === 3" @change-step="changeStep" />
          </keep-alive>
        </div>
      </a-card>
    </a-spin>
  </div>
</template>

<script lang="ts" setup>
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import {
    CreateEventApi,
    EventBaseInfoModel,
    EventTicketsInfoModel,
    originalEventModel,
    UnitEventModel,
  } from '@/api/event';
  import BaseInfo from './components/base-info.vue';
  import ChannelInfo from './components/advance-info.vue';
  import Success from './components/success.vue';
//   import { start } from 'nprogress';

  const { loading, setLoading } = useLoading(false);
  const step = ref(1);
  const submitModel = ref<originalEventModel>({} as originalEventModel);
  const submitForm = async () => {
    setLoading(true);
    const Dates: Date[] = submitModel.value.time_range;

    const startDate = new Date(Dates[0]).getTime();
    const endDate = new Date(Dates[1]).getTime();
    try {
      const sendData = ref<UnitEventModel>();
      sendData.value = {
        title: submitModel.value.title,
        category_id: 0,
        start_time: startDate,
        end_time: endDate,
        latitude: 0,
        longitude: 0,
        location_name: submitModel.value.address,
        tickets: submitModel.value.tickets,
        document_url: '',
        image_url: '',
      };
      const res = await CreateEventApi(sendData.value); // The mock api default success
      console.log(res);
      step.value = 3;
      submitModel.value = {} as originalEventModel; // init
    } catch (err) {
      console.log(submitModel.value);
      console.log(err);
      // you can report use errorHandler or other
    } finally {
      setLoading(false);
    }
  };
  const changeStep = (
    direction: string | number,
    model: EventBaseInfoModel | EventTicketsInfoModel
  ) => {
    if (typeof direction === 'number') {
      step.value = direction;
      return;
    }

    if (direction === 'forward' || direction === 'submit') {
      submitModel.value = {
        ...submitModel.value,
        ...model,
      };
      if (direction === 'submit') {
        submitForm();
        return;
      }
      step.value += 1;
    } else if (direction === 'backward') {
      step.value -= 1;
    }
  };
</script>

<script lang="ts">
  export default {
    name: 'Step',
  };
</script>

<style scoped lang="less">
  .container {
    padding: 0 20px 20px 20px;
  }
  .wrapper {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 64px 0;
    background-color: var(--color-bg-2);
    :deep(.arco-form) {
      .arco-form-item {
        &:last-child {
          margin-top: 20px;
        }
      }
    }
  }

  .steps {
    margin-bottom: 76px;
  }
</style>
