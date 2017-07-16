package com.aman.teenscribblers.galgotiasuniversitymsim.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.BoringLayout;
import android.text.Layout;
import android.util.AttributeSet;

import com.aman.teenscribblers.galgotiasuniversitymsim.R;

/**
 * Created by amankapoor on 14/07/17.
 */

public class GradientTextView extends android.support.v7.widget.AppCompatTextView {

    private BoringLayout boringLayout;
    private Shader textShader;
    private int startColor;
    private int endColor;

    public GradientTextView(Context context) {
        super(context);
        init();
    }

    public GradientTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GradientTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        String text = getText().toString();
        startColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        endColor = ContextCompat.getColor(getContext(), R.color.colorPrimaryDark);
        final float textSize = getTextSize();
        getPaint().setTextSize(textSize);
        BoringLayout.Metrics boringMetrics = BoringLayout.isBoring(text, getPaint());
        boringLayout = new BoringLayout(text, getPaint(), 0, Layout.Alignment.ALIGN_NORMAL,
                0.0f, 0.0f, boringMetrics, true);
    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        setMeasuredDimension((int) getPaint().measureText(getText().toString()), (int) getPaint().getFontSpacing());
//    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //Assumes bottom and top are colors defined above
        textShader = new LinearGradient(0, 0, 0, h * 0.7f,
                new int[]{startColor, endColor},
                new float[]{0, 1}, Shader.TileMode.CLAMP);
        getPaint().setShader(textShader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        boringLayout.draw(canvas);
    }
}
