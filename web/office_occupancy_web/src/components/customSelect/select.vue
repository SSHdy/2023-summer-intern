<template >
  <div :id="selectId" class="select-components" :style="{width: inputWidth}">
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
      <slot></slot>
    </div>
  </div>
</template>                                                                                                                                                                  
<script>
import { eventBus } from "@/eventBus/eventBus";
import uuidv1 from 'uuid/v1';
export default {
  props: {
    name: {
      type: String,
      default: 'selectTree'
    },
    inputHeight: {
      type: String,
      default: '32px'
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
    inputText: {
      type: String,
      default: ''
    },
    common_quantity: {
      type: String,
      default: ''
    },
    common_price: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      showContent: false,
      selectId: this.name + '_select'
    };
  },
  methods: {
    selectInputClick($event) {
      if (this.common_quantity && this.common_price) {
        $event.stopPropagation();
        if (!this.showContent) {
          $('#' + this.selectId).find('.el-icon-arrow-up').addClass('is-reverse');
          this.$emit('getSectionList');
        } else {
          $('#' + this.selectId).find('.el-icon-arrow-up').removeClass('is-reverse');
        }
        this.showContent = !this.showContent;
      } else {
        this.$message({
          message: '请先填写采购单价和采购数量！',
          type: "warning"
        });
      }
    }
  },
  mounted() {
    document.onclick = (e) => {
      this.showContent = false;
      $('#' + this.selectId).find('.el-icon-arrow-up').removeClass('is-reverse');
    }
  }
};
</script>  
<style scoped>
.select-components,
.select-components > .selectInput-box {
  position: relative;
}
.select-components > .selectInput-box > input {
  cursor: pointer;
  font-size: 14px;
}
.select-components > .selectInput-box .el-select__caret {
  color: #c0c4cc;
  font-size: 14px;
  transition: all 0.3s;
  transform: rotateZ(180deg);
  cursor: pointer;
}
.select-components > .selectInput-box .el-select__caret.is-reverse {
  transform: rotateZ(0deg);
}
.select-components .content-box-dropdown {
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
