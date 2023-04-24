package com.hjq.demo.ui.utils;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import com.hjq.demo.R;


public class WpkHintFlyEditText extends AppCompatEditText {
    /**
     * 绘制hint画笔
     **/
    private Paint mPaint;

    /**
     * hint坐标
     **/
    private float mX, mY;

    /**
     * 目标倍数和当前进度
     **/
    private float mMultiple = 0.5f, mProgress = 1f;

    private float mTextSize = 32f;

    private int mTextColor = 0xffA8B2BD;

    private int mCenterHintColor = 0xFF757575;
    private int mTopHintColor = 0xFF757575;

    private String mHint = "Password";

    public WpkHintFlyEditText(Context context) {
        super(context);
        init(context, null);
    }

    public WpkHintFlyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public WpkHintFlyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.WpkHintFlyEditText);
            mMultiple = ta.getFloat(R.styleable.WpkHintFlyEditText_multiple, mMultiple);
            ta.recycle();
        }
        if (getHint() != null && !"".equals(getHint().toString())) {
            mHint = getHint().toString();
            setHint("");
        }
        mTextSize = getTextSize();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mCenterHintColor);
        mPaint.setTypeface(UserProductUtils.Companion.getFont(UserProductUtils.GOTHAM_BOOK));
        mX = getPaddingStart();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!"".equals(getText().toString())) {
            mProgress = mMultiple;
        }
        mPaint.setTextSize(mTextSize * mProgress);
        Paint.FontMetricsInt fmi = mPaint.getFontMetricsInt();
        mY = (getHeight() - fmi.bottom - fmi.top) / 2 * mProgress;
        canvas.drawText(mHint, mX + getScrollX(), mY, mPaint);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused && "".equals(getText().toString())) {
            moveHint(1f, mMultiple);
        } else if (!focused && "".equals(getText().toString())) {
            moveHint(mMultiple, 1f);
        }
    }

    /**
     * 移动hint
     */
    public void moveHint(float from, float to) {
        ValueAnimator anim = ValueAnimator.ofFloat(from, to).setDuration(200);
        anim.addUpdateListener(animation -> {
            mProgress = (Float) animation.getAnimatedValue();
            invalidate();
        });
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                //
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (from > to) {
                    mTextColor = mTopHintColor;
                } else {
                    mTextColor = mCenterHintColor;
                }
                mPaint.setColor(mTextColor);
                invalidate();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                //
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                //
            }
        });
        anim.start();
    }

    public void setHintTextColor(int mCenterHintColor, int mTopHintColor) {
        this.mCenterHintColor = mCenterHintColor;
        this.mTopHintColor = mTopHintColor;
    }

    public void setHintText(String text) {
        mHint = text;
        moveHint(mMultiple, 1f);
        invalidate();
    }

}
