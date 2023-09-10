const importDirective = Vue => {
  Vue.directive('enterTrim', {
    inserted: function (el) {
      el.addEventListener("keyup", function (e) {
        let val = el.firstElementChild.value
        val = val.trim()
        el.firstElementChild.value = val
        el.firstElementChild.dispatchEvent(new Event('input'));
      });
    }
  });
}
export default importDirective