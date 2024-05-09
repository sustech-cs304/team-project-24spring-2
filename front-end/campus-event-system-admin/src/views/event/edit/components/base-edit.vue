<template>
  <div class="container">
    <a-form ref="formRef" layout="vertical" :model="formData">
      <a-space direction="vertical" :size="16">
        <a-card class="info-card" hoverable>
          <template #title>
            {{ $t('groupForm.title.video') }}
          </template>
          <a-row :gutter="80">
            <a-col :span="8">
              <a-form-item
                :label="$t('groupForm.form.label.video.mode')"
                field="video.mode"
              >
                <a-select :placeholder="$t('groupForm.placeholder.video.mode')">
                  <a-option value="custom">自定义</a-option>
                  <a-option value="mode1">模式1</a-option>
                  <a-option value="mode2">模式2</a-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item
                :label="$t('groupForm.form.label.video.acquisition.resolution')"
                field="video.acquisition.resolution"
              >
                <a-select
                  :placeholder="
                    $t('groupForm.placeholder.video.acquisition.resolution')
                  "
                >
                  <a-option value="resolution1">分辨率1</a-option>
                  <a-option value="resolution2">分辨率2</a-option>
                  <a-option value="resolution3">分辨率3</a-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item
                :label="$t('groupForm.form.label.video.acquisition.frameRate')"
                field="video.acquisition.frameRate"
              >
                <a-input
                  :placeholder="
                    $t('groupForm.placeholder.video.acquisition.frameRate')
                  "
                >
                  <template #append> fps </template>
                </a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="80">
            <a-col :span="8">
              <a-form-item
                :label="$t('groupForm.form.label.video.encoding.resolution')"
                field="video.encoding.resolution"
              >
                <a-select
                  :placeholder="
                    $t('groupForm.placeholder.video.encoding.resolution')
                  "
                >
                  <a-option value="resolution1">分辨率1</a-option>
                  <a-option value="resolution2">分辨率2</a-option>
                  <a-option value="resolution3">分辨率3</a-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item
                :label="$t('groupForm.form.label.video.encoding.rate.min')"
                field="video.encoding.rate.min"
              >
                <a-input
                  :placeholder="
                    $t('groupForm.placeholder.video.encoding.rate.min')
                  "
                  add-after="bps"
                >
                  <template #append> bps </template>
                </a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item
                :label="$t('groupForm.form.label.video.encoding.rate.max')"
                field="video.encoding.rate.max"
              >
                <a-input
                  :placeholder="
                    $t('groupForm.placeholder.video.encoding.rate.max')
                  "
                >
                  <template #append> bps </template>
                </a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="80">
            <a-col :span="8">
              <a-form-item
                :label="$t('groupForm.form.label.video.encoding.rate.default')"
                field="video.encoding.rate.default"
              >
                <a-input
                  :placeholder="
                    $t('groupForm.placeholder.video.encoding.rate.default')
                  "
                >
                  <template #append> bps </template>
                </a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item
                :label="$t('groupForm.form.label.video.encoding.frameRate')"
                field="video.encoding.frameRate"
              >
                <a-input
                  :placeholder="
                    $t('groupForm.placeholder.video.encoding.frameRate')
                  "
                >
                  <template #append> fps </template>
                </a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item
                :label="$t('groupForm.form.label.video.encoding.profile')"
                field="video.encoding.profile"
              >
                <a-input
                  :placeholder="
                    $t('groupForm.placeholder.video.encoding.profile')
                  "
                >
                  <template #append> bps </template>
                </a-input>
              </a-form-item>
            </a-col>
          </a-row>
        </a-card>
        <a-card class="info-card" hoverable>
          <template #title>
            {{ $t('groupForm.title.video') }}
          </template>
          <a-row :gutter="80">
            <a-col :span="8">
              <a-form-item
                :label="$t('groupForm.form.label.video.mode')"
                field="video.mode"
              >
                <a-select :placeholder="$t('groupForm.placeholder.video.mode')">
                  <a-option value="custom">自定义</a-option>
                  <a-option value="mode1">模式1</a-option>
                  <a-option value="mode2">模式2</a-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item
                :label="$t('groupForm.form.label.video.acquisition.resolution')"
                field="video.acquisition.resolution"
              >
                <a-select
                  :placeholder="
                    $t('groupForm.placeholder.video.acquisition.resolution')
                  "
                >
                  <a-option value="resolution1">分辨率1</a-option>
                  <a-option value="resolution2">分辨率2</a-option>
                  <a-option value="resolution3">分辨率3</a-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item
                :label="$t('groupForm.form.label.video.acquisition.frameRate')"
                field="video.acquisition.frameRate"
              >
                <a-input
                  :placeholder="
                    $t('groupForm.placeholder.video.acquisition.frameRate')
                  "
                >
                  <template #append> fps </template>
                </a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="80">
            <a-col :span="8">
              <a-form-item
                :label="$t('groupForm.form.label.video.encoding.resolution')"
                field="video.encoding.resolution"
              >
                <a-select
                  :placeholder="
                    $t('groupForm.placeholder.video.encoding.resolution')
                  "
                >
                  <a-option value="resolution1">分辨率1</a-option>
                  <a-option value="resolution2">分辨率2</a-option>
                  <a-option value="resolution3">分辨率3</a-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item
                :label="$t('groupForm.form.label.video.encoding.rate.min')"
                field="video.encoding.rate.min"
              >
                <a-input
                  :placeholder="
                    $t('groupForm.placeholder.video.encoding.rate.min')
                  "
                  add-after="bps"
                >
                  <template #append> bps </template>
                </a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item
                :label="$t('groupForm.form.label.video.encoding.rate.max')"
                field="video.encoding.rate.max"
              >
                <a-input
                  :placeholder="
                    $t('groupForm.placeholder.video.encoding.rate.max')
                  "
                >
                  <template #append> bps </template>
                </a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="80">
            <a-col :span="8">
              <a-form-item
                :label="$t('groupForm.form.label.video.encoding.rate.default')"
                field="video.encoding.rate.default"
              >
                <a-input
                  :placeholder="
                    $t('groupForm.placeholder.video.encoding.rate.default')
                  "
                >
                  <template #append> bps </template>
                </a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item
                :label="$t('groupForm.form.label.video.encoding.frameRate')"
                field="video.encoding.frameRate"
              >
                <a-input
                  :placeholder="
                    $t('groupForm.placeholder.video.encoding.frameRate')
                  "
                >
                  <template #append> fps </template>
                </a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item
                :label="$t('groupForm.form.label.video.encoding.profile')"
                field="video.encoding.profile"
              >
                <a-input
                  :placeholder="
                    $t('groupForm.placeholder.video.encoding.profile')
                  "
                >
                  <template #append> bps </template>
                </a-input>
              </a-form-item>
            </a-col>
          </a-row>
        </a-card>
      </a-space>
    </a-form>
  </div>
</template>

<script lang="ts" setup>
  import { onMounted, ref } from 'vue';
  import { originalEventCreationModel } from '@/api/event';
  import { FormInstance } from '@arco-design/web-vue/es/form';

  const formData = ref<originalEventCreationModel>({} as originalEventCreationModel);
  const formRef = ref<FormInstance>();


  onMounted(() => {
    console.log('mounted');
  });
</script>

<style scoped lang="less">
  .container {
    padding: 0 20px 40px 20px;
    overflow: hidden;
  }

  .info-card {
    border-radius: 8px;
  }
</style>
