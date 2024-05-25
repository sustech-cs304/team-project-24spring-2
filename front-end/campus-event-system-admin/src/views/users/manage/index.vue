<template>
  <div class="container">
    <Breadcrumb :items="['menu.user', 'menu.user.manage']" />
    <a-card class="general-card" :title="$t('User.Manage')">
      <a-row>
        <a-col :flex="1">
          <a-form
            :model="searchForm"
            :label-col-props="{ span: 6 }"
            :wrapper-col-props="{ span: 18 }"
            label-align="left"
          >
            <a-row :gutter="16">
              <a-col :span="24">
                <a-form-item field="nickname" :label="$t('User.info.nickname')">
                  <a-input
                    v-model="searchForm.nickname"
                    :placeholder="$t('search.User.nickname.placeholder')"
                    allow-clear
                    @change="search"
                  />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row :gutter="16">
              <a-col :span="24">
                <a-form-item field="nickname" :label="$t('User.info.email')">
                  <a-input
                    v-model="searchForm.email"
                    :placeholder="$t('search.Event.Publisher.placeholder')"
                    allow-clear
                    @change="search"
                  />
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-col>
        <a-divider style="height: 84px" direction="vertical" />
        <a-col :flex="'86px'" style="text-align: right">
          <a-space direction="vertical" :size="18">
            <a-button type="primary" @click="search">
              <template #icon>
                <icon-search />
              </template>
              {{ $t('search.event.search') }}
            </a-button>
            <a-button @click="reset">
              <template #icon>
                <icon-refresh />
              </template>
              {{ $t('search.event.reset') }}
            </a-button>
          </a-space>
        </a-col>
      </a-row>
      <a-divider style="margin-top: 0" />
      <a-row style="margin-bottom: 16px">
        <a-col :span="12">
          <a-space>
            <!-- <a-button type="primary">
              <template #icon>
                <icon-download />
              </template>
              {{ $t('manageEventTable.operation.download') }}
            </a-button> -->
          </a-space>
        </a-col>
        <a-col
          :span="12"
          style="display: flex; align-items: center; justify-content: end"
        >
          <a-button @click="search">
            <template #icon> <icon-refresh /> </template>
            {{ $t('manageEventTable.actions.refresh') }}
          </a-button>
          <a-dropdown @select="handleSelectDensity">
            <a-tooltip :content="$t('manageEventTable.actions.density')">
              <div class="action-icon"><icon-line-height size="18" /></div>
            </a-tooltip>
            <template #content>
              <a-doption
                v-for="item in densityList"
                :key="item.value"
                :value="item.value"
                :class="{ active: item.value === size }"
              >
                <span>{{ item.name }}</span>
              </a-doption>
            </template>
          </a-dropdown>
          <a-tooltip :content="$t('manageEventTable.actions.columnSetting')">
            <a-popover
              trigger="click"
              position="bl"
              @popup-visible-change="popupVisibleChange"
            >
              <div class="action-icon"><icon-settings size="18" /></div>
              <template #content>
                <div id="tableSetting">
                  <div
                    v-for="(item, index) in showColumns"
                    :key="item.dataIndex"
                    class="setting"
                  >
                    <div style="margin-right: 4px; cursor: move">
                      <icon-drag-arrow />
                    </div>
                    <div>
                      <a-checkbox
                        v-model="item.checked"
                        @change="
                          handleChange($event, item as TableColumnData, index)
                        "
                      >
                      </a-checkbox>
                    </div>
                    <div class="title">
                      {{ item.title === '#' ? '序列号' : item.title }}
                    </div>
                  </div>
                </div>
              </template>
            </a-popover>
          </a-tooltip>
        </a-col>
      </a-row>
      <a-table
        row-key="id"
        :loading="loading"
        :pagination="pagination"
        :columns="(cloneColumns as TableColumnData[])"
        :data="renderData"
        :bordered="false"
        :size="size"
        @page-change="onPageChange"
      >
        <template #index="{ rowIndex }">
          {{ rowIndex + 1 + (pagination.current - 1) * pagination.pageSize }}
        </template>

        <template #title="{ record }">
          {{ record.title }}
        </template>

        <template #start_time="{ record }">
          {{ longTime2String(record.start_time) }}
        </template>

        <template #end_time="{ record }">
          {{ longTime2String(record.end_time) }}
        </template>

        <template #location_name="{ record }">
          {{ record.location_name }}
        </template>

        <template #count="{ record }">
          {{ record.count + ' / ' + record.capacity }}
        </template>

        <template #operations="{ record }">
          <a-space>
            <!-- <a-button size="small" type="primary" @click.prevent="">
              {{ $t('manageUserTable.columns.operation') }}
            </a-button> -->
            <a-select @change.prevent="changeUserPermission(record.id, '')">
              <a-option
                v-for="(item, index) in roleList"
                :key="index"
                :value="item"
              >
                {{ $t(`User.permission.group.${item}`) }}
              </a-option>
            </a-select>
          </a-space>
        </template>
      </a-table>
    </a-card>
  </div>
