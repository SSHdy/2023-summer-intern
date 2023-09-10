<template >
  <div :id="treeId" class="tree-select-components" :style="{width: inputWidth}">
    <div class="selectInput-box" :style="{width: inputWidth}">
      <input
        type="text"
        readonly="readonly"
        autocomplete="off"
        :placeholder="placeholderText"
        class="el-input__inner"
        v-model="inputText"
        @click="selectInputClick($event)"
        :style="{height:inputHeight, width: inputWidth}"
      >
      <span class="el-input__suffix">
        <span class="el-input__suffix-inner">
          <i class="el-select__caret el-input__icon el-icon-arrow-up"></i>
        </span>
      </span>
    </div>
    <div
      class="content-box-dropdown"
      :style="{height:contentHeight, width: inputWidth, top: inputHeight}"
      v-show="showContent"
    >
      <el-tree
        :style="{overflowY:'scroll',height: contentHeight}"
        :ref="treeName"
        node-key="id"
        :load="loadNode"
        lazy
        :props="defaultProps"
        accordion
        check-strictly
        show-checkbox
        @check="handleCheckChange"
        @node-expand="handleNodeExpand"
        :default-expanded-keys="defaultExpandedkeys"
        v-if="treeShow"
      ></el-tree>
    </div>
  </div>
</template>                                                                                                                                                                  
<script>
import { eventBus } from "@/eventBus/eventBus";
import uuidv1 from 'uuid/v1';
export default {
  props: {
    treeName: {
      type: String,
      default: 'selectTree'
    },
    inputHeight: {
      type: String,
      default: '40px'
    },
    inputWidth: {
      type: String,
      default: '300px'
    },
    contentHeight: {
      type: String,
      default: '200px'
    },
    placeholderText: {
      type: String,
      default: '请选择'
    },
    treeShow: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      showContent: false,
      defaultProps: {
        children: 'children',
        label: 'text',
        isLeaf: 'leaf'
      },
      inputText: '',
      treeId: this.treeName + '_select_tree',
      defaultExpandedkeys: []
    };
  },
  methods: {
    selectInputClick($event) {
      $event.stopPropagation();
      if (!this.showContent) {
        $('#' + this.treeId).find('.el-icon-arrow-up').addClass('is-reverse');
      } else {
        $('#' + this.treeId).find('.el-icon-arrow-up').removeClass('is-reverse');
      }
      this.showContent = !this.showContent;
    },
    loadNode(node, resolve) {
      this.$emit('loadNode', node, resolve);
    },
    handleCheckChange(data, obj) {
      if (obj.checkedKeys.length > 0) {
        this.$refs[this.treeName].setCheckedKeys([data.id]);
        this.inputText = data.text;
        this.$emit('handleCheckChange',data);
      } else {
        this.$refs[this.treeName].setCheckedKeys([]);
        this.$emit('handleCheckChange',{});
        this.inputText = ''
      }
    },
    handleNodeExpand(data, node) {
      this.$emit('handleNodeExpand', data, node);
    },
  },
  mounted() {
    document.onclick = (e) => {
      this.showContent = false;
      $('#' + this.treeId).find('.el-icon-arrow-up').removeClass('is-reverse');
    }
    eventBus.$off('clearTreeSelectVal');
    eventBus.$off('setTreeSelectVal');
    eventBus.$on('clearTreeSelectVal', () => {
      this.inputText = '';
      this.defaultExpandedkeys = [];
      this.$refs[this.treeName].setCheckedKeys([]);
    });;
 
    eventBus.$on('setTreeSelectVal', (houseNameList, houseIdList, expandIdList) => {
      this.inputText = houseNameList[0];
      this.defaultExpandedkeys = expandIdList;
      this.$nextTick(()=>{
        this.$refs[this.treeName].setCheckedKeys(houseIdList);
      })
    })
  }
};
</script>  
<style scoped>
.tree-select-components,
.tree-select-components > .selectInput-box {
  position: relative;
  font-size: 14px;
}
.tree-select-components > .selectInput-box > input {
  cursor: pointer;
}
.tree-select-components > .selectInput-box .el-select__caret {
  color: #c0c4cc;
  font-size: 14px;
  transition: all 0.3s;
  transform: rotateZ(180deg);
  cursor: pointer;
}
.tree-select-components > .selectInput-box .el-select__caret.is-reverse {
  transform: rotateZ(0deg);
}
.tree-select-components .content-box-dropdown {
  position: absolute;
  z-index: 1001;
  border: solid 1px #e4e7ed;
  border-radius: 4px;
  background-color: #fff;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  box-sizing: border-box;
  margin: 12px 0;
  left: 0;
}
</style>
