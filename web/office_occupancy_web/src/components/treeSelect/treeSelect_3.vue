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
      />
      <span class="el-input__suffix" @click="selectInputClick($event)">
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
        :props="defaultProps"
        :data="treeData"
        show-checkbox
        @check="handleCheckChange"
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
    },
    treeData: {
      type: Array,
      default: () => {
        return [];
      }
    }
  },
  data() {
    return {
      showContent: false,
      defaultProps: {
        children: 'children',
        label: 'name'
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
    handleCheckChange(data, obj) {
      this.$emit('handleCheckChange', obj.checkedNodes, obj.halfCheckedNodes);
      let houseName = [];
      obj.checkedNodes.forEach(node => {
        if (this.treeName === 'disposeSearchTree') {
          if (node.type === 3) {
            houseName.push(node.name);
          }
        } else if (this.treeName === 'specialSearchTree') {
          if (node.type === 0) {
            houseName.push(node.name);
          }
        } else if (this.treeName === 'houseCertificate' || this.treeName === 'landCertificate' || this.treeName === 'immovables') {
          if (node.type === 1) {
            houseName.push(node.name);
          }
        } else {
          if (node.level === 3) {
            houseName.push(node.name);
          }
        }
      });
      this.inputText = houseName.join(',');
    }
  },
  mounted() {
    document.onclick = (e) => {
      this.showContent = false;
      $('#' + this.treeId).find('.el-icon-arrow-up').removeClass('is-reverse');
    }
    eventBus.$off(`${this.treeName}_select`);
    eventBus.$off(`${this.treeName}_clear`);
    eventBus.$off(`${this.treeName}_clearInput`);
    eventBus.$on(`${this.treeName}_select`, (houseNameList, houseIdList, expandIdList) => {
      this.inputText = houseNameList.join(',');
      setTimeout(() => {
        this.defaultExpandedkeys = expandIdList;
        this.$refs[this.treeName].setCheckedKeys(houseIdList);
      }, 100);
    });
    eventBus.$on(`${this.treeName}_clear`, () => {
      this.inputText = '';
      for (var i = 0; i < this.$refs[this.treeName].store._getAllNodes().length; i++) {
        this.$refs[this.treeName].store._getAllNodes()[i].expanded = false;
      }
      this.$refs[this.treeName].setCheckedKeys([]);
    });
    eventBus.$on(`${this.treeName}_clearInput`, () => {
      this.inputText = '';
    });
  }
};
</script>  
<style scoped>
.tree-select-components,
.tree-select-components > .selectInput-box {
  position: relative;
}
.tree-select-components > .selectInput-box > input {
  cursor: pointer;
  font-size: 14px;
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
