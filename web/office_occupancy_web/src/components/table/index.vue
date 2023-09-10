<template>
  <div class="table-container">
    <el-table
      v-if="tableHeight"
      v-loading="loading"
      :height="tableHeight"
      :data="tableData"
      border
      :header-cell-style="tableHeaderCellStyle"
      :cell-style="tableCellStyle"
      @selection-change="handleSelectionChange"
    >
      <slot></slot>
    </el-table>
  </div>
</template>

<script>
export default {
  props: {
    tableData: {
      type: Array,
      default: () => [],
    },
    loading: {
      type: Boolean,
      default: false,
    },
    tableCellPadding: {
      type: String,
      default: "6px 0",
    },
    tableHeight: {
      type: Number,
      default: 0,
    },
  },
  data() {
    return {
      tableHeaderCellStyle: {
        "background-color": "#0296FF",
        "border-color": "#E9E9F2",
        padding: "6px 0",
        color: "#fff",
      },
      tableCellStyle: {
        "border-color": "#E9E9F2",
        padding: this.tableCellPadding,
      },
    };
  },
  methods: {
    handleSelectionChange(selection) {
      this.$emit("handleSelectionChange", selection);
    },
  },
};
</script>

<style>
.table-container .el-table__body tr.hover-row > td {
  background-color: #e0f0ff;
}
.table-container > .el-table--enable-row-hover .el-table__body tr:hover > td {
  background-color: #e0f0ff;
}
.table-container > .el-table {
  border-color: #e9e9f2;
  box-sizing: border-box;
}
.table-container > .el-table::before {
  background-color: #e9e9f2;
}
.table-container > .el-table--border::after,
.table-container > .el-table--group::after {
  background-color: #e9e9f2;
}
.table-container .el-table__fixed-right::before,
.table-container .el-table__fixed::before {
  background-color: #e9e9f2;
}
</style>
