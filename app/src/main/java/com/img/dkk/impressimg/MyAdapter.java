package com.img.dkk.impressimg;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.img.dkk.impressimg.engine.ImgParamBean;
import java.util.List;

/**
 * Created by  dingkangkang on 2019/9/26
 * email：851615943@qq.com
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    private Context mContext;
    private List<ImgParamBean> imgParamBeanList;

    public MyAdapter(Context mContext,
        List<ImgParamBean> imgParamBeanList) {
        this.mContext = mContext;
        this.imgParamBeanList = imgParamBeanList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layoutitem,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(ViewHolder viewHolder, int i) {


        viewHolder.tv_original.setText("图片原来大小："+this.imgParamBeanList.get(i).getOriginalLen() +" byte");
        viewHolder.tv_compress.setText("图片压缩后大小："+this.imgParamBeanList.get(i).getCompressLen()+ " byte");

        Glide.with(this.mContext).load(this.imgParamBeanList.get(i).getFile()).into(viewHolder.iv_compress);

    }

    @Override public int getItemCount() {
        return imgParamBeanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_original,tv_compress;
        ImageView iv_compress;
        public ViewHolder( View itemView) {
            super(itemView);
            tv_original = itemView.findViewById(R.id.tv_original);
            tv_compress = itemView.findViewById(R.id.tv_compress);
            iv_compress = itemView.findViewById(R.id.iv_compress);

        }
    }
}