</template>

<script lang="ts" setup>
  import { useRouter } from 'vue-router';
  import { computed, ref, reactive, watch, nextTick, onBeforeMount } from 'vue';
  import { useI18n } from 'vue-i18n';
  import useLoading from '@/hooks/loading';
  import { roleList } from '@/store/modules/user';
  import {
    listUsers,
    listUsersSize,
    UsersRecord,
    UsersParams,
  } from '@/api/users';
  import { getSetting } from '@/api/global';
  import { Pagination } from '@/types/global';
  import type { SelectOptionData } from '@arco-design/web-vue/es/select/interface';
  import type { TableColumnData } from '@arco-design/web-vue/es/table/interface';
  import cloneDeep from 'lodash/cloneDeep';
  import Sortable from 'sortablejs';
  import { keys } from 'lodash';

  type SizeProps = 'mini' | 'small' | 'medium' | 'large';
  type Column = TableColumnData & { checked?: true };

  const router = useRouter();

  const { loading, setLoading } = useLoading(true);
  const { t } = useI18n();
  const renderData = ref<UsersRecord[]>([]);
  const searchForm = ref<UsersParams>({} as UsersParams);
  const cloneColumns = ref<Column[]>([]);
  const showColumns = ref<Column[]>([]);

  const size = ref<SizeProps>('medium');

  const basePagination: Pagination = {
    current: 1,
    pageSize: 10,
  };

  const defaultPagenation = {
    page: basePagination.current,
    size: basePagination.pageSize,
  };
  const pagination = reactive({
    ...basePagination,
  });

  //   const auditEvent = (uuid: string) => {
  //     router.push({
  //       path: '/event/audit',
  //       query: {
  //         uuid,
  //         usage: 'AUDITING',
  //       },
  //     });
  //   };
  const changeUserPermission = (uuid: string, group: string) => {
    router.push({
      path: '/user/permission',
      query: {
        userId: uuid,
        permissionGroup: group,
      },
    });
  };

  const densityList = computed(() => [
    {
      name: t('manageEventTable.size.mini'),
      value: 'mini',
    },
    {
      name: t('manageEventTable.size.small'),
      value: 'small',
    },
    {
      name: t('manageEventTable.size.medium'),
      value: 'medium',
    },
    {
      name: t('manageEventTable.size.large'),
      value: 'large',
    },
  ]);
  const columns = computed<TableColumnData[]>(() => [
    {
      title: t('manageEventTable.columns.index'),
      dataIndex: 'index',
      slotName: 'index',
    },
    {
      title: t('manageEventTable.columns.title'),
      dataIndex: 'title',
    },

    {
      title: t('manageEventTable.columns.startTime'),
      dataIndex: 'start_time',
      slotName: 'start_time',
    },
    {
      title: t('manageEventTable.columns.endTime'),
      dataIndex: 'end_time',
      slotName: 'end_time',
    },

    {
      title: t('manageEventTable.columns.location'),
      dataIndex: 'location_name',
      slotName: 'location_name',
    },
    {
      title: t('manageEventTable.columns.operations'),
      dataIndex: 'operations',
      slotName: 'operations',
      align: 'center',
    },
  ]);

  const fetchData = async (params: UsersParams = { page: 1, size: 20 }) => {
    setLoading(true);
    try {
      const newParam = Object.fromEntries(
        Object.entries(params).filter(([_, v]) => v !== '')
      ) as UsersParams;
      const queryParams = {
        ...newParam,
        page: params.page - 1,
      };
      const sizeParams = {
        ...newParam,
      } as any;

      delete sizeParams.page;
      delete sizeParams.size;

      const resLen = await listUsersSize(sizeParams);
      const res = await listUsers(queryParams);

      console.log(res.data);
      renderData.value = res.data;
      pagination.current = params.page;
      pagination.total = resLen.data;
    } catch (err) {
      // you can report use errorHandler or other
    } finally {
      setLoading(false);
    }
  };

  const search = () => {
    console.log(searchForm.value);
    fetchData({
      ...searchForm.value,
      ...defaultPagenation,
    } as unknown as UsersParams);
  };

  const onPageChange = (current: number) => {
    fetchData({
      ...searchForm.value,
      ...defaultPagenation,
      page: current,
    });
  };

  const reset = () => {
    searchForm.value = {} as UsersParams;
    search();
  };

  const handleSelectDensity = (
    val: string | number | Record<string, any> | undefined,
    e: Event
  ) => {
    size.value = val as SizeProps;
  };

  const handleChange = (
    checked: boolean | (string | boolean | number)[],
    column: Column,
    index: number
  ) => {
    if (!checked) {
      cloneColumns.value = showColumns.value.filter(
        (item) => item.dataIndex !== column.dataIndex
      );
    } else {
      cloneColumns.value.splice(index, 0, column);
    }
  };

  const exchangeArray = <T extends Array<any>>(
    array: T,
    beforeIdx: number,
    newIdx: number,
    isDeep = false
  ): T => {
    const newArray = isDeep ? cloneDeep(array) : array;
    if (beforeIdx > -1 && newIdx > -1) {
      // 先替换后面的，然后拿到替换的结果替换前面的
      newArray.splice(
        beforeIdx,
        1,
        newArray.splice(newIdx, 1, newArray[beforeIdx]).pop()
      );
    }
    return newArray;
  };

  const onEventEditClicked = (uuid: string) => {
    router.push({
      path: '/event/edit',
      query: {
        uuid,
      },
    });
  };

  const longTime2String = (time: number) => {
    const date = new Date(time);
    const dateStr = `${date.getFullYear()}-${
      date.getMonth() + 1
    }-${date.getDate()} ${date.getHours()}:${date.getMinutes()}:${date.getSeconds()}`;
    return dateStr;
  };

  const popupVisibleChange = (val: boolean) => {
    if (val) {
      nextTick(() => {
        const el = document.getElementById('tableSetting') as HTMLElement;
        const sortable = new Sortable(el, {
          onEnd(e: any) {
            const { oldIndex, newIndex } = e;
            exchangeArray(cloneColumns.value, oldIndex, newIndex);
            exchangeArray(showColumns.value, oldIndex, newIndex);
          },
        });
      });
    }
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
  onBeforeMount(() => {
    fetchData();
  });
</script>

<script lang="ts">
  export default {
    name: 'EventTable',
  };
</script>

<style scoped lang="less">
  .container {
    padding: 0 20px 20px 20px;
  }

  :deep(.arco-table-th) {
    &:last-child {
      .arco-table-th-item-title {
        margin-left: 16px;
      }
    }
  }

  .action-icon {
    margin-left: 12px;
    cursor: pointer;
  }

  .active {
    color: #0960bd;
    background-color: #e3f4fc;
  }

  .setting {
    display: flex;
    align-items: center;
    width: 200px;

    .title {
      margin-left: 12px;
      cursor: pointer;
    }
  }
</style>
