package com.bruce.expandablelayoutlib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.LinearLayout;

/**
 * 向下扩展布局
 * Created by Bruce on 2017/3/14.
 */
public class ExpandableLayout extends LinearLayout{

    private static final String TAG = ExpandableLayout.class.getSimpleName();

    private static final int DEFAULT_DURATION = 300;

    private boolean mExpandWithParentScroll;
    private boolean mExpandScrollTogether;
    private int mExpandState;
    private ScrolledParent mScrolledParent;

    private int mExpandDuration = DEFAULT_DURATION;
    private ValueAnimator mExpandAnimator;
    private ValueAnimator mParentAnimator;
    private AnimatorSet mExpandScrollAnimatorSet;

    private boolean mIsInit = true;
    private int mExpandViewHeight;

    private OnExpandListener mOnExpandListener;

    public ExpandableLayout(Context context) {
        super(context);
        init(context, null);
    }

    public ExpandableLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ExpandableLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setClickable(true);
        setOrientation(VERTICAL);
        setClipChildren(false);
        setClipToPadding(false);
        mExpandState = ExpandState.PRE_INIT;

        if (attrs != null) {
            TypedArray typedArray = null;
            try {
                typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableLayout);
                mExpandDuration = typedArray.getInt(R.styleable.ExpandableLayout_expandDuration, DEFAULT_DURATION);
                mExpandWithParentScroll = typedArray.getBoolean(R.styleable.ExpandableLayout_expandWithParentScroll, false);
                mExpandScrollTogether = typedArray.getBoolean(R.styleable.ExpandableLayout_expandScrollTogether, true);
            } finally {
                if(typedArray != null) {
                    typedArray.recycle();
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() != 2) {
            throw new IllegalArgumentException("ExpandableLayout must has two child view!!");
        }

        if (mIsInit) {
            ((MarginLayoutParams)getChildAt(0).getLayoutParams()).bottomMargin = 0;
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams)getChildAt(1).getLayoutParams();
            marginLayoutParams.bottomMargin = 0;
            marginLayoutParams.topMargin = 0;
            marginLayoutParams.height = 0;
            mExpandViewHeight = getChildAt(1).getMeasuredHeight();
            Log.i(TAG, "ExpandViewHeight = " + mExpandViewHeight);
            mIsInit = false;
            mExpandState = ExpandState.CLOSED;
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(mExpandWithParentScroll) {
            mScrolledParent = getScrolledParent(this);
        }
    }

    private ScrolledParent getScrolledParent(ViewGroup child) {
        ViewParent parent = child.getParent();
        int childBetweenParentCount = 0;
        while (parent != null) {
            if (parent instanceof AbsListView || parent instanceof RecyclerView) {
                ScrolledParent scrolledParent = new ScrolledParent();
                scrolledParent.scrolledView = (ViewGroup) parent;
                scrolledParent.childBetweenParentCount = childBetweenParentCount;
                return scrolledParent;
            }
            childBetweenParentCount++;
            parent = parent.getParent();
        }
        return null;
    }

    private int getParentScrollDistance() {
        int distance = 0;
        if(mScrolledParent == null) {
            return distance;
        }

        distance = (int) (getY() + getMeasuredHeight() + mExpandViewHeight - mScrolledParent.scrolledView.getMeasuredHeight());
        for (int i = 0; i < mScrolledParent.childBetweenParentCount; i++) {
            distance += ((ViewGroup)getParent()).getY();
        }

        return distance;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

    }

    private void verticalAnimation(final int startHeight, final int endHeight) {

        final View target = getChildAt(1);
        mExpandAnimator = ValueAnimator.ofInt(startHeight, endHeight);
        mExpandAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                target.getLayoutParams().height = (int) animation.getAnimatedValue();
                target.requestLayout();
            }
        });
        mExpandAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if(endHeight - startHeight < 0) {
                    mExpandState = ExpandState.CLOSED;
                    if(mOnExpandListener != null) {
                        mOnExpandListener.onExpand(false);
                    }
                } else {
                    mExpandState = ExpandState.EXPANDED;
                    if(mOnExpandListener != null) {
                        mOnExpandListener.onExpand(true);
                    }
                }
            }
        });

        mExpandState = mExpandState == ExpandState.EXPANDED ? ExpandState.CLOSING : ExpandState.EXPANDING;
        mExpandAnimator.setDuration(mExpandDuration);

        final int distance = getParentScrollDistance();
        if(mExpandState == ExpandState.EXPANDING && mExpandWithParentScroll && distance > 0) {
            mParentAnimator = ValueAnimator.ofInt(0, distance);
            mParentAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                int lastDy;
                int dy;
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    dy = (int) animation.getAnimatedValue() - lastDy;
                    lastDy = (int) animation.getAnimatedValue();
                    mScrolledParent.scrolledView.scrollBy(0, dy);
                }
            });
            mParentAnimator.setDuration(mExpandDuration);
            mExpandScrollAnimatorSet = new AnimatorSet();
            if(mExpandScrollTogether) {
                mExpandScrollAnimatorSet.playTogether(mExpandAnimator, mParentAnimator);
            } else {
                mExpandScrollAnimatorSet.playSequentially(mExpandAnimator, mParentAnimator);
            }
            mExpandScrollAnimatorSet.start();
        } else {
            mExpandAnimator.start();
        }
    }

    public void setExpand(boolean expand) {
        if(mExpandState == ExpandState.PRE_INIT) {
            return ;
        }
        getChildAt(1).getLayoutParams().height = expand ? mExpandViewHeight : 0;
        requestLayout(); //???
        mExpandState = expand ? ExpandState.EXPANDED : ExpandState.CLOSED;
    }

    public boolean isExpanded() {
        return mExpandState == ExpandState.EXPANDED;
    }

    @Override
    public boolean performClick() {
        toggle();
        return super.performClick();
    }

    private void toggle() {
        if(mExpandState == ExpandState.EXPANDED) {
            close();
        } else if(mExpandState == ExpandState.CLOSED) {
            expand();
        }
    }

    private void close() {
        verticalAnimation(mExpandViewHeight, 0);
    }

    private void expand() {
        verticalAnimation(0, mExpandViewHeight);
    }

    public boolean isExpandWithParentScroll() {
        return mExpandWithParentScroll;
    }

    public void setExpandWithParentScroll(boolean expandWithParentScroll) {
        mExpandWithParentScroll = expandWithParentScroll;
    }

    public boolean isExpandScrollTogether() {
        return mExpandScrollTogether;
    }

    public void setExpandScrollTogether(boolean expandScrollTogether) {
        mExpandScrollTogether = expandScrollTogether;
    }

    public int getExpandDuration() {
        return mExpandDuration;
    }

    public void setExpandDuration(int expandDuration) {
        mExpandDuration = expandDuration;
    }

    private class ExpandState {
        static final int PRE_INIT = -1;
        static final int CLOSED = 0;
        static final int EXPANDED = 1;
        static final int EXPANDING = 2;
        static final int CLOSING = 3;
    }

    private class ScrolledParent {
        ViewGroup scrolledView;
        int childBetweenParentCount;
    }

    public interface OnExpandListener {
        void onExpand(boolean expanded) ;
    }

}
