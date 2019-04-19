package com.lefuorgn.gov.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.GovApi;
import com.lefuorgn.api.remote.ImageLoader;
import com.lefuorgn.base.BaseRecyclerViewActivity;
import com.lefuorgn.gov.bean.OrgActive;
import com.lefuorgn.util.StringUtils;

import java.util.List;

/**
 * 机构活动页面
 */
public class AllOrgActiveActivity extends BaseRecyclerViewActivity<OrgActive> {

    private RelativeLayout mHeadView;
    private OrgActive orgActive;
    private double windowWidth;
    private long mGroupId; // 集团账号ID

    @Override
    protected int getItemLayoutId() {
        return R.layout.gov_item;
    }

    @Override
    protected void initChildData() {
        setToolBarTitle("机构活动");
        mGroupId = getIntent().getLongExtra("groupId", 0);
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        windowWidth = metrics.widthPixels;
    }

    @Override
    protected void loadData(final int pageNo) {
        GovApi.getOrgActivities(mGroupId, pageNo, 10, new RequestCallback<List<OrgActive>>() {
            @Override
            public void onSuccess(List<OrgActive> result) {
                if(pageNo == 1) {
                    if(result != null && result.size() >= 1) {
                        orgActive= result.get(0);
                        result.remove(0);
                    }
                }
                setResult(pageNo,result);
            }
            @Override
            public void onFailure(ApiHttpException e) {

            }
        });
    }
    private void initHeader(final OrgActive orgActive){
        ImageView headImg= (ImageView) mHeadView.findViewById(R.id.gov_header_head_img);
        TextView headTheme = (TextView) mHeadView.findViewById(R.id.gov_header_headline);
        TextView dateDay= (TextView) mHeadView.findViewById(R.id.gov_header_date_day);
        TextView dateMonth= (TextView) mHeadView.findViewById(R.id.gov_header_date_month);
        TextView dateWeek= (TextView) mHeadView.findViewById(R.id.gov_header_date_week);
        mHeadView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String url= "lefuyun/agencyActivites/toInfoPage"+"?id="+orgActive.getId()+"&version=160427";
                Intent intent=new Intent(getApplicationContext(),OrgActiveActivity.class);
                intent.putExtra("url",url);
                intent.putExtra("extra",orgActive);
                startActivity(intent);
            }
        });
        ImageLoader.loadImg(orgActive.getPic(),headImg);
        headTheme.setText(orgActive.getTheme());
        dateDay.setText(StringUtils.getFormatData(orgActive.getHoldTime(), "dd"));
        dateMonth.setText(StringUtils.getFormatData(orgActive.getHoldTime(), "MM月"));
        dateWeek.setText(StringUtils.getWeekOfTime(orgActive.getHoldTime()));
    }

    @Override
    protected void initListener(final BaseAdapter baseAdapter) {
        if(orgActive==null){
            return;
        }
        mHeadView = (RelativeLayout) View.inflate(getApplicationContext(), R.layout.gov_list_header, null);
        mHeadView.setLayoutParams(new RecyclerView.LayoutParams((int)windowWidth,(int)(windowWidth*0.6)));
        initHeader(orgActive);
        baseAdapter.addHeaderView(mHeadView);
        baseAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                OrgActive orgActive = (OrgActive) baseAdapter.getData().get(i);
                String url= "lefuyun/agencyActivites/toInfoPage"+"?id="+orgActive.getId()+"&version=160427";
                Intent intent=new Intent(getApplicationContext(),OrgActiveActivity.class);
                intent.putExtra("url",url);
                intent.putExtra("extra",orgActive);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void convert(BaseViewHolder holder, OrgActive orgActive) {
        ImageView img = holder.getView(R.id.item_img);
        ImageLoader.loadImg(orgActive.getPic(), img);
        holder.setText(R.id.item_title, orgActive.getTheme())
                .setText(R.id.item_holdtime, StringUtils.getFormatData(orgActive.getHoldTime(), "yyyy年MM月dd日"))
                .setVisible(R.id.item_type, false);
    }

    @Override
    protected boolean hasToolBar() {
        return true;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

}
