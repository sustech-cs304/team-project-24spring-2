<script>
import { ref, toRefs, onMounted } from 'vue';
import axios from 'axios';
import { Message } from '@arco-design/web-vue';
import { parse } from 'vue/compiler-sfc';
import utils from '@/api/utils';

export default {
    name: 'OrderCard',
    components: {
    },
    props: {
        order: {
            required: true,
        }
    },
    setup(props) {
        const { order } = toRefs(props);
        const statusName = ref('');
        const statusColor = ref('');
        const payLeftTime = ref(0);
        const timer = ref(null);
        onMounted(async () => {
            if (order.value.status === "UNPAID") {
                statusName.value = "未支付"
                statusColor.value = "red"
                let currentTime = new Date().getTime()
                let response = await axios.post(`/api/global/get-setting?key=order_expire_time`)
                let expire_time = parseInt(response.data)
                payLeftTime.value = order.value.order_create_time + expire_time - currentTime
                startTimer();
            } else if (order.value.status === "PAID") {
                statusName.value = "已支付"
                statusColor.value = "green"
            } else if (order.value.status === "CANCELED") {
                statusName.value = "已取消"
                statusColor.value = "gray"
            }
        });

        function startTimer() {
            timer.value = setInterval(() => {
                payLeftTime.value -= 1000;
                if (payLeftTime.value <= 0) {
                    order.value.status === "CANCELED"
                    statusName.value = "已取消"
                    statusColor.value = "gray"
                    clearInterval(timer.value);
                }
            }, 1000);
        }

        function formatPayLeftTime(time) {
            let minute = Math.floor(time / 60000)
            let second = Math.floor((time - minute * 60000) / 1000)
            return minute + "分" + second + "秒"
        }

        async function payOrder() {
            if (!utils.verifyLoginState()) {
                Message.error('请先登录');
                router.push('/login').then(() => {
                    window.location.reload();
                });
            }
            try {
                let payResponse = await axios.post(`/api/pay/pay-order?orderId=${order.value.id}&purchaseMethod=ALIPAY`, {},
                    {
                    headers: {
                        'Authorization': localStorage.getItem('token_type') + ' ' + localStorage.getItem('access_token')
                    }
                    }
                );
                if (payResponse.status !== 200) {
                    console.log(payResponse)
                    throw new Error('支付订单失败');
                }
                let payWindow = window.open("", "_blank");
                payWindow.document.write(payResponse.data);
            } catch(error) {
                Message.error('支付订单失败');
            }
        }

        async function cancelOrder() {
            if (!utils.verifyLoginState()) {
                Message.error('请先登录');
                router.push('/login').then(() => {
                    window.location.reload();
                });
            }
            try {
                let response = await axios.post(`/api/order/cancel-order?orderId=${order.value.id}`, {}, {
                    headers: {
                        'Authorization': localStorage.getItem('token_type') + ' ' + localStorage.getItem('access_token')
                    }
                });
                if (response.status !== 200) {
                    throw new Error('取消订单失败');
                }
                order.value.status = "CANCELED"
                statusName.value = "已取消"
                statusColor.value = "gray"
                clearInterval(timer.value);
                Message.success('取消订单成功');
            } catch (error) {
                Message.error('取消订单失败');
            }
        }

        return {
            order,
            statusName,
            statusColor,
            payLeftTime,
            formatPayLeftTime,
            cancelOrder,
            payOrder
        }
    },
};
</script>

<template>
    <a-card :style="{ width: '400px' }" :title="order.name" hoverable>
        <template #extra>
            <div v-if="order.status === 'UNPAID'" class="unpaid_buttons">
                <p>{{ formatPayLeftTime(payLeftTime) }}</p>
                <a-button @click="cancelOrder">取消订单</a-button>
                <a-button type="primary" status="success" @click="payOrder" >支付</a-button>
            </div>
        </template>
        <div class="card_layout">
            <p><strong>订单号:</strong> <a-tag>{{ order.id }}</a-tag></p>
            <p><strong>价格:</strong> <a-tag color="purple">{{ order.price }}</a-tag></p>
            <p><strong>状态:</strong> <a-tag :color="statusColor">{{ statusName }}</a-tag></p>
            <p><strong>订单创建时间:</strong> <a-tag>{{ $formatDateTime(order.order_create_time) }}</a-tag></p>
            <p v-if="order.status === 'PAID'">
                <strong>支付完成时间:</strong> <a-tag>{{ $formatDateTime(order.purchase_finish_time) }}</a-tag>
            </p>
            <p v-if="order.status === 'PAID'">
                <strong>支付方式:</strong> <a-tag color="blue">{{ order.purchase_method }}</a-tag>
            </p>
        </div>
    </a-card>
    <a-divider />
</template>


<style scoped>

.unpaid_buttons {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 10px;
}

.card_layout {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    align-items: flex-start;
    gap: 0.5vh;
    height: 100%;
    padding: 10px;
}

</style>
