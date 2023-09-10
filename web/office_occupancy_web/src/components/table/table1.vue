<template>
  <div class="tab_1">
    <el-table
      :data="tableData"
      border
      :header-cell-style="tableHeaderCellStyle"
      :cell-style="tableCellStyle"
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
      default: () => []
    },
    tableCellPadding: {
      type: String,
      default: "6px 0"
    }
  },
  data() {
    return {
      tableHeaderCellStyle: {
        "background-color": "#bee2f3",
        "border-color": "#c8c8c8",
        padding: "6px 0",
        color: "#333333"
      },
      tableCellStyle: {
        "border-color": "#c8c8c8",
        padding: this.tableCellPadding
      }
    };
  }
};
</script>

<style>
.tab_1 .el-table__body tr.hover-row > td {
  background-color: #fff6e6;
}
.tab_1 > .el-table--enable-row-hover .el-table__body tr:hover > td {
  background-color: #fff6e6;
}
.tab_1 > .el-table {
  border-color: #c8c8c8;
  box-sizing: border-box;
}
.tab_1 > .el-table::before {
  background-color: #c8c8c8;
}
.tab_1 > .el-table--border::after,
.tab_1 > .el-table--group::after {
  background-color: #c8c8c8;
}
.tab_1 .el-table__fixed-right::before,
.tab_1 .el-table__fixed::before {
  background-color: #c8c8c8;
}
</style>

