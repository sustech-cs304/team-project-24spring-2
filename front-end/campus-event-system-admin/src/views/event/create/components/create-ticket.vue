<template>
  <a-button @click="handleClick">Open Form Modal</a-button>
  <a-modal
    v-model:visible="visible"
    title="Modal Form"
    @cancel="handleCancel"
    @before-ok="handleBeforeOk"
  >
    <a-form :model="form" ref="formRef">
      <a-form-item
        field="description"
        :label="$t('ticket.description')"
        :rules="[
          {
            required: true,
            message: $t('ticket.description.required'),
          },
        ]"
      >
        <a-input v-model="form.description" />
      </a-form-item>


      <a-form-item
        field="price"
        :label="$t('ticket.price')"
        :rules="[
          {
            required: true,
            message: $t('ticket.price.required'),
          },
          {
            match: /^[0-9]*$/,
            message: $t('ticket.price.pattern'),
          },
        ]"
      >
        <a-input v-model="form.price" />
      </a-form-item>


      <a-form-item
        field="total_amount"
        :label="$t('ticket.total_amount')"
        :rules="[
          {
            required: true,
            message: $t('ticket.total_amount.required'),
          },
          {
            match: /^[0-9]*$/,
            message: $t('ticket.total_amount.pattern'),
          },
        ]"
      >
        <a-input v-model="form.total_amount" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts" setup>
  import { ref } from 'vue';
  import { FormInstance } from '@arco-design/web-vue/es/form';
  import { Tickets } from '@/api/event';

  const formRef = ref<FormInstance>();

  const visible = ref(false);
  const emits = defineEmits(['editConfirm']);

  const form = ref<Tickets>({
    description: '',
    price: '',
    total_amount: '',
  });

  const handleClick = () => {
    visible.value = true;
    form.value = {
      description: '',
      price: '',
      total_amount: '',
    };
  };

  const handleBeforeOk = async (done: any) => {
    console.log('emit', form.value);
    const res = await formRef.value?.validate();
    if (!res) {
      emits('editConfirm', form.value);
      done();
    } else {
      done(false);
    }
    // window.setTimeout(() => {
    //   done()
    //   // prevent close
    //   // done(false)
    // }, 3000)
    // this.$emit('close-dialog')
  };
  const handleCancel = () => {
    visible.value = false;
  };
</script>
