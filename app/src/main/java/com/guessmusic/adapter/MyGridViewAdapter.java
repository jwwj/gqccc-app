package com.guessmusic.adapter;


import com.example.jwj.curriculumdesign.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

/**
 * GridView的适配器
 *Created by len on 2017/6/28.
 */
public class MyGridViewAdapter extends BaseAdapter {
	private String[] strs;
	private Context context;

	public  MyGridViewAdapter(String[] strs,Context context) {
		this.context=context;
		this.strs=strs;

	}
      //获取显示数据的数量
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return strs.length;
	}
     
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
     //设置GridView每一项的视图
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
      //将xml文件装换为view对象
	 View view = View.inflate(context, R.layout.main_activity_foot_gridview_option_button, null);
     Button button=(Button) view.findViewById(R.id.main_activity_gv_option_item_btn);
     button.setText(strs[position]);
		return view;
	}

}
