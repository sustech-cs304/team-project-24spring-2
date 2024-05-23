<template>
  <div class="success-wrap">
    <a-result
      status="success"
      :title="$t('Event.edit.submit.success')"
      :subtitle="$t('Event.edit.submit.success.info')"
    />
    <a-space :size="16">
      <a-button key="view" type="primary" @click="onView">
        {{ $t('Event.View') }}
      </a-button>
      <a-button key="again" type="secondary" @click="goBack">
        {{ $t('Event.edit.submit.goback') }}
      </a-button>
    </a-space>

    <!-- <div class="details-wrapper">
      <a-typography-title :heading="6" style="margin-top: 0">
        {{ $t('stepForm.form.description.title') }}
      </a-typography-title>
      <a-typography-paragraph style="margin-bottom: 0">
        {{ $t('stepForm.form.description.text') }}
        <a-link href="link">{{ $t('stepForm.button.view') }}</a-link>
      </a-typography-paragraph>
    </div> -->
  </div>
</template>

<script lang="ts" setup>

  const emits = defineEmits(['backToManage', 'viewEvent']);

  const goBack = () => {
    emits('backToManage', 1);
  };
  const onView = () => {
    emits('viewEvent');
  }
</script>

<style scoped lang="less">
  .success-wrap {
    text-align: center;
    margin-top: 100px;
    margin-bottom: 100px;
  }
  :deep(.arco-result) {
    padding-top: 0;
  }
  .details-wrapper {
    width: 895px;
    margin-top: 54px;
    padding: 20px;
    text-align: left;
    background-color: var(--color-fill-2);
  }
</style>
