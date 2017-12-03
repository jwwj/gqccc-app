package com.guessmusic.views;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;


import com.example.jwj.curriculumdesign.R;
import com.guessmusic.bean.LevelBtn;
import com.guessmusic.bean.UserBean;
import com.guessmusic.listeners.MyLvBtnListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by len on 2017/6/30.
 */

public class LevelGridView extends GridView{
    private Context context;
    private MyAdapter lvAdapter;
    private MyLvBtnListener listener;
    private List<LevelBtn> levelBtns=new ArrayList<LevelBtn>();
    private HashMap<Integer, View> viewMap = new HashMap<>();//防止GridView混乱，添加标识
    public LevelGridView(Context context, AttributeSet attrs){
        super(context,attrs);
        lvAdapter=new MyAdapter();
        this.setAdapter(lvAdapter);
        this.context=context;
    }


        class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return levelBtns.size();
        }

        @Override
        public Object getItem(int position) {
            return levelBtns.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
        //    if(convertView==null){
            if(!viewMap.containsKey(position) || viewMap.get(position) == null){
                holder=new ViewHolder();
                convertView = View.inflate(context,R.layout.level_gridview_button,null);
                holder.lvBtn = levelBtns.get(position);
                holder.lvBtn.levelIndex = position;
                //Log.i("position",position+"");
                if(holder.lvBtn.levelButton==null){
                    holder.lvBtn.levelButton = (Button) convertView.findViewById(R.id.level_gv_item_btn);
                }
                holder.lvBtn.levelButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onLvBtnClick(holder.lvBtn);
                    }
                });

                // holder.opBtn.optionButton.setBackgroundResource(R.drawable.game_word);
                //  LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(110,110);
                //  convertView.setLayoutParams(params);

                int p=240;
                AbsListView.LayoutParams params = new AbsListView.LayoutParams(p,p);
                convertView.setLayoutParams(params);
                Log.i("isPass",holder.lvBtn.isPass+"");
                if(holder.lvBtn.isPass) {
                    if (holder.lvBtn.levelText == UserBean.uLevel+1) {
                        convertView.setBackgroundResource(R.drawable.game_level_current);
                    } else {
                        convertView.setBackgroundResource(R.drawable.game_level);
                    }
                }else{
                    convertView.setBackgroundResource(R.drawable.game_level_title);
                }

                Log.i("getView",holder.lvBtn.levelText+"");
                holder.lvBtn.levelButton.setText(holder.lvBtn.levelText + "");
                Log.i("levelButtonText",holder.lvBtn.levelButton.getText()+"");

               // Animation animation = AnimationUtils.loadAnimation(context,R.anim.scale_btn);
              //  animation.setStartOffset(position*100);
              //  holder.lvBtn.levelButton.setAnimation(animation);

                convertView.setTag(holder);

                viewMap.put(position, convertView);
            }else{
                convertView = viewMap.get(position);
                holder = (ViewHolder)convertView.getTag();
            }

            return convertView;
        }
    }
    //提供缓存的成员内部类
    class ViewHolder{
        LevelBtn lvBtn;
    }
    public void updateGridView(List<LevelBtn> btns ){
        this.levelBtns = btns;
        this.setAdapter(lvAdapter);
        Log.i("levelBtns",levelBtns.toString());
    }
    //给GridView注册监听 接口回调
    public void registOpBtnListener(MyLvBtnListener listener){
        this.listener = listener;
    }

    //防止viewMap溢出
    public void cleanViewMap(View convertView){
        if(viewMap.size() > 32){
            synchronized (convertView) {
                for(int i = 1;i < getFirstVisiblePosition() - 4;i ++){
                    viewMap.remove(i);
                }
                for(int i = getLastVisiblePosition() + 4;i < getCount();i ++){
                    viewMap.remove(i);
                }
            }
        }
    }
}
