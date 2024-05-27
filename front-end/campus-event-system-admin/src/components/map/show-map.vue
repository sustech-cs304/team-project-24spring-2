<template>
  <div id="MyMap" style="height: 400px; width: 100%; margin-top: 10px"></div>
</template>

<script setup lang="ts">
  import { ref, onMounted, onUnmounted } from 'vue';
  import { getSetting } from '@/api/global';

  import AMapLoader from '@amap/amap-jsapi-loader';

  const props = defineProps({
    lng: {
      type: Number,
      default: 113.99986,
      readonly: false,
    },
    lat: {
      type: Number,
      default: 22.598965,
    },
  });
  const emits = defineEmits(['confirm']);

  let map: any = null;
  const loading: any = ref(false);
  const checkedForm: any = ref({});

  let aMap: any = null;
  let toolbar: any = null;
  let scaler: any = null;

  let marker: any = null;
  const removeMarker = () => {
    map.remove(marker);
  };

  const addmark = (lng: any, lat: any, AMap: any) => {
    if (marker) removeMarker();
    marker = new AMap.Marker({
      position: new AMap.LngLat(lng, lat),
      title: '活动地点',
      zoom: 13,
    });
    checkedForm.value = {
      lng,
      lat,
    };

    const lnglat = [lng, lat];
    map.add(marker);
    map.setCenter(lnglat, '', 500);
  };
  const currentSelect = (val: any) => {
    checkedForm.value = {
      lat: val.location?.lat,
      lng: val.location?.lng,
    };
    addmark(val.location?.lng, val.location?.lat, aMap);
    map.setCenter([val.location?.lng, val.location?.lat], '', 500);
  };

  const initMap = async () => {
    const API_CODE = await getSetting('amap_api_code');
    const API_KEY = await getSetting('amap_api_key');
   
    (window as any)._AMapSecurityConfig = {
      securityJsCode: API_CODE.data,
    };
    AMapLoader.load({
      key: API_KEY.data, //  申请好的Web端开发者Key，首次调用 load 时必填
      version: '2.0', //  指定要加载的 JSAPI 的版本，缺省时默认为 1.4.15
      plugins: ['AMap.ToolBar', 'AMap.Scale'], //  需要使用的的插件列表，如比例尺'AMap.Scale'等
    })
      .then((AMap: any) => {
        aMap = AMap;
        map = new AMap.Map('MyMap', {
          //  设置地图容器id
          zoom: 16, //  初始化地图级别
          center: [113.99986, 22.598965], //  初始化地图中心点位置
        });

        toolbar = new AMap.ToolBar();
        scaler = new AMap.Scale();
        map.addControl(scaler);
        map.addControl(toolbar);

        currentSelect({
          location: {
            lat: props.lat,
            lng: props.lng,
          },
        });
      })
      .catch((e) => {
        console.log(e);
      });
  };

  const open = () => {};

  defineExpose({
    open,
  });
  onMounted(() => {
    initMap();
  });
  onUnmounted(() => {
    map?.destroy();
  });
</script>
