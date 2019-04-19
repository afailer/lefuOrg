package com.lefuorgn.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.util.DividerItemDecoration;

import java.util.List;


/**
 * 展示列表Dialog
 */
public final class ListDialog<E> extends DialogFragment {

    /**
     * 列表文本居左显示
     */
    public static final int LEFT = Gravity.START;
    /**
     * 列表文本居中显示
     */
    public static final int CENTER = Gravity.CENTER_HORIZONTAL;

    private String title = "标题"; // 标题
    private List<E> data; // 要显示的数据
    private int gravity = LEFT; // 列表数据显示
    private String emptyNote = "这里还没有内容哦~"; // 空数据提示内容
    private Callback callback;
    // 点击其他区域是否消失
    private boolean cancelOnTouchOutSide;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_list, null);
        ((TextView) view.findViewById(R.id.tv_dialog_list_title)).setText(title);
        view.findViewById(R.id.iv_dialog_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_dialog_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST,
                getResources().getColor(R.color.recycler_view_item_division_color)));
        final ListDialogAdapter adapter = new ListDialogAdapter(data);
        // 创建空布局
        View emptyView = LayoutInflater.from(getActivity())
                .inflate(R.layout.item_recyclerview_empty, recyclerView, false);
        ((TextView)emptyView.findViewById(R.id.item_recycler_view_item)).setText(emptyNote);
        adapter.setEmptyView(emptyView);
        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                E t = adapter.getItem(i);
                if(callback != null) {
                    callback.onItemClick(view, t);
                }
                dismiss();
            }
        });
        recyclerView.setAdapter(adapter);
        Dialog dialog = builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(cancelOnTouchOutSide);
        dialog.setContentView(view);
        return dialog;
    }

    public ListDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public ListDialog setData(List<E> data) {
        this.data = data;
        return this;
    }

    public ListDialog setGravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    public ListDialog setEmptyNote(String emptyNote) {
        this.emptyNote = emptyNote;
        return this;
    }

    public ListDialog setCallBack(Callback<E> callBack) {
        this.callback = callBack;
        return this;
    }

    /**
     * 设置点击其他区域是否消失
     * @param flag true: 消失; false: 不消失
     * @return 当前对象
     */
    public ListDialog isCancelOutside(boolean flag) {
        cancelOnTouchOutSide = flag;
        return this;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 初始化布局大小
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        Display display = wm.getDefaultDisplay();
        display.getMetrics(outMetrics);
        int width = (int) (outMetrics.widthPixels * 0.8);
        int height = (int) (outMetrics.heightPixels * 0.7);
        Window window = getDialog().getWindow();
        if(window != null) {
            window.setLayout(width, height);
        }
    }

    private class ListDialogAdapter extends BaseQuickAdapter<E> {

        ListDialogAdapter(List<E> data) {
            super(R.layout.item_dialog_list, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, E t) {
            TextView tv = holder.getView(R.id.tv_item_dialog_list);
            tv.setGravity(gravity);
            if(callback != null) {
                callback.convert(tv, t);
            }
        }
    }

    /**
     * 列表数据展示和点击监听事件
     */
    public interface Callback<T> {

        /**
         * 列表数据展示
         */
        void convert(TextView view, T t);

        /**
         * 题目数据点击事件
         */
        void onItemClick(View view, T t);

    }

}
