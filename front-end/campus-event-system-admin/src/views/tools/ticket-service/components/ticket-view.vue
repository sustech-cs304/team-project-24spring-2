<template>
  <div class="ticket-view">
    <a-spin
      :loading="loading"
      tip="This may take a while..."
      class="spin"
      style="width: 100%; height: 100%"
    >
      <div class="ticket-container">
        <div class="ticket">
          <div class="ticket-ticket">
            <img :src="ticketFrameSVG" class="ticket-bone" />
          </div>
          <div class="ticket-qrcode">
            <canvas id="qrcode-canvas" class="qrcode-canvas" />
          </div>
          <div class="ticket-cover">
            <img :src="image_url" class="cover" />
          </div>
          <div class="ticket-info">
            <div class="event-title"> {{ props.event_info.title }}</div>
            <div class="event-value-form">
              {{ '开始时间:' }}
              <span class="event-value">
                {{
                  `${new Date(props.event_info.start_time).toLocaleString()}`
                }}
              </span>
            </div>
            <div class="event-value-form">
              {{ '结束时间:' }}
              <span class="event-value">
                {{ `${new Date(props.event_info.end_time).toLocaleString()}` }}
              </span>
            </div>

            <div class="event-value-form">
              {{ '地点:' }}
              <span class="event-value">{{
                props.event_info.location_name
              }}</span>
            </div>

            <div class="event-value-form">
              {{ '票种:' }}
              <span class="event-value-ticket-type">{{
                props.ticket_form.description
              }}</span>
            </div>

            <div class="event-value-form">
              {{ '票价:' }}
              <span class="event-value">
                {{
                  props.ticket_form.price === 0
                    ? '免费'
                    : `${props.ticket_form.price}元`
                }}
              </span>
            </div>

            <div class="event-value-form">
              {{ '票号:' }}
              <span class="event-value-number">{{
                `# ${addZeroBeforeNum(props.user_ticket.number)}`
              }}</span>
            </div>
          </div>
        </div>
      </div>
    </a-spin>
  </div>
</template>

<script lang="ts" setup>
  import ticketFrameSVG from '@/assets/images/ticket-root.png';
  import Qrcode from 'qrcode';

  import { onMounted } from 'vue';
  import { EventRecord, Tickets, UserTicket } from '@/api/event';

  const props = defineProps<{
    image_url: string;
    user_ticket: UserTicket;
    ticket_form: Tickets;
    loading: boolean;
    event_info: EventRecord;
  }>();

  const addZeroBeforeNum = (num: number) => {
    // width = 6
    return num.toString().padStart(8, '0');
  };
  onMounted(() => {
    Qrcode.toCanvas(
      document.getElementById('qrcode-canvas'),
      props.user_ticket.id,
      { margin: 1 }
    );
  });
</script>

<style scoped lang="less">
  .ticket-view {
    width: 980px;
    height: 367.5px;
    padding: 10px 10px 10px 10px;
    background-color: #ffffff;
    border-radius: 8px;
    margin: auto;
    display: flex;
  }
  .ticket-bone {
    width: 100%;
    border-radius: 8px;
  }
  .ticket {
    position: relative;
    margin-top: 1%;
    .ticket-ticket {
      margin: auto;
      position: relative;
      z-index: 1;
    }
    // ticket 2 is on ticket-1
    .ticket-qrcode {
      position: relative;
      margin-top: -13%;
      margin-left: 89%;
      width: 7.5%;
      z-index: 2;
      .qrcode-canvas {
        width: 100% !important;
        height: 100% !important;
      }
    }
    .ticket-cover {
      position: relative;
      margin-top: -234px;
      margin-left: 5.2%;
      width: 62.5%;
      z-index: 2;
    }
    .ticket-info {
      position: relative;
      margin-top: -224px;
      width: 80px;
      margin-left: 740px;
      z-index: 2;
      width: 175px;
      .event-title {
        word-break: break-all;
        width: 100%;
        text-align: center;
        font-size: large;
        margin-bottom: 15px;
      }

      .event-value-form {
        margin-bottom: 12px;
        font-size: 12px;

        .event-value-ticket-type {
          word-break: break-all;
          width: 100%;
          text-align: left;
          font-size: 20px;
          font-weight: bold;
          color: #000000;
          margin-bottom: 20px;
        }

        .event-value-number {
          word-break: break-all;
          width: 100%;
          text-align: left;
          font-size: 12px;
          color: #000000;
          margin-bottom: 15px;
        }
        .event-value {
          word-break: break-all;
          width: 100%;
          text-align: left;
          font-size: 10px;
          color: #666666;
          margin-bottom: 15px;
        }
      }
    }
  }

  .ticket-container {
    height: 80;
  }
  .cover {
    width: 100%;
    height: 100%;
  }
</style>
