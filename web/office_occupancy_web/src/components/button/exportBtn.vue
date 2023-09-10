<template>
  <button class="export-btn" v-debounce="clickBtn" :style="style">
    <img src="../../assets/imgs/export-icon.png" v-if="showIcon" />
    {{text}}
  </button>
</template>                                                                                                                                                                  
<script>
export default {
  props: {
    text: {
      type: String,
      default: "导出"
    },
    fontSize: {
      type: Number,
      default: 16
    },
    padding: {
      type: String,
      default: "7px 22px"
    },
    marginLeft: {
      type: Number,
      default: 0
    },
    showIcon: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      style: {
        padding: this.padding,
        "font-size": `${this.fontSize}px`,
        "margin-left": `${this.marginLeft}px`
      }
    };
  },
  directives: {
    debounce: {
      inserted: (el, binding) => {
        let timer;
        el.addEventListener('click', () => {
          if (timer) {
            clearTimeout(timer)
          }
          timer = setTimeout(() => {
            binding.value()
          }, 300)
        })
      }
    }
  },
  methods: {
    clickBtn() {
      this.$emit("clickBtn");
    }
  }
};
</script>

<style scoped>
.export-btn {
  background-color: #3891e0;
  cursor: pointer;
  border-radius: 5px;
  margin-right: 20px;
  color: white;
  position: relative;
}
.export-btn:hover {
  background-color: #2378c4;
}
.export-btn > img {
  width: 12px;
  height: 12px;
}
</style>

