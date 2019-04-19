package com.lefuorgn.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.interf.Pinyinable;
import com.lefuorgn.util.DividerItemDecoration;
import com.lefuorgn.util.TLog;
import com.lefuorgn.widget.ClearEditText;
import com.lefuorgn.widget.quicksidebar.QuickSideBarTipsView;
import com.lefuorgn.widget.quicksidebar.QuickSideBarView;
import com.lefuorgn.widget.quicksidebar.listener.OnQuickSideBarTouchListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 通讯录基类页面, 添加时要进行排序
 */
public abstract class BaseContactFragment<T extends Pinyinable> extends BaseFragment
        implements OnQuickSideBarTouchListener {

    private RecyclerView recyclerView;
    private QuickSideBarView quickSideBarView;
    private QuickSideBarTipsView quickSideBarTipsView;

    private HashMap<String, Integer> letters = new HashMap<String, Integer>();
    protected BaseContactAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private boolean initialization;
    private int mRecyclerViewHeight;
    private int mRecyclerViewItemHeight;
    private List<String> mContactLetters = new ArrayList<String>();
    private boolean search; // 当前是否是搜索状态

    protected List<T> mData=new ArrayList<T>();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_base_contact;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        quickSideBarView = (QuickSideBarView) view.findViewById(R.id.quickSideBarView);
        quickSideBarTipsView = (QuickSideBarTipsView) view.findViewById(R.id.quickSideBarTipsView);
        ClearEditText clearEditText = (ClearEditText) view.findViewById(R.id.cet_fragment_base_contact);

        //设置监听
        quickSideBarView.setOnQuickSideBarTouchListener(this);

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST,
                Color.parseColor("#E0E0E0")));
        //不自定义则默认26个字母
        quickSideBarView.setLetters(mContactLetters);

        mAdapter = new BaseContactAdapter(getItemLayoutId(), new ArrayList<T>());
        mAdapter.setEmptyView(getEmptyView());
        recyclerView.setAdapter(mAdapter);

        clearEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

    }

    @Override
    public void onLetterChanged(String letter, int position, float y) {
        quickSideBarTipsView.setText(letter, position, y);
        //有此key则获取位置并滚动到该位置
        if(!search && letters.containsKey(letter)) {
            moveToPosition(letters.get(letter));
        }
    }

    private void moveToPosition(int n) {
        if(mAdapter.getItemCount() == 0) {
            return;
        }
        // 先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        int firstItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        int lastItem = mLinearLayoutManager.findLastVisibleItemPosition();
        if(!initialization) {
            initialization = true;
            mRecyclerViewHeight = recyclerView.getMeasuredHeight();
            mRecyclerViewItemHeight = recyclerView.getChildAt(0).getBottom();
            TLog.log("当前屏幕的距离" + mRecyclerViewHeight);
            TLog.log("当前条目的高度" + mRecyclerViewItemHeight);
        }
        // 然后区分情况
        if (n <= firstItem ){
            // 当要置顶的项在当前显示的第一个项的前面时
            recyclerView.scrollToPosition(n);
        }else if ( n <= lastItem ){
            // 当要置顶的项已经在屏幕上显示时
            int top = recyclerView.getChildAt(n - firstItem).getTop();
            TLog.log("top == " + top);
            recyclerView.scrollBy(0, top);
        }else{
            // 当要置顶的项在当前显示的最后一项的后面时
            recyclerView.scrollToPosition(n);
            // 继续向上滚动
            recyclerView.scrollBy(0, mRecyclerViewHeight - mRecyclerViewItemHeight);
        }

    }

    @Override
    public void onLetterTouching(boolean touching) {
        //可以自己加入动画效果渐显渐隐
        quickSideBarTipsView.setVisibility(touching ? View.VISIBLE : View.INVISIBLE);
    }

    public class BaseContactAdapter extends BaseQuickAdapter<T> {

        BaseContactAdapter(int layoutResId, List<T> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, T t) {
            int position = holder.getLayoutPosition() - this.getHeaderViewsCount();
            if(position == 0) {
                BaseContactFragment.this.convert(holder, t, true);
            }else {
                Pinyinable t1 = (Pinyinable) getData().get(position - 1);
                if(t1.getSortLetters().equals(t.getSortLetters())) {
                    BaseContactFragment.this.convert(holder, t, false);
                }else {
                    BaseContactFragment.this.convert(holder, t, true);
                }
            }
        }
    }

    /**
     * 根据选择条件过滤数据
     * @param filterStr 关键字
     */
    private void filterData(String filterStr) {
        List<T> filterDateList = new ArrayList<T>();
        if (TextUtils.isEmpty(filterStr)) {
            search = false;
            filterDateList = mData;
        } else {
            search = true;
            filterDateList.clear();
            filterStr = filterStr.toLowerCase();
            for (T t : mData) {
                if (t.getCharacters().startsWith(filterStr) ||
                        t.getFullPinYin().startsWith(filterStr) ||
                        t.getInitial().startsWith(filterStr)) {
                    filterDateList.add(t);
                }
            }
        }
        mAdapter.setNewData(filterDateList);
        updateQuickSideBarLetters(filterDateList);
    }

    protected abstract int getItemLayoutId();

    /**
     * 条目填充页面
     * @param baseViewHolder 页面缓存器
     * @param t 当前条目数据
     * @param flag 当前条目是否是第一字母的开始条目; true: 第一个条目; false: 不是第一个条目
     */
    protected abstract void convert(BaseViewHolder baseViewHolder, T t, boolean flag);

    protected void setData(List<T> data) {
        mData = data;
        if(mData == null) {
            mData = new ArrayList<T>();
        }
        mAdapter.setNewData(mData);
        updateQuickSideBarLetters(data);
    }

    /**
     * 更新侧拉条目
     * @param data 当前页面展示的数据
     */
    protected void updateQuickSideBarLetters(List<T> data) {
        mContactLetters.clear();
        letters.clear();
        int position = 0;
        for(T t: data){
            String letter = t.getSortLetters();
            //如果没有这个key则加入并把位置也加入
            if(!letters.containsKey(letter)){
                letters.put(letter,position);
                mContactLetters.add(letter);
            }
            position++;
        }
        quickSideBarView.setLetters(mContactLetters);
    }

    /**
     * 获取空内容指示信息控件
     * @return 控件
     */
    private View getEmptyView() {
        View view = getActivity().getLayoutInflater().inflate(R.layout.item_recyclerview_empty,
                (ViewGroup) recyclerView.getParent(), false);
        ((TextView) view.findViewById(R.id.item_recycler_view_item)).setText(getEmptyNote());
        return view;
    }

    /**
     * 当前数据为空的时候提示内容
     */
    protected String getEmptyNote() {
        return "这里还没有内容哦~";
    }

}
