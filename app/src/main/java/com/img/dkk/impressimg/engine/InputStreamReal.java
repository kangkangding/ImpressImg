package com.img.dkk.impressimg.engine;

import android.content.Context;
import android.net.Uri;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by  dingkangkang on 2019/9/25
 * email：851615943@qq.com
 */
public class InputStreamReal<T> extends ImgInputStreamImp{

    private T imgUrl;
    private Context context;

    public InputStreamReal(T imgUrl, Context context) {
        this.imgUrl = imgUrl;
        this.context = context;
    }

    @Override protected InputStream openIntenal() throws IOException {

        if(imgUrl instanceof String){
            return new FileInputStream((String) imgUrl);
        }else if(imgUrl instanceof File){
            return new FileInputStream((File) imgUrl);
        }else if(imgUrl instanceof Uri){
            return context.getContentResolver().openInputStream((Uri)imgUrl);
        }

        return null;
    }

    @Override public String getPath() {
        if(imgUrl instanceof String){
            return (String) imgUrl;
        }else if(imgUrl instanceof File){
            return ((File) imgUrl).getAbsolutePath();
        }else if(imgUrl instanceof Uri){
            return ((Uri) imgUrl).getPath();
        }
        return "";
    }
}
