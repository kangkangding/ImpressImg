# ImgCompress

 —— `Android`图片压缩工具。 测试demo


# 使用

### 方法列表

方法 | 描述
---- | ----
load | 传入原图
ignoreBy | 不压缩的阈值，单位为K
setTargetDir | 缓存压缩图片路径
setCompressListener | 压缩回调接口

### 异步调用

`ImgCompress`内部采用`IO`线程进行图片压缩，外部调用只需设置好结果监听即可：

```java
ImgCompress.with(this)
            .load(imgList)
            .ignoreSize(100)
            .setTargetDir("dkk")
            .setCompressListener(new OnCompressListener() {

                @Override public void onCompressStart(ImgParamBean imgParamBean) {

                }

                @Override public void onCompressSuccess(ImgParamBean imgParamBean) {

                }

                @Override public void onCompressError(ImgParamBean imgParamBean) {

                }
            }).lauch();


```

### 同步调用

同步方法请尽量避免在主线程调用以免阻塞主线程

```java
ImgCompress.with(this)
            .load(imgList)
            .ignoreSize(100)
            .setTargetDir("dkk")
            .setCompressListener(new OnCompressListener() {

                @Override public void onCompressStart(ImgParamBean imgParamBean) {

                }

                @Override public void onCompressSuccess(ImgParamBean imgParamBean) {

                }

                @Override public void onCompressError(ImgParamBean imgParamBean) {

                }
            }).get();
```
