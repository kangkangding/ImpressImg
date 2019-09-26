package com.img.dkk.impressimg.engine;

import java.io.File;

/**
 * Created by  dingkangkang on 2019/9/20
 * email：851615943@qq.com
 */
public class ImgParamBean {

    private File file; // 返回路径
    private String originalUrl; // 传入路径
    private float originalLen;
    private float compressLen;

    public float getOriginalLen() {
        return originalLen;
    }

    public void setOriginalLen(float originalLen) {
        this.originalLen = originalLen;
    }

    public float getCompressLen() {
        return compressLen;
    }

    public void setCompressLen(float compressLen) {
        this.compressLen = compressLen;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
