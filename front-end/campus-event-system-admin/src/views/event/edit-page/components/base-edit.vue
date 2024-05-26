<template>
  <div class="container">
    <a-form ref="formRef" layout="vertical" :model="formData" class="form">
      <a-space direction="vertical" :size="16">
        <a-card class="basic-info-card" hoverable>
          <template #title>
            {{ $t('eventEdit.info.event') }}
          </template>
          <a-row :gutter="80">
            <a-col :span="12">
              <a-form-item
                :label="$t('event.label.eventName')"
                :rules="[
                  {
                    required: true,
                    message: $t('event.error.eventName.required'),
                  },
                  {
                    match: /^.{1,20}$/,
                    message: $t('event.error.eventName.pattern'),
                  },
                ]"
                field="title"
              >
                <a-input
                  v-model="formData.title"
                  :placeholder="$t('event.placeholder.eventName')"
                >
                </a-input>
              </a-form-item>
            </a-col>

            <a-col :span="8">
              <a-form-item
                :label="$t('event.label.eventType')"
                :rules="[
                  {
                    required: true,
                    message: $t('event.error.eventName.required'),
                  },
                ]"
                field="category"
              >
                <a-select
                  v-model="formData.category"
                  :placeholder="$t('event.placeholder.eventType')"
                  :options="categoryOptions"
                >
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="80">
            <a-col :span="12">
              <a-form-item
                field="time_range"
                :label="$t('event.label.eventTime')"
                :rules="[
                  {
                    required: true,
                    message: $t('event.error.eventTime.required'),
                  },
                ]"
              >
                <a-range-picker
                  v-model="formData.time_range"
                  format="YYYY-MM-DD HH:mm"
                  type="datetime"
                  show-time
                  :disabledDate="(current) => dayjs(current).isAfter(dayjs())"
                />
              </a-form-item>
            </a-col>

            <a-col :span="12">
              <a-form-item
                field="address"
                :label="$t('event.label.eventAddress')"
                :rules="[
                  {
                    required: true,
                    message: $t('event.error.eventAddress.required'),
                  },
                ]"
                row-class="keep-margin"
              >
                <a-input
                  v-model="formData.address"
                  :placeholder="$t('event.placeholder.address')"
                />
                <MyMAP
                  :address="formData.address"
                  :lat="formData.lat"
                  :lng="formData.lng"
                  @confirm="onSelectedAddress"
                />

                <template #help>
                  <span>{{ $t('event.tip.eventAddress') }}</span>
                </template>
              </a-form-item>
            </a-col>
          </a-row>
        </a-card>
        <a-card class="ticket-info-card" hoverable>
          <template #title>
            {{ $t('eventEdit.info.ticket') }}
          </template>
          <a-col :span="24">
            <createTicketButton ref="child" @edit-confirm="onAddTicket" />
            <a-divider style="margin-top: 0" />

            <a-table
              row-key="id"
              :columns="(cloneColumns as TableColumnData[])"
              :data="formData.tickets"
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
                  type="text"
                  size="small"
                  @click.prevent="onDeleteTicket(record.id)"
                >
                  {{ $t('tickets.operation.delete') }}
                </a-button>
              </template>
            </a-table>
          </a-col>
        </a-card>
      </a-space>
    </a-form>
  </div>
</template>

<script lang="ts" setup>
  import { ref, watch, computed, defineModel } from 'vue';
  import { useI18n } from 'vue-i18n';
  import { originalEventCreationModel, Tickets } from '@/api/event';
  import { FormInstance } from '@arco-design/web-vue/es/form';
  import createTicketButton from '@/components/ticket/create-ticket.vue';
  import MyMAP from '@/components/map/select-map.vue';
  import { getSetting } from '@/api/global';
  import cloneDeep from 'lodash/cloneDeep';
  import type { TableColumnData } from '@arco-design/web-vue/es/table/interface';
  import type { SelectOptionData } from '@arco-design/web-vue/es/select/interface';
  import { onBeforeMount } from 'vue';
  import dayjs from 'dayjs';

  type SizeProps = 'mini' | 'small' | 'medium' | 'large';
  type Column = TableColumnData & { checked?: true };
  const { t } = useI18n();

  //   const props = defineProps({
  //     eventInfo: {
  //       type: Object,
  //     },
  //   });

  const formData = defineModel<originalEventCreationModel>('form', {
    default: {} as originalEventCreationModel,
  });

  let cnt = 0;
  const integralDigits = 6;
  const decimalPlaces = 2;
  const symbol = '¥';
  const cloneColumns = ref<Column[]>([]);
  const showColumns = ref<Column[]>([]);
  const size = ref<SizeProps>('medium');
  const categoryOptions = ref<SelectOptionData[]>([]);

  const getCategories = async () => {
    const categories = await getSetting('categories');
    categories.data.split(',').forEach((element: string) => {
      categoryOptions.value.push({
        label: t(`Event.Category.${element}`),
        value: element,
      });
    });
  };
  //   const formData = ref<originalEventCreationModel>(
  //
  //   );

  const formRef = ref<FormInstance>();
  const onSelectedAddress = (form: any) => {
    formData.value.address = form.address;
    formData.value.lng = form.lng;
    formData.value.lat = form.lat;
  };
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
    if (formData.value.tickets === undefined) {
      formData.value.tickets = [];
    }
    formData.value.tickets.push(Ticket);
  };

  const onDeleteTicket = (id: number) => {
    for (let i = 0; i < formData.value.tickets.length; i += 1) {
      if (formData.value.tickets[i].id === id) {
        formData.value.tickets.splice(i, 1);
        break;
      }
    }
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
    () => formData,
    (newVal: any) => {
      formData.value.title = newVal.title;
      formData.value.time_range = newVal.time_range;
      formData.value.address = newVal.address;
      formData.value.category = newVal.category;
      formData.value.lng = newVal.lng;
      formData.value.lat = newVal.lat;
      formData.value.tickets = newVal.tickets;
    }
  );
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
  onBeforeMount(() => {
    getCategories();
  });
</script>

<style scoped lang="less">
  .container {
    padding: 0 20px 40px 20px;
    overflow: hidden;
    align-items: center;
  }

  .ticket-info-card {
    border-radius: 8px;
    width: 80%;
    margin: auto;
    max-width: 1000px;
  }

  .basic-info-card {
    border-radius: 8px;
    width: 80%;
    margin: auto;
    max-width: 1000px;
  }

  .ticket {
    margin: 0;
    padding: 0;
    flex: auto;
    width: 100%;
    align-items: center;
  }
</style>
