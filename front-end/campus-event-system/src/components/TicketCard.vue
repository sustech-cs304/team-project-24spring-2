<script>
import { ref, toRefs, onMounted } from 'vue';
import axios from 'axios';
import { Message } from '@arco-design/web-vue';
import { parse } from 'vue/compiler-sfc';
import utils from '@/api/utils';

export default {
    name: 'TicketCard',
    components: {
    },
    props: {
        ticket: {
            required: true,
        }
    },
    setup(props) {
        const { ticket } = toRefs(props);
        const statusName = ref('');
        const statusColor = ref('');
        const payLeftTime = ref(0);
    
        onMounted(async () => {
            // 判断是否开始和结束
            // if (ticket.value.startTime > new Date()) {
            //     statusName.value = "未开始"
            //     statusColor.value = "blue"
            // } else if (ticket.value.endTime < new Date()) {
            //     statusName.value = "已结束"
            //     statusColor.value = "gray"
            // } else {
            //     statusName.value = "进行中"
            //     statusColor.value = "green"
            // }
        });

        return {
            ticket,
        }
    },
};
</script>

<template>
    <a-card :style="{ width: '500px' }" :title="ticket.eventInfo.title" hoverable>
        <div class="card_layout">
            <p><strong>票档:</strong> <a-tag color="gold">{{ ticket.ticketInfo.description }}</a-tag></p>
            <p><strong>编号:</strong> <a-tag color="arcoblue">NO. {{ ticket.number }}</a-tag></p>
            <p><strong>时间:</strong> <a-tag>{{ $formatDateTime(ticket.eventInfo.startTime) }} - {{ $formatDateTime(ticket.eventInfo.endTime) }}</a-tag></p>
            <p><strong>地点:</strong> <a-tag>{{ ticket.eventInfo.location_name }}</a-tag></p>
        </div>
    </a-card>
    <a-divider/>
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
