<template>
  <div class="container">
    <Breadcrumb :items="['menu.event', 'menu.event.audit-page']" />
    <div v-if="pageStep == 1">
      <a-space direction="vertical" :size="16" fill>
        <a-card class="general-card" :title="$t('Event.Audit')">
          <template #extra>
            <a-space>
              <a-button type="primary" @click="goBack">
                {{ $t('basicProfile.goBack') }}
              </a-button>
            </a-space>
          </template>
          <a-steps v-model:current="step" line-less class="steps">
            <a-step>{{ $t('Event.Status.EDIT') }}</a-step>
            <a-step>{{ $t('Event.Status.AUDIT') }}</a-step>
            <a-step>{{ $t('Event.Status.PEND') }}</a-step>
            <a-step>{{ $t('Event.Status.ONLINE') }}</a-step>
            <a-step>{{ $t('Event.Status.FINISH') }}</a-step>
          </a-steps>
        </a-card>

        <div class="cover">
          <a-spin
            :loading="loading"
            tip="This may take a while..."
            class="spin"
          >
            <div
              class="has-cover"
              v-if="formData.image_url !== ''"
              @click="ViewEvent()"
            >
              <img :src="formData.image_url" class="cover-image" />
            </div>
            <div class="has-cover" @click="ViewEvent()" v-else>
              <icon-image style="width: 50%; height: 200px" />
            </div>
          </a-spin>
        </div>

        <div>
          <PublisherInfoCard
            class="publisher"
            :user-info="publisherInfo"
            :loading="loading"
          />
          <a-card class="general-card basic-info">
            <ProfileItem :loading="loading" :render-data="formData" />
          </a-card>
          <div class="more-info">
            <a-card class="general-card location-info">
              <template #title>
                {{ $t('Event.Address.map') }}
              </template>
              <show-map />
            </a-card>
            <a-card class="general-card date-info">
              <template #title>
                {{ $t('Event.Ticket.info') }}
              </template>
              <TicketTable
                :loading="loading"
                :render-data="formData.tickets"
                :max-height="400"
              />
            </a-card>
          </div>
          <!-- <OperationLog /> -->
        </div>
        <a-card v-if="usage == 'AUDITING'" class="audit-area-card">
          <template #title>
            {{ $t('Event.Audit.action') }}
          </template>
          <div class="audit-area">
            <div>
              <span style="margin-right: 20px"
                >{{ $t('Event.Audit.select') + ':' }}
              </span>
              <a-select
                :style="{ width: '320px' }"
                :placeholder="$t('Event.Audit.select.placeholder')"
                v-model="review.ac"
                style="margin-bottom: 20px"
              >
                <a-option :value="'true'">
                  <icon-check-circle />
                  {{ $t('Event.Audit.select.accept') }}
                </a-option>
                <a-option :value="'false'">
                  <icon-close-circle />
                  {{ $t('Event.Audit.select.reject') }}</a-option
                >
              </a-select>
            </div>

            <a-textarea
              placeholder="Please enter something"
              :max-length="{ length: 200, errorOnly: true }"
              v-model="review.reason"
              style="border-radius: 8px"
              allow-clear
              show-word-limit
            />

            <div class="actions">
              <a-space>
                <a-button type="primary" @click="confirmVis = true">
                  {{ $t('Event.Audit.submit') }}
                </a-button>
              </a-space>
            </div>
          </div>
        </a-card>
      </a-space>
    </div>
    <div v-else-if="pageStep == 2">
      <a-card class="general-card" :title="$t('Event.Audit')">
        <successPage @back-to-manage="goBack" @view-event="goAuditEvent" />
      </a-card>
    </div>
    <a-modal
      v-model:visible="confirmVis"
      @cancel="handleCancel"
      :on-before-ok="handleBeforeOk"
      unmountOnClose
    >
      <template #title> {{ $t('Event.Audit.submit') }} </template>
      <div>
        {{ $t('Event.Audit.submit.info') }}
      </div>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
  import { ref, onBeforeMount, computed } from 'vue';
  import { useRouter } from 'vue-router';
  import useLoading from '@/hooks/loading';
  import {
    originalEventCreationModel,
    getEventInfo,
    getTicketInfo,
    auditEvent,
  } from '@/api/event';
  import showMap from '@/components/map/show-map.vue';
  import { UserState } from '@/store/modules/user/types';
  import { getUserInfo } from '@/api/users';
  import { Notification } from '@arco-design/web-vue';

  import ProfileItem from './components/profile-item.vue';
  import TicketTable from './components/ticket-table.vue';
  import PublisherInfoCard from './components/publisher-info.vue';
  import successPage from './components/success.vue';

  const router = useRouter();
  const { loading, setLoading } = useLoading(true);
  const confirmVis = ref(false);

  const step = ref(0);
  const pageStep = ref(1);

  const review = ref({
    ac: '',
    reason: '',
  });
  const args = new URLSearchParams(window.location.search);
  const uuid = args.get('uuid') as string;
  const usage = args.get('usage') as string;
  const formData = ref<originalEventCreationModel>(
    {} as originalEventCreationModel
  );

  const handleCancel = () => {
    confirmVis.value = false;
  };

  const handleBeforeOk = () => {
    confirmVis.value = false;
    onAuditEvent();
  };

  const publisherInfo = ref<UserState>({} as UserState);

  const ViewEvent = () => {
    router.replace(`/event/view?uuid=${uuid}`);
  };
  const goAuditEvent = () => {
    router.push(`/event/audit?uuid=${uuid}`);
  };
  const fetchData = async () => {
    setLoading(true);
    try {
      const { data } = await getEventInfo(uuid);
      const promises = Object.values(data.tickets).map((id) =>
        getTicketInfo(id)
      );
      const res = await Promise.all(promises);
      console.log(data);
      formData.value = {
        title: data.title,
        address: data.location_name,
        category: data.category,
        lng: data.longitude,
        lat: data.latitude,
        tickets: [...res.map((item) => item.data)],
        document_url: data.document_url,
        image_url: data.image_url,
        time_range: [new Date(data.start_time), new Date(data.end_time)],
        uuid,
      };
      console.log(data.status);
      if (data.status !== 'AUDITING' && usage === 'AUDITING') {
        Notification.warning({
          title: '审核失败',
          content: '已经提交审核的活动无法再进入审核',
        });
        ViewEvent();
      }

      switch (data.status) {
        case 'EDITING':
          step.value = 1;
          break;
        case 'AUDITING':
          step.value = 2;
          break;
        case 'PENDING':
          step.value = 3;
          break;
        case 'IN_PROGRESS':
          step.value = 4;
          break;
        case 'FINISHED':
          step.value = 5;
          break;
        default:
          step.value = 1;
      }
      const publisher = await getUserInfo(data.publisher);
      console.log(publisher.data);
      publisherInfo.value = publisher.data;
    } finally {
      setLoading(false);
    }
  };
  const goBack = () => {
    if (usage === 'AUDITING') {
      router.push('/event/audit-manage');
    } else {
      router.push('/event/manage');
    }
  };

  onBeforeMount(async () => {
    const res = await fetchData();
  });

  const goSuccess = () => {
    pageStep.value = 2;
  };

  const onAuditEvent = async () => {
    if (review.value.ac !== 'true' && review.value.ac !== 'false') {
      Notification.warning({
        title: 'Warning',
        content: '请选择审核意见',
      });
      return;
    }
    const reply = review.value.reason === '' ? '无' : review.value.reason;
    try {
      setLoading(true);
      const res = await auditEvent(uuid, review.value.ac as any, reply);
      goSuccess();
      Notification.success({
        title: 'Success',
        content: '审核结果提交成功',
      });
    } catch (err) {
      Notification.error({
        title: 'Error',
        content: '审核失败',
      });
    } finally {
      setLoading(false);
    }
  };
