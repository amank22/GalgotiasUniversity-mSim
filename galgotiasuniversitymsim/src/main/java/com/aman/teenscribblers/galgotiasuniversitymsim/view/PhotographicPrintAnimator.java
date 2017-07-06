package com.aman.teenscribblers.galgotiasuniversitymsim.view;

/**
 * Created by amankapoor on 07/07/17.
 */

import android.animation.ValueAnimator;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import java.lang.ref.SoftReference;

public class PhotographicPrintAnimator {

    SoftReference<ImageView> mView;
    final ColorMatrix mColorMatrix;
    final float[] mBrightness = new float[]
            {
                    1, 0, 0, 0, 0,
                    0, 1, 0, 0, 0,
                    0, 0, 1, 0, 0,
                    0, 0, 0, 1, 0
            };

    final ColorMatrix mBrightnessColorMatrix;
    final Interpolator mInterpolator;

    public PhotographicPrintAnimator() {
        this(null);
    }

    public PhotographicPrintAnimator(ImageView v) {
        setView(v);
        mColorMatrix = new ColorMatrix();
        mBrightnessColorMatrix = new ColorMatrix();
        mInterpolator = new AccelerateDecelerateInterpolator();
    }

    public PhotographicPrintAnimator setView(ImageView v) {
        mView = new SoftReference<ImageView>(v);
        return this;
    }

    private int duration;
    private int saturationDuration = -1;
    private int brightnessDuration = -1;
    private int alphaDuration = -1;

    public PhotographicPrintAnimator setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public PhotographicPrintAnimator setSaturationDuration(int duration) {
        this.saturationDuration = duration;
        return this;
    }

    public PhotographicPrintAnimator setBrightnessDuration(int duration) {
        this.brightnessDuration = duration;
        return this;
    }

    public PhotographicPrintAnimator setAlphaDuration(int duration) {
        this.alphaDuration = duration;
        return this;
    }

    public int getDuration() {
        return duration;
    }

    public int getSaturationDuration() {
        return this.saturationDuration;
    }

    public int getBrightnessDuration() {
        return this.brightnessDuration;
    }

    public int getAlphaDuration() {
        return this.alphaDuration;
    }

    public void start() {

        final ValueAnimator v = ValueAnimator.ofFloat(0f, 1f);
        final ValueAnimator v2 = ValueAnimator.ofFloat(0f, 1f);
        final ValueAnimator v3 = ValueAnimator.ofFloat(0f, 1f);

        v.setInterpolator(mInterpolator);
        v.setDuration((saturationDuration != -1) ? saturationDuration : duration);
        v.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mColorMatrix.setSaturation(animation.getAnimatedFraction());

                if (mView.get() != null) {
                    mView.get().setColorFilter(new ColorMatrixColorFilter(mColorMatrix));
                } else {
                    v.cancel();
                    v2.cancel();
                    v3.cancel();
                    //mView.get().clearColorFilter(); // neccessary?
                }
            }
        });

        v2.setInterpolator(mInterpolator);
        v2.setDuration((brightnessDuration != -1) ? brightnessDuration : (int) (duration * 0.75));
        v2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float brightness = (1f - animation.getAnimatedFraction()) * 100;
                mBrightness[4] = brightness;
                mBrightness[9] = brightness;
                mBrightness[14] = brightness;
                mBrightnessColorMatrix.set(mBrightness);
                mColorMatrix.postConcat(mBrightnessColorMatrix);

                if (mView.get() != null) {
                    mView.get().setColorFilter(new ColorMatrixColorFilter(mColorMatrix));
                } else {
                    v.cancel();
                    v2.cancel();
                    v3.cancel();
                    //mView.get().clearColorFilter(); // neccessary?
                }
            }
        });

        v3.setInterpolator(mInterpolator);
        v3.setDuration((alphaDuration != -1) ? alphaDuration : (int) (duration / 2));
        v3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = animation.getAnimatedFraction();
                if (mView.get() != null) {
                    mView.get().setAlpha(alpha);
                } else {
                    v.cancel();
                    v2.cancel();
                    v3.cancel();
                }
            }
        });

        if (mView.get() != null) {
            v.start();
            v2.start();
            v3.start();
        }
    }
}