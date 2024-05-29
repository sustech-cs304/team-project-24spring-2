<template>
  <div class="container">
    <Breadcrumb :items="['menu.event', 'menu.event.view']" />
    <a-space direction="vertical" :size="16" fill>
      <a-card class="general-card">
        <template #title>
          {{ $t('menu.event.view') }}
        </template>
        <template #extra>
          <a-button type="primary" @click="goBack">
            {{ $t('button.back') }}
          </a-button>
        </template>
      </a-card>

      <div class="ticket-view">
        <a-spin :loading="loading" tip="This may take a while..." class="spin">
          <div class="has-cover" v-if="formData.image_url !== ''">
            <img :src="formData.image_url" class="cover-image" />
          </div>
          <div class="has-cover" v-else>
            <icon-image style="width: 50%; height: 200px" />
          </div>
        </a-spin>
      </div>
      <a-card class="markdown">
        <div class="markdown-html">
          <VMdEditor v-model="source" />
        </div>
      </a-card>
    </a-space>
  </div>
</template>

<script lang="ts" setup>
  import { ref, onBeforeMount } from 'vue';
  import { useRouter } from 'vue-router';
  import useLoading from '@/hooks/loading';
  import { getFile } from '@/api/file';
  import {
    originalEventCreationModel,
    getEventInfo,
    getTicketInfo,
  } from '@/api/event';
  import {} from 'vditor';

  import VMdEditor from './components/EditorMarkdown.vue';

  const router = useRouter();
  const formData = ref<originalEventCreationModel>(
    {} as originalEventCreationModel
  );

  const { loading, setLoading } = useLoading(true);
  const args = new URLSearchParams(window.location.search);
  const uuid = args.get('uuid') as string;
  const fetchData = async () => {
    setLoading(true);
    try {
      const { data } = await getEventInfo(uuid);
      const promises = Object.values(data.tickets).map((id) =>
        getTicketInfo(id)
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
    } finally {
      setLoading(false);
    }
  };
  const source = ref<string>('');

  const fetchMarkdown = async () => {
    const url = formData.value.document_url;
    if (!url) return;
    const res = await getFile(url);
    source.value = res.data;
  };
  const goBack = () => {
    router.go(-1);
  };

  const renderMarkdown = () => {
    // VMdEditor.use(githubTheme);
  };

  onBeforeMount(async () => {
    await fetchData();
    await fetchMarkdown();
    renderMarkdown();
  });
</script>
<style scoped lang="less">
  .container {
    padding: 0 20px 20px 20px;
  }
  .ticket-view {
    min-height: 275px;
    // max-height: 300px;
    padding: 10px 10px 10px 10px;
    background-color: #ffffff;
  }
  .has-cover {
    width: 100%;
    height: 100%;
    border-radius: 8px;
    background-color: #fafafa;
    display: flex;
    justify-content: center;
    align-items: center;
    text-align: center;
    .cover-image {
      height: 100%;
    }
  }
  .spin {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 250px;
  }
  .markdown {
    width: 100%;

    padding-top: 50px;
    padding-bottom: 50px;
    padding-left: 100px;
    padding-right: 100px;
    align-items: center;
    border-radius: 8px;
    min-height: 200px;
    background-color: #ffffff;
  }
  .markdown-html {
    padding: 20px;
    margin: auto;
    max-width: 1000px;
    background-color: #ffffff;
  }
</style>
