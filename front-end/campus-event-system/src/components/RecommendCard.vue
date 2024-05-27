<script>
import { toRefs } from 'vue';

export default {
    name: 'RecommendCard',
    components: {
    },
    props: {
        event: {
            required: true,
        }
    },
    setup(props) {
        const { event } = toRefs(props);

        return {
            event,
        }
    },
};
</script>

<template>
    <a-card :style="{ width: '350px' }" :title="event.title" hoverable>
        <template #extra>
            <a-link :href="`/eventInfo?id=${event.id}`">查看详情</a-link>
        </template>
        <div class="card_layout">
            <p><strong>时间:</strong> <a-tag>{{ $formatDateTime(event.startTime) }} - {{ $formatDateTime(event.endTime) }}</a-tag></p>
            <p><strong>地点:</strong> <a-tag>{{ event.location_name }}</a-tag></p>
        </div>
    </a-card>
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
