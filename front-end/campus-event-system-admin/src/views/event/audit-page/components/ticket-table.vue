<template>
  <div class="item-container">
    <a-list :loading="loading" :max-height="props.maxHeight">
      <a-list-item v-for="ticket in props.renderData" :key="ticket.id">
        <div>
          <a-list-item-meta style="width: 60%; float: left">
            <template #title>
              <div style="font-size: 20px">{{ ticket.description }}</div>
            </template>

            <template #description>
              <div style="font-size: 16px">{{
                inputNumberF(ticket.price)
              }}</div>
            </template>

            <template #avatar>
              <a-avatar :size="48" shape="square">
                <icon-subscribe :size="32" />
              </a-avatar>
            </template>
          </a-list-item-meta>

          <div style="float: right; width: 40%">
            <div class="more-info">
              <span
                >{{ $t('ticket.total_amount') }}:
                {{ ticket.total_amount }}</span
              >
              <span
                >{{ $t('ticket.sold_amount') }}: {{ ticket.sold_amount }}</span
              >
            </div>
          </div>
        </div>
      </a-list-item>
    </a-list>
  </div>
</template>

<script lang="ts" setup>
  import { computed, PropType, ref } from 'vue';
  import { useI18n } from 'vue-i18n';
  import { Tickets, inputNumberF } from '@/api/event';
  import { size } from 'lodash';

  const props = defineProps({
    renderData: {
      type: Object as PropType<Tickets[]>,
      default: [] as Tickets[],
    },
    loading: {
      type: Boolean,
      default: false,
    },
    maxHeight: {
      type: Number,
      default: 400,
    },
  });
</script>

<style scoped lang="less">
  .item-container {
    width: 100%;
    max-height: 100px;
    margin-top: 10px;
  }

  .more-info {
    padding: 10px;
    font-size: 16px;
    color: #8492a6;
    text-align: center;
    margin: auto;
    display: flex;
    align-items: baseline;
    flex-direction: column;
  }
</style>
