package com.abu.timermanager.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @date: 2019/7/10 15:25
 * @author: 董长峰
 * @blog: https://www.jianshu.com/u/04a705fae99b
 * @description:
 */
public class MultistageProgress extends View {
    private Paint backgroundPaint, progressPaint, linePaint;//背景和进度条画笔
    private Rect progressRect = new Rect();//进度条;
    private Rect backgroundRects[];//背景矩形区域
    private float weights[];//每个区域的权重
    private int colors[];//颜色
    private float totalWeight;//总的权重
    private float progress = 10, maxProgress = 100;//进度和最大进度
    private OnProgressChangeListener listener;


    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
        onProgressChange();
    }

    private void onProgressChange() {
        if (listener != null) {
            int position = 0;
            int currentWidth = (int) getWidthForWeight(getProgress(), getMaxProgress());
            int tmpWidth = 0;
            for (int i = 0; i < weights.length; i++) {
                tmpWidth += (int) getWidthForWeight(weights[i], totalWeight);
                if (tmpWidth >= currentWidth) {
                    position = i;
                    break;
                }
            }
            listener.onProgressChange(getProgress(), position);
        }
    }

    public float getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(float maxProgress) {
        this.maxProgress = maxProgress;
        invalidate();
    }

    public OnProgressChangeListener getProgressChangeListener() {
        return listener;
    }

    public void setProgressChangeListener(OnProgressChangeListener listener) {
        this.listener = listener;
    }

    public MultistageProgress(Context context) {
        super(context);
        init();
    }

    public MultistageProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MultistageProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MultistageProgress(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    public void init() {
        backgroundPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        backgroundPaint.setColor(Color.RED);
        progressPaint = new Paint();
        progressPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        progressPaint.setColor(Color.parseColor("#d9d9d9"));
        linePaint = new Paint();
        linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        linePaint.setColor(Color.parseColor("#e7eaf0"));
        linePaint.setStrokeWidth(2);
       // setColors(DEF_COLORS, DEF_WEIGHTS);

    }


    /**
     * 设置每一段的颜色以及对应的权重
     *
     * @param colors
     * @param weights
     */
    public void setColors(int[] colors, float weights[]) {
        if (colors == null || weights == null) {
            throw new NullPointerException("colors And weights must be not null");
        }
        if (colors.length != weights.length) {
            throw new IllegalArgumentException("colors And weights length must be same");
        }
        backgroundRects = new Rect[colors.length];
        this.colors = colors;
        this.weights = weights;
        totalWeight = 0;
        for (int i = 0; i < weights.length; i++) {
            totalWeight += weights[i];
            backgroundRects[i] = new Rect();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (backgroundRects == null) {
            return;
        }
        if (maxProgress <= 0) {
            maxProgress = getWidth();
        }
        //绘制背景颜色块
        int x = 0, y = getHeight();
        int progressX = (int) getWidthForWeight(progress, maxProgress);
        for (int i = 0; i < colors.length; i++) {
            Rect rect = backgroundRects[i];
            backgroundPaint.setColor(colors[i]);
            int width = (int) (getWidthForWeight(weights[i], totalWeight));
            rect.set(x, 0, x + width, y);
            x += width;//计算下一个的开始位置
            canvas.drawRect(rect, backgroundPaint);//绘制矩形
        }
        progressRect.set(0, 0, progressX, getHeight());//设置进度条区域
        canvas.drawRect(progressRect, progressPaint);//绘制进度条
        for (int i = 0, lineX = 0; i < colors.length; i++) {
            int width = (int) (getWidthForWeight(weights[i], totalWeight));
            //绘制矩形块之间的分割线
            lineX = lineX + width;
            if (lineX < progressX) {//给已经走过了的区域画上竖线
                canvas.drawLine(lineX, 0, lineX, getHeight(), linePaint);
            }
        }


    }

    /**
     * 根据权重获取对应的宽度
     *
     * @param weight
     * @param totalWeight
     * @return
     */
    public float getWidthForWeight(float weight, float totalWeight) {
        return getWidth() * (weight / totalWeight) + 0.5f;
    }


    ObjectAnimator valueAnimator;
    @Override
    protected void onDetachedFromWindow() {
        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.cancel();
        }
        super.onDetachedFromWindow();
    }

    public interface OnProgressChangeListener {
        /**
         * 进度改变时触发
         * @param progress 进度
         * @param position 所在区间段
         */
        void onProgressChange(float progress, int position);
    }

}
