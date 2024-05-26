<template>
  <div class="card-wrap">
    <a-card v-if="props.loading" :bordered="false" hoverable>
      <slot name="skeleton"></slot>
    </a-card>
    <a-card v-else :bordered="false" hoverable>
      <a-space align="start">
        <a-avatar
          :size="36"
          style="margin-right: 8px; background-color: #626aea"
        >
          <icon-settings />
        </a-avatar>
        <a-card-meta>
          <template #title>
            <a-typography-text style="margin-right: 10px">
              {{ renderData.title }}
            </a-typography-text>
          </template>
          <template #description>
            {{ renderData.description }}
            <slot></slot>
          </template>
        </a-card-meta>
      </a-space>
      <template #actions>
        <div class="action">
          <modifyModal
            :setting="renderData"
            :clickText="$t('Event.Settings.change')"
            @update="updateSetting"
          />
        </div>
      </template>
    </a-card>
  </div>
</template>

<script lang="ts" setup>
  import { PropType, computed, ref } from 'vue';
  import { useToggle } from '@vueuse/core';
  import { SettingRecord, setSetting } from '@/api/global';
  import { useI18n } from 'vue-i18n';
  import { Notification } from '@arco-design/web-vue';
  import modifyModal from './modify-modal.vue';

  const { t } = useI18n();
  const props = defineProps({
    setting: {
      type: Object as PropType<SettingRecord>,
      default: {} as SettingRecord,
    },
    loading: {
      type: Boolean,
      default: false,
    },
  });

  const renderData = computed<SettingRecord>(() => {
    return {
      title: props.setting.title,
      description: props.setting.description,
      type: props.setting.type,
      value: props.setting.value,
      key: props.setting.key,
    };
  });

  const updateSetting = async (key: string, value: string) => {
    console.log('update', key, value);
    try {
      const res = await setSetting(key, value);
      Notification.success({
        title: '更新成功',
        content: `${renderData.value.title} 已更新`,
      });
      renderData.value.value = value;
    } catch (e) {
      Notification.error({
        title: '更新失败',
        content: `${renderData.value.title} 更新失败`,
      });
      console.error(e);
    } 
  };
</script>

<style scoped lang="less">
  .card-wrap {
    height: 100%;
    transition: all 0.3s;
    border: 1px solid var(--color-neutral-3);
    border-radius: 4px;
    &:hover {
      transform: translateY(-4px);
      // box-shadow: 4px 4px 10px rgba(0, 0, 0, 0.1);
    }
    :deep(.arco-card) {
      height: 100%;
      border-radius: 4px;
      .arco-card-body {
        height: 100%;
        .arco-space {
          width: 100%;
          height: 100%;
          .arco-space-item {
            height: 100%;
            &:last-child {
              flex: 1;
            }
            .arco-card-meta {
              height: 100%;
              display: flex;
              flex-flow: column;
              .arco-card-meta-content {
                flex: 1;
                .arco-card-meta-description {
                  margin-top: 8px;
                  color: rgb(var(--gray-6));
                  line-height: 20px;
                  font-size: 12px;
                }
              }
              .arco-card-meta-footer {
                margin-top: 0;
              }
            }
          }
        }
      }
    }
    :deep(.arco-card-meta-title) {
      display: flex;
      align-items: center;

      // To prevent the shaking
      line-height: 28px;
    }
    :deep(.arco-skeleton-line) {
      &:last-child {
        display: flex;
        justify-content: flex-end;
        margin-top: 20px;
      }
    }
  }

  .action {
    float: right;
    align-items: right;
  }
</style>
