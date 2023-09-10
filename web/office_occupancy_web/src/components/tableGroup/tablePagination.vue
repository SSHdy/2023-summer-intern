<template>
  <div class="pagination">
    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage" :page-sizes="[5, 15, 25, 50]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
    </el-pagination>
  </div>
</template>
<script>
export default {
  props: {
    currentPage: {
      type: Number,
      default: 1
    },
    pageSize: {
      type: Number,
      default: 15
    },
    total: {
      type: Number,
      default: 0
    }
  },
  data() {
    return {};
  },
  methods: {
    // 改变页面的size
    handleSizeChange(size) {
      this.$emit("size-change", size);
    },
    // 改变当前页面
    handleCurrentChange(page) {
      this.$emit("page-change", page);
    }
  }
};
</script>

<style>
.pagination .el-pagination .el-select .el-input .el-input__inner,
.pagination .el-pagination__editor.el-input .el-input__inner {
  background: #3891e0;
  color: #fff;
}
.pagination .el-pager li {
  background: #3891e0;
  border-right: 1px solid #fff;
  color: #fff;
}
.pagination .el-pager li.active {
  color: #fff;
  background: #7f8281;
}
.pagination .el-pagination .btn-next,
.pagination .el-pagination .btn-prev {
  background: #3891e0;
  color: #fff;
  border-right: 1px solid #fff;
}
.pagination .el-pagination button:disabled {
  background-color: #3891e0;
  cursor: not-allowed;
  color: #fff;
}
.pagination .el-pagination__total,
.pagination .el-pagination__jump {
  color: #333;
}
.pagination .el-select .el-input .el-select__caret {
  color: #fff;
}
.pagination .el-pagination button:hover {
  background-color: #2378c4;
  color: #fff;
}
.pagination .el-pager li:hover {
  background-color: #2378c4;
}
</style>

