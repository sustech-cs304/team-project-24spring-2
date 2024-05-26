<template>
  <div>
    <a-button type="primary" @click="open">
      {{ props.clickText }}
    </a-button>
    <a-modal
      class="modal"
      :visible="modalVisible"
      @cancel="close"
      @ok="submit"
      unmount-on-close
    >
      <template #title>
        {{ props.setting.title }}
      </template>
      <div class="content">
        {{ props.setting.description }}
        <div class="input-form">
          <a-input-tag
            v-if="props.setting.type === 'Multi-Select-String'"
            v-model="submition"
            size="large"
            :style="{ width: '80%', textAlign: 'left' }"
            placeholder="Please Enter"
            allow-clear
          />
          <a-input-number
            v-if="props.setting.type === 'Input-Integer'"
            v-model="submition"
            mode="button"
            size="large"
            :precision="0"
            :style="{ width: '80%', textAlign: 'center' }"
            placeholder="Please Enter"
            allow-clear
          />
        </div>
      </div>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
  import { PropType, computed, ref } from 'vue';
  import { SettingRecord } from '@/api/global';

  const props = defineProps({
    setting: {
      type: Object as PropType<SettingRecord>,
      default: {} as SettingRecord,
    },
    clickText: {
      type: String,
      default: '更改设置',
    },
  });

  const submition = ref<any>();

  const emits = defineEmits(['update']);

  const modalVisible = ref(false);

  const open = () => {
    switch (props.setting.type) {
      case 'Input-Integer':
        submition.value = parseInt(props.setting.value, 10);
        break;
      case 'Multi-Select-String':
        submition.value = props.setting.value.split(',');
        break;
      default:
        submition.value = props.setting.value;
    }
    modalVisible.value = true;
  };
  const close = () => {
    modalVisible.value = false;
  };

  const submit = () => {
    let value = '';
    switch (props.setting.type) {
      case 'Input-Integer':
        value = submition.value.toString();
        break;
      case 'Multi-Select-String':
        value = submition.value.join(',');
        break;
      default:
        value = submition.value;
    }
    emits('update', props.setting.key, value);
    close();
  };
</script>

<style scoped lang="less">
  .content {
    padding: 10px;
  }
  .input-form {
    margin-top: 20px;
    align-items: center;
    text-align: center;
    width: 100%;
  }
</style>
