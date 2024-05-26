<template>
  <div class="list-wrap">
    <a-typography-title class="block-title" :heading="6">
      {{ $t('cardList.tab.title.preset') }}
    </a-typography-title>
    <a-row class="list-row" :gutter="24">
      <a-col
        v-for="item in renderData"
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
  import useRequest from '@/hooks/request';
  import useLoading from '@/hooks/loading';
  import SettingCard from './setting-card.vue';

  const defaultValue: SettingRecord[] = new Array(6).fill({} as SettingRecord);
  const renderData = ref<SettingRecord[]>(defaultValue);
  const { loading, setLoading } = useLoading();

  const fetchData = async () => {
    try {
      setLoading(true);
      const res = await getSettings();
      renderData.value = res.data;
    } finally {
      setLoading(false);
    }
  };

  onBeforeMount(async () => {
    await fetchData();
  });
</script>

<style scoped lang="less"></style>
