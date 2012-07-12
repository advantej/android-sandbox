package com.advantej.mobile.android.tests;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import com.foo.R;

/**
 * Created by IntelliJ IDEA.
 * User: Tejas
 * Date: 6/13/12
 * Time: 6:12 PM
 */
public class CustomView extends View
{

    private float mLastTouchX;
    private float mLastTouchY;

    private float mBearing;


    private Drawable mShareWheelBitmap;
    private GestureDetector mGestureDetector;


    public CustomView(Context context)
    {
        super(context);
        initCustomView();
    }

    public CustomView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initCustomView();
    }

    public CustomView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initCustomView();
    }

    protected void initCustomView()
    {
        setFocusable(true);

        Resources r = this.getResources();
        mShareWheelBitmap = r.getDrawable(R.drawable.share_wheel_outer);
        mShareWheelBitmap.setBounds(0, 0, mShareWheelBitmap.getIntrinsicWidth(), mShareWheelBitmap.getIntrinsicHeight());

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int measuredHeight = measureHeight(heightMeasureSpec);
        int measuredWidth = measureWidth(widthMeasureSpec);

        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    private int measureHeight(int heightMeasureSpec)
    {
        return mShareWheelBitmap.getIntrinsicHeight();
    }

    private int measureWidth(int widthMeasureSpec)
    {
        return mShareWheelBitmap.getIntrinsicHeight();
    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        int mMeasuredWidth = getMeasuredWidth();
        int mMeasuredHeight = getMeasuredHeight();

        int px = mMeasuredWidth / 2;
        int py = mMeasuredHeight / 2;

        canvas.save();

        //canvas.translate(mMeasuredWidth - mShareWheelBitmap.getIntrinsicWidth(), mMeasuredHeight - mShareWheelBitmap.getIntrinsicHeight());
        canvas.rotate(mBearing, px, py);
        mShareWheelBitmap.draw(canvas);

        canvas.restore();
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        final int action = ev.getAction();
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
            {
                final float x = ev.getX();
                final float y = ev.getY();

                // Remember where we started
                mLastTouchX = x;
                mLastTouchY = y;

                break;
            }

            case MotionEvent.ACTION_MOVE:
            {
                final float x = ev.getX();
                final float y = ev.getY();

                // Calculate the distance moved
                final float dx = x - mLastTouchX;
                final float dy = y - mLastTouchY;

                //mBearing = (float) calculateBearing(dx, dy, x, y);

                if (y > getMeasuredHeight() / 2)
                {
                    mBearing += -dx;
                } else
                {
                    mBearing += dx;
                }

                // Remember this touch position for the next move event
                mLastTouchX = x;
                mLastTouchY = y;

                // Invalidate to request a redraw
                invalidate();
                break;
            }
        }

        return true;
    }

    private double calculateBearing(float dx, float dy, float x, float y)
    {
        double a=Math.atan2(dy,dx);

        float dpx= mLastTouchX - x;
        float dpy= mLastTouchY - y;
        double b=Math.atan2(dpy, dpx);

        double diff  = a-b;

        return -Math.toDegrees(diff);
    }


}
