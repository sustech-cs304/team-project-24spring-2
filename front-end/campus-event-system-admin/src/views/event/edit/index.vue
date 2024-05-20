<template>
  <div class="container">
    <Breadcrumb :items="['menu.event', 'menu.event.edit']" />
    <a-spin
      :loading="loading"
      tip="This may take a while..."
      style="width: 100%"
    >
      <a-row :gutter="20" align="stretch">
        <a-col :span="24">
          <a-card :title="$t('menu.event.edit')">
            <a-row justify="space-between">
              <a-col :span="24">
                <a-tabs :default-active-tab="1" type="rounded">
                  <a-tab-pane key="1" :title="$t('eventEdit.tab.title.basic')">
                    <baseEdit
                      ref="be"
                      v-model:form="formData"
                      v-model:mod="modification"
                    />
                  </a-tab-pane>
                  <a-tab-pane key="2" :title="$t('eventEdit.tab.title.detail')">
                    <infoEdit
                      ref="ie"
                      :cover-image="formData.image_url"
                      :markdown-doc="formData.document_url"
                      v-model:form="formData"
                      v-model:mod="modification"
                    />
                  </a-tab-pane>
                </a-tabs>
              </a-col>
            </a-row>
          </a-card>
        </a-col>
      </a-row>
      <a-card class="actions">
        <a-space>
          <a-button @click="reset">
            <template #icon>
              <icon-redo />
            </template>
            {{ $t('eventEdit.reset') }}
          </a-button>
          <a-button type="secondary" @click="saveEvent">
            <template #icon>
              <icon-save />
            </template>
            {{ $t('eventEdit.save') }}
          </a-button>
          <a-button type="primary">
            {{ $t('eventEdit.submit') }}
          </a-button>
        </a-space>
      </a-card>
    </a-spin>
  </div>
</template>

<script lang="ts" setup>
  //   import QualityInspection from './components/quality-inspection.vue';
  import { onBeforeMount, ref } from 'vue';
  import { Notification } from '@arco-design/web-vue';
  import { useI18n } from 'vue-i18n';
  import {
    originalEventCreationModel,
    EventUpdateModel,
    getEventInfo,
    getTicketInfo,
    updateEvent,
  } from '@/api/event';
  import { uploadFile, getFile } from '@/api/file';
  import { keys } from 'lodash';

  import useLoading from '@/hooks/loading';
  import baseEdit from './components/base-edit.vue';
  import infoEdit from './components/detail-edit.vue';

  const be = ref();
  const ie = ref();

  const { loading, setLoading } = useLoading(true);

  const formData = ref<originalEventCreationModel>(
    {} as originalEventCreationModel
  );

  const originData = ref<originalEventCreationModel>(
    {} as originalEventCreationModel
  );

  const modification = ref({});
  const args = new URLSearchParams(window.location.search);
  const uuid = args.get('uuid') as string;

  const fetchData = async () => {
    setLoading(true);
    try {
      const { data } = await getEventInfo(uuid);
      const promises = Object.values(data.tickets).map((uuid) =>
        getTicketInfo(uuid)
      );
      const res = await Promise.all(promises);

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

      originData.value = {
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
    } catch (err) {
      // operations
    } finally {
      setLoading(false);
    }
  };

  const updateMkd = async () => {
    const mkdText = ie.value.getDiffMkd();
    if (mkdText !== '') {
      const newMkd = new File([mkdText], 'content.md', {
        type: 'text/markdown',
      });
      const mkdData = new FormData();
      mkdData.append('file', newMkd);
      const resMkd = await uploadFile(mkdData, {
        usage: 'event',
        eventId: uuid,
      });
      return resMkd.data;
    }
    return '';
  };

  const saveEvent = async () => {
    try {
      const coverUrl = await ie.value.updateCover();
      const mkdUrl = await updateMkd();

      const Dates: Date[] = formData.value.time_range;
      const startDate = new Date(Dates[0]).getTime();
      const endDate = new Date(Dates[1]).getTime();
      const sendData = ref<EventUpdateModel>({} as EventUpdateModel);

      if (formData.value.title !== originData.value.title) {
        sendData.value.title = originData.value.title;
      }
      if (
        formData.value.time_range[0].toString() !==
        originData.value.time_range[0].toString()
      ) {
        sendData.value.start_time = startDate;
      }
      if (
        formData.value.time_range[1].toString() !==
        originData.value.time_range[1].toString()
      ) {
        sendData.value.end_time = endDate;
      }

      if (formData.value.address !== originData.value.address) {
        sendData.value.location_name = formData.value.address;
        sendData.value.latitude = formData.value.lat;
        sendData.value.longitude = formData.value.lng;
      }
      if (formData.value.category !== originData.value.category) {
        sendData.value.category = formData.value.category;
      }
      if (
        formData.value.tickets.toString() !==
        originData.value.tickets.toString()
      ) {
        sendData.value.tickets = formData.value.tickets;
      }

      if (coverUrl !== '') {
        sendData.value.image_url = coverUrl;
      }
      if (mkdUrl !== '') {
        sendData.value.document_url = mkdUrl;
      }
      const updKeys = keys(sendData.value);
      console.log('updated', updKeys);
      if (updKeys.length > 0) {
        const res = await updateEvent(uuid, sendData.value);
        Notification.success({
          title: 'Success',
          content: '更新成功！',
        });
        fetchData();
      } else {
        console.log('Nothing Updated');
        Notification.info({
          title: 'Info',
          content: '没有更新',
        });
      }
    } catch (e) {
      console.log(e);
    }
  };
  onBeforeMount(async () => {
    const res = await fetchData();
  });

  const reset = () => {
    formData.value = {
      ...originData.value,
    };
    ie.value.reset();
  };
</script>

<script lang="ts">
  export default {
    name: 'Card',
  };
</script>

<style scoped lang="less">
  .container {
    padding: 0 20px 20px 20px;
    :deep(.arco-list-content) {
      overflow-x: hidden;
    }

    :deep(.arco-card-meta-title) {
      font-size: 14px;
    }
  }
  :deep(.arco-list-col) {
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    justify-content: space-between;
  }

  :deep(.arco-list-item) {
    width: 33%;
  }

  :deep(.block-title) {
    margin: 0 0 12px 0;
    font-size: 14px;
  }
  :deep(.list-wrap) {
    // min-height: 140px;
    .list-row {
      align-items: stretch;
      .list-col {
        margin-bottom: 16px;
      }
    }
    :deep(.arco-space) {
      width: 100%;
      .arco-space-item {
        &:last-child {
          flex: 1;
        }
      }
    }
  }

  .actions {
    position: flex;
    height: 65px;
    margin-top: 10px;
    // margin:  auto;
    background: var(--color-bg-2);
    text-align: right;
  }
</style>
