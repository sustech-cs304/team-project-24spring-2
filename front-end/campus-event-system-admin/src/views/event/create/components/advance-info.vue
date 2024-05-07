<template>
  <a-form
    ref="formRef"
    :model="formData"
    class="form"
    :label-col-props="{ span: 6 }"
    :wrapper-col-props="{ span: 18 }"
  >
    <createTicketButton ref="child" @editConfirm="onAddTicket" />

    <a-divider style="margin-top: 0" />

    <a-table
      row-key="id"
      :columns="(cloneColumns as TableColumnData[])"
      :data="renderData"
      :bordered="false"
      :size="size"
    >
      <template #description="{ record }">
        {{ record.description }}
      </template>

      <template #price="{ record }">
        {{ inputNumberF(record.price) }}
      </template>

      <template #operations="{ record }">
        <a-button
          v-permission="['admin']"
          type="text"
          size="small"
          @click.prevent="onDeleteTicket(record.id)"
        >
          {{ $t('tickets.operation.delete') }}
        </a-button>
      </template>
    </a-table>
    <a-form-item>
      <a-space>
        <a-button type="secondary" @click="goPrev">
          {{ $t('stepForm.button.prev') }}
        </a-button>
        <a-button type="primary" @click="onNextClick">
          {{ $t('stepForm.button.next') }}
        </a-button>
      </a-space>
    </a-form-item>
  </a-form>
</template>

<script lang="ts" setup>
  import { ref, watch, computed } from 'vue';
  import { useI18n } from 'vue-i18n';
  import { FormInstance } from '@arco-design/web-vue/es/form';
  import { EventTicketsInfoModel } from '@/api/event';

  import cloneDeep from 'lodash/cloneDeep';
  import type { TableColumnData } from '@arco-design/web-vue/es/table/interface';
  import { Tickets } from '@/api/event';
  import createTicketButton from './create-ticket.vue';

  const emits = defineEmits(['changeStep']);
  type SizeProps = 'mini' | 'small' | 'medium' | 'large';

  type Column = TableColumnData & { checked?: true };
  let cnt = 0;
  const integralDigits = 6;
  const decimalPlaces = 2;
  const symbol = '¥';
  const cloneColumns = ref<Column[]>([]);
  const showColumns = ref<Column[]>([]);
  const renderData = ref<Tickets[]>([]);
  //   const renderData = ref;

  const { t } = useI18n();

  const size = ref<SizeProps>('medium');

  const formRef = ref<FormInstance>();

  const formData = ref<EventTicketsInfoModel>({
    tickets: [],
    document_url: '',
    image_url: '',
  });

  const columns = computed<TableColumnData[]>(() => [
    {
      title: t('tickets.columns.description'),
      dataIndex: 'description',
      slotName: 'description',
    },
    {
      title: t('tickets.columns.price'),
      dataIndex: 'price',
      slotName: 'price',
    },
    {
      title: t('tickets.columns.total_amount'),
      dataIndex: 'total_amount',
      slotName: 'total_amount',
      align: 'center',
    },
    {
      title: t('tickets.columns.operation'),
      dataIndex: 'operations',
      slotName: 'operations',
      align: 'center',
    },
  ]);
  const onAddTicket = (Ticket: Tickets) => {
    cnt += 1;
    Ticket.id = cnt;
    renderData.value.push(Ticket);
  };

  const onNextClick = async () => {
    const res = await formRef.value?.validate();
    if (!res) {
      emits('changeStep', 'submit', { ...formData.value });
    }
  };
  const goPrev = () => {
    emits('changeStep', 'backward');
  };

  const onDeleteTicket = (id: number) => {
    for (let i = 0; i < renderData.value.length; i += 1) {
      if (renderData.value[i].id === id) {
        renderData.value.splice(i, 1);
        break;
      }
    }
    console.log('data', renderData.value);
    console.log('delete', id);
  };

  const regHandel = (value: any) => {
    let reg = null;
    let gs = null;
    const dIndex = value.toString().indexOf('.');
    // 点开头处理为 0.
    if (dIndex === 0) {
      value = '0.';
    } else {
      // 连续点转为一个点
      const dIndex2 = value.toString().indexOf('..');
      if (dIndex2 !== -1) {
        value = value.replace(/\.\./, '.');
      }
    }
    value = value.replace(/[^0-9.]/g, '');
    const arr = value.split('.');
    if (arr.length === 2 && arr[1] !== '') {
      reg = new RegExp(
        `^(-)*(\\d{0,${integralDigits}})\\d*\\.(\\d{0,${decimalPlaces}}).*$`
      );
      gs = '$1$2.$3';
    } else {
      reg = new RegExp(`^(-)*(\\d{0,${integralDigits}}).*$`);
      if (dIndex !== -1) {
        gs = '$1$2.';
      } else {
        gs = '$1$2';
      }
    }
    return { reg, gs };
  };
  const inputNumberF = (value: any) => {
    const strValue = value.toString();
    const res = regHandel(strValue);
    const val = strValue.replace(res.reg, res.gs);
    return `${symbol} ${val}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  };
  watch(
    () => columns.value,
    (val) => {
      cloneColumns.value = cloneDeep(val);
      cloneColumns.value.forEach((item, index) => {
        item.checked = true;
      });
      showColumns.value = cloneDeep(cloneColumns.value);
    },
    { deep: true, immediate: true }
  );
</script>

<style scoped lang="less">
  .container {
    .keep-margin {
      margin-bottom: 20px;
    }
  }

  .wrapper {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 64px 0;
    background-color: var(--color-bg-2);
  }

  .steps {
    margin-bottom: 36px;
  }

  .form {
    width: 540px;
  }

  .form-content {
    padding: 8px 50px 0 30px;
  }

  :deep(.arco-table-th) {
    &:last-child {
      .arco-table-th-item-title {
        margin-left: 16px;
      }
    }
  }
</style>
