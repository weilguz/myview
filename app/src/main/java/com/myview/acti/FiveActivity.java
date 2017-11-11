package com.myview.acti;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.myview.R;

import java.util.ArrayList;

public class FiveActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<String> arrayList;
    private MyAdapter myAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_five);
        arrayList = getData();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.five_rlv);
        Button addItem = (Button) findViewById(R.id.add_btn);
        Button removeItem = (Button) findViewById(R.id.remove_btn);
        Button six = (Button) findViewById(R.id.remove_btn);
        addItem.setOnClickListener(this);
        removeItem.setOnClickListener(this);
        removeItem.setOnClickListener(this);

        linearLayoutManager = new LinearLayoutManager(FiveActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        myAdapter = new MyAdapter();
        myAdapter.setData(arrayList);
        recyclerView.setAdapter(myAdapter);

        recyclerView.addItemDecoration(new MyItemDecoration(this,LinearLayoutManager.VERTICAL));

        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    public ArrayList<String> getData() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i <50; i++) {
            arrayList.add(i+"  item");
        }
        return arrayList;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.add_btn:
                myAdapter.addItem();
                linearLayoutManager.scrollToPosition(0);
                break;
            case R.id.remove_btn:
                myAdapter.deleteItem();
                linearLayoutManager.scrollToPosition(0);
                break;
            case R.id.six_btn:
                Intent intent = new Intent(FiveActivity.this,SixActivity.class);
                startActivity(intent);
                break;

        }
    }

    static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

        private MyAdapter.OnItemClickListener listener;

        ArrayList<String> array;
        public void setData(ArrayList<String> arrayList) {
            this.array=arrayList;
        }

        private void deleteItem() {
            if (array == null || array.isEmpty()){
                return;
            }
            array.remove(0);
            notifyDataSetChanged();
            return;
        }

        private void addItem() {
            if (array == null){
                array = new ArrayList<>();
            }
            array.add(0,"new Item");
            notifyDataSetChanged();
        }

        public void setOnItemClickListener(MyAdapter.OnItemClickListener listener){
            this.listener=listener;
        }


        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate=null;
            if (viewType == 0){
                //实例化展示的view
                inflate = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycle_layout, parent, false);
            }else if(viewType == 1){
                inflate = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycle_layout2, parent, false);
            }
            //实例化viewHolder
            ViewHolder viewHolder = new ViewHolder(inflate);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            int itemViewType = getItemViewType(position);
            if (itemViewType == 0){
                holder.tv.setText(array.get(position));
//                holder.tv.setOnClickListener();

            }else if(itemViewType == 1){
                holder.tv1.setText(array.get(position));
                holder.tv2.setText(array.get(position));
            }
        }


        @Override
        public int getItemCount() {
            return array != null ? array.size():0;
        }

        @Override
        public int getItemViewType(int position) {
            int i = position % 3;
            if (i == 0){
                return 0;
            }else if(i<3){
                return 1;
            }else{
                return 0;
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv;
            TextView tv2;
            TextView tv1;

            public ViewHolder(View itemView) {
                super(itemView);
                tv = (TextView)itemView.findViewById(R.id.recy_item_tv);
                tv2 = (TextView)itemView.findViewById(R.id.recy_item2_tv2);
                tv1 = (TextView)itemView.findViewById(R.id.recy_item2_tv);
            }
        }

        interface OnItemClickListener {
            void onItemClick(View view, int position);
            void onItemLongClick(View view, int position);
        }

    }

    static class MyItemDecoration extends RecyclerView.ItemDecoration{
        private static final int[] ATTRS =  new int[]{android.R.attr.listDivider};

        public static final int HORIZONTA_LIST = LinearLayoutManager.HORIZONTAL;
        public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;


        //用于绘制间隔的样式
        private final Drawable mDrawable;

        //列表的方向  纵向/横向
        private int mOrentation;

        public MyItemDecoration(Context context,int orientation){
            TypedArray typedArray = context.obtainStyledAttributes(ATTRS);
            mDrawable = typedArray.getDrawable(0);
            typedArray.recycle();
            setOrientation(orientation);
        }

        private void setOrientation(int orientation) {
            if ( orientation != HORIZONTA_LIST && orientation != VERTICAL_LIST){
                throw new IllegalArgumentException("invalid orientation");
            }
            mOrentation = orientation;
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDraw(c, parent, state);
            if (mOrentation == HORIZONTA_LIST){
                drawHorizonta(c,parent);
            }else if(mOrentation == VERTICAL_LIST){
                drawVertical(c,parent);
            }
        }

        private void drawVertical(Canvas c, RecyclerView parent) {
            int Left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();
            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = parent.getChildAt(i);

                RecyclerView.LayoutParams layoutParams =
                        (RecyclerView.LayoutParams) childAt.getLayoutParams();
                int top = childAt.getBottom() + layoutParams.bottomMargin +
                        Math.round(ViewCompat.getTranslationY(childAt));
                int bottom = top + mDrawable.getIntrinsicHeight();
                mDrawable.setBounds(Left,right,top,bottom);
                mDrawable.draw(c);
            }
        }

        private void drawHorizonta(Canvas c, RecyclerView parent) {
            int top = parent.getPaddingTop();
            int bottom =parent.getHeight() - parent.getPaddingBottom();
            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = parent.getChildAt(i);
                RecyclerView.LayoutParams layoutParams =
                        (RecyclerView.LayoutParams) childAt.getLayoutParams();
                int left = childAt.getRight() + layoutParams.rightMargin +
                        Math.round(ViewCompat.getTranslationX(childAt));
                final int right = left + mDrawable.getIntrinsicHeight();
                mDrawable.setBounds(left, top, right, bottom);
                mDrawable.draw(c);

            }

        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            if (mOrentation == VERTICAL_LIST) {
                outRect.set(0, 0, 0, mDrawable.getIntrinsicHeight());
            } else {
                outRect.set(0, 0, mDrawable.getIntrinsicWidth(), 0);
            }

        }


    }
}
