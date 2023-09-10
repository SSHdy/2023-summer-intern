const mouseEvent = {
  wheel(opt) {
    if (!opt.target) {
      return;
    }
    let callback = opt.callback || function () { };
    let target = opt.target;

    //获取兼容事件
    let mouseWheel = (/Firefox/i.test(navigator.userAgent)) ? "DOMMouseScroll" : "mousewheel";

    //IE
    if (document.attachEvent) {
      document.attachEvent('on' + mouseWheel, function (e) {
        if (e.target.parentElement == target) {
          callback(e, e.wheelDelta)
        }
      })
    }

    //FF、Chrome、Opera、safari
    else {
      document.addEventListener(mouseWheel, function (e) {
        e = e || window.event;
        callback(e);
      })
    }

  }
};
export { mouseEvent }
