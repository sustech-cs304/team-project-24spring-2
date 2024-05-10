<template>
  <a-button type="primary" @click="handleClick" style="width: inherit">{{$t('ticket.create')}}</a-button>
  <a-modal
    v-model:visible="visible"
    :title="$t('ticket.create')"
    @cancel="handleCancel"
    @before-ok="handleBeforeOk"
  >
    <a-form ref="formRef" :model="form">
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
          {},
        ]"
      >
        <a-input-number
          v-model="form.price"
          :formatter="inputNumberF"
          :parser="inputNumberP"
          :precision="2"
          :min="0"
          :max="9999"
        >
          <!-- <template #suffix>
            <span>¥</span>
          </template> -->
        </a-input-number>
      </a-form-item>

      <a-form-item
        field="total_amount"
        :label="$t('ticket.total_amount')"
        :rules="[
          {
            required: true,
            message: $t('ticket.total_amount.required'),
          },
        ]"
      >
        <a-input-number v-model="form.total_amount" :min="1" :max="100" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts" setup>
  import { ref } from 'vue';
  import { FormInstance } from '@arco-design/web-vue/es/form';
  import { Tickets } from '@/api/event';

  const formRef = ref<FormInstance>();

  const integralDigits = 6;
  const decimalPlaces = 2;
  const symbol = '¥';

  const visible = ref(false);
  const emits = defineEmits(['editConfirm']);

  const form = ref<Tickets>({
    description: '',
    price: 0,
    total_amount: 0,
  });

  const regHandel = (value: any) => {
    let reg = null;
    let gs = null;
    const dIndex = value.toString().indexOf('.');
    // 点开头处理为 0.
    if (dIndex === 0) {
      value = '0.';
    } else {
      // 连续点转为一个点
      const dIndex2 = value.toString().indexOf('..');
      if (dIndex2 !== -1) {
        value = value.replace(/\.\./, '.');
      }
    }
    value = value.replace(/[^0-9.]/g, '');
    const arr = value.split('.');
    if (arr.length === 2 && arr[1] !== '') {
      reg = new RegExp(
        `^(-)*(\\d{0,${integralDigits}})\\d*\\.(\\d{0,${decimalPlaces}}).*$`
      );
      gs = '$1$2.$3';
    } else {
      reg = new RegExp(`^(-)*(\\d{0,${integralDigits}}).*$`);
      if (dIndex !== -1) {
        gs = '$1$2.';
      } else {
        gs = '$1$2';
      }
    }
    return { reg, gs };
  };

  const inputNumberF = (value: any) => {
    const res = regHandel(value);
    const val = value.replace(res.reg, res.gs);
    // if (symbol !== '') {
    return `${symbol} ${val}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    // } else {
    // return val.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    // }
  };
  const inputNumberP = (value: any) => {
    const res = regHandel(value);
    let reg2 = null;
    // if (symbol != '') {
    reg2 = new RegExp(`${symbol}\\s?|(,*)`, 'g');
    // } else {
    // reg2 = new RegExp('(,*)', 'g');
    // }
    const val = value.replace(reg2, '');

    return val.replace(res.reg, res.gs);
  };

  const handleClick = () => {
    visible.value = true;
    form.value = {
      description: '',
      price: 0,
      total_amount: 1,
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
