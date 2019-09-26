package com.img.dkk.impressimg.engine;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by  dingkangkang on 2019/9/25
 * email：851615943@qq.com
 */
public abstract class ImgInputStreamImp implements ImgInputStream{


    InputStream inputStream;

    @Override public InputStream open() throws IOException {
        close();
        inputStream = openIntenal();
        return inputStream;
    }

    protected abstract InputStream openIntenal() throws IOException;

    @Override public void close() {

        if (inputStream != null){
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                inputStream = null;
            }
        }
    }
}
