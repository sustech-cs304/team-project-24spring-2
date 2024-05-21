<template>
  <div class="item-container">
    <a-space :size="16" direction="vertical" fill>
      <a-descriptions
        v-for="(item, idx) in blockDataList"
        :key="idx"
        :label-style="{
          textAlign: 'right',
          width: '200px',
          paddingRight: '10px',
          color: 'rgb(var(--gray-8))',
          fontSize: '16px',
        }"
        :value-style="{ width: '400px', fontSize: '16px' }"
        :title="item.title"
        :data="item.data"
      >
        <template #value="{ value }">
          <a-skeleton v-if="loading" :animation="true">
            <a-skeleton-line :widths="['200px']" :rows="1" />
          </a-skeleton>
          <span v-else>{{ value }}</span>
        </template>
      </a-descriptions>
    </a-space>
  </div>
</template>

<script lang="ts" setup>
  import { computed, PropType, ref } from 'vue';
  import { useI18n } from 'vue-i18n';
  import { originalEventCreationModel } from '@/api/event';

  type BlockList = {
    title: string;
    data: {
      label: string;
      value: string;
    }[];
  }[];

  const props = defineProps({
    renderData: {
      type: Object as PropType<originalEventCreationModel>,
      default: {} as originalEventCreationModel,
    },
    loading: {
      type: Boolean,
      default: false,
    },
  });

  const timeFormatter = (time: any) => {
    return time.toLocaleString();
  };

  const StartTime = computed(() => {
    const { renderData } = props;
    return renderData.time_range?.at(0);
  });
  const EndTime = computed(() => {
    const { renderData } = props;
    return renderData.time_range?.at(1);
  });

  const { t } = useI18n();
  const blockDataList = computed<BlockList>(() => {
    const { renderData } = props;
    const result = [];
    result.push({
      title: t('eventEdit.tab.title.basic'),
      data: [
        {
          label: t('Event.Title'),
          value: renderData.title,
        },

        {
          label: t('Event.Category'),
          value: t(`Event.Category.${renderData.category}`),
        },
        {
          label: t('Event.Address'),
          value: renderData.address,
        },
        {
          label: t('Event.StartTime'),
          value: StartTime.value ? timeFormatter(StartTime.value) : '',
        },
        {
          label: t('Event.EndTime'),
          value: EndTime.value ? timeFormatter(EndTime.value) : '',
        },
      ],
    });
    return result;
  });
</script>

<style scoped lang="less">
  .item-container {
    padding-top: 20px;
    :deep(.arco-descriptions-item-label) {
      font-weight: normal;
    }
  }
</style>
