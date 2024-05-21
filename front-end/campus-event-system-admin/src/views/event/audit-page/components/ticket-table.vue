<template>
  <div class="item-container">
    <a-list :loading="loading" :max-height="props.maxHeight">
      <a-list-item v-for="ticket in props.renderData" :key="ticket.id">
        <a-list-item-meta
          :title="ticket.description"
          :description="inputNumberF(ticket.price)"
        >
          <template #avatar>
            <a-avatar shape="square">
              <icon-subscribe />
            </a-avatar>
          </template>
        </a-list-item-meta>
      </a-list-item>
    </a-list>
  </div>
</template>

<script lang="ts" setup>
  import { computed, PropType, ref } from 'vue';
  import { useI18n } from 'vue-i18n';
  import { Tickets, inputNumberF } from '@/api/event';

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
</style>
