package com.lefuorgn.lefu.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.AppConfig;
import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.base.BaseContactFragment;
import com.lefuorgn.db.model.basic.OldPeople;
import com.lefuorgn.db.util.DictionaryManager;
import com.lefuorgn.db.util.OldPeopleManager;
import com.lefuorgn.lefu.activity.PersonalDataActivity;
import com.lefuorgn.lefu.activity.TelephoneActivity;

import java.util.List;

/**
 * 老人分类联系人页面,关心的老人以及所有老人
 */

public class ElderlyTypeFragment extends BaseContactFragment<OldPeople> {

    public static final String BUNDLE_ELDERLY_TYPE = "BUNDLE_ELDERLY_TYPE";
    private boolean attention;
    private LoadOldPeopleTask mLoadOldPeopleTask;
    private SparseArray<String> mSexInfo;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(AppConfig.INTENT_ACTION_NOTICE_ELDERLY)) {
                // 老人数据同步广播
                mLoadOldPeopleTask = (LoadOldPeopleTask) new LoadOldPeopleTask().execute();
            }
        }
    };

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_fragment_all_elderly;
    }

    @Override
    protected void initData() {
        Bundle args = getArguments();
        if(args != null) {
            attention = args.getBoolean(BUNDLE_ELDERLY_TYPE);
        }
        mLoadOldPeopleTask = (LoadOldPeopleTask) new LoadOldPeopleTask().execute();
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Intent intent = new Intent(getActivity(), PersonalDataActivity.class);
                intent.putExtra("oldPeople", mAdapter.getItem(i));
                startActivity(intent);
            }
        });
        // 注册刷新广播
        IntentFilter filter = new IntentFilter(AppConfig.INTENT_ACTION_NOTICE_ELDERLY);
        getActivity().registerReceiver(mReceiver, filter);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final OldPeople t, boolean flag) {
        holder.setText(R.id.tv_item_fragment_elderly_name, t.getElderly_name())
                .setText(R.id.tv_item_fragment_elderly_letter, t.getSortLetters())
                .setText(R.id.tv_item_fragment_elderly_sex, getSexInfo(t.getGender()))
                .setText(R.id.tv_item_fragment_elderly_age, t.getAge() + "岁")
                .setVisible(R.id.tv_item_fragment_elderly_letter, flag)
                .setImageResource(R.id.iv_item_fragment_elderly_attention,
                        t.iscAttention() ? R.mipmap.attention_select : R.mipmap.attention_normal)
                .setOnClickListener(R.id.iv_item_fragment_elderly_call, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 跳转到家属联系人页面
                        Intent intent = new Intent(getActivity(), TelephoneActivity.class);
                        intent.putExtra("oldPeople", t);
                        startActivity(intent);
                    }
                });
        ImageView aView = holder.getView(R.id.iv_item_fragment_elderly_attention);
        aView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t.setcAttention(!t.iscAttention());
                holder.setImageResource(R.id.iv_item_fragment_elderly_attention,
                        t.iscAttention() ? R.mipmap.attention_select : R.mipmap.attention_normal);
                OldPeopleManager.updateOldPeopleAttention(t);
                if(attention) {
                    // 通知所有老人页面进行数据刷新, 并移除当前条目
                    mAdapter.getData().remove(t);
                    // 刷新侧拉条目
                    updateQuickSideBarLetters(mAdapter.getData());
                    mAdapter.notifyDataSetChanged();
                }
                for (Fragment fragment : getFragmentManager().getFragments()) {
                    if(fragment instanceof ElderlyTypeFragment) {
                        ((ElderlyTypeFragment) fragment).refreshItemData(attention, t);
                    }
                }
                // 保存当前关注的老人已经修改
                AppContext.setCareForTheElderly(true);
            }
        });
    }

    /**
     * 老人数据加载类
     */
    private class LoadOldPeopleTask extends AsyncTask<Void, Void, List<OldPeople>> {

        @Override
        protected List<OldPeople> doInBackground(Void... params) {
            return OldPeopleManager.getOldPeople(attention);
        }

        @Override
        protected void onPostExecute(List<OldPeople> data) {
            setData(data);
        }
    }

    /**
     * 刷新某一条目信息
     */
    public void refreshItemData(boolean a, OldPeople o) {
        if(!attention && a) {
            // 所有老人页面, 信息来源于关注页面
            for (int i = 0; i < mData.size(); i++) {
                OldPeople oldPeople = mData.get(i);
                if(oldPeople.getId() == o.getId()) {
                    oldPeople.setcAttention(o.iscAttention());
                    mAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }
        if(attention && !a) {
            // 关注老人页面, 信息来源于所有页面
            List<OldPeople> list = OldPeopleManager.getOldPeople(true);
            setData(list);
        }
    }

    @Override
    protected String getEmptyNote() {
        return "未关注任何老人";
    }

    private String getSexInfo(int sex) {
        if(mSexInfo == null) {
            mSexInfo = new SparseArray<String>();
        }
        String result = mSexInfo.get(sex);
        if(result == null) {
            result = DictionaryManager.getContent(sex);
            mSexInfo.append(sex, result);
        }
        return result;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
        if (mLoadOldPeopleTask != null) {
            mLoadOldPeopleTask.cancel(true);
            mLoadOldPeopleTask = null;
        }
    }
}
