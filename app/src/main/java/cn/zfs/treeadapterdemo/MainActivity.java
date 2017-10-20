package cn.zfs.treeadapterdemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.zfs.treeadapter.Node;
import cn.zfs.treeadapter.TreeAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView lv = new ListView(this);
        setContentView(lv, new ViewGroup.LayoutParams(-1, -1));
        final List<Item> list = new ArrayList<>();
        list.add(new Item(0, 0, 0, false, "Android"));
        list.add(new Item(1, 0, 1, false, "Service"));
        list.add(new Item(2, 0, 1, false, "Activity"));
        list.add(new Item(3, 0, 1, false, "Receiver"));
        list.add(new Item(4, 0, 0, true, "Java Web"));
        list.add(new Item(5, 4, 1, false, "CSS"));
        list.add(new Item(6, 4, 1, false, "Jsp"));
        list.add(new Item(7, 4, 1, true, "Html"));
        list.add(new Item(8, 7, 2, false, "p"));
        final MyAdapter adapter = new MyAdapter(list);
        adapter.setOnInnerItemClickListener(new TreeAdapter.OnInnerItemClickListener<Item>() {
            @Override
            public void onClick(Item node) {
                Toast.makeText(MainActivity.this, "click: " + node.name, Toast.LENGTH_SHORT).show();
            }
        });
        adapter.setOnInnerItemLongClickListener(new TreeAdapter.OnInnerItemLongClickListener<Item>() {
            @Override
            public void onLongClick(Item node) {
                Toast.makeText(MainActivity.this, "long click: " + node.name, Toast.LENGTH_SHORT).show();
            }
        });
        lv.setAdapter(adapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                list.add(new Item(9, 7, 2, false, "a"));
                adapter.notifyDataSetChanged();
            }
        }, 2000);
    }

    private class MyAdapter extends TreeAdapter<Item> {
        MyAdapter(List<Item> nodes) {
            super(nodes);
        }

        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount() + 1;
        }

        /**
         * 获取当前位置的条目类型
         */
        @Override
        public int getItemViewType(int position) {
            if (getItem(position).hasChild()) {
                return 1;
            }
            return 0;
        }
        
        @Override
        protected Holder<Item> getHolder(int position) {
            switch(getItemViewType(position)) {
                case 1:
                    return new Holder<Item>() {
                        private ImageView iv;
                        private TextView tv;

                        @Override
                        protected void setData(Item node) {
                            iv.setVisibility(node.hasChild() ? View.VISIBLE : View.INVISIBLE);
                            iv.setBackgroundResource(node.isExpand ? R.mipmap.expand : R.mipmap.fold);
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv.getLayoutParams();
                            params.leftMargin = (node.level + 1) * dip2px(20);
                            iv.setLayoutParams(params);
                            tv.setText(node.name);
                        }

                        @Override
                        protected View createConvertView() {
                            View view = View.inflate(MainActivity.this, R.layout.item_tree_list_has_child, null);
                            iv = (ImageView) view.findViewById(R.id.ivIcon);
                            tv = (TextView) view.findViewById(R.id.tvName);
                            return view;
                        }
                    };
                default:
                    return new Holder<Item>() {
                        private TextView tv;
                        
                        @Override
                        protected void setData(Item node) {
                            tv.setText(node.name);
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tv.getLayoutParams();
                            params.leftMargin = (node.level + 3) * dip2px(20);
                            tv.setLayoutParams(params);
                        }

                        @Override
                        protected View createConvertView() {
                            View view = View.inflate(MainActivity.this, R.layout.item_tree_list_no_child, null);
                            tv = (TextView) view.findViewById(R.id.tvName);
                            return view;
                        }
                    };
            }
        }
    }

    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素) 
     */
    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    
    private class Item extends Node<Item> {
        String name;

        Item(int id, int pId, int level, boolean isExpand, String name) {
            super(id, pId, level, isExpand);
            this.name = name;
        }
    }
}
