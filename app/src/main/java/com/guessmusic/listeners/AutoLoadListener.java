package com.guessmusic.listeners;

/**
 * Created by len on 2017/8/26.
 */
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Toast;

import com.example.jwj.curriculumdesign.LevelActivity;
import com.example.jwj.curriculumdesign.R;

/**
 * 滚动至列表底部，读取下一页数据
 */
public class AutoLoadListener implements OnScrollListener {

    public interface AutoLoadCallBack {
        void execute();
    }

    private int getLastVisiblePosition = 0, lastVisiblePositionY = 0;
    private AutoLoadCallBack mCallback;
    private int scrolledX;
    private int scrolledY;

    public AutoLoadListener(AutoLoadCallBack callback) {
        this.mCallback = callback;
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {

        //不滚动时的状态，通常会在滚动停止时监听到此状态
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            //滚动到底部

            if (view.getLastVisiblePosition() == (view.getCount() - 1)) {

                View v = (View) view.getChildAt(view.getChildCount() - 1);
                int[] location = new int[2];
                v.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
                int y = location[1];

                if (view.getLastVisiblePosition() != getLastVisiblePosition && lastVisiblePositionY != y)//第一次拖至底部
                {
                    Animation moveupAn = AnimationUtils.loadAnimation(view.getContext(), R.anim.gridview_moveup);
                    view.startAnimation(moveupAn);
                    mCallback.execute();
                  //  Toast.makeText(view.getContext(), "再次拖动即可获得5个新关卡", Toast.LENGTH_SHORT).show();
                    getLastVisiblePosition = view.getLastVisiblePosition();
                    lastVisiblePositionY = y;
                    return;
                } else if (view.getLastVisiblePosition() == getLastVisiblePosition && lastVisiblePositionY == y)//第二次拖至底部
                {
                    Animation moveupAn = AnimationUtils.loadAnimation(view.getContext(), R.anim.gridview_moveup);
                    view.startAnimation(moveupAn);
                        mCallback.execute();
                     //   view.setSelection(view.getLastVisiblePosition());
                        Log.i("getLastVisiblePosition",getLastVisiblePosition+"");
                      //  Toast.makeText(view.getContext(), "数据加载完毕", Toast.LENGTH_SHORT).show();
                }
            }

            //未滚动到底部，第二次拖至底部都初始化
            getLastVisiblePosition = 0;
            lastVisiblePositionY = 0;
        }
    }

    public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {

    }
}