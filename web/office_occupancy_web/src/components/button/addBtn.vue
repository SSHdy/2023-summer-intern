<template>
  <el-button
    class="add-btn"
    type="primary"
    size="medium"
    :style="style"
    v-debounce="clickBtn"
  >{{text}}</el-button>
</template>                                                                                                                                                                  
<script>
export default {
  props: {
    text: {
      type: String,
      default: "新增"
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
      default: 180
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