</script>

<script lang="ts">
  export default {
    name: 'Basic',
  };
</script>

<style scoped lang="less">
  .container {
    padding: 0 20px 20px 20px;
  }

  .steps {
    max-width: 548px;
    margin: 0 auto 10px;
    border-radius: 8px;
  }

  .basic-info {
    float: right;
    width: 75%;
    height: 190px;
    margin-bottom: 10px;
    border-radius: 8px;
  }
  .publisher {
    float: left;
    height: 700px;
    width: 24%;
    border-radius: 8px;
  }

  .more-info {
    width: 75%;
    float: right;
    .location-info {
      border-radius: 8px;

      float: right;
      width: 49%;
      margin-left: 2%;
      height: 500px;
    }

    .date-info {
      border-radius: 8px;

      float: right;
      width: 49%;
      height: 500px;
    }
  }

  .cover {
    min-height: 275px;
    padding: 10px 10px 10px 10px;
    background-color: #ffffff;
    border-radius: 8px;

    .has-cover {
      width: 100%;
      height: 100%;
      border-radius: 8px;
      background-color: #fafafa;
      cursor: pointer;
      justify-content: center;
      align-items: center;
      text-align: center;
      .cover-image {
        height: 100%;
      }
    }
  }
  .spin {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 250px;
  }
  .actions {
    border-radius: 8px;
    position: flex;
    margin-top: 20px;
    background: var(--color-bg-2);
    text-align: right;
  }

  .audit-area {
    border-radius: 8px;
    padding: 20px;
  }

  .audit-area-card {
    border-radius: 8px;
  }
</style>
