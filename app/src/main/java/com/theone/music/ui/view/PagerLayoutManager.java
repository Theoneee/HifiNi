package com.theone.music.ui.view;
import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;


//  ┏┓　　　┏┓
//┏┛┻━━━┛┻┓
//┃　　　　　　　┃
//┃　　　━　　　┃
//┃　┳┛　┗┳　┃
//┃　　　　　　　┃
//┃　　　┻　　　┃
//┃　　　　　　　┃
//┗━┓　　　┏━┛
//    ┃　　　┃                  神兽保佑
//    ┃　　　┃                  永无BUG！
//    ┃　　　┗━━━┓
//    ┃　　　　　　　┣┓
//    ┃　　　　　　　┏┛
//    ┗┓┓┏━┳┓┏┛
//      ┃┫┫　┃┫┫
//      ┗┻┛　┗┻┛

/**
 * @author The one
 * @date 2021-06-28 15:21
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */

public class PagerLayoutManager extends LinearLayoutManager {

    //
    private PagerSnapHelper mPagerSnapHelper;
    private OnViewPagerListener mOnViewPagerListener;

    /**
     *  位移，用来判断移动方向
     */
    private int mDrift;

    public PagerLayoutManager(Context context, int orientation) {
        super(context, orientation, false);
        init();
    }

    public PagerLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        init();
    }

    private void init() {
        mPagerSnapHelper = new PagerSnapHelper();
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        mPagerSnapHelper.attachToRecyclerView(view);
        view.addOnChildAttachStateChangeListener(mChildAttachStateChangeListener);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
    }

    /**
     * 滑动状态的改变
     * 缓慢拖拽-> SCROLL_STATE_DRAGGING
     * 快速滚动-> SCROLL_STATE_SETTLING
     * 空闲状态-> SCROLL_STATE_IDLE
     *
     * @param state
     */
    @Override
    public void onScrollStateChanged(int state) {
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            View viewIdle = mPagerSnapHelper.findSnapView(this);
            if (viewIdle != null) {
                int positionIdle = getPosition(viewIdle);
                if (mOnViewPagerListener != null && getChildCount() == 1) {
                    mOnViewPagerListener.onPageSelected(positionIdle, positionIdle == getItemCount() - 1, viewIdle);
                }
            }
        }
    }

    public void setPosition() {
        View viewIdle = mPagerSnapHelper.findSnapView(this);
        if (viewIdle != null) {
            int positionIdle = getPosition(viewIdle);
            if (mOnViewPagerListener != null && getChildCount() == 1) {
                mOnViewPagerListener.onPageSelected(positionIdle, positionIdle == getItemCount() - 1, viewIdle);
            }
        }
    }

    /**
     * 监听竖直方向的相对偏移量
     *
     * @param dy
     * @param recycler
     * @param state
     * @return
     */
    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.mDrift = dy;
        return super.scrollVerticallyBy(dy, recycler, state);
    }

    /**
     * 监听水平方向的相对偏移量
     *
     * @param dx
     * @param recycler
     * @param state
     * @return
     */
    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.mDrift = dx;
        return super.scrollHorizontallyBy(dx, recycler, state);
    }

    /**
     * 设置监听
     *
     * @param listener
     */
    public void setOnViewPagerListener(OnViewPagerListener listener) {
        this.mOnViewPagerListener = listener;
    }

    private RecyclerView.OnChildAttachStateChangeListener mChildAttachStateChangeListener = new RecyclerView.OnChildAttachStateChangeListener() {
        /**
         * itemView依赖Window
         */
        @Override
        public void onChildViewAttachedToWindow(View view) {
            if (mOnViewPagerListener != null && getChildCount() == 1) {
                mOnViewPagerListener.onInitComplete(view);
            }
        }

        /**
         *itemView脱离Window
         */
        @Override
        public void onChildViewDetachedFromWindow(View view) {
            if (mDrift >= 0) {
                if (mOnViewPagerListener != null){
                    mOnViewPagerListener.onPageRelease(true, getPosition(view), view);
                }
            } else {
                if (mOnViewPagerListener != null){
                    mOnViewPagerListener.onPageRelease(false, getPosition(view), view);
                }
            }

        }
    };
}