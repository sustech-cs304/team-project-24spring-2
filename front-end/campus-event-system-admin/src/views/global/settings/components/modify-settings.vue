<template>
  <div class="list-wrap">
    <a-typography-title class="block-title" :heading="6">
      {{ $t('Global.settings.title.all') }}
    </a-typography-title>
    <a-row class="list-row" :gutter="24">
      <a-col
        v-for="item in props.settings"
        :key="item.key"
        :xs="12"
        :sm="12"
        :md="12"
        :lg="6"
        :xl="6"
        :xxl="6"
        class="list-col"
        style="min-height: 140px"
      >
        <SettingCard
          :setting="item"
        >
          <template #skeleton>
            <a-skeleton :animation="true">
              <a-skeleton-line :widths="['100%', '40%']" :rows="2" />
              <a-skeleton-line :widths="['40%']" :rows="1" />
            </a-skeleton>
          </template>
        </SettingCard>
      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts" setup>
  import { onBeforeMount, ref } from 'vue';
  import {
    SettingRecord,
    getSettings,
    getSetting,
    setSetting,
  } from '@/api/global';
  import useLoading from '@/hooks/loading';
  import SettingCard from './setting-card.vue';


  const props = defineProps<{
    settings: SettingRecord[];
  }>();

</script>

<style scoped lang="less">
.list-wrap{
    min-height: 200px;
    .block-title{
        margin-bottom: 16px;
    }
    .list-row{
        .list-col{
        margin-bottom: 24px;
        }
    }

}
</style>
