package com.img.dkk.impressimg;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.img.dkk.impressimg.engine.ImgParamBean;
import com.img.dkk.impressimg.engine.ImgCompress;
import com.img.dkk.impressimg.engine.OnCompressListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> originPhotos = new ArrayList<>();
    private RecyclerView rv_vh;
    TextView tv_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv_vh = findViewById(R.id.rv_vh);
        tv_button = findViewById(R.id.tv_button);
        initPermission();
        assetsToFiles();
        tv_button.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startCompress(originPhotos);
            }
        });
    }

    public void startCompress(List<String> imgList){

        List<ImgParamBean> imgParamBeanListss = ImgCompress.with(this)
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

        rv_vh.setLayoutManager(new LinearLayoutManager(this));

        rv_vh.setAdapter(new MyAdapter(this,imgParamBeanListss));

    }

    private List<File> assetsToFiles() {
        final List<File> files = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            try {
                InputStream is = getResources().getAssets().open("img_" + i);
                File file = new File(getExternalFilesDir(null), "test_" + i);
                FileOutputStream fos = new FileOutputStream(file);

                byte[] buffer = new byte[4096];
                int len = is.read(buffer);
                while (len > 0) {
                    fos.write(buffer, 0, len);
                    len = is.read(buffer);
                }
                fos.close();
                is.close();

                files.add(file);
                originPhotos.add(file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return files;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x0001);
        }
    }

    public void refresh(View view) {
        startCompress(originPhotos);
    }


}
