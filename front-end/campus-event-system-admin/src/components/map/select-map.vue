<template>
  <a-button type="primary" @click="open">{{ $t('选择地点') }}</a-button>

  <a-modal
    :visible="visible"
    class="dialog companygoodsLog"
    title="位置选择"
    style="border-radius: 4px"
    top="100px"
    width="50%"
    @cancel="cancel"
    @ok="confirmData"
  >
    <div class="sel">
      {{ selectedPosition }}
    </div>
    <div style="height: 40px; width: 100%; display: flex; align-items: center">
      <a-select
        v-model="selectValue"
        allow-search
        filter-option
        style="width: 350px"
        remote
        placeholder="请输入关键词"
        :loading="loading"
        @search="remoteMethod"
        :merge-props="false"
        :style="{ width: '100%' }"
      >
        <a-option
          v-for="(item, index) in areaList"
          :key="item.id"
          :label="item.name"
          :value="index"
          :index="index"
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
  </a-modal>
</template>

<script setup lang="ts">
  import { ref, onUnmounted } from 'vue';
  //    import { ElMessage } from 'element-plus';
  import { getSetting } from '@/api/global';
  import { EventLocation } from '@/api/event';

  import AMapLoader from '@amap/amap-jsapi-loader';
  import { Notification } from '@arco-design/web-vue';
  import { cloneDeep } from 'lodash';
  import { watch } from 'vue';

  const selectValue = ref();
  const selectedPosition = ref('');

  const model = defineModel<EventLocation>({
    default: {
      lng: {
        type: Number,
        default: NaN,
      },
      lat: {
        type: Number,
        default: NaN,
      },
      address: {
        type: String,
        default: '',
      },
    },
  });

  const originData = ref<EventLocation>({
    lng: NaN,
    lat: NaN,
    address: '',
  });

  const emits = defineEmits(['confirm']);
  const visible: any = ref(false);
  const areaList: any = ref([]);
  const areaValue = ref<any>('');

  let map: any = null;
  const loading: any = ref(false);
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
    model.value.lng = lng;
    model.value.lat = lat;

    const lnglat = [lng, lat];
    geoCoder.getAddress(lnglat, (status: any, result: any) => {
      if (status === 'complete' && result.info === 'OK') {
        const { province, city, district } = result.regeocode.addressComponent;
        const { formattedAddress: formated } = result.regeocode;
        const cleaned = formated
          .replace(province, '')
          .replace(city, '')
          .replace(district, '');
        model.value.address = cleaned;
        selectedPosition.value = cleaned;
      }
    });
    map.add(marker);
    map.setCenter(lnglat, '', 500);
  };

  const addmarkWithAddress = (lng: any, lat: any, AMap: any) => {
    if (marker) removeMarker();
    marker = new AMap.Marker({
      position: new AMap.LngLat(lng, lat),
      title: '活动地点',
      zoom: 13,
    });
    model.value.lng = lng;
    model.value.lat = lat;
    const lnglat = [lng, lat];
    selectedPosition.value = model.value.address;
    map.add(marker);
    map.setCenter(lnglat, '', 500);
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

      if (model.value.lat && model.value.lng) {
        map.setCenter([model.value.lng, model.value.lat], '', 500);
        addmarkWithAddress(model.value.lng, model.value.lat, AMap);
      }
    });
  };

  const remoteMethod = (searchValue: any) => {
    if (searchValue !== '') {
      setTimeout(() => {
        AutoComplete.search(searchValue, (status: any, result: any) => {
          if (result.tips?.length) {
            areaList.value = [];
            result.tips.forEach((element: any) => {
              areaList.value.push(element);
            });
          }
        });
      }, 200);
    }
  };

  const cancel = () => {
    visible.value = false;
    model.value = cloneDeep(originData.value);
  };

  const confirmData = () => {
    if (!model.value.lat || !model.value.lng) {
      return Notification.info({
        content: '选择地址!',
        showIcon: false,
      });
    }
    emits('confirm');
    visible.value = false;
    areaValue.value = '';
    return map?.destroy();
  };

  const open = () => {
    originData.value = cloneDeep(model.value);
    visible.value = true;
    initMap();
  };
  watch(selectValue, (val) => {
    if (val) {
      const { lng, lat } = areaList.value[val].location;
      addmark(lng, lat, aMap);
    }
  });
  defineExpose({
    open,
  });
  //   onMounted(() => {});
  onUnmounted(() => {
    map?.destroy();
  });
</script>

<style lang="less" scoped>
  .sel {
    width: 100%;
    display: flex;
    justify-content: center;
    font-size: 20px;
    color: #8492a6;
  }
</style>
