package com.img.dkk.impressimg.engine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.img.dkk.impressimg.R;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by  dingkangkang on 2019/9/24
 * email：851615943@qq.com
 */
class ImgEngine {

    ImgInputStream imgUrl;
    File outFile;
    private int srcWidth;
    private int srcHeight;
    private ImgParamBean imgParamBean;

    public ImgEngine(ImgInputStream imgUrl, File outFile,ImgParamBean imgParamBean) {
        this.imgUrl = imgUrl;
        this.outFile = outFile;
        this.imgParamBean = imgParamBean;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 1;

        try {
            BitmapFactory.decodeStream(imgUrl.open(),null,options);
        } catch (Exception e) {
            e.printStackTrace();
        }

        srcWidth = options.outWidth;
        srcHeight = options.outHeight;
    }

    public ImgParamBean compress() {

        try {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = computeSampleSize();

            Bitmap bitmap = BitmapFactory.decodeStream(imgUrl.open(),null,options);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG,60,outputStream);

            bitmap.recycle();

            FileOutputStream outputStream1 = new FileOutputStream(outFile);

            outputStream1.write(outputStream.toByteArray());

            outputStream1.flush();
            outputStream1.close();
            outputStream.close();
            imgParamBean.setFile(outFile);
            return imgParamBean;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            imgUrl.close();
        }
        return null;
    }

    private int computeSampleSize() {

        srcWidth = srcWidth % 2 == 1 ? srcWidth + 1 : srcWidth;
        srcHeight = srcHeight % 2 == 1 ? srcHeight + 1 : srcHeight;

        int longSide = Math.max(srcWidth, srcHeight);
        int shortSide = Math.min(srcWidth, srcHeight);

        float scale = ((float) shortSide / longSide);
        if (scale <= 1 && scale > 0.5625) {
            if (longSide < 1664) {
                return 1;
            } else if (longSide < 4990) {
                return 2;
            } else if (longSide > 4990 && longSide < 10240) {
                return 4;
            } else {
                return longSide / 1280 == 0 ? 1 : longSide / 1280;
            }
        } else if (scale <= 0.5625 && scale > 0.5) {
            return longSide / 1280 == 0 ? 1 : longSide / 1280;
        } else {
            return (int) Math.ceil(longSide / (1280.0 / scale));
        }
    }

}
