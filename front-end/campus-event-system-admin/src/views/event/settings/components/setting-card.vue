<template>
  <div class="card-wrap">
    <a-card v-if="loading" :bordered="false" hoverable>
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
          <a-button type="primary" @click="renew">
            {{ $t('Event.Settings.change') }}
          </a-button>
        </div>
      </template>
    </a-card>
  </div>
</template>

<script lang="ts" setup>
  import { PropType, computed, ref } from 'vue';
  import { useToggle } from '@vueuse/core';
  import { SettingRecord } from '@/api/global';
  import { useI18n } from 'vue-i18n';

  const { t } = useI18n();
  const props = defineProps({
    setting: {
      type: Object as PropType<SettingRecord>,
      default: {} as SettingRecord,
    },
  });

  const getSettingProps = (key: string) => {
    switch (key) {
      case 'categories':
        return {
          title: '活动类型',
          description: '设置可选的活动类型，决定了在创建和编辑过程中，部门负责人可以选择的活动类型',
          type: 'Multi-Select-String',
        };
      case 'comment_max_weight':
        return {
          title: '评论最大权重',
          description: '设置评论的最大容量，决定了一条评论可以容下多少视频和图片内容，与图片视频权重一起设置可以达到限制的目的',
          type: 'Input-Integer',
        };
        case 'comment_image_weight':
        return {
          title: '图片权重',
          description: '设置图片的权重，决定了一张图片占用的评论容量，通常设置为1',
          type: 'Input-Integer',
        };
        case 'comment_video_weight':
        return {
          title: '视频权重',
          description: '设置视频的权重，决定了一段视频占用的评论容量，通常设置为9',
          type: 'Input-Integer',
        };
      default:
        return {
          title: '未知',
          description: '未知',
          type: '未知',
        };
    }
  };

  const renderData = computed<SettingRecord>(() => {
    const p = getSettingProps(props.setting.key);
    return {
      title: props.setting.title || p.title,
      description: props.setting.description || p.description,
      type: props.setting.type || p.type,
    };
  });
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
