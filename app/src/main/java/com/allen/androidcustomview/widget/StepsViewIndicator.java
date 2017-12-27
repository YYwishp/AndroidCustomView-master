package com.allen.androidcustomview.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;


public class StepsViewIndicator extends View {


    private Paint bgPaint;
    private Paint progressPaint;

    private Paint textPaint;

    private int mWidth;
    private int mHeight;

    private int mViewHeight;
    /**
     * 进度
     */
    private float total;
    private float mProgress;
    private float textHeight; //描述文字的高度
    private float textWidth; //描述文字的高度
    private float currentProgress;//当前进度
    private ValueAnimator progressAnimator;//进度动画
    private int duration = 1000;//动画执行时间
    private int startDelay = 500;//动画延时启动时间
    private int progressPaintWidth;//进度条画笔的宽度
    private int progressHeight;
    private int progressMarginTop;//进度条距离提示框的高度
    private float moveDis;//进度移动的距离

    private Rect textRect = new Rect();

    private String textString = "已售0%";
    /**
     * 百分比文字字体大小
     */
    private int textPaintSize;

    /**
     * 进度条背景颜色
     */
    private int bgColor = 0xFFeaeef0;
    /**
     * 进度条颜色
     */
    private int progressColor = 0xFFf66b12;


    private RectF bgRectF = new RectF();
    private RectF progressRectF = new RectF();

    /**
     * 圆角矩形的圆角半径
     */
    private int roundRectRadius;

    /**
     * 进度监听回调
     */
    private ProductProgressBar.ProgressListener progressListener;

    //
    private Paint paint = new Paint();
    private Paint selectedPaint = new Paint();
    private Paint progressTextPaint = new Paint();
    private int mNumOfStep;
    private float mProgrssStrokeWidth;
    private float mLineHeight;
    private float mCircleRadius;
    private float mMargins;
    private int mProgressColor;
    private int mBarColor;
    private int mProgressTextColor;
    private boolean hideProgressText;

    private float mCenterY;
    private float mLineY;
    private List<Float> mThumbContainerXPosition = new ArrayList<>();
    String day[] = {"1", "7", "15", "30"};
    private int mCompletedPosition;
    private OnDrawListener mDrawListener;

    public StepsViewIndicator(Context context) {
        this(context, null);
    }

    public StepsViewIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepsViewIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mLineHeight = mProgrssStrokeWidth;

        /**
         * 初始化画笔宽度及view大小
         */
        progressPaintWidth = dp2px(1);
        progressHeight = dp2px(3);
        roundRectRadius = dp2px(3);
        textPaintSize = sp2px(10);
        textHeight = dp2px(10);
        progressMarginTop = dp2px(4);

        //view真实的高度
        mViewHeight = (int) (textHeight + progressMarginTop + progressPaintWidth * 2 + progressHeight);

        initPaint();
        initTextPaint();
    }

    public void setStepTotal(int size) {
        mNumOfStep = size;
        invalidate();
    }

    public void setDrawListener(OnDrawListener drawListener) {
        mDrawListener = drawListener;
    }

    public List<Float> getThumbContainerXPosition() {
        return mThumbContainerXPosition;
    }


    private void initPaint() {
        bgPaint = getPaint(progressPaintWidth, bgColor, Paint.Style.FILL);
        progressPaint = getPaint(progressPaintWidth, progressColor, Paint.Style.FILL);
    }

    /**
     * 初始化文字画笔
     */
    private void initTextPaint() {
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textPaintSize);
        textPaint.setColor(progressColor);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
    }


    /**
     * 统一处理paint
     *
     * @param strokeWidth 画笔宽度
     * @param color       颜色
     * @param style       风格
     * @return paint
     */
    private Paint getPaint(int strokeWidth, int color, Paint.Style style) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStyle(style);
        return paint;
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mThumbContainerXPosition = new ArrayList<>();

        mCenterY = 0.5f * getHeight();
        float mLeftX = mMargins;
        mLineY = mCenterY - (mLineHeight / 2);
        float mRightX = getWidth() - mMargins;
        float mDelta = (mRightX - mLeftX) / (mNumOfStep - 1);

        mThumbContainerXPosition.add(mLeftX);
        for (int i = 1; i < mNumOfStep - 1; i++) {
            mThumbContainerXPosition.add(mLeftX + (i * mDelta));
        }
        mThumbContainerXPosition.add(mRightX);
        Log.v("steps view indicator", mThumbContainerXPosition.toString());
        mDrawListener.onReady();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureDimension(getWidth(), widthMeasureSpec);
        int height = measureDimension(200, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    protected int measureDimension(int defaultSize, int measureSpec) {
        int result = defaultSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(defaultSize, specSize);
        } else {
            result = defaultSize;
        }

        return result;
    }

    public void setCompletedPosition(int position) {
        mCompletedPosition = position;
    }

    public void reset() {
        setCompletedPosition(0);
    }

    public void setProgressColor(int progressColor) {
        mProgressColor = progressColor;
    }

    public void setBarColor(int barColor) {
        mBarColor = barColor;
    }

    public void setProgressTextColor(int textColor) {
        mProgressTextColor = textColor;
    }

    public void setProgressStrokeWidth(float width) {
        mProgrssStrokeWidth = width;
    }

    public void setMargins(float margin) {
        mMargins = margin;
    }

    public void setCircleRadius(float radius) {
        mCircleRadius = radius;
    }


    public void setHideProgressText(boolean hide) {
        hideProgressText = hide;
    }


    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mDrawListener.onReady();
        // bar progress paint
        paint.setAntiAlias(true);
        paint.setColor(mBarColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);

        // progress paint
        selectedPaint.setAntiAlias(true);
        selectedPaint.setColor(mProgressColor);
        selectedPaint.setStyle(Paint.Style.STROKE);
        selectedPaint.setStrokeWidth(1);

        // progress text
        progressTextPaint.setAntiAlias(true);
        progressTextPaint.setTextSize(mCircleRadius);
        progressTextPaint.setColor(mProgressTextColor);
