<template>
  <div class="container">
    <Breadcrumb :items="['menu.profile', 'menu.profile.basic']" />
    <a-space direction="vertical" :size="16" fill>
      <a-card class="general-card" :title="$t('basicProfile.title.form')">
        <template #extra>
          <a-space>
            <a-button type="primary" @click="goBack">
              {{ $t('basicProfile.goBack') }}
            </a-button>
          </a-space>
        </template>
        <a-steps v-model:current="step" line-less class="steps">
          <a-step>{{ $t('basicProfile.steps.commit') }}</a-step>
          <a-step>{{ $t('basicProfile.steps.approval') }}</a-step>
          <a-step>{{ $t('basicProfile.steps.finish') }}</a-step>
        </a-steps>
      </a-card>

      <div class="cover">
        <a-spin :loading="loading" tip="This may take a while..." class="spin">
          <div
            class="has-cover"
            v-if="formData.image_url !== ''"
            @click="ViewEvent()"
          >
            <img
              :src="formData.image_url"
              class="cover-image"
            />
          </div>
          <div class="has-cover" v-else>
            <icon-image
              style="width: 50%; height: 200px"
              @click="ViewEvent()"
            />
          </div>
        </a-spin>
      </div>
      <a-card class="general-card basic-info">
        <ProfileItem :loading="loading" :render-data="formData" />
      </a-card>
      <div class="card-info">
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
      <a-card class="actions" v-if="usage == 'AUDITING'">
        <a-space>
          <a-button type="primary">
            {{ $t('eventEdit.submit') }}
          </a-button>
        </a-space>
      </a-card>
    </a-space>
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
  } from '@/api/event';
  import showMap from '@/components/map/show-map.vue';

  import ProfileItem from './components/profile-item.vue';
  import TicketTable from './components/ticket-table.vue';

  const router = useRouter();
  const { loading, setLoading } = useLoading(true);

  const step = ref(0);

  const args = new URLSearchParams(window.location.search);
  const uuid = args.get('uuid') as string;
  const usage = args.get('usage') as string;
  const formData = ref<originalEventCreationModel>(
    {} as originalEventCreationModel
  );
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
      switch (data.status) {
        case 'EDITING':
          step.value = 1;
          break;
        case 'AUDITING':
          step.value = 2;
          break;
        default:
          step.value = 1;
      }
    } finally {
      setLoading(false);
    }
  };
  const goBack = () => {
    router.go(-1);
  };
  const ViewEvent = () => {
    router.push(`/event/view?uuid=${uuid}`);
  };
  onBeforeMount(async () => {
    const res = await fetchData();
  });
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
  }

  .basic-info {
    float: right;
    width: 75%;
    // height: 200px;
  }
  .location-info {
    float: right;
    width: 49%;
    margin-left: 2%;
    height: 500px;
  }

  .date-info {
    float: right;
    width: 49%;
    height: 500px;
  }

  .card-info {
    width: 75%;
    float: right;
  }

  .cover {
    min-height: 275px;
    padding: 10px 10px 10px 10px;
    background-color: #ffffff;
  }
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
  .spin {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 250px;
  }
  .actions {
    position: flex;
    height: 65px;
    margin-top: 10px;
    background: var(--color-bg-2);
    text-align: right;
  }
</style>
