package com.advantej.mobile.android.tests.YACV;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by IntelliJ IDEA.
 * User: Tejas
 * Date: 2/11/13
 * Time: 9:34 AM
 */
public class CustomView extends ViewGroup
{
    public static final int ORIENTATION_HORIZONTAL_LEFT_RIGHT = 0;
    public static final int ORIENTATION_HORIZONTAL_RIGHT_LEFT = 1;
    public static final int ORIENTATION_VERTICAL_TOP_DOWN = 2;
    public static final int ORIENTATION_VERTICAL_BOTTOM_UP = 3;

    private final int mHandleId;
    private final int mContentId;

    private View mHandle;
    private View mContent;
    private int mOrientation;

    public CustomView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        //See attrs.xml
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomView, defStyle, 0);

        int handleId = typedArray.getResourceId(R.styleable.CustomView_handle, 0);
        int contentId = typedArray.getResourceId(R.styleable.CustomView_content, 0);
        mOrientation = typedArray.getInt(R.styleable.CustomView_orientation, 2);

        mHandleId = handleId;
        mContentId = contentId;
        typedArray.recycle();

        setAlwaysDrawnWithCacheEnabled(true);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        final int width = r - l;
        final int height = b - t;

        final View handle = mHandle;
        final View content = mContent;

        int handleWidth = handle.getMeasuredWidth();
        int handleHeight = handle.getMeasuredHeight();

        int contentWidth = content.getMeasuredWidth();
        int contentHeight = content.getMeasuredHeight();

        int contentLeft = 0;
        int contentTop = 0;

        int handleTop = 0;
        int handleLeft = 0;

        switch (mOrientation)
        {
            case ORIENTATION_HORIZONTAL_LEFT_RIGHT:
                contentLeft = contentTop = 0;
                handleTop = (height - handleHeight) / 2;
                handleLeft = contentWidth;
                if (mContent.getVisibility() == View.GONE)
                    handleLeft = 0;


                break;

            case ORIENTATION_HORIZONTAL_RIGHT_LEFT:
                contentTop = 0;
                contentLeft = handleWidth;
                handleTop = (height - handleHeight) / 2;
                handleLeft = 0;
                if (mContent.getVisibility() == View.GONE)
                    handleLeft = width - handleWidth;

                break;

            case ORIENTATION_VERTICAL_TOP_DOWN:
                contentLeft = contentTop = 0;
                handleTop = contentHeight;
                handleLeft = (width - handleWidth) / 2;
                if(mContent.getVisibility() == View.GONE)
                    handleTop = 0;

                break;

            case ORIENTATION_VERTICAL_BOTTOM_UP:
                contentTop = handleHeight;
                contentLeft = 0;
                handleTop = 0;
                handleLeft = (width - handleWidth) / 2;
                if(mContent.getVisibility() == View.GONE)
                    handleTop = height - handleHeight;

                break;

        }

        handle.layout(handleLeft, handleTop, handleLeft + handleWidth, handleTop + handleHeight);
        content.layout(contentLeft, contentTop, contentLeft + contentWidth, contentTop + contentHeight);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.UNSPECIFIED || heightSpecMode == MeasureSpec.UNSPECIFIED)
        {
            throw new RuntimeException("SlidingDrawer cannot have UNSPECIFIED dimensions");
        }

        final View handle = mHandle;
        measureChild(handle, widthMeasureSpec, heightMeasureSpec);

        switch (mOrientation)
        {
            case ORIENTATION_VERTICAL_TOP_DOWN:
            case ORIENTATION_VERTICAL_BOTTOM_UP:
                int height = heightSpecSize - handle.getMeasuredHeight();
                mContent.measure(MeasureSpec.makeMeasureSpec(widthSpecSize, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
                break;

            case ORIENTATION_HORIZONTAL_LEFT_RIGHT:
            case ORIENTATION_HORIZONTAL_RIGHT_LEFT:
                int width = widthSpecSize - handle.getMeasuredWidth();
                mContent.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(heightSpecSize, MeasureSpec.EXACTLY));
                break;
        }

        setMeasuredDimension(widthSpecSize, heightSpecSize);
    }

    @Override
    protected void onFinishInflate()
    {
        mHandle = findViewById(mHandleId);
        if (mHandle == null) {
            throw new IllegalArgumentException("The handle attribute is must refer to an"
                    + " existing child.");
        }

        mContent = findViewById(mContentId);
        if (mContent == null) {
            throw new IllegalArgumentException("The content attribute is must refer to an"
                    + " existing child.");
        }

        mContent.setVisibility(View.GONE);

        mHandle.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                toggle();
            }
        });
    }

    public void toggle()
    {
        int visibility = mContent.getVisibility() == View.GONE ? View.VISIBLE : View.GONE;
        mContent.setVisibility(visibility);
        invalidate();
        requestLayout();
    }
}
