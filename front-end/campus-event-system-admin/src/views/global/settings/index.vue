<template>
  <div class="container">
    <Breadcrumb :items="['menu.global', 'menu.global.settings']" />
    <a-row :gutter="20" align="stretch">
      <a-col :span="24">
        <a-card class="general-card" :title="$t('menu.global.settings')">
          <a-row justify="space-between">
            <a-col :span="24">
              <a-tabs :default-active-tab="1" type="rounded">
                <a-tab-pane key="1" :title="$t('Global.tab.all')">
                  <!-- <QualityInspection /> -->
                  <!-- <TheService /> -->
                  <ModifySettings :settings="settingsList" />
                </a-tab-pane>
                <!-- <a-tab-pane
                  key="2"
                  :title="$t('eventSettings.tab.title.content')"
                >
                  <QualityInspection />
                </a-tab-pane>
                <a-tab-pane
                  key="3"
                  :title="$t('eventSettings.tab.title.service')"
                >
                  <TheService />
                </a-tab-pane>
                <a-tab-pane
                  key="4"
                  :title="$t('eventSettings.tab.title.preset')"
                >
                  <RulesPreset /> -->
                <!-- </a-tab-pane> -->
              </a-tabs>
            </a-col>
            <a-input-search
              :placeholder="$t('Global.settings.search.placeholder')"
              style="width: 240px; position: absolute; top: 60px; right: 20px"
              @search="search"
            />
          </a-row>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts" setup>
  //   import QualityInspection from './components/quality-inspection.vue';
  //   import TheService from './components/the-service.vue';
  import useLoading from '@/hooks/loading';

  import { onBeforeMount, ref } from 'vue';
  import {
    SettingRecord,
    getSettings,
    getSetting,
    setSetting,
  } from '@/api/global';
  import ModifySettings from './components/modify-settings.vue';

  const { loading, setLoading } = useLoading();
  const defaultValue: SettingRecord[] = new Array(4).fill({} as SettingRecord);
  const settingsList = ref<SettingRecord[]>(defaultValue);

  const getSettingProps = (key: string) => {
    switch (key) {
      case 'categories':
        return {
          title: '活动类型',
          description:
            '设置可选的活动类型，决定了在创建和编辑过程中，部门负责人可以选择的活动类型',
          type: 'Multi-Select-String',
        };
      case 'comment_max_weight':
        return {
          title: '评论最大权重',
          description:
            '设置评论的最大容量，决定了一条评论可以容下多少视频和图片内容，通常设置为9',
          type: 'Input-Integer',
        };
      case 'comment_image_weight':
        return {
          title: '图片权重',
          description:
            '设置图片的权重，决定了一张图片占用的评论容量，通常设置为1',
          type: 'Input-Integer',
        };
      case 'comment_video_weight':
        return {
          title: '视频权重',
          description:
            '设置视频的权重，决定了一段视频占用的评论容量，通常设置为9',
          type: 'Input-Integer',
        };
      case 'order_expire_time':
        return {
          title: '订单过期时间',
          description:
            '设置订单的过期时间，超过这个时间的订单将被自动取消，单位为毫秒',
          type: 'Input-Integer',
        };

      case 'amap_api_code':
        return {
          title: '高德地图API Code',
          description: '设置高德地图API Code，用于地图服务，该API管理平台共享',
          type: 'Input-String',
        };

      case 'amap_api_key':
        return {
          title: '高德地图API Key',
          description: '设置高德地图API Key，用于地图服务，该API管理平台共享',
          type: 'Input-String',
        };
      default:
        return {
          title: key,
          description: key,
          type: 'Input-String',
        };
    }
  };

  const fetchData = async (searchKeyWord = '') => {
    try {
      setLoading(true);
      const res = await getSettings();
      settingsList.value = res.data;
      settingsList.value = settingsList.value.map((item) => {
        const p = getSettingProps(item.key);
        return {
          title: item.title || p.title,
          description: item.description || p.description,
          type: item.type || p.type,
          value: item.value,
          key: item.key,
        };
      });

      settingsList.value = settingsList.value.filter((item) => {
        return item.description?.includes(searchKeyWord);
      });
      settingsList.value.sort((a, b) => {
        return a.key.localeCompare(b.key);
      });
    } finally {
      setLoading(false);
    }
  };

  const search = (value: string) => {
    fetchData(value);
  };

  onBeforeMount(async () => {
    await fetchData();
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
</style>
