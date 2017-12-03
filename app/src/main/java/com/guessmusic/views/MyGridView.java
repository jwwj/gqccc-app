package com.guessmusic.views;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.example.jwj.curriculumdesign.R;
import com.guessmusic.bean.OptionBtn;
import com.guessmusic.listeners.MyOpBtnListener;
import com.guessmusic.utils.MusicUtil;
import com.guessmusic.utils.RandomUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by len on 2017/6/30.
 */

public class MyGridView extends GridView{
    private Context context;
    private MyAdapter myAdapter;
    private MyOpBtnListener listener;
    private List<OptionBtn> optionBtns=new ArrayList<OptionBtn>();
    public MyGridView(Context context, AttributeSet attrs){
        super(context,attrs);
        myAdapter=new MyAdapter();
        this.setAdapter(myAdapter);
        this.context=context;
    }

    //提示按钮 返回和答案第一个字的相同的按钮
    public OptionBtn idea(MusicUtil cu, int musicIndex, int anIndex){
        char[] anText = cu.MUSIC_ANSWER.toCharArray();
     //   Log.i("anText",String.valueOf(anText));
        for (int k = 0; k < MusicUtil.FIRST_OPTIONS.length; k++) {

            char opText = optionBtns.get(k).optionText.charAt(0);
        //    Log.i("opText",opText+"");
            if(opText == anText[anIndex]){
                return optionBtns.get(k);
            }
        }
        return null;
    }
//删除按钮 删除非答案的按钮
    public OptionBtn delet(MusicUtil cu, int musicIndex) {
        boolean isNobtn;

        char[] anText = cu.MUSIC_ANSWER.toString().toCharArray();

        for (int k = 0; k < MusicUtil.FIRST_OPTIONS.length; k++) {
            int i = RandomUtil.RandomDel();
            if (optionBtns.get(i).isViseable == false) {

                continue;
            } else {
                char opText = optionBtns.get(i).optionText.charAt(0);
                boolean flag = false;
                for (int j = 0; j < anText.length; j++) {
                    // ToastUtil.showToast(context, anText[j] + "");
                    if (opText == anText[j]) {
                        flag = true;
                    }
                }
                if (flag == false) {
                    return optionBtns.get(i);
                }
            }
        }
        return null;
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return optionBtns.size();
        }

        @Override
        public Object getItem(int position) {
            return optionBtns.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if(convertView==null){

                holder=new ViewHolder();
                convertView = View.inflate(context,R.layout.main_activity_foot_gridview_option_button,null);
                holder.opBtn = optionBtns.get(position);
                holder.opBtn.optionIndex = position;
                if(holder.opBtn.optionButton==null){
                    holder.opBtn.optionButton = (Button) convertView.findViewById(R.id.main_activity_gv_option_item_btn);
                }
                holder.opBtn.optionButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onOpBtnClick(holder.opBtn);
                    }
                });
                holder.opBtn.optionButton.setText(holder.opBtn.optionText);

               // holder.opBtn.optionButton.setBackgroundResource(R.drawable.game_word);
              //  LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(110,110);
              //  convertView.setLayoutParams(params);

                Resources res =getResources();
                int p = res.getInteger(R.integer._110);
                AbsListView.LayoutParams params = new AbsListView.LayoutParams(p,p);
                convertView.setLayoutParams(params);


                convertView.setBackgroundResource(R.drawable.game_word);

                Animation animation = AnimationUtils.loadAnimation(context,R.anim.scale_btn);
                animation.setStartOffset(position*100);
                holder.opBtn.optionButton.setAnimation(animation);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }

            return convertView;
        }
    }
    //提供缓存的成员内部类
    class ViewHolder{
        OptionBtn opBtn;
    }
    public void updateGridView(List<OptionBtn> btns ){
        this.optionBtns = btns;
        this.setAdapter(myAdapter);
    }
    //给GridView注册监听 接口回调
    public void registOpBtnListener(MyOpBtnListener listener){
          this.listener = listener;
    }
}
