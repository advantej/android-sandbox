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
public class AnotherCustomView extends ViewGroup
{

    private static float mLeftViewRightPadding;
    private static float mRightViewLeftPadding;

    private final int mLeftViewId;
    private final int mMainViewId;
    private final int mRightViewId;

    private View mLeftView;
    private View mMainView;
    private View mRightView;

    public static final int CONFIG_MAIN_ONLY = 0;
    public static final int CONFIG_LEFT_OPEN = 1;
    public static final int CONFIG_RIGHT_OPEN = 2;
    private int mCurrentConfiguration = CONFIG_MAIN_ONLY;

    public AnotherCustomView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public AnotherCustomView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        //See attrs.xml
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AnotherCustomView, defStyle, 0);

        int leftViewId = typedArray.getResourceId(R.styleable.AnotherCustomView_left_view, 0);
        int mainViewId = typedArray.getResourceId(R.styleable.AnotherCustomView_main_view, 0);
        int rightViewId = typedArray.getResourceId(R.styleable.AnotherCustomView_right_view, 0);
        float leftViewPadding = typedArray.getDimension(R.styleable.AnotherCustomView_left_view_padding, 20.0f);
        float rightViewPadding = typedArray.getDimension(R.styleable.AnotherCustomView_right_view_padding, 20.0f);

        mLeftViewId = leftViewId;
        mMainViewId = mainViewId;
        mRightViewId = rightViewId;

        mLeftViewRightPadding = leftViewPadding;
        mRightViewLeftPadding = rightViewPadding;

        typedArray.recycle();

        setAlwaysDrawnWithCacheEnabled(true);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        final int width = r - l;
        final int height = b - t;

        final View leftView = mLeftView;
        final View mainView = mMainView;
        final View rightView = mRightView;

        int leftViewWidth = leftView.getMeasuredWidth();
        int leftViewHeight = leftView.getMeasuredHeight();

        int mainViewWidth = mainView.getMeasuredWidth();
        int mainViewHeight = mainView.getMeasuredHeight();

        int rightViewWidth = rightView.getMeasuredWidth();
        int rightViewHeight = rightView.getMeasuredHeight();

        int leftViewLeft = 0;
        int leftViewTop = 0;

        int mainViewLeft = 0;
        int mainViewTop = 0;

        int rightViewLeft = 0;
        int rightViewTop = 0;


        switch (mCurrentConfiguration)
        {
            case CONFIG_MAIN_ONLY:
                break;
            case CONFIG_LEFT_OPEN:
                mainViewLeft = leftViewWidth;
                break;
            case CONFIG_RIGHT_OPEN:
                rightViewLeft = (int) mRightViewLeftPadding;
                mainViewLeft = - rightViewWidth;
                break;
        }

        leftView.layout(leftViewLeft, leftViewTop, leftViewLeft + leftViewWidth, leftViewTop + leftViewHeight);
        mainView.layout(mainViewLeft, mainViewTop, mainViewLeft + mainViewWidth, mainViewTop + mainViewHeight);
        rightView.layout(rightViewLeft, rightViewTop, rightViewLeft + rightViewWidth, rightViewTop + rightViewHeight);

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
            throw new RuntimeException("View cannot have UNSPECIFIED dimensions");
        }

        mMainView.measure(widthMeasureSpec, heightMeasureSpec);

        int leftViewWidth = (int) (widthSpecSize - mLeftViewRightPadding);
        mLeftView.measure(MeasureSpec.makeMeasureSpec(leftViewWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(heightSpecSize, MeasureSpec.EXACTLY));

        int rightViewWidth = (int) (widthSpecSize - mRightViewLeftPadding);
        mRightView.measure(MeasureSpec.makeMeasureSpec(rightViewWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(heightSpecSize, MeasureSpec.EXACTLY));

        setMeasuredDimension(widthSpecSize, heightSpecSize);
    }

    @Override
    protected void onFinishInflate()
    {
        mLeftView = findViewById(mLeftViewId);
        if (mLeftView == null) {
            throw new IllegalArgumentException("The left_view attribute is must refer to an"
                    + " existing child.");
        }

        mMainView = findViewById(mMainViewId);
        if (mMainView == null) {
            throw new IllegalArgumentException("The main_view attribute is must refer to an"
                    + " existing child.");
        }

        mRightView = findViewById(mRightViewId);
        if (mRightView == null) {
            throw new IllegalArgumentException("The right_view attribute is must refer to an"
                    + " existing child.");
        }
        //TMP
        mCurrentConfiguration = CONFIG_LEFT_OPEN;
        doConfig();
    }

    public void setConfig(int config)
    {
        mCurrentConfiguration = config;
        doConfig();
        invalidate();
        requestLayout();
    }

    public int getCurrentConfiguration()
    {
        return mCurrentConfiguration;
    }


    private void doConfig()
    {
        mMainView.setVisibility(View.VISIBLE);
        switch (mCurrentConfiguration)
        {
            case CONFIG_MAIN_ONLY:
                mLeftView.setVisibility(View.GONE);
                mRightView.setVisibility(View.GONE);
                break;
            case CONFIG_LEFT_OPEN:
                mLeftView.setVisibility(View.VISIBLE);
                mRightView.setVisibility(View.GONE);
                break;
            case CONFIG_RIGHT_OPEN:
                mLeftView.setVisibility(View.GONE);
                mRightView.setVisibility(View.VISIBLE);
                break;
        }
    }
}
