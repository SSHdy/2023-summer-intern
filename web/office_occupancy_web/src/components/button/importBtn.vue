<template>
  <button class="import-btn" v-debounce="clickBtn">
    <img src="../../assets/imgs/import-icon.png" />
    {{text}}
  </button>
</template>                
<script>
export default {
  props: {
    text: {
      type: String,
      default: "导入"
    }
  },
  data() {
    return {};
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
.import-btn {
  height: 30px;
  padding: 0 10px;
  line-height: 29px;
  background-color: #3891e0;
  cursor: pointer;
  border-radius: 5px;
  margin-right: 20px;
  color: white;
  position: relative;
  padding-left: 27px;
}
.import-btn:hover {
  background-color: #2378c4;
}
.import-btn > img {
  width: 12px;
  height: 12px;
  position: absolute;
  top: 9px;
  left: 10px;
}
</style>

