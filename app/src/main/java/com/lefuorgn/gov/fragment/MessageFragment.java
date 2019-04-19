package com.lefuorgn.gov.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.GovApi;
import com.lefuorgn.api.remote.ImageLoader;
import com.lefuorgn.base.BaseRecyclerViewFragment;
import com.lefuorgn.bean.User;
import com.lefuorgn.gov.GovMainActivity;
import com.lefuorgn.gov.activity.MessageDetailActivity;
import com.lefuorgn.gov.bean.MessageInfo;
import com.lefuorgn.util.StringUtils;

import java.util.List;

/**
 * 通知公告下的每一个Message页
 */

public class MessageFragment extends BaseRecyclerViewFragment<MessageInfo>{

    public static final String MESSAGE_TYPE = "MESSAGE_TYPE";
    /**
     * 当前类型是全部消息
     */
    public static final int ALL_MESSAGE = 0;
    /**
     * 当前消息是未读消息
     */
    public static final int UNREAD_MESSAGE = 1;
    private int mCurrentType; // 当前fragment的类型
    private User userBean;
    private AppContext appContext;

    @Override
    protected void initChildData() {
        super.initChildData();
        Bundle bundle = getArguments();
        boolean isUnRead = bundle.getBoolean("isUnRead");
        if(isUnRead){
            mCurrentType=1;
        }else{
            mCurrentType=0;
        }
        appContext=AppContext.getInstance();
        userBean=appContext.getUser();
    }


    @Override
    protected int getItemLayoutId() {
        return R.layout.item_fragment_message;
    }

    @Override
    protected void loadData(final int pageNo) {
        GovApi.getNotice(mCurrentType, pageNo, 10, new RequestCallback<List<MessageInfo>>() {
            @Override
            public void onSuccess(List<MessageInfo> result) {
                setResult(pageNo,result);
            }

            @Override
            public void onFailure(ApiHttpException e) {

            }
        });
    }

    @Override
    protected void initListener(final BaseRecyclerViewFragmentAdapter baseAdapter) {
        super.initListener(baseAdapter);
        baseAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Intent intent=new Intent(getActivity(), MessageDetailActivity.class);
                intent.putExtra("extra",(MessageInfo)baseAdapter.getData().get(i));
                startActivity(intent);
                AnnouncementFragment.setReadState(((MessageInfo)baseAdapter.getData().get(i)).getId());
            }
        });
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, MessageInfo messageInfo) {
        TextView mDateView = baseViewHolder.getView(R.id.date_item_message_fragment);
        TextView mThemeView =  baseViewHolder.getView(R.id.theme_item_message_fragment);
        TextView mStatusView = baseViewHolder.getView(R.id.status_item_message_fragment);
        TextView mDateCreateView = baseViewHolder.getView(R.id.date_create_item_message_fragment);
        ImageView img = baseViewHolder.getView(R.id.img_item_message_fragment);
        TextView mName = baseViewHolder.getView(R.id.details_item_message_fragment);
        mDateView.setText(StringUtils.getFormatData(messageInfo.getCreate_dt(),"yyyy-MM-dd HH:mm:ss"));
        mThemeView.setText(messageInfo.getTheme());
        if(messageInfo.getStatus() == 1){
            mStatusView.setText("未读");
            ((GovMainActivity) getActivity()).setRedDotState(false);
            mStatusView.setBackgroundColor(Color.parseColor("#FFC3C9"));
        }else{
            mStatusView.setText("已读");
            mStatusView.setBackgroundColor(Color.parseColor("#EFEFF4"));
        }
        if(messageInfo.getType()==2){//政府
            for(int i=0;i<userBean.getGovOrg().size();i++){
                if(messageInfo.getType_id()==userBean.getGovOrg().get(i).getId()){
                    mName.setText(userBean.getGovOrg().get(i).getName());
                    break;
                }
            }
        }else if(messageInfo.getType()==1){//集团
            for(int i=0;i<userBean.getGroupOrg().size();i++){
                if(messageInfo.getType_id()==userBean.getGroupOrg().get(i).getId()){
                    mName.setText(userBean.getGroupOrg().get(i).getName());
                    break;
                }
            }
        }else if(messageInfo.getType()==3){//机构
            mName.setText(userBean.getAgencys().get(0).getAgency_name());
        }
        mDateCreateView.setText(StringUtils.getFormatData(messageInfo.getCreate_dt(),"M月d日"));
        ImageLoader.loadImg(messageInfo.getPicture(),img);
    }

    /**
     * 根据当前消息ID置消息的状态或者移除当前消息
     * @param id 未读消息ID
     */
    public void setReadState(long id) {
        List<MessageInfo> messageInfo = mBaseAdapter.getData();
        if(mCurrentType == ALL_MESSAGE) {
            // 当前页面是全部消息页面
            for (MessageInfo info : messageInfo) {
                if(id == info.getId()) {
                    // 将当前消息置为已读状态
                    info.setStatus(2);
                    mBaseAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }else if(mCurrentType == UNREAD_MESSAGE) {
            // 当前页面是未读消息页面
            MessageInfo info = null;
            int pos = 0;
            for (int i = 0; i < messageInfo.size(); i++) {
                if(id == messageInfo.get(i).getId()) {
                    // 获取当前未读消息
                    info = messageInfo.get(i);
                    pos = i;
                    break;
                }
            }
            if(info != null) {
                // 将当前消息移除
                mBaseAdapter.remove(pos);
                mBaseAdapter.notifyDataSetChanged();
            }
            if(messageInfo.size() == 0){
                ((GovMainActivity) getActivity()).setRedDotState(false);
                ((GovMainActivity) getActivity()).resetNoticeAll();
            }else{
                ((GovMainActivity) getActivity()).setRedDotState(true);
            }
        }

    }
}
