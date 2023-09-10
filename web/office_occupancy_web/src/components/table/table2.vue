<template>
  <div class="tab_2">
    <el-table
      border
      :data="tableData"
      :height="tableHeight"
      :tree-props="{ children: 'children' }"
      :header-cell-style="tableHeaderCellStyle"
      :cell-style="tableCellStyle"
      :row-style="tableRowStyle"
      :row-key="rowKey"
      @selection-change="handleSelectionChange"
    >
      <slot></slot>
    </el-table>
  </div>
</template>

<script>
export default {
  props: {
    tableHeight: {
      type: Number,
      default: 0,
    },
    tableData: {
      type: Array,
      default: () => [],
    },
    tableCellPadding: {
      type: String,
      default: "6px 0",
    },
    rowKey: {
      type: String,
      default: "",
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
      selectData: [],
      selectRow: [],
    };
  },
  watch: {
    selectData(data) {
      this.selectRow = [];
      if (data.length > 0) {
        data.forEach((item, index) => {
          this.selectRow.push(this.tableData.indexOf(item));
        });
      }
    },
  },
  methods: {
    // 选中行高亮
    tableRowStyle({ row, rowIndex }) {
      if (this.selectRow.includes(rowIndex)) {
        return { "background-color": "#fff6e6" };
      }
    },
    // 获取选中的row
    handleSelectionChange(data) {
      this.selectData = data;
      this.$emit("select", data);
    },
  },
};
</script>

<style>
.tab_2 .el-table__body tr.hover-row > td {
  background-color: #fff6e6;
}
.tab_2 > .el-table--enable-row-hover .el-table__body tr:hover > td {
  background-color: #fff6e6;
}
.tab_2 > .el-table {
  border-color: #c8c8c8;
  box-sizing: border-box;
}
.tab_2 > .el-table::before {
  background-color: #c8c8c8;
}
.tab_2 > .el-table--border::after,
.tab_2 > .el-table--group::after {
  background-color: #c8c8c8;
}
.tab_2 .el-table__fixed-right::before,
.tab_2 .el-table__fixed::before {
  background-color: #c8c8c8;
}
</style>

