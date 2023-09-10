<template>
  <el-button
    class="submit-btn"
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
      default: "提交"
    },
    fontSize: {
      type: Number,
      default: 16
    },
    marginLeft: {
      type: Number,
      default: 180
    }
  },
  data() {
    return {
      style: {
        padding: "7px 22px",
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

