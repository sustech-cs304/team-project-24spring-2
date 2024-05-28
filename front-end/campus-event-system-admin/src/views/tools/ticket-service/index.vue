<template>
  <div class="container">
    <Breadcrumb :items="['menu.tools', 'menu.tools.ticketService']" />
    <div>
      <a-space direction="vertical" :size="16" fill>
        <div>
          <TicketView
            v-if="userTicket.id"
            :image_url="eventData.image_url"
            :user_ticket="userTicket"
            :ticket_form="ticketFrom"
            :loading="loading"
            :event_info="eventData"
          />
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
                  {{ displayTicketID }}
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
                style="margin-top: 20px; width: 180px"
                size="large"
                @click="onConfirm"
                >{{ '确认' }}</a-button
              >
              <span>
                <span style="margin-left: 10px ;">
                  {{ '自动确认' }}
                  <a-switch v-model="autoConfirm"> </a-switch>
                </span>
              </span>
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
    UserTicket,
    Tickets,
  } from '@/api/event';
  import { UserState } from '@/store/modules/user/types';
  import { getUserInfo } from '@/api/users';
  import { Notification } from '@arco-design/web-vue';
  import { watch } from 'vue';

  import showMap from '@/components/map/show-map.vue';
  import TicketView from './components/ticket-view.vue';
  import GuestInfoCard from './components/guest-info.vue';

  const router = useRouter();
  const { loading, setLoading } = useLoading(false);
  const confirmVis = ref(false);

  const step = ref(0);
  const pageStep = ref(1);
  const autoConfirm = ref(false);

  const review = ref({
    ac: '',
    reason: '',
  });

  const ticketId = ref('');
  const displayTicketID = ref('');

  const eventData = ref<EventRecord>({} as EventRecord);

  const ticketFrom = ref({} as Tickets);

  const publisherInfo = ref<UserState>({} as UserState);

  const userTicket = ref({} as UserTicket);

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
      userTicket.value = ticket.data.user_ticket;
      eventData.value = ticket.data.event;
      ticketFrom.value = ticket.data.ticket;
      Notification.success({
        title: '检票成功',
        content: '欢迎参加，玩得愉快！',
      });
    } catch (err) {
      Notification.warning({
        title: '检票失败',
        content: '票根错误或已经入场',
      });
    } finally {
      setLoading(false);
    }
  };

  onBeforeMount(async () => {});

  watch(ticketId, async (newVal) => {
    if (newVal.length === 0) {
      return;
    }
    if (newVal.length === 36 && autoConfirm.value) {
      await onConfirm();
      displayTicketID.value = newVal;
      ticketId.value = '';
    } else {
        displayTicketID.value = newVal;
    }
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
    // float: right;
    margin: auto;

    width: 80%;
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
