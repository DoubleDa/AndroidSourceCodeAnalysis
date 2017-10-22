package com.dyx.asca.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.dyx.asca.R;
import com.dyx.asca.utils.DensityUtils;
import com.orhanobut.logger.Logger;

import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.OkHttpClient;

/**
 * Author：dayongxin
 * Function：
 */
public class GlideActivity extends AppCompatActivity {
    @BindView(R.id.iv_glide)
    ImageView ivGlide;
    @BindView(R.id.pb_glide)
    ProgressBar pbGlide;
    private Unbinder mUnbinder;
    private static final String URL_IMG = "http://oqle0m5m6.bkt.clouddn.com/IMG_0265.JPG";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide);
        mUnbinder = ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        /**
         * 1、基本用法
         */
//        Glide.with(this).load(URL_IMG).into(ivGlide);
        /**
         * 2、修剪大小
         */
//        Glide.with(GlideActivity.this).load(URL_IMG).override(DensityUtils.dip2px(this, 200), DensityUtils.dip2px(this, 300)).into(ivGlide);
        Glide.with(this).load(URL_IMG).asBitmap().override(DensityUtils.dip2px(this, 200), DensityUtils.dip2px(this, 300)).error(R.drawable.ic_error).placeholder(R.drawable.ic_placeholder).into(ivGlide);
        /**
         * 3、设置占位图和错误图
         */
//        Glide.with(this).load(URL_IMG).placeholder(R.drawable.ic_placeholder).error(R.drawable.ic_error).into(ivGlide);
        /**
         * 4、裁剪图片
         */
//        Glide.with(this).load(URL_IMG).centerCrop().into(ivGlide);
        /**
         * 5、设置一个方向的大小
         */
//        Glide.with(this).load(URL_IMG).override(DensityUtils.dip2px(this, 200), Target.SIZE_ORIGINAL).into(ivGlide);
        /**
         * 6、设置centerCrop
         */
//        Glide.with(this).load(URL_IMG).override(DensityUtils.dip2px(this, 200), DensityUtils.dip2px(this, 300)).centerCrop().into(ivGlide);
        /**
         * 7、fitCenter
         */
//        Glide.with(this).load(URL_IMG).override(DensityUtils.dip2px(this, 200), DensityUtils.dip2px(this, 300)).fitCenter().into(ivGlide);
        /**
         * 8、加载错误
         */
        //loadingErrors();
        /**
         * 9、Glide转换
         */
        //glideSimpleTransformations();
        //glidemultipleTransformations();
        /**
         * 10、设置圆角
         */
        pbGlide.setVisibility(View.GONE);
//        Glide.with(this).load(URL_IMG).bitmapTransform(new RoundedCornersTransformation(this, 60, 10)).into(ivGlide);
        /**
         * 11、修剪
         */
//        Glide.with(this).load(URL_IMG).bitmapTransform(new CropCircleTransformation(this)).into(ivGlide);
        /**
         * 12、各种效果
         */
        //Blur
//        Glide.with(this).load(URL_IMG).bitmapTransform(new BlurTransformation(this)).into(ivGlide);
        //复杂的转换
//        Glide.with(this).load(URL_IMG).bitmapTransform(new BlurTransformation(this, 25), new CropCircleTransformation(this)).into(ivGlide);
        /**
         * 13、显示进度条
         */
        //showProgressBarWithGlide();
        /**
         * 14、动态的调整图片大小
         */
//        AdjustingImageSizeDynamically();
        /**
         * 15、网络：默认情况下Glide使用Volley作为网络请求框架
         */
        /**
         * 16、网络：使用OkHttp
         */
        //useOkHttpWithGlide();
    }

    private void useOkHttpWithGlide() {
        OkHttpClient client = new OkHttpClient();
        Glide.get(getApplicationContext()).register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(client));
        /**
         * 加载图片
         */
        Glide.with(this).load(URL_IMG).into(ivGlide);
    }

    private void AdjustingImageSizeDynamically() {
        pbGlide.setVisibility(View.GONE);
        Glide.with(this).load(URL_IMG).asBitmap().into(target);
//        Glide.with(this).load(URL_IMG).into(new SimpleTarget<GlideDrawable>() {
//            @Override
//            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
//
//            }
//        });
    }

    private SimpleTarget target = new SimpleTarget<Bitmap>() {

        @Override
        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
            ivGlide.setImageBitmap(resource);
        }
    };

    private void showProgressBarWithGlide() {
        pbGlide.setVisibility(View.VISIBLE);
        Glide.with(this).load(URL_IMG).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                pbGlide.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                pbGlide.setVisibility(View.GONE);
                return false;
            }
        }).into(ivGlide);
    }

    private void glidemultipleTransformations() {
//        MultiTransformation multi = new MultiTransformation(
//                new BlurTransformation(25),
//                new RoundedCornersTransformation(128, 0, RoundedCornersTransformation.CornerType.BOTTOM))))
//        Glide.with(this).load(URL_IMG)
//                .apply(bitmapTransform(multi))
//                .into((ImageView) findViewById(R.id.image));
    }

    private void glideSimpleTransformations() {
//        Glide.with(this).load(URL_IMG).apply(bitmapTransform(new BlurTransformation(25))).into(ivGlide);
    }

    private void loadingErrors() {
        Glide.with(this).load(URL_IMG).placeholder(R.drawable.ic_placeholder).error(R.drawable.ic_error).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                Logger.d("Glide Image Loading Error:" + e);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                return false;
            }
        }).into(ivGlide);
    }
}
