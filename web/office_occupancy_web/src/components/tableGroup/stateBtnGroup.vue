<template>
    <div class="state-btn-group">
        <button @click="clickBtn">签订</button>
        <button v-if="hasClear">清退</button>
        <button v-else>已清退</button>
    </div>
</template>

<script>
export default {
  data() {
    return {
      hasClear: true
    };
  },
  methods:{
    clickBtn(){
      this.$emit('clickBtn');
    }
  }
};
</script>

<style scoped>
.state-btn-group > button {
  height: 23px;
  width: 50px;
  text-align: center;
  color: #fff;
  background-color: #3891e0;
}
.state-btn-group > button:first-of-type {
    margin-right: 15px;
}
.state-btn-group > button:hover {
  background-color: #035cac;
}
.state-btn-group > button:nth-of-type(3) {
  background-color: #ff9e00;
}
.state-btn-group > button:nth-of-type(3):hover {
  background-color: #ff5400;
}
</style>
