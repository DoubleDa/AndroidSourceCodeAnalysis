package com.dyx.asca;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.GlideModule;

/**
 * Author：dayongxin
 * Function：
 */
public class GlideConfigor implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        /**
         * 1、在这里为构建器应用选项；
         * 2、Glide默认位图格式被设置为rgb565，因为与argb8888相比它只消耗了50%的内存；
         */
        //为了保证图片质量增加内存消耗
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        /**
         * 注册ModelLoaders
         */
    }
}
