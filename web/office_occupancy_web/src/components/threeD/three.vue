<template>
  <div id="3DView" style="position: absolute; left: 0px; top: 0px; width:100%; height:100%"></div>
</template>

<script>
import ThreeBSP from './js/ThreeBSP'
import './js/OrbitControls'
import './js/JDLoader.min.js'
export default {
  data() {
    return {
      data: {
        wall: [],
        door: [],
        info: [
          [{ "type": "1" }, { "x": "10", "y": "20" }],
          [{ "type": "2" }, { "x": "500", "y": "700" }]
        ]
      },
      scene: null,
      camera: null,
      renderer: null,
      container: null,
      controls: null,
      wallMaterial: null,
      ASPECT: null,
      dummy: null, // 仿制品
      loader: null, // 纹理加载器
      jdLoader: null, // 3d模型建模加载器
      VIEW_ANGLE: 30,
      NEAR: 0.1,
      FAR: 10000,
      WALL_HEIGHT: 100, // 墙高
      WALL_THICK: 8, // 墙厚
      DOOR_WALL_THICK: 8, // 门厚度
      devOrdinateX: 0,
      devOrdinateY: 0,
      stop: null
    }
  },
  methods: {
    // 初始化三维模型
    init(wall_coordinate) {
      this.data.wall = wall_coordinate;
      const SCREEN_WIDTH = window.innerWidth
      const SCREEN_HEIGHT = window.innerHeight
      this.ASPECT = SCREEN_WIDTH / SCREEN_HEIGHT;
      this.dummy = new THREE.Object3D();
      this.loader = new THREE.TextureLoader();
      this.jdLoader = new THREE.JDLoader();
      this.initScene();
      this.initCamera();
      this.initRender();
      this.initControls();
      this.initLight();
      this.initObject();
      this.animate();
      this.$emit('finished');
    },
    // 动画
    animate() {
      this.stop = requestAnimationFrame(this.animate)
      this.renderer.render(this.scene, this.camera)
    },
    /**
     * 计算线段夹角
     * @param x1 端点A的X坐标
     * @param y1 端点A的Y坐标
     * @param x2 端点B的X坐标
     * @param y2 端点B的Y坐标
     * @returns {number}
     */
    getLineAngle(x1, y1, x2, y2) {
      return Math.atan2(x2 - x1, y2 - y1);
    },
    /**
     * 根据原始的线段数据，转换成3D展示中相对坐标和长度
     * @param ObjectData 原始的线段数据
     * @returns {Object} 3D展示中的相对几何对象
     */
    getObjectGeometryData(ObjectData, devOrdinateX, devOrdinateY) {
      let objectGeometryData = new Object();
      //计算线段长度
      objectGeometryData.objLength = Math.sqrt(Math.pow((ObjectData.xSource - ObjectData.xSink), 2) + Math.pow((ObjectData.ySource - ObjectData.ySink), 2));
      //计算线段的相对XY坐标(平移)
      objectGeometryData.objXPoint = (ObjectData.xSource + ObjectData.xSink) / 2 - devOrdinateX;
      objectGeometryData.objYPoint = (ObjectData.ySource + ObjectData.ySink) / 2 - devOrdinateY;
      return objectGeometryData;
    },
    /**
     * 判断墙中间是否有门
     * @param doorArray 线段（门）坐标数组
     * @param link 线段（墙）的坐标
     * @returns {boolean}
     */
    findDoor(doorArray, link) {
      let doorArr = new Array();
      let slope1, slope2, slope3;
      for (let i = 0; i < doorArray.length; i++) {
        if (link.xSource == link.xSink == doorArray[i].xSource == doorArray[i].xSink) {
          doorArr.push(doorArray[i]);
          continue;
        }
        slope1 = (link.ySink - doorArray[i].ySource) / (link.xSink - doorArray[i].xSource);
        slope2 = (link.ySink - doorArray[i].ySink) / (link.xSink - doorArray[i].xSink);
        slope3 = (link.ySink - link.ySource) / (link.xSink - link.xSource);
        if (slope1 == slope2 && slope2 == slope3) {
          doorArr.push(doorArray[i]);
        }
      }
      return doorArr;
    },
    // 初始化场景
    initScene() {
      this.scene = new THREE.Scene();
    },
    // 初始化相机
    initCamera() {
      this.camera = new THREE.PerspectiveCamera(this.VIEW_ANGLE, this.ASPECT, this.NEAR, this.FAR)
      this.camera.position.set(1000, 1500, 2500)
      this.camera.lookAt(0, 0, 0)
      this.scene.add(this.camera)
    },
    // 初始化渲染器
    initRender() {
      this.renderer = new THREE.WebGLRenderer({ antialias: true })
      // 设置渲染器的大小为窗口的内宽度，也就是内容区的宽度。
      const eleHeight = $('#mainDiv').outerHeight();
      const eleWidth = $('#mainDiv').outerWidth();
      this.renderer.setSize(eleWidth, eleHeight)
      this.container = document.getElementById('3DView')
      if (this.container.childNodes.length > 0) {
        this.container.removeChild(this.container.childNodes[0])
      }
      this.container.appendChild(this.renderer.domElement)
      this.renderer.setClearColor(0x4682B4, 1.0)
    },
    // 初始化控制器
    initControls() {
      this.controls = new THREE.OrbitControls(this.camera, this.renderer.domElement)
    },
    // 初始化光源
    initLight() {
      // 位置不同，方向光作用于物体的面也不同，看到的物体各个面的颜色也不同
      let directionalLight = new THREE.PointLight(0xf0f0f0, 0.4);// 模拟远处类似太阳的光源
      directionalLight.position.set(0, 100, 0).normalize();
      this.scene.add(directionalLight);
      let ambient = new THREE.AmbientLight(0xe0e0e0, 0.8); // AmbientLight,影响整个场景的光源
      ambient.position.set(0, 0, 0);
      this.scene.add(ambient);
    },
    // 初始化OBJ对象
    initObject() {
      this.initMaterial();
      this.initLayout();
    },
    // 初始化材质
    initMaterial() {
      this.wallMaterial = new THREE.MeshLambertMaterial(
        {
          map: this.loader.load(require("./texture/wall.jpg"),
            (texture) => {
              texture.wrapS = texture.wrapT = THREE.RepeatWrapping;
              texture.repeat.set(1, 1);
            })
        }
      );
      this.wallMaterial.transparent = true;
    },
    // 初始化布局
    initLayout() {
      let xMin = 0, xMax = 0, yMin = 0, yMax = 0;
      let wall, door;
      let wallArray = new Array();
      let doorArray = new Array();
      let layoutData = new Object();
      for (let i = 0; i < this.data.wall.length; i++) {
        wall = new Object();
        wall.xSource = parseFloat(this.data.wall[i][0].x);
        wall.ySource = parseFloat(this.data.wall[i][0].y);
        wall.xSink = parseFloat(this.data.wall[i][1].x);
        wall.ySink = parseFloat(this.data.wall[i][1].y);
        wallArray.push(wall);

        if (i == 0) {
          xMin = Math.min(this.data.wall[i][0].x, this.data.wall[i][1].x);
          xMax = Math.max(this.data.wall[i][0].x, this.data.wall[i][1].x);
          yMin = Math.min(this.data.wall[i][0].y, this.data.wall[i][1].y);
          yMin = Math.max(this.data.wall[i][0].y, this.data.wall[i][1].y);
        }
        else {
          xMin = Math.min(xMin, this.data.wall[i][0].x);
          xMin = Math.min(xMin, this.data.wall[i][1].x);
          xMax = Math.max(xMax, this.data.wall[i][0].x);
          xMax = Math.max(xMax, this.data.wall[i][1].x);

          yMin = Math.min(yMin, this.data.wall[i][0].y);
          yMin = Math.min(yMin, this.data.wall[i][1].y);
          yMax = Math.max(yMax, this.data.wall[i][0].y);
          yMax = Math.max(yMax, this.data.wall[i][1].y);
        }
      }
      for (let i = 0; i < this.data.door.length; i++) {
        door = new Object();
        door.xSource = parseFloat(this.data.door[i][0].x);
        door.ySource = parseFloat(this.data.door[i][0].y);
        door.xSink = parseFloat(this.data.door[i][1].x);
        door.ySink = parseFloat(this.data.door[i][1].y);
        doorArray.push(door);
      }
      layoutData.width = xMax - xMin;
      layoutData.length = yMax - yMin;
      layoutData.wallArray = wallArray;
      layoutData.doorArray = doorArray;
      this.devOrdinateX = (xMax + xMin) / 2;
      this.devOrdinateY = (yMax + yMin) / 2;
      this.createLayout(layoutData);
      this.createFloor(xMax - xMin, yMax - yMin);
    },
    // 创建地板
    createFloor(width, length) {
      this.loader.load(require("./texture/floor.png"), (texture) => {
        texture.wrapS = texture.wrapT = THREE.RepeatWrapping;
        texture.repeat.set(10, 10);
        let floorGeometry = new THREE.BoxGeometry(length, width, 1);
        let floorMaterial = new THREE.MeshBasicMaterial({ map: texture, side: THREE.DoubleSide });
        let floor = new THREE.Mesh(floorGeometry, floorMaterial);
        floor.position.z = 0;
        floor.position.y = -100;
        floor.position.x = 0;
        floor.rotation.x = Math.PI / 2;
        floor.rotation.z = Math.PI / 2;
        this.scene.add(floor);
      });
    },
    // 墙上挖门，通过两个几何体生成BSP对象
    createResultBsp(bsp, doorArray) {
      let resultBSP = new ThreeBSP(bsp);
      for (let i = 0; i < doorArray.length; i++) {
        let cube2BSP = new ThreeBSP(doorArray[i]);// 0x9cb2d1 淡紫,0xC3C3C3 白灰 , 0xafc0ca灰
        resultBSP = resultBSP.subtract(cube2BSP);
      }
      let result = resultBSP.toMesh(this.wallMaterial);
      result.material.flatshading = THREE.FlatShading;
      result.geometry.computeFaceNormals();  // 重新计算几何体侧面法向量
      result.geometry.computeVertexNormals();
      result.material.needsUpdate = true;  // 更新纹理
      result.geometry.buffersNeedUpdate = true;
      result.geometry.uvsNeedUpdate = true;
      this.scene.add(result);
    },
    /**
     * 创建墙体，原点为几何中心
     * @param width 宽度
     * @param height 高度
     * @param depth 深度
     * @param angle 角度
     * @param material 材质
     * @param x 原点x坐标
     * @param y 原点y坐标
     * @param z 原点z坐标
     */
    createCubeWall(width, height, depth, angle, x, y, z) {
      let cubeGeometry = new THREE.BoxGeometry(width, height, depth);
      let cube = new THREE.Mesh(cubeGeometry, this.wallMaterial);
      cube.position.x = x;
      cube.position.y = y;
      cube.position.z = z;
      cube.rotation.y = angle;
      this.scene.add(cube);
    },
    // 返回墙对象
    returnWallObject(width, height, depth, angle, x, y, z) {
      let cubeGeometry = new THREE.BoxGeometry(width, height, depth);
      let cube = new THREE.Mesh(cubeGeometry, this.wallMaterial);
      cube.position.x = x;
      cube.position.y = y;
      cube.position.z = z;
      cube.rotation.y = angle;
      return cube;
    },
    // 创建房间布局
    createLayout(layoutData) {
      let wallArray = layoutData.wallArray;
      let doorArray = layoutData.doorArray;
      let doorInWallArray, doorObjArray;
      let allDoorObjArray = new Array();
      for (let i = 0; i < wallArray.length; i++) {
        let angle = 0;
        // 计算线段与坐标轴夹角
        angle = this.getLineAngle(wallArray[i].xSource, wallArray[i].ySource, wallArray[i].xSink, wallArray[i].ySink);
        // 根据原始的线段数据，转换成3D展示中相对坐标和长度
        let wallGeometryData = this.getObjectGeometryData(wallArray[i], this.devOrdinateX, this.devOrdinateY);
        // 判断墙面中是否还有门
        doorInWallArray = this.findDoor(doorArray, wallArray[i]);
        if (doorInWallArray.length == 0) {
          this.createCubeWall(this.WALL_THICK, this.WALL_HEIGHT, wallGeometryData.objLength, angle, wallGeometryData.objXPoint, this.WALL_HEIGHT / 2 - 100, wallGeometryData.objYPoint);
        }
        else {
          let wall = this.returnWallObject(this.DOOR_WALL_THICK, this.WALL_HEIGHT, wallGeometryData.objLength, angle, wallGeometryData.objYPoint, this.WALL_HEIGHT / 2 - 100, wallGeometryData.objXPoint);
          doorObjArray = new Array();
          for (let j = 0; j < doorInWallArray.length; j++) {
            let doorGeometryData = this.getObjectGeometryData(doorInWallArray[j], this.devOrdinateX, this.devOrdinateY);
            let doorObj = this.returnWallObject(doorGeometryData.objLength, this.WALL_HEIGHT - 20, this.DOOR_WALL_THICK, angle + Math.PI / 2, doorGeometryData.objYPoint, 0, doorGeometryData.objXPoint)
            doorObjArray.push(doorObj);
            allDoorObjArray.push(doorObj);
          }
          this.createResultBsp(wall, doorObjArray);
        }
      }
      this.addDoorTexture(allDoorObjArray);
    },
    // 为墙面安装门,右门
    addDoorTexture(doorArray) {
      this.loader.load(require("./texture/door_susan3.jpg"), (texture) => {
        let door = null;
        let doorgeometry = new THREE.BoxGeometry(100, 180, 2);
        let doormaterial = new THREE.MeshBasicMaterial({ map: texture, color: 0xffffff });
        doormaterial.opacity = 1.0;
        doormaterial.transparent = true;
        for (let i = 0; i < doorArray.length; i++) {
          door = new THREE.Mesh(doorgeometry, doormaterial);
          door.position.set(doorArray[i].position.x + 400, doorArray[i].position.y, doorArray[i].position.z - 250);
          door.rotation.y = doorArray[i].rotation.y;
          let door1 = door.clone();
          door1.position.set(doorArray[i].position.x + 400, doorArray[i].position.y, doorArray[i].position.z - 250);
          door1.rotation.y = doorArray[i].rotation.y;
          door1.visible = false;
          this.dummy.add(door);
          this.dummy.add(door1);
          this.dummy.position.set(doorArray[i].position.x + 400, doorArray[i].position.y, doorArray[i].position.z - 250);
          this.scene.add(this.dummy);
        }
      });
    },
    clear() {
      this.container = document.getElementById('3DView')
      if (this.container.childNodes.length > 0) {
        this.container.removeChild(this.container.childNodes[0])
      }
    }
  },
  mounted() {
  }
}
</script>

<style scoped>
h1,
h2 {
  font-weight: normal;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
