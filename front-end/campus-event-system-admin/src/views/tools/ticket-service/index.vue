<template>
  <div class="container">
    <Breadcrumb :items="['menu.tools', 'menu.tools.tickService']" />
    <div>
      <a-space direction="vertical" :size="16" fill>
        <div>
          <GuestInfoCard
            class="guest-user"
            :user-info="publisherInfo"
            :loading="loading"
          />
          <div class="cover">
            <a-spin
              :loading="loading"
              tip="This may take a while..."
              class="spin"
            >
              <div class="has-cover" v-if="formData.image_url !== ''">
                <img :src="formData.image_url" class="cover-image" />
              </div>
              <div class="has-cover" v-else>
                <icon-image style="width: 50%; height: 200px" />
              </div>
            </a-spin>
          </div>
          <div class="ticket-id">
            <a-card class="ticket-id-card">
              <div>
                <div
                  style="
                    width: 100%;
                    font-size: 40px;
                    height: 50px;
                    margin-bottom: 20px;
                    border: 1px solid #e8e8e8;
                  "
                >
                  {{ ticketId }}
                </div>
                <span>{{ '票码：' }}</span>
                <a-input
                  v-model="ticketId"
                  class="ticket-id-input"
                  :max-length="36"
                  size="large"
                />
              </div>
              <a-button
                type="primary"
                long
                style="margin-top: 20px"
                size="large"
                @click="onConfirm"
                >{{ '确认' }}</a-button
              >
            </a-card>
          </div>
        </div>
      </a-space>
    </div>
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
    checkoutTicket,
    EventRecord,
  } from '@/api/event';
  import showMap from '@/components/map/show-map.vue';
  import { UserState } from '@/store/modules/user/types';
  import { getUserInfo } from '@/api/users';
  import { Notification } from '@arco-design/web-vue';

  import GuestInfoCard from './components/guest-info.vue';
  import {} from '@/api/event';

  const router = useRouter();
  const { loading, setLoading } = useLoading(false);
  const confirmVis = ref(false);

  const step = ref(0);
  const pageStep = ref(1);

  const review = ref({
    ac: '',
    reason: '',
  });

  const ticketId = ref('');

  const formData = ref<EventRecord>({} as EventRecord);

  const publisherInfo = ref<UserState>({} as UserState);


  const onConfirm = async () => {
    if (ticketId.value.length !== 36) {
      Notification.warning({
        title: '请输入票码',
        content: '请输入正确的票码',
      });
      return;
    }
    await fetchTicketData(ticketId.value);
  };
  const fetchTicketData = async (uuid: string) => {
    setLoading(true);
    try {
      const ticket = await checkoutTicket(uuid);
      formData.value = ticket.data.event;
    } catch (err) {
      Notification.warning({
        title: '检票失败',
        content: '票根错误或已经入场',
      });
    } finally {
      setLoading(false);
    }
  };

  const submitTicket = async () => {
    setLoading(true);
    try {
      // fetching
    } finally {
      setLoading(false);
    }
  };

  onBeforeMount(async () => {});
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
  .guest-user {
    float: left;
    height: 700px;
    width: 28%;
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
    width: 70%;
    float: right;
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

  .ticket-id {
    font-size: 30px;
    float: right;
    width: 70%;
    height: 100px;
    border-radius: 8px;
    margin-top: 20px;
    text-align: center;
  }
  .ticket-id-input {
    text-align: center;
    width: 300px;
    font-family: Arial, sans-serif;
    font-size: 24px;
  }
  .ticket-id-card {
    border-radius: 8px;
    padding: 50px;
  }
</style>
