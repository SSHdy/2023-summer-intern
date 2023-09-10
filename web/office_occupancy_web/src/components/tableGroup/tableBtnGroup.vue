<template>
  <div class="group">
    <button class="group-del" @click="batchDel()" v-show="batchDelBtnShow">
      <img src="../../assets/imgs/batchDelete.png" />
      {{delText}}
    </button>
    <button class="group-add" @click="add()" v-show="addBtnShow">
      <img src="../../assets/imgs/img-add.png" />
      {{addText}}
    </button>
    <button class="group-deal" @click="deal()" v-show="dealBtnShow">
      <img src="../../assets/imgs/deal.png" />
      {{dealText}}
    </button>
    <button class="group-read" @click="haveRead()" v-show="haveReadBtnShow">
      <i class="iconfont">&#xe61e;</i>
      {{haveReadText}}
    </button>
  </div>
</template>

<script>
export default {
  props: {
    delText: {
      type: String,
      default: "批量删除"
    },
    addText: {
      type: String,
      default: "添加"
    },
    dealText: {
      type: String,
      default: "处理"
    },
    haveReadText: {
      type: String,
      default: "关闭"
    },
    batchDelBtnShow: {
      type: Boolean,
      default: true
    },
    addBtnShow: {
      type: Boolean,
      default: true
    },
    dealBtnShow: {
      type: Boolean,
      default: false
    },
    haveReadBtnShow: {
      type: Boolean,
      default: false
    }
  },
  methods: {
    add() {
      this.$emit("addClick");
    },
    batchDel() {
      this.$emit("batchDeleteClick");
    },
    deal() {
      this.$emit("dealClick");
    },
    haveRead() {
      this.$emit("haveReadClick");
    }
  }
};
</script>

<style scoped>
.group > button {
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
.group > button:hover {
  background-color: #2378c4;
}
.group > button > img {
  width: 12px;
  height: 12px;
  position: absolute;
  top: 9px;
  left: 10px;
}
.group-read > i {
  position: absolute;
  left: 10px;
}
</style>
