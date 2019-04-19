package com.lefuorgn.lefu.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.ImageLoader;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.base.BaseRecyclerViewActivity;
import com.lefuorgn.lefu.bean.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * 院方活动页面
 */
public class EventActivity extends BaseRecyclerViewActivity<Event> {

    private long mAgencyId;

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_activity_event;
    }

    @Override
    protected void initChildData() {
        setToolBarTitle("院方活动");
        mAgencyId = AppContext.getInstance().getAgencyId();
    }

    @Override
    protected void initListener(final BaseRecyclerViewActivity<Event>.BaseAdapter baseAdapter) {
        baseAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Event event = (Event) baseAdapter.getData().get(position);
                Intent intent = new Intent(EventActivity.this, EventDetailsActivity.class);
                intent.putExtra("event", event);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void loadData(final int pageNo) {
        LefuApi.getEvent(mAgencyId, pageNo, new RequestCallback<List<Event>>() {
            @Override
            public void onSuccess(List<Event> result) {
                setResult(pageNo, result);
            }

            @Override
            public void onFailure(ApiHttpException e) {
                setResult(pageNo, new ArrayList<Event>());
                showToast(e.getMessage());
            }
        });
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Event event) {
        baseViewHolder.setText(R.id.tv_item_activity_event_theme, event.getTheme())
                .setText(R.id.tv_item_activity_event_content, event.getReserved())
                .setText(R.id.tv_item_activity_event_org_name, event.getAgency_name())
                .setText(R.id.tv_item_activity_event_time, event.getHoldTimeView());
        ImageLoader.loadImg(event.getPic(), (ImageView) baseViewHolder.getView(R.id.iv_item_activity_event));
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
