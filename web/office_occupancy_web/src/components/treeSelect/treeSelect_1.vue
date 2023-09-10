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
import uuidv1 from "uuid/v1";
export default {
  props: {
    treeName: {
      type: String,
      default: "selectTree"
    },
    inputHeight: {
      type: String,
      default: "40px"
    },
    inputWidth: {
      type: String,
      default: "300px"
    },
    contentHeight: {
      type: String,
      default: "200px"
    },
    placeholderText: {
      type: String,
      default: "请选择"
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
        children: "children",
        label: "name"
      },
      inputText: "",
      treeId: this.treeName + "_select_tree",
      defaultExpandedkeys: [],
      oldCheckedNodes: null,
      checkedfatherNodeId: null
    };
  },
  methods: {
    selectInputClick($event) {
      $event.stopPropagation();
      if (!this.showContent) {
        $("#" + this.treeId)
          .find(".el-icon-arrow-up")
          .addClass("is-reverse");
      } else {
        $("#" + this.treeId)
          .find(".el-icon-arrow-up")
          .removeClass("is-reverse");
      }
      this.showContent = !this.showContent;
    },
    handleCheckChange(data, obj) {
      // 判断勾选还是取消勾选
      let checkedNodesId = [];
      let isChecked = true;
      const checkedNodes = obj.checkedNodes.concat(obj.halfCheckedNodes);
      checkedNodes.forEach(node => {
        checkedNodesId.push(node.id);
      });
      if (checkedNodesId.indexOf(data.id) > -1) {
        isChecked = true; // 勾选
      } else {
        isChecked = false; // 取消勾选
      }
      if (isChecked) {
        let fatherNodeId = null;
        if (data.type !== 0) {
          fatherNodeId = parseInt(data.id.split(":")[0]);
        } else {
          fatherNodeId = data.value;
        }
        if (this.treeName == "special") {
            this.$refs[this.treeName].setCheckedKeys([data.id]);
        }
        if (
          this.checkedfatherNodeId &&
          fatherNodeId !== this.checkedfatherNodeId
        ) {
          this.$refs[this.treeName].setCheckedKeys([data.id]);
        }
      }
      const currentCheckedNodes = this.$refs[this.treeName].getCheckedNodes();
      const currentHalfCheckedNodes = this.$refs[
        this.treeName
      ].getHalfCheckedNodes();

      let flag = false;
      let temp = [];
      let num = 0;
      if (currentCheckedNodes.length > 0) {
        for (let i = 0; i < currentCheckedNodes.length; i++) {
          if (currentCheckedNodes[i].type === 3) {
            const houseId = currentCheckedNodes[i].value;
            temp.push(houseId);
          }
        }
      }
      for (let i = 0; i < currentCheckedNodes.length; i++) {
        if (currentCheckedNodes[i].type === 1) {
          const floors = currentCheckedNodes[i].children;
          if (floors && floors.length > 0) {
            for (let j = 0; j < floors.length; j++) {
              const house = floors[j].children;
              for (let a = 0; a < house.length; a++) {
                if (temp.indexOf(house[a].value) > -1) {
                  num++;
                }
              }
            }
          }
        }
      }
      if (num === temp.length) {
        flag = true;
      }

      const currentAllCheckedNodes = currentCheckedNodes.concat(
        currentHalfCheckedNodes
      );
      this.$emit("handleCheckChange", currentAllCheckedNodes, flag);
      let houseName = [];
      if (flag) {
        currentCheckedNodes.forEach(node => {
          if (node.type === 1) {
            houseName.push(node.name);
          }
        });
      } else {
        currentCheckedNodes.forEach(node => {
          if (node.type === 3) {
            houseName.push(node.name);
          }
        });
      }
      this.inputText = houseName.join(",");
      // 保存已选节点
      this.oldCheckedNodes = currentCheckedNodes.concat(
        currentHalfCheckedNodes
      );
      for (let i = 0; i < this.oldCheckedNodes.length; i++) {
        if (this.oldCheckedNodes[i].type === 0) {
          this.checkedfatherNodeId = this.oldCheckedNodes[i].id;
          break;
        }
      }
    }
  },
  mounted() {
    document.onclick = e => {
      this.showContent = false;
      $("#" + this.treeId)
        .find(".el-icon-arrow-up")
        .removeClass("is-reverse");
    };
    eventBus.$off(`${this.treeName}_select`);
    eventBus.$off(`${this.treeName}_clear`);
    eventBus.$off(`${this.treeName}_clearInput`);
    eventBus.$on(
      `${this.treeName}_select`,
      (nameList, checkedIdList, expandIdList) => {
        this.inputText = nameList.join(",");
        setTimeout(() => {
          this.defaultExpandedkeys = expandIdList;
          this.$refs[this.treeName].setCheckedKeys(checkedIdList);
        }, 100);
      }
    );
    eventBus.$on(`${this.treeName}_clear`, () => {
      this.inputText = "";
      for (
        var i = 0;
        i < this.$refs[this.treeName].store._getAllNodes().length;
        i++
      ) {
        this.$refs[this.treeName].store._getAllNodes()[i].expanded = false;
      }
      this.$refs[this.treeName].setCheckedKeys([]);
    });
    eventBus.$on(`${this.treeName}_clearInput`, () => {
      this.inputText = "";
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
