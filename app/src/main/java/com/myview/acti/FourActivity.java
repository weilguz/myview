package com.myview.acti;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.myview.R;

import java.util.ArrayList;

public class FourActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView lv;
    private ArrayList<String> strings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four);
        lv = (ListView) findViewById(R.id.four_lv);
        Button btn = (Button) findViewById(R.id.four_btn);
        btn.setOnClickListener(this);
        strings = new ArrayList<>();

        for (int i = 0; i < 99; i++) {
            strings.add(i+"");
        }

        MyAdapter myAdapter = new MyAdapter();
        myAdapter.setData(strings);
        lv.setAdapter(myAdapter);
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState){
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE://停止滑动状态
                        Log.e("weilgu","SCROLL_STATE_IDLE");
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://触摸滑动
                        Log.e("weilgu","SCROLL_STATE_TOUCH_SCROLL");
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING://快速滑动
                        Log.e("weilgu","SCROLL_STATE_FLING");
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, FiveActivity.class);
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    class MyAdapter extends BaseAdapter{
         ArrayList<String> arr = new ArrayList<>();
        public void setData(ArrayList<String> arr){
            this.arr = arr;
        }
        final int TYPE_1 = 0;
        final int TYPE_2 = 1;
        final int TYPE_3 = 2;

        @Override
        public int getCount() {
            return arr.size();
        }

        class ViewHolder{
            TextView textView;
            ImageView imageView;
            TextView textView1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater in = LayoutInflater.from(FourActivity.this);
            ViewHolder viewHolder = null;
            int type=getItemViewType(position);
            if (convertView == null){
                viewHolder = new ViewHolder();
                switch (type){
                    case TYPE_1:
                        convertView = in.inflate(R.layout.type1_item,parent,false);
                        viewHolder.textView = (TextView) convertView.findViewById(R.id.type1_text1);
                        viewHolder.textView1 = (TextView) convertView.findViewById(R.id.type1_text2);
                        break;
                    case TYPE_2:
                        convertView = in.inflate(R.layout.type2_item,parent,false);
                        viewHolder.textView = (TextView) convertView.findViewById(R.id.type2_text);
                        viewHolder.imageView = (ImageView) convertView.findViewById(R.id.type2_img);
                        break;
                    case TYPE_3:
                        convertView = in.inflate(R.layout.type3_item,parent,false);
                        viewHolder.textView = (TextView) convertView.findViewById(R.id.type3_text);
                        viewHolder.imageView = (ImageView) convertView.findViewById(R.id.type3_iv);
                        viewHolder.textView1 = (TextView) convertView.findViewById(R.id.type3_text2);
                        break;
                }
                convertView.setTag(viewHolder);
            }else{
                viewHolder= (ViewHolder)convertView.getTag();
            }

            switch (type){
                case TYPE_1:
                    viewHolder.textView.setText(arr.get(position));
                    viewHolder.textView1.setText(arr.get(position));
                    break;
                case TYPE_2:
                    viewHolder.textView.setText(arr.get(position));
                    break;
                case TYPE_3:
                    viewHolder.textView.setText(arr.get(position));
                    viewHolder.textView1.setText(arr.get(position));
                    break;
            }
            return convertView;
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public int getItemViewType(int position) {
            int p = position % 6;
            if (p==0){
                return TYPE_1;
            }else if (p==1){
                return TYPE_2;
            }else if (p==2){
                return TYPE_3;
            }else{
                return TYPE_1;
            }
        }

        @Override
        public Object getItem(int position) {
            return strings.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

}
