<script>
import { ref, toRefs, onMounted } from 'vue';
import axios from 'axios';
import { Message } from '@arco-design/web-vue';

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
        
        return {

        }
    },
};
</script>

<template>
    <a-card :style="{ width: '360px' }" :title="order.name" hoverable>
        <template #extra>
            <a-link>More</a-link>
        </template>
        Card content <br />
        Card content
    </a-card>
    <a-divider />
</template>


<style scoped></style>
