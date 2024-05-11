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
            <Success
              v-else-if="step === 3"
              @change-step="changeStep"
              @edit-event="editEvent"
            />
          </keep-alive>
        </div>
      </a-card>
    </a-spin>
  </div>
</template>

<script lang="ts" setup>
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import { useRouter } from 'vue-router';

  import {
    CreateEventApi,
    EventBaseInfoModel,
    EventTicketsInfoModel,
    originalEventCreationModel,
    EventCreationModel,
  } from '@/api/event';
  import { Notification } from '@arco-design/web-vue';
  import BaseInfo from './components/base-info.vue';
  import ChannelInfo from './components/advance-info.vue';
  import Success from './components/success.vue';

  //   import { start } from 'nprogress';

  const { loading, setLoading } = useLoading(false);
  const step = ref(1);
  const router = useRouter();
  const submitModel = ref<originalEventCreationModel>(
    {} as originalEventCreationModel
  );
  const uuid = ref<string>('');

  const submitForm = async () => {
    setLoading(true);
    const Dates: Date[] = submitModel.value.time_range;

    const startDate = new Date(Dates[0]).getTime();
    const endDate = new Date(Dates[1]).getTime();
    try {
      const sendData = ref<EventCreationModel>();
      const jsonTickets = [];
      for (let i = 0; i < submitModel.value.tickets.length; i += 1) {
        jsonTickets.push({
          description: submitModel.value.tickets[i].description,
          price: submitModel.value.tickets[i].price,
          total_amount: submitModel.value.tickets[i].total_amount,
        });
      }
      sendData.value = {
        title: submitModel.value.title,
        start_time: startDate,
        end_time: endDate,
        document_url: '',
        image_url: '',
        latitude: submitModel.value.lat,
        longitude: submitModel.value.lng,
        location_name: submitModel.value.address,
        category: submitModel.value.category,
        // tickets: submitModel.value.tickets,
        tickets: jsonTickets,
      };
      console.log('sending', sendData.value);
      const res = await CreateEventApi(sendData.value); // The mock api default success
      uuid.value = res.data.id;
      Notification.success({
        title: 'Success',
        content: '创建成功！',
      });
      step.value = 3;
      submitModel.value = {} as originalEventCreationModel; // init
    } catch (err) {
      console.log(submitModel.value);
      console.log(err);
      // you can report use errorHandler or other
    } finally {
      setLoading(false);
    }
  };
  const editEvent = () => {
    router.push({
      path: '/event/edit',
      query: {
        uuid: uuid.value,
      },
    });
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
