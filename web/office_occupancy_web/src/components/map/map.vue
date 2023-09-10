<template>
  <baidu-map
    id="baidu_map"
    ak="2IMiGQlksUfRyo9UiGGq6EY6EsZAtFAl"
    :center="center"
    :zoom="zoom"
    :scroll-wheel-zoom="true"
    @ready="handler"
    @click="mapClick"
  >
    <bm-navigation anchor="BMAP_ANCHOR_TOP_RIGHT"></bm-navigation>
    <bm-map-type :map-types="['BMAP_NORMAL_MAP', 'BMAP_HYBRID_MAP']" anchor="BMAP_ANCHOR_TOP_LEFT"></bm-map-type>
    <bm-marker
      :position="markerPosition"
      animation="BMAP_ANIMATION_BOUNCE"
      :icon="{url: yardImageUrl, size: {width: 50, height: 39}}"
    >
      <bm-label
        :content="labelContent"
        :labelStyle="{color: '#333', fontSize : '14px',border:'1px solid #ccc'}"
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
export default {
  components: {
    BaiduMap,
    bmNavigation,
    bmMapType,
    bmMarker,
    bmLabel
  },
  props: {
    labelContent: {
      type: String,
      default: ""
    },
    operateType: {
      type: String,
      default: ""
    },
    position: {
      type: Object,
      default: () => {
        return { lng: 0, lat: 0 };
      }
    }
  },
  data() {
    return {
      yardImageUrl: `${BASE_URL}${url_name_1}/images/yard.png`,
      loading: false,
      center: { lng: LNG, lat: LAT },
      zoom: 15,
      markerPosition: {
        lng: 0,
        lat: 0
      }
    };
  },
  methods: {
    handler({ BMap, map }) {
      if (this.operateType === "edit") {
        this.center.lng = this.position.lng;
        this.center.lat = this.position.lat;
        this.markerPosition.lng = this.position.lng;
        this.markerPosition.lat = this.position.lat;
      }
    },
    mapClick(target) {
      let lng = target.point.lng;
      let lat = target.point.lat;
      this.markerPosition.lng = lng;
      this.markerPosition.lat = lat;
      this.$emit("mapClick", lng, lat);
    }
  },
  mounted() {
    eventBus.$off("mapBindCoord");
    eventBus.$on("mapBindCoord", (lng, lat) => {
      this.center.lng = lng;
      this.center.lat = lat;
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