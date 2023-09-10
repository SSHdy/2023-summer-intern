<template>
  <baidu-map
    id="baidu_map"
    ak="2IMiGQlksUfRyo9UiGGq6EY6EsZAtFAl"
    :center="center"
    :zoom="zoom"
    :scroll-wheel-zoom="true"
    @ready="handler"
    @click="mapClick"
    ref="baiduMap"
  >
    <bm-navigation anchor="BMAP_ANCHOR_TOP_RIGHT"></bm-navigation>
    <bm-map-type :map-types="['BMAP_NORMAL_MAP', 'BMAP_HYBRID_MAP']" anchor="BMAP_ANCHOR_TOP_LEFT"></bm-map-type>

    <bm-marker
      :position="markerPosition"
      :icon="{url: buildImageUrl, size: {width: 39, height: 39}}"
    >
      <bm-label
        :content="'所选位置'"
        :labelStyle="{color: 'red', fontSize : '14px'}"
        :offset="{width: -25, height: 30}"
      />
    </bm-marker>
    <bm-marker :position="yardPosition" :icon="{url: yardImageUrl, size: {width: 39, height: 39}}">
      <bm-label
        :content="currentYardName"
        :labelStyle="{color: 'red', fontSize : '14px'}"
        :offset="{width: -10, height: 30}"
      />
    </bm-marker>
    <bm-marker :position="buildPosition" :icon="{url: buildImageUrl, size: {width: 9, height: 25}}">
      <bm-label
        :content="currentBuildName"
        :labelStyle="{color: 'red', fontSize : '14px'}"
        :offset="{width: -10, height: 30}"
      />
    </bm-marker>
  </baidu-map>
</template>

<script>
import { BASE_URL, url_name_1, LNG, LAT } from "@/axios/global"
import { eventBus } from "@/eventBus/eventBus";
import BaiduMap from "vue-baidu-map/components/map/Map.vue";
import bmNavigation from "vue-baidu-map/components/controls/Navigation.vue";
import bmMapType from "vue-baidu-map/components/controls/MapType.vue";
import bmMarker from "vue-baidu-map/components/overlays/Marker.vue";
import bmLabel from "vue-baidu-map/components/overlays/Label.vue";
import { debug } from 'util';
export default {
  components: {
    BaiduMap,
    bmNavigation,
    bmMapType,
    bmMarker,
    bmLabel
  },
  data() {
    return {
      center: { lng: LNG, lat: LAT },
      zoom: 15,
      markerPosition: {
        lng: 0,
        lat: 0
      },
      yardPosition: {
        lng: 0,
        lat: 0
      },
      buildPosition: {
        lng: 0,
        lat: 0
      },
      currentYardName: '',
      currentBuildName: '',
      yardImageUrl: `${BASE_URL}${url_name_1}/images/yard.png`,
      buildImageUrl: `${BASE_URL}${url_name_1}/images/build.png`
    };
  },
  methods: {
    handler({ BMap, map }) {
      this.zoom = this.zoom;
    },
    mapClick(target) {
      let lng = target.point.lng;
      let lat = target.point.lat;
      this.markerPosition.lng = lng;
      this.markerPosition.lat = lat;
      this.$emit("mapClick", lng, lat);
    },
    setYardPosition(lng, lat, name) {
      this.yardPosition.lng = lng;
      this.yardPosition.lat = lat;
      this.center.lng = lng;
      this.center.lat = lat;
      this.currentYardName = name;
    },
    setBuildPosition(lng, lat, name) {
      this.buildPosition.lng = lng;
      this.buildPosition.lat = lat;
      this.currentBuildName = name;
    }
  },
  mounted() {
    eventBus.$off("mapBindCoord");
    eventBus.$on("mapBindCoord", (lng, lat) => {
      this.markerPosition.lng = lng;
      this.markerPosition.lat = lat;
    });
  }
};
</script>

<style>
#baidu_map {
  width: 100%;
  height: 100%;
}
</style>