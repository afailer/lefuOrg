package com.lefuorgn.gov.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.lefuorgn.gov.Utils.GovUtils;
import com.lefuorgn.gov.activity.OrgNewsActivity;
import com.lefuorgn.gov.bean.LeaderNews;
import com.lefuorgn.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 民生政话内容列表页面
 */

public class LeaderNewsFragment extends BaseRecyclerViewFragment<LeaderNews>{

    private RelativeLayout mHeadView; // 新闻列表头信息
    private LeaderNews news;
    private long mTypeId; // 新闻类型ID
    private long mGovId; // 政府账号ID
    private double windowWidth;

    @Override
    protected int getItemLayoutId() {
        return R.layout.gov_item;
    }

    @Override
    protected void initChildData() {
        Bundle arguments = getArguments();
        mTypeId = arguments.getLong("typeId");
        mGovId = arguments.getLong("govId");
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        windowWidth = metrics.widthPixels;
    }

    @Override
    protected void loadData(final int pageNo) {
        GovApi.getOrgNews(mGovId, mTypeId, pageNo,10, new RequestCallback<List<LeaderNews>>() {
            @Override
            public void onSuccess(List<LeaderNews> result) {
                if(pageNo == 1) {
                    if(result != null && result.size() >= 1) {
                        news = result.get(0);
                        result.remove(0);
                    }
                }
                setResult(pageNo, result);
            }

            @Override
            public void onFailure(ApiHttpException e) {
                setResult(pageNo, new ArrayList<LeaderNews>());
            }
        });
    }

    private void initHeader(final LeaderNews leaderNews){
        ImageLoader.loadImg(leaderNews.getPicture(), (ImageView) mHeadView.findViewById(R.id.gov_header_head_img));
        ((TextView)mHeadView.findViewById(R.id.gov_header_headline))
                .setText(leaderNews.getTheme());
        ((TextView)mHeadView.findViewById(R.id.gov_header_date_day))
                .setText(StringUtils.getFormatData(leaderNews.getCreate_dt(), "dd"));
        ((TextView)mHeadView.findViewById(R.id.gov_header_date_month))
                .setText(StringUtils.getFormatData(leaderNews.getCreate_dt(), "MM月"));
        ((TextView)mHeadView.findViewById(R.id.gov_header_date_week))
                .setText(StringUtils.getWeekOfTime(leaderNews.getCreate_dt()));
        mHeadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLeaderNewActivity(leaderNews);
            }
        });

    }


    @Override
    protected void convert(BaseViewHolder baseViewHolder, LeaderNews leaderNews) {
        ImageView img = baseViewHolder.getView(R.id.item_img);
        ImageLoader.loadImg(leaderNews.getPicture(), img);
        baseViewHolder.setText(R.id.item_title,leaderNews.getTheme())
                .setText(R.id.item_holdtime, StringUtils.getFormatData(leaderNews.getCreate_dt(), "yyyy年MM月dd日"));
        TextView type = baseViewHolder.getView(R.id.item_type);
        type.setText(leaderNews.getType_name());
        type.setBackgroundColor(GovUtils.getInstance().getColor(leaderNews.getType()));
    }

    @Override
    protected void initListener(final BaseRecyclerViewFragmentAdapter baseAdapter) {
        super.initListener(baseAdapter);
        if(news == null){
            return;
        }
        mHeadView = (RelativeLayout) View.inflate(getActivity(), R.layout.gov_list_header, null);
        mHeadView.setLayoutParams(new RecyclerView.LayoutParams((int)windowWidth,(int)(windowWidth*0.6)));
        initHeader(news);
        baseAdapter.addHeaderView(mHeadView);
        baseAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                LeaderNews leaderNews = (LeaderNews) baseAdapter.getData().get(i);
                startLeaderNewActivity(leaderNews);
            }
        });
    }

    /**
     * 跳转到新闻详情页面
     * @param leaderNews 新闻条目信息
     */
    private void startLeaderNewActivity(LeaderNews leaderNews){
        String sb = "lefuyun/leaderNewsCtr/toInfoPage?id=" +
                leaderNews.getId() + "&uid=" +
                AppContext.getInstance().getUser().getUser_id();
        Intent intent = new Intent(getActivity(), OrgNewsActivity.class);
        intent.putExtra("url", sb);
        intent.putExtra("extra", leaderNews);
        startActivity(intent);
    }

}
