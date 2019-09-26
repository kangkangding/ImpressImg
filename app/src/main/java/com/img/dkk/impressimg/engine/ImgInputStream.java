package com.img.dkk.impressimg.engine;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by  dingkangkang on 2019/9/25
 * email：851615943@qq.com
 */
public interface ImgInputStream {

    InputStream open() throws IOException;


    void close();


    String getPath();

}
