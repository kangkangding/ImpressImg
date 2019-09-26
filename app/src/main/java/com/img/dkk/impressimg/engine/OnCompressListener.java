package com.img.dkk.impressimg.engine;

/**
 * Created by  dingkangkang on 2019/9/20
 * email：851615943@qq.com
 */
public interface OnCompressListener {

    void onCompressStart(ImgParamBean imgParamBean);

    void onCompressSuccess(ImgParamBean imgParamBean);

    void onCompressError(ImgParamBean imgParamBean);

}