//        progressTextPaint.setTextAlign(Paint.Align.CENTER);

        paint.setStyle(Paint.Style.FILL);
        selectedPaint.setStyle(Paint.Style.FILL);

        // draw lines
        for (int i = 0; i < mThumbContainerXPosition.size() - 1; i++) {
            final float pos = mThumbContainerXPosition.get(i);
            final float pos2 = mThumbContainerXPosition.get(i + 1);
            //float left, float top, float right, float bottom, @NonNull Paint paint
//            canvas.drawRect(pos, mLineY, pos2, mLineY + mProgrssStrokeWidth, (i < mCompletedPosition) ? selectedPaint : paint);
            //
            bgRectF.left = pos;
            bgRectF.top = mLineY;//这里
            bgRectF.right = pos2;
            mWidth += pos2;
            bgRectF.bottom = mLineY + mProgrssStrokeWidth;
            canvas.drawRoundRect(bgRectF, roundRectRadius, roundRectRadius, bgPaint);
            //
        }

        float quarterRadius = mCircleRadius / 4;
        // Draw circles
        for (int i = 1; i < mThumbContainerXPosition.size(); i++) {
            final float pos = mThumbContainerXPosition.get(i);
            //float cx, float cy, float radius, @NonNull Paint paint
            canvas.drawCircle(pos, mCenterY, mCircleRadius, (i <= mCompletedPosition) ? selectedPaint : paint);
            if (!hideProgressText) {
                String d = day[i];
                if (d.length() >= 2) {
                    canvas.drawText(d, pos - quarterRadius - 14, mCenterY + quarterRadius, progressTextPaint);
                } else {
                    canvas.drawText(d, pos - quarterRadius, mCenterY + quarterRadius, progressTextPaint);
                }
            }
            // in current completed position color with alpha
            if (i == mCompletedPosition) {
                selectedPaint.setColor(getColorWithAlpha(mProgressColor, 0.2f));
                canvas.drawCircle(pos, mCenterY, mCircleRadius * 1.3f, selectedPaint);
            }
        }
        progressRectF.left = 0;
        progressRectF.top = mLineY;
        progressRectF.right = currentProgress;
        progressRectF.bottom = mLineY + mProgrssStrokeWidth;
        canvas.drawRoundRect(progressRectF, roundRectRadius, roundRectRadius, progressPaint);


    }

    public StepsViewIndicator setProgress(float progress, float total) {
        mProgress = progress;
        this.total = total;
        initAnimation();
        return this;
    }

    /**
     * 进度移动动画  通过插值的方式改变移动的距离
     */
    private void initAnimation() {

        if (total != 0) {
            progressAnimator = ValueAnimator.ofFloat(mProgress, total);
        } else {
            progressAnimator = ValueAnimator.ofFloat(0, mProgress);
        }


        progressAnimator.setDuration(duration);
        progressAnimator.setStartDelay(startDelay);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                textWidth = textPaint.measureText(textString);
                currentProgress = value * mWidth / 100;
                if (progressListener != null) {
                    progressListener.currentProgressListener(value);
                }
                //移动百分比提示框，只有当前进度到提示框中间位置之后开始移动，当进度框移动到最右边的时候停止移动，但是进度条还可以继续移动
                if (currentProgress >= textWidth && currentProgress <= mWidth) {
                    moveDis = currentProgress - textWidth;
                }
                invalidate();
            }
        });
        if (!progressAnimator.isStarted()) {
            progressAnimator.start();
        }
    }


    public static int getColorWithAlpha(int color, float ratio) {
        int newColor = 0;
        int alpha = Math.round(Color.alpha(color) * ratio);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        newColor = Color.argb(alpha, r, g, b);
        return newColor;
    }

    public interface OnDrawListener {
        void onReady();
    }

    /**
     * dp 2 px
     *
     * @param dpVal
     */
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getResources().getDisplayMetrics());
    }

    /**
     * sp 2 px
     *
     * @param spVal
     * @return
     */
    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, getResources().getDisplayMetrics());
    }
}
