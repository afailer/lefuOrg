package com.lefuorgn.gov.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.GovApi;
import com.lefuorgn.base.BaseFragment;
import com.lefuorgn.gov.bean.MessageInfo;
import java.util.List;

/**
 * Created by liuting on 2016/12/19.
 */

public class AnnouncementFragment extends BaseFragment{

    static private MessageFragment mAllMessageFragment;
    static private MessageFragment mUNReadMessageFragment;
    private FragmentManager mFManager;
    TextView mAllMessagesBtn,mUnreadMessagesBtn;
    private String ALLMessage="all",UNReadMessage="unread";
    private int allmsg=3,unread=1;
    private int mSelectedBtnId=0;
    public static void setReadState(long id){
        mAllMessageFragment.setReadState(id);
        mUNReadMessageFragment.setReadState(id);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.announcement_fragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        mAllMessagesBtn= (TextView) view.findViewById(R.id.all_messages_gov_circular_activity);
        mUnreadMessagesBtn= (TextView) view.findViewById(R.id.unread_messages_gov_circular_activity);
        addAllFragment();
    }

    @Override
    protected void initData() {
        super.initData();
        mUnreadMessagesBtn.setOnClickListener(this);
        mAllMessagesBtn.setOnClickListener(this);
        switchFragment(UNReadMessage);
        getUnreadMsg();
    }

    public void getUnreadMsg(){
        GovApi.getNotice(1, 1, 1, new RequestCallback<List<MessageInfo>>() {
            @Override
            public void onSuccess(List<MessageInfo> result) {
                if(result.size()==0){
                    switchFragment(ALLMessage);
                }
            }

            @Override
            public void onFailure(ApiHttpException e) {

            }
        });
    }

    /**
     * 添加所有的fragment到FragmentManager中
     */
    private void addAllFragment() {
        if (mFManager == null) {
            mFManager = getActivity().getSupportFragmentManager();
        }
        mAllMessageFragment = new MessageFragment();
        Bundle all=new Bundle();
        all.putBoolean("isUnRead",false);
        mAllMessageFragment.setArguments(all);

        mUNReadMessageFragment =new MessageFragment();
        Bundle unRead=new Bundle();
        unRead.putBoolean("isUnRead",true);
        mUNReadMessageFragment.setArguments(unRead);
        FragmentTransaction transaction = mFManager.beginTransaction();
        transaction.add(R.id.real_content_gov_circular_activity, mUNReadMessageFragment, UNReadMessage);
        transaction.add(R.id.real_content_gov_circular_activity, mAllMessageFragment, ALLMessage);
        transaction.commit();
    }
    /**
     * 页面以及条目整体切换
     *
     * @param tag
     */
    private void switchFragment(String tag) {
        setFragment(tag);
        if (ALLMessage.equals(tag)) {
            onBtnChanged(allmsg);
            // 记录当前被选中的按钮的ID
            mSelectedBtnId = R.id.all_messages_gov_circular_activity;

        } else if (UNReadMessage.equals(tag)) {
            onBtnChanged(unread);
            mSelectedBtnId = R.id.unread_messages_gov_circular_activity;
        }
    }

    /**
     * 进行按钮切换
     *
     * @param tag
     */
    private void onBtnChanged(int tag) {
        if (allmsg == tag) {
            mAllMessagesBtn.setTextColor(getResources().getColor(
                    R.color.colorPrimaryGov));
            mUnreadMessagesBtn
                    .setTextColor(getResources().getColor(R.color.gray));
        } else if (unread == tag) {
            mAllMessagesBtn.setTextColor(getResources().getColor(R.color.gray));
            mUnreadMessagesBtn.setTextColor(getResources().getColor(
                    R.color.colorPrimaryGov));
        }
    }

    /**
     * 进行fragment切换
     * @param tag
     */
    private void setFragment(String tag) {
        if (mAllMessageFragment == null) {
            // 如果引用被置为空,则获取到其原有对象的引用, 防止对象引用被置空引起页面出现重叠的现象
            mAllMessageFragment = (MessageFragment) mFManager
                    .findFragmentByTag(ALLMessage);
        }
        if (mUNReadMessageFragment == null) {
            mUNReadMessageFragment = (MessageFragment) mFManager
                    .findFragmentByTag(UNReadMessage);
        }
        FragmentTransaction transaction = mFManager.beginTransaction();
        // 显示指定页面,影藏对应的页面
        if (ALLMessage.equals(tag)) {
            transaction.show(mAllMessageFragment);
            transaction.hide(mUNReadMessageFragment);

        } else if (UNReadMessage.equals(tag)) {
            transaction.show(mUNReadMessageFragment);
            transaction.hide(mAllMessageFragment);
        }
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.all_messages_gov_circular_activity:
                if (mSelectedBtnId == R.id.all_messages_gov_circular_activity) {
                    return;
                }
                switchFragment(ALLMessage);
                break;
            case R.id.unread_messages_gov_circular_activity:
                if (mSelectedBtnId == R.id.unread_messages_gov_circular_activity) {
                    return;
                }
                switchFragment(UNReadMessage);
                break;

            default:
                break;
        }
    }
}
