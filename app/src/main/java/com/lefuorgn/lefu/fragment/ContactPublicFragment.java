package com.lefuorgn.lefu.fragment;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.base.BaseRecyclerViewFragment;
import com.lefuorgn.lefu.bean.ContactPublic;

import java.util.ArrayList;
import java.util.List;

/**
 * 公用联系人页面
 */

public class ContactPublicFragment extends BaseRecyclerViewFragment<ContactPublic> {

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_fragment_contact_public;
    }

    @Override
    protected void loadData(final int pageNo) {
        LefuApi.getContactPublic(pageNo, new RequestCallback<List<ContactPublic>>() {
            @Override
            public void onSuccess(List<ContactPublic> result) {
                setResult(pageNo, result);
            }

            @Override
            public void onFailure(ApiHttpException e) {
                showToast(e.getMessage());
                setResult(pageNo, new ArrayList<ContactPublic>());
            }
        });
    }

    @Override
    protected void convert(BaseViewHolder holder, ContactPublic c) {
        holder.setText(R.id.tv_fragment_contact_public_name, c.getWork());
    }

    @Override
    protected void initListener(final BaseRecyclerViewFragmentAdapter baseAdapter) {
        baseAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                        + baseAdapter.getItem(i).getPhone()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}
