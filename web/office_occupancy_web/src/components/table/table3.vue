<template lang="html">
  <div id="tree_table">
    <zk-table
      ref="table"
      sum-text="sum"
      index-text="#"
      :data="tableData"
      :columns="columns"
      :stripe="props.stripe"
      :border="props.border"
      :show-header="props.showHeader"
      :show-summary="props.showSummary"
      :show-row-hover="props.showRowHover"
      :show-index="props.showIndex"
      :tree-type="props.treeType"
      :is-fold="props.isFold"
      :expand-type="props.expandType"
      :max-height="maxHeight"
      :selection-type="props.selectionType"
      @cell-click="cellClickHandle"
      @tree-icon-click="treeIconHandle"
      >
      <template slot="operate" slot-scope="scope">
        <single-btn-group 
        :pid="scope.row[pid_name]" 
        :selfId="scope.row[id_name]"
        :isShowDetail="isShowDetail"
        :unitClassify="scope.row.unitClassify ? scope.row.unitClassify : 0"
        :type="type"
        ></single-btn-group>
      </template>
    </zk-table>
  </div>
</template>

<script>
import singleBtnGroup from "@/components/tableGroup/singleBtnGroup_2";
import { eventBus } from "@/eventBus/eventBus";
export default {
  components: {
    singleBtnGroup
  },
  props: {
    tableData: {
      type: Array,
      default: () => {
        return []
      }
    },
    maxHeight: {
      type: Number,
      default: 0
    },
    columns: {
      type: Array,
      default: () => {
        return []
      }
    },
    isShowDetail: {
      type: Boolean,
      default: true
    },
    type: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      props: {
        stripe: false,
        border: true,
        showHeader: true,
        showSummary: false,
        showRowHover: true,
        showIndex: false,
        treeType: true,
        isFold: false,
        expandType: false,
        selectionType: false,
      },
      id_name: `${this.type}Id`,
      pid_name: `${this.type}Pid`
    };
  },
  methods: {
    cellClickHandle($event, data) {
      const target = $event.target;
      const id = $(target).parent().parent().attr('selfId');
      const type = target.parentNode.className;
      if (type === 'update') {
        this.$emit('edit', id);
      } else if (type === 'add') {
        this.$emit('add', id);
      } else if (type === 'delete') {
        this.$emit('delete', id);
      } else if (type === 'detail') {
        this.$emit('detail', id);
      }
    },
    treeIconHandle($event, data) {
      setTimeout(() => {
        const contentWidth = $(".zk-table__body").width();
        $(".zk-table__header").width(contentWidth);
      }, 10);
    }
  },
  mounted() {
    eventBus.$off('resetTable');
    eventBus.$on('resetTable', (h) => {
      $('.zk-table__body-wrapper').height(h);
      setTimeout(() => {
        const contentWidth = $(".zk-table__body").width();
        $(".zk-table__header").width(contentWidth);
      }, 10);
    });
    window.onresize = () => {
      const contentWidth = $(".zk-table__body").width();
      $(".zk-table__header").width(contentWidth);
    };
  }
};
</script>

<style>
#tree_table .zk-table__header-row {
  background: rgb(190, 226, 243) !important;
  border-color: rgb(200, 200, 200) !important;
}
#tree_table .zk-table__header-row > th {
  border-color: rgb(200, 200, 200) !important;
}
#tree_table .zk-table--border-cell {
  border-color: rgb(200, 200, 200) !important;
  border-bottom: 1px solid rgb(200, 200, 200);
}
#tree_table .zk-table--border {
  border-color: rgb(200, 200, 200) !important;
}
#tree_table .zk-table__footer-row,
#tree_table .zk-table__header-row {
  height: 36px;
}
#tree_table .zk-table__body-row {
  height: 36px !important;
}
#tree_table .zk-table--row-hover {
  background-color: #fff6e6;
}
#tree_table
  .zk-table__header-row
  > .zk-table__header-cell:first-of-type
  > .zk-table__cell-inner {
  text-align: center;
}
#tree_table .zk-table__body,
.zk-table__footer,
.zk-table__header {
  min-width: 10px;
}
</style>