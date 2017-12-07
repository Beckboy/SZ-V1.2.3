package com.junhsue.ksee.file;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * Created by Sasa on 2016/7/29.
 */

public class ImageLoaderOptions {


    public static DisplayImageOptions option(int defultImg) {

        return new DisplayImageOptions.Builder()

                .showStubImage(defultImg)
                .showImageForEmptyUri(defultImg)
                //.displayer(new FadeInBitmapDisplayer(500))
                .cacheInMemory().cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();

    }

    /**
     * 带圆角option
     * @param defultImg
     * @param roundedValue 圆角值
     * @return
     */
    public static DisplayImageOptions optionRounded(int defultImg, int roundedValue) {

        return new DisplayImageOptions.Builder()

                .showStubImage(defultImg)
                .showImageForEmptyUri(defultImg)
                //.displayer(new FadeInBitmapDisplayer(500))
                .cacheInMemory().cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).displayer(new RoundedBitmapDisplayer(roundedValue)).build();


    }
}
