<script>
import { toRefs } from 'vue';

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
            <p><strong>状态:</strong> <a-tag v-if="ticket.checked_in" color="green">已使用</a-tag><a-tag v-else color="red">未使用</a-tag></p>
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
