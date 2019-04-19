package com.lefuorgn.gov.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.GovApi;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.bean.OrgInfo;
import com.lefuorgn.bean.User;
import com.lefuorgn.gov.Utils.JsonToBeanUtil;
import com.lefuorgn.gov.adapter.GovOrganizationAdapter;
import com.lefuorgn.gov.bean.GovOrgInfo;
import com.lefuorgn.gov.bean.GovOrgInfoItem;
import com.lefuorgn.util.DividerItemDecoration;
import com.lefuorgn.util.TLog;
import com.lefuorgn.util.ToastUtils;
import com.lefuorgn.widget.tree.adapter.TreeListViewAdapter;
import com.lefuorgn.widget.tree.bean.Node;

import java.util.ArrayList;
import java.util.List;

public class GovOrganizationActivity extends BaseActivity {

    private RecyclerView mTree;
    private GovOrganizationAdapter mOrganizationAdapter;
    private String mOrgInfo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gov_organization;
    }

    @Override
    protected void initView() {
        findViewById(R.id.submit_gov_org_activity).setOnClickListener(this);
        findViewById(R.id.seek_gov_org_activity).setOnClickListener(this);
        mTree= (RecyclerView) findViewById(R.id.gov_chose_org_recycle);
        mTree.setLayoutManager(new LinearLayoutManager(this));
        mTree.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST,
                getResources().getColor(R.color.recycler_view_item_division_color)));
    }

    @Override
    protected void initData() {
        User user = AppContext.getInstance().getUser();
        if(user.isGroup()) {
            // 集团优先级最高
            loadOrgInfo(user.getGroupOrg_id());
        }else if(user.getAgency_id() > 0) {
            // 机构优先级次之,
            List<GovOrgInfoItem> list = new ArrayList<GovOrgInfoItem>();
            for (OrgInfo orgInfo : user.getAgencys()) {
                GovOrgInfoItem item = new GovOrgInfoItem();
                item.setId(orgInfo.getAgency_id());
                item.setpId(0);
                item.setName(orgInfo.getAgency_name());
                item.setmId(orgInfo.getAgency_id());
                list.add(item);
            }
            initAdapter(list);
        }else {
            // 政府优先级最后
            loadOrgInfo(user.getGovOrg_id());
        }
    }

    private void loadOrgInfo(long id){
        showWaitDialog();
        GovApi.getOrgInfo(id, new RequestCallback<GovOrgInfo>() {
            @Override
            public void onSuccess(GovOrgInfo result) {
                hideWaitDialog();
                mOrgInfo = result.getAgencyInfos();
                List<GovOrgInfoItem> list = JsonToBeanUtil.getInstance().getGovOrgInfoItem(result);
                initAdapter(list);
            }

            @Override
            public void onFailure(ApiHttpException e) {
                hideWaitDialog();
                ToastUtils.show(getApplicationContext(),e.getMessage());
            }
        });
    }

    /**
     * 初始化Adapter
     * @param list 要展示的数据
     */
    private void initAdapter(List<GovOrgInfoItem> list) {
        try{
            try {
                mOrganizationAdapter=new GovOrganizationAdapter(R.layout.item_fragment_gov_organization,mTree, list,0);
                /*mOrganizationAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
                    @Override
                    public void onClick(Node node, View view, int position) {
                        if(node.isLeaf()){
                            ImageView selector= (ImageView) view.findViewById(R.id.id_treenode_select_gov);
                            if(!node.isSelect()){
                                selector.setImageResource(R.mipmap.gov_select);
                            }else{
                                selector.setImageResource(R.mipmap.gov_normal);
                            }
                            mOrganizationAdapter.notifyDataSetChanged();
                        }
                    }
                });*/
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }catch (IllegalArgumentException e){
                e.printStackTrace();
            }
        }catch (Exception e){
            TLog.error(e.toString());
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.seek_gov_org_activity:
                // 搜索按钮点击触发事件
                Intent intent = new Intent(this, SeekOrgActivity.class);
                intent.putExtra("orgInfo", mOrgInfo);
                startActivityForResult(intent, 100);
                break;
            case R.id.submit_gov_org_activity:
                String ids = mOrganizationAdapter.getCheckedIds();
                if("".equals(ids)) {
                    ToastUtils.show(getApplicationContext(), "您没有选择任何机构");
                    return;
                }
                Intent data = new Intent();
                data.putExtra("ids", ids);
                setResult(800, data);
                finish();
                break;

            default:
                break;
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 600 && data != null) {
            long id = data.getLongExtra("id", 0);
            if(id > 0) {
                Intent intent = new Intent();
                intent.putExtra("ids", id + "");
                setResult(800, intent);
                finish();
            }
        }
    }
}
