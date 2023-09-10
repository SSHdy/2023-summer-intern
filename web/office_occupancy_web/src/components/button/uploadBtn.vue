<template>
  <el-button class="upload-btn" type="primary" size="medium" :style="style" v-debounce="clickBtn">
    <i class="el-icon-upload2" v-if="showIcon"></i>
    {{text}}
  </el-button>
</template>
                                                                                                                                                         
<script>
export default {
  props: {
    text: {
      type: String,
      default: "上传"
    },
    fontSize: {
      type: Number,
      default: 16
    },
    marginLeft: {
      type: Number,
      default: 180
    },
    padding: {
      type: String,
      default: "7px 22px"
    },
    showIcon: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      style: {
        "padding": this.padding,
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

