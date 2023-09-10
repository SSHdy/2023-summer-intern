<template>
  <div class="singleBtn clearfix">
    <div
      class="add"
      v-if="(isShowAdd && unitClassify !== 2 && type==='unit') || (isShowAdd && !menuPid && type==='menu')"
      @click="add"
    >
      <a class="el-icon-circle-plus-outline">添加</a>
    </div>
    <div class="update" v-if="isShowUpdate" @click="update">
      <a class="el-icon-edit">修改</a>
    </div>
    <div class="detail" v-if="isShowDetail" @click="detail">
      <a class="el-icon-view">详情</a>
    </div>
    <div class="delete" v-if="isShowDelete" @click="del">
      <a class="el-icon-delete">删除</a>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    type: {
      type: String,
      default: ''
    },
    menuPid: {
      type: Number,
      default: 0
    },
    unitClassify: {
      type: Number,
      default: 0
    },
    isShowAdd: {
      type: Boolean,
      default: true
    },
    isShowUpdate: {
      type: Boolean,
      default: true
    },
    isShowDelete: {
      type: Boolean,
      default: true
    },
    isShowDetail: {
      type: Boolean,
      default: true
    }
  },
  methods: {
    detail() {
      this.$emit("detailClick");
    },
    update() {
      this.$emit("updateClick");
    },
    del() {
      this.$emit("deleteClick");
    },
    add() {
      this.$emit("addClick");
    }
  },
  mounted() {
  },
};
</script>


<style scoped>
.singleBtn > div {
  float: left;
  cursor: pointer;
  margin-left: 10px;
}
.singleBtn > .add {
  color: #f79657;
}
.singleBtn > .add:hover {
  color: #e67e3a;
}
.singleBtn > .update {
  color: #3891e0;
}
.singleBtn > .update:hover {
  color: #035cac;
}
.singleBtn > .delete {
  color: #f65c5f;
}
.singleBtn > .delete:hover {
  color: #d82a2d;
}
.singleBtn > .detail {
  color: #ff9e00;
}
.singleBtn > .detail:hover {
  color: #e2920f;
}
</style>