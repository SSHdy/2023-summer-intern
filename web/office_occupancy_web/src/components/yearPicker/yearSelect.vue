<template>
  <el-select
    v-model="value_"
    filterable
    placeholder="请选择"
    size="small"
    @change="change"
    style="width:200px;"
  >
    <el-option v-for="item in options" :key="item" :label="item" :value="item"></el-option>
  </el-select>
</template>

<script>
export default {
  props: {
    name: {
      type: String,
      default: ""
    },
    value: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      options: [],
      value_: null
    };
  },
  watch: {
    value(curVal, oldVal) {
      if (this.value) {
        this.value_ = this.value;
      } else {
        this.value_ = '';
      }
    }
  },
  methods: {
    change(val) {
      this.$emit("selectYear", this.name, val);
    }
  },
  mounted() {
    const currentDate = new Date().getFullYear();
    const years = currentDate;
    for (var i = years - 10; i <= years + 10; i++) {
      this.options.push(i + '');
    }
  }
};
</script>