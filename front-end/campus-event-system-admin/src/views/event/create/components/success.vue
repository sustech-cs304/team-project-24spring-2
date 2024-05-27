<template>
  <div class="success-wrap">
    <a-result
      status="success"
      :title="$t('event.create.title.finish')"
      :subtitle="$t('event.create.subTitle.finish')"
    />
    <a-space :size="16">
      <a-button key="view" type="primary" @click="onView">
        {{ $t('event.button.view') }}
      </a-button>
      <a-button key="again" type="secondary" @click="oneMore">
        {{ $t('event.button.again') }}
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
  const emits = defineEmits(['changeStep', 'editEvent']);

  const oneMore = () => {
    emits('changeStep', 1);
  };
  const onView = () => {
    emits('editEvent');
  };
</script>

<style scoped lang="less">
  .success-wrap {
    text-align: center;
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
