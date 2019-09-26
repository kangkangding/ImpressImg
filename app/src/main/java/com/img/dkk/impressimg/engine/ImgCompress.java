package com.img.dkk.impressimg.engine;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Created by  dingkangkang on 2019/9/20
 * email：851615943@qq.com
 */
public class ImgCompress implements Handler.Callback {

    private List<ImgInputStream> imgList;

    private int ignoreSize;

    private String targetDir;

    private OnCompressListener onCompressListener;

    private Context mContext;

    private Handler handler = new Handler(Looper.getMainLooper(),this);

    public ImgCompress(Builder builder) {
        this.ignoreSize = builder.ignoreSize;
        this.targetDir = builder.targetDir;
        this.onCompressListener = builder.onCompressListener;
        this.mContext = builder.mContext;
        this.imgList = builder.imgList;
    }

    public static Builder with(Context mContext){
        return new Builder(mContext);
    }

    @Override public boolean handleMessage(Message message) {
        if(onCompressListener == null){
            return false;
        }
        switch (message.what){
            case 0:
                onCompressListener.onCompressStart((ImgParamBean)message.obj);
                break;
            case 1:
                onCompressListener.onCompressSuccess((ImgParamBean)message.obj);
                break;
            case 2:
                onCompressListener.onCompressError((ImgParamBean)message.obj);
                break;
        }


        return false;
    }

    public static class Builder{
        public List<ImgInputStream> imgList;

        private int ignoreSize;

        private String targetDir;

        private OnCompressListener onCompressListener;

        private Context mContext;

        private ImgCompress build(){
            return new ImgCompress(this);
        }

        public Builder(Context mContext) {
            this.mContext = mContext;
            this.imgList = new ArrayList<>();
        }

        public Builder load(String imgUrl){
            this.imgList.add(new InputStreamReal<>(imgUrl,mContext));
            return this;
        }

        public <T>Builder load(List<T> imgList){

            for (T src : imgList) {
                if(src instanceof String){
                    this.imgList.add(new InputStreamReal<>(src,mContext));
                }else if(src instanceof File){
                    this.imgList.add(new InputStreamReal<>(src,mContext));
                }else if(src instanceof Uri){
                    this.imgList.add(new InputStreamReal<>(src,mContext));
                }
            }

            return this;
        }


        public Builder ignoreSize(int ignoreSize){
            this.ignoreSize = ignoreSize;
            return this;
        }

        public Builder setTargetDir(String targetDir){
            this.targetDir = targetDir;
            return this;
        }

        public Builder setCompressListener(OnCompressListener onCompressListener){
            this.onCompressListener = onCompressListener;
            return this;
        }

        public void lauch() {
            build().launch(mContext);
        }

        public List<ImgParamBean> get() {
            return build().get(mContext);
        }
    }

    /**
     * 开始压缩
     */
    public void launch(final Context context){

        Iterator<ImgInputStream> iterator = imgList.iterator();

        while (iterator.hasNext()){
            final ImgInputStream imgUrl = iterator.next();

            AsyncTask.SERIAL_EXECUTOR.execute(new Runnable() {
                @Override public void run() {
                    ImgParamBean imgParamBean = new ImgParamBean();
                    try {
                        imgParamBean.setOriginalUrl(imgUrl.getPath());
                        handler.sendMessage(handler.obtainMessage(0,imgParamBean));
                        Log.e("压缩log","压缩前：：：：：："+ new File(imgUrl.getPath()).length());

                        imgParamBean = realCompress(context,imgUrl,imgParamBean);

                        Log.e("压缩log","压缩后：：：：：："+ imgParamBean.getFile().length());
                        handler.sendMessage(handler.obtainMessage(1,imgParamBean));
                    }catch (Exception e){
                        handler.sendMessage(handler.obtainMessage(2,imgParamBean));
                    }

                }
            });
        }
    }

    public List<ImgParamBean> get(final Context context){

        final List<ImgParamBean> fileList = new ArrayList<>();
        Iterator<ImgInputStream> iterator = imgList.iterator();

        while (iterator.hasNext()){
            ImgInputStream imgUrl = iterator.next();
            ImgParamBean imgParamBean = new ImgParamBean();
            try {
                imgParamBean.setOriginalUrl(imgUrl.getPath());
                handler.sendMessage(handler.obtainMessage(0,imgParamBean));
                imgParamBean.setOriginalLen(new File(imgUrl.getPath()).length());
                Log.e("压缩log","压缩前：：：：：："+ new File(imgUrl.getPath()).length());

                imgParamBean = realCompress(context,imgUrl,imgParamBean);

                fileList.add(imgParamBean);

                imgParamBean.setCompressLen(imgParamBean.getFile().length());
                Log.e("压缩log","压缩后：：：：：："+ imgParamBean.getFile().length());
                handler.sendMessage(handler.obtainMessage(1,imgParamBean));
            }catch (Exception e){
                handler.sendMessage(handler.obtainMessage(2,imgParamBean));
            }
            iterator.remove();
        }

        return fileList;
    }


    private ImgParamBean realCompress(Context context, ImgInputStream imgUrl,ImgParamBean imgParamBean) throws Exception {

        ImgParamBean result;

        File outFile = getCacheDir(context);

        if(outFile != null){

            if(new File(imgUrl.getPath()).length() > ignoreSize){
                result = new ImgEngine(imgUrl,outFile,imgParamBean).compress();
            }else {
                result = getImgInfo(imgUrl);
            }

            return result;
        }

        return null;
    }

    private ImgParamBean getImgInfo(ImgInputStream imgUrl) throws Exception {
        ImgParamBean result = new ImgParamBean();
        if(imgUrl != null && !TextUtils.isEmpty(imgUrl.getPath()) && new File(imgUrl.getPath()).exists()){
            result.setOriginalUrl(imgUrl.getPath());
            result.setFile(new File(imgUrl.getPath()));

            result.setOriginalLen(new File(imgUrl.getPath()).length());
            result.setCompressLen(new File(imgUrl.getPath()).length());
        }else {
            throw new Exception("文件不存在！！！！");
        }
        return result;
    }

    private File getCacheDir(Context context) {

        File file = context.getExternalCacheDir();

        if(TextUtils.isEmpty(targetDir)){
            targetDir = "dkk";
        }
        File file1 = new File(file,targetDir);
        if(!file1.mkdirs() && (!file1.exists() || !file1.isDirectory())){
            return null;
        }

        try {

            String str = file1.getAbsolutePath() +"/"+ UUID.randomUUID()+".jpg";
            return new File(str);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
