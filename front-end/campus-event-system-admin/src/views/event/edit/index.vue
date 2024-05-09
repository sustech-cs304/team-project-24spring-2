<template>
  <div class="container">
    <Breadcrumb :items="['menu.event', 'menu.event.edit']" />
    <a-row :gutter="20" align="stretch">
      <a-col :span="24">
        <a-card :title="$t('menu.event.edit')">
          <a-row justify="space-between">
            <a-col :span="24">
              <a-tabs :default-active-tab="1" type="rounded">
                <a-tab-pane key="1" :title="$t('eventEdit.tab.title.basic')">
                  <TheService />
                </a-tab-pane>
                <a-tab-pane key="2" :title="$t('eventEdit.tab.title.detail')">
                  <RulesPreset />
                </a-tab-pane>
              </a-tabs>
            </a-col>
            <a-input-search
              :placeholder="$t('eventEdit.searchInput.placeholder')"
              style="width: 240px; position: absolute; top: 60px; right: 20px"
            />
          </a-row>
        </a-card>
      </a-col>
    </a-row>
    <a-card class="actions">
      <a-space>
        <a-button>
          {{ $t('groupForm.reset') }}
        </a-button>
        <a-button type="primary" :loading="loading" @click="onSubmitClick">
          {{ $t('groupForm.submit') }}
        </a-button>
      </a-space>
    </a-card>
  </div>
</template>

<script lang="ts" setup>
  //   import QualityInspection from './components/quality-inspection.vue';
  import { onBeforeMount, ref } from 'vue';
  import { useI18n } from 'vue-i18n';
  import { originalEventCreationModel, getEventInfo } from '@/api/event';
  import useLoading from '@/hooks/loading';
  import TheService from './components/base-edit.vue';
  import RulesPreset from './components/detail-edit.vue';

  const { loading, setLoading } = useLoading(true);

  const formData = ref<originalEventCreationModel>(
    {} as originalEventCreationModel
  );
  const { t } = useI18n();
  setLoading(true);
  onBeforeMount(async () => {
    try {
      const args = new URLSearchParams(window.location.search);
      const { data } = await getEventInfo(args.get('uuid') as string);

      formData.value = {
        title: data.title,
        address: data.location_name,
        category_id: data.category_id,
        lng: data.longitude,
        lat: data.latitude,
        tickets: data.tickets,
        document_url: data.document_url,
        image_url: data.image_url,
        time_range: [new Date(data.start_time), new Date(data.end_time)],
      };
    } catch (err) {
        console.log(err)
    } finally {
      setLoading(false);
    }
  });
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
