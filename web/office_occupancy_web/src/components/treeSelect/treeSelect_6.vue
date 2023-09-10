<template >
  <!-- 存放地点下拉选择树 -->
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
      :style="{height:contentHeight, top: inputHeight}"
      v-show="showContent"
    >
      <el-tree
        v-if="showSelectTree"
        :ref="treeName"
        node-key="id"
        :load="loadNode"
        lazy
        :props="defaultProps"
        accordion
        :check-strictly="checkStrictly"
        show-checkbox
        @check-change="handleCheckChange"
        @check="handleCheck"
      ></el-tree>
    </div>
  </div>
</template>                                                                                                                                                                  
<script>
import { getAllYardTree, getCategoryTree, getYardTree } from './api';
import { eventBus } from "@/eventBus/eventBus";
export default {
  props: {
    treeName: {
      type: String,
      default: ''
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
    assetsType: {
      type: Number,
      default: null
    },
    isMultipleSelect: {
      type: Boolean,
      default: false
    },
    checkStrictly: {
      type: Boolean,
      default: true
    },
    showSelectTree: {
      type: Boolean,
      default: true
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
      treeData: [],
      yardId: '',
      buildId: '',
      floorName: '',
      yardName: '',
      buildName: '',
      floor: '',
      treeId: this.treeName + '_select_tree'
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
      // 存放地点
      if (this.treeName === 'storageLocation') {
        if (node.level === 1) {
          this.yardId = node.data.id;
          this.yardName = node.data.text;
        } else if (node.level === 2) {
          this.buildId = node.data.id;
          this.buildName = node.data.text;
        } else if (node.level === 3) {
          this.floorName = node.data.id;
          this.floor = node.data.text;
        }
        if (node.level === 0) {
          getAllYardTree(null, 0, null).then(res => {
            res.forEach(item => {
              if (!item.expand || item.expand === undefined) {
                item.leaf = true;
              }
            })
            return resolve(res);
          }).catch(err => {
            this.$message({
              message: `树获取失败`,
              type: "error"
            });
          })
        } else if (node.level === 1 || node.level === 2 || node.level === 3) {
          const buildId = node.data.buildId ? node.data.buildId : null;
          getAllYardTree(node.data.value, node.data.type, buildId).then(res => {
            res.forEach(item => {
              if (!item.expand || item.expand === undefined) {
                item.leaf = true;
              }
            })
            return resolve(res);
          }).catch(err => {
            this.$message({
              message: `树获取失败`,
              type: "error"
            });
          })
        }
        if (node.level > 3) return resolve([]);
      }
      // 资产分类名称
      if (this.treeName === 'categoryName') {
        if (node.level === 0) {
          getCategoryTree(null, this.assetsType).then(res => {
            res.forEach(item => {
              if (!item.expand || item.expand === undefined) {
                item.leaf = true;
              }
            })
            return resolve(res);
          }).catch(err => {
            this.$message({
              message: `类别获取失败`,
              type: "error"
            });
          })
        } else {
          getCategoryTree(node.data.id, this.assetsType).then(res => {
            res.forEach(item => {
              if (!item.expand || item.expand === undefined) {
                item.leaf = true;
              }
            })
            return resolve(res);
          }).catch(err => {
            this.$message({
              message: `类别获取失败`,
              type: "error"
            });
          })
        }
      }
      // 档案管理
      if (this.treeName === 'recordSearchTree') {
        if (node.level === 0) {
          getYardTree(null, 0, null)
            .then(res => {
              res.forEach(item => {
                if (!item.expand || item.expand === undefined) {
                  item.leaf = true;
                }
              });
              return resolve(res);
            })
            .catch(err => {
              const msg = err.result ? err.result : '树获取失败';
              this.$message({
                message: msg,
                type: "error"
              });
            });
        } else {
          getYardTree(node.data.value, node.data.type, null)
            .then(res => {
              if (node.data.type === 1) {
                res.forEach(item => {
                  item.leaf = true;
                });
              }
              return resolve(res);
            })
            .catch(err => {
              const msg = err.result ? err.result : '树获取失败';
              this.$message({
                message: msg,
                type: "error"
              });
            });
        }
      }
    },
    handleCheckChange(data, checked) {
      if (checked && !this.isMultipleSelect) {
        this.$refs[this.treeName].setCheckedKeys([]);
        this.$refs[this.treeName].setCheckedKeys([data.id]);
      }
      if (checked) {
        // 存放地点
        if (this.treeName === 'storageLocation') {
          if (data.type === 1) {
            let tempObj = {}
            tempObj.level = 1;
            tempObj.yardId = data.id;
            tempObj.label = data.text;
            tempObj.treeName = this.treeName;
            this.inputText = tempObj.label;
            this.$emit('select-tree-checked', tempObj);
          } else if (data.type === 2) {
            let tempObj = {}
            tempObj.level = 2;
            tempObj.yardId = this.yardId;
            tempObj.buildId = data.id;
            tempObj.label = `${this.yardName}${data.text}`;
            tempObj.treeName = this.treeName;
            this.inputText = tempObj.label;
            this.$emit('select-tree-checked', tempObj);
          } else if (data.type === 3) {
            let tempObj = {}
            tempObj.level = 3;
            tempObj.yardId = this.yardId;
            tempObj.buildId = this.buildId;
            tempObj.floorName = data.id;
            tempObj.label = `${this.yardName}${this.buildName}${data.text}`;
            tempObj.treeName = this.treeName;
            this.inputText = tempObj.label;
            this.$emit('select-tree-checked', tempObj);
          } else if (!data.type || data.type === undefined) {
            let tempObj = {}
            tempObj.level = 4;
            tempObj.yardId = this.yardId;
            tempObj.buildId = this.buildId;
            tempObj.floorName = this.floorName;
            tempObj.houseId = data.id;
            tempObj.label = `${this.yardName}${this.buildName}${this.floor}${data.text}`;
            tempObj.treeName = this.treeName;
            this.inputText = tempObj.label;
            this.$emit('select-tree-checked', tempObj);
          }
        }
        // 资产分类名称
        if (this.treeName === 'categoryName') {
          let tempObj = {}
          tempObj.text = data.text;
          tempObj.unit = (data.unit && data.unit.length > 0) ? data.unit[0] : '暂无单位';
          tempObj.id = data.id;
          tempObj.treeName = this.treeName;
          this.inputText = tempObj.text;
          this.$emit('select-tree-checked', tempObj);
        }
      }
    },
    handleCheck(data, obj) {
      // 档案管理
      if (this.treeName === 'recordSearchTree') {
        let nameArr = [];
        obj.checkedNodes.forEach(item => {
          nameArr.push(item.text);
        })
        this.inputText = nameArr.join(',');
        this.$emit('select-tree-checked', obj.checkedNodes);
      }
    },
    closeInput() {
      this.showContent = false;
      $('#' + this.treeId).find('.el-icon-arrow-up').removeClass('is-reverse');
    }
  },
  mounted() {
    let self = this;
    $(document).click(function () {
      self.showContent = false;
      $('#' + self.treeId).find('.el-icon-arrow-up').removeClass('is-reverse');
    })
    eventBus.$off('clearRecordPosition');
    eventBus.$off('closeInput');
    eventBus.$on('clearRecordPosition', () => {
      this.inputText = '';
    })
    eventBus.$on('closeInput', () => {
      this.showContent = false;
      $('#' + this.treeId).find('.el-icon-arrow-up').removeClass('is-reverse');
    })
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
  overflow: auto;
  min-width: 200px;
  z-index: 1001;
  border: solid 1px #e4e7ed;
  border-radius: 4px;
  background-color: #fff;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  box-sizing: border-box;
  margin: 12px 0;
  left: 0;
}
.tree-select-components .el-input__inner {
  font-size: 14px;
}
</style>
