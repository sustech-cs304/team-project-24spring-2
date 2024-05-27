<template>
    <canvas :id="text"></canvas>
</template>

<script>
import QRCode from 'qrcode'
import { toRefs, onMounted, ref } from 'vue'

export default {

    props: {
        text: {
            type: String,
            required: true
        }
    },
    setup(props) {
        const { text } = toRefs(props);

        function generateQRCode(canvas, context) {
            let cav = document.getElementById(canvas);
            QRCode.toCanvas(cav, context, { errorCorrectionLevel: 'H' }, function (error) {
                if (error) console.error(error)
            })
        }

        onMounted(() => {
            generateQRCode(text.value, text.value);
        })

        return {
            text,
            generateQRCode
        }
    }
}
</script>

<style>
</style>