<template>
  <a-button type="primary" @click="open">{{ $t('选择地点') }}</a-button>

  <a-modal
    v-model:visible="visible"
    class="dialog companygoodsLog"
    title="位置选择"
    style="border-radius: 4px"
    top="100px"
    width="50%"
  >
    <div style="height: 40px; width: 100%; display: flex; align-items: center">
      <a-select
        v-model="areaValue"
        allow-search
        filter-option
        style="width: 350px"
        remote
        reserve-keyword
        placeholder="请输入关键词"
        :loading="loading"
        @search="remoteMethod"
        @change="currentSelect"
      >
        <a-option
          v-for="item in areaList"
          :key="item.id"
          :label="item.name"
          :value="item"
          class="one-text"
        >
          <span style="float: left">{{ item.name }}</span>
          <span style="float: right; color: #8492a6; font-size: 13px">{{
            item.district
          }}</span>
        </a-option>
      </a-select>
    </div>
    <div
      id="selectPointMap"
      style="height: 500px; width: 100%; margin-top: 10px"
    ></div>
    <template #footer>
      <span class="dialog-footer">
        <a-space>
          <a-button @click="visible = false">{{ '关闭' }}</a-button>

          <a-button type="primary" @click="confirmData">
            {{ '确定' }}
          </a-button>
        </a-space>
      </span>
    </template>
  </a-modal>
</template>

<script setup lang="ts">
  import { ref, onUnmounted } from 'vue';
  //    import { ElMessage } from 'element-plus';

  import AMapLoader from '@amap/amap-jsapi-loader';
  import { Notification } from '@arco-design/web-vue';

  (window as any)._AMapSecurityConfig = {
    //  安全密钥
    // securityJsCode: "123",
    securityJsCode: import.meta.env.VITE_AMAP_API_CODE as string,
  };

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
    address: {
      type: String,
      default: '',
    },
  });
  const emits = defineEmits(['confirm']);
  const visible: any = ref(false);
  const areaList: any = ref([]);
  const areaValue = ref('');

  let map: any = null;
  const loading: any = ref(false);
  const checkedForm: any = ref({});
  let AutoComplete: any = null;
  let aMap: any = null;
  let toolbar: any = null;
  let scaler: any = null;
  let geoCoder: any = null;

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
    geoCoder.getAddress(lnglat, (status: any, result: any) => {
      if (status === 'complete' && result.info === 'OK') {
        const { province, city, district } = result.regeocode.addressComponent;
        const { formattedAddress: formated } = result.regeocode;
        const clean = formated
          .replace(province, '')
          .replace(city, '')
          .replace(district, '');
        areaValue.value = clean;
        checkedForm.value = {
          ...checkedForm.value,
          address: clean,
        };
      }
    });
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

  const initMap = () => {
    AMapLoader.load({
      key: import.meta.env.VITE_AMAP_API_KEY as string, //  申请好的Web端开发者Key，首次调用 load 时必填
      version: '2.0', //  指定要加载的 JSAPI 的版本，缺省时默认为 1.4.15
      plugins: [
        'AMap.Geocoder',
        'AMap.AutoComplete',
        'AMap.ToolBar',
        'AMap.Scale',
      ], //  需要使用的的插件列表，如比例尺'AMap.Scale'等
    }).then((AMap: any) => {
      aMap = AMap;
      map = new AMap.Map('selectPointMap', {
        //  设置地图容器id
        zoom: 16, //  初始化地图级别
        center: [113.99986, 22.598965], //  初始化地图中心点位置
      });
      AutoComplete = new AMap.AutoComplete({
        city: '深圳',
      });
      geoCoder = new AMap.Geocoder({
        city: '深圳', // 城市设为北京，默认：“全国”
        radius: 1000, // 范围，默认：500
      });
      toolbar = new AMap.ToolBar();
      scaler = new AMap.Scale();
      map.addControl(scaler);
      map.addControl(toolbar);
      map.on('click', (e: any) => {
        addmark(e.lnglat.getLng(), e.lnglat.getLat(), AMap);
      });

      if (props.address) {
        currentSelect({
          location: {
            lat: props.lat,
            lng: props.lng,
          },
        });
      }
    });
    //   .catch((e) => {
    //     // handle error
    // });
  };

  const remoteMethod = (searchValue: any) => {
    if (searchValue !== '') {
      setTimeout(() => {
        AutoComplete.search(searchValue, (status: any, result: any) => {
          if (result.tips?.length) {
            areaList.value = result?.tips;
          }
        });
      }, 200);
    }
  };

  const confirmData = () => {
    if (!checkedForm.value?.lat || !checkedForm.value?.lng) {
      return Notification.info({
        content: '选择地址!',
        showIcon: false,
      });
    }
    emits('confirm', checkedForm.value);
    visible.value = false;
    areaValue.value = '';

    return map?.destroy();
  };
  const open = () => {
    visible.value = true;
    initMap();
  };

  defineExpose({
    open,
  });
  //   onMounted(() => {});
  onUnmounted(() => {
    map?.destroy();
  });
</script>
