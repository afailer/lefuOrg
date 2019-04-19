package com.lefuorgn.gov.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.GovApi;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.bean.OrgInfo;
import com.lefuorgn.bean.User;
import com.lefuorgn.dialog.AlertDialog;
import com.lefuorgn.gov.Utils.JsonToBeanUtil;
import com.lefuorgn.gov.adapter.ChoseOrgnizationAdapter;
import com.lefuorgn.gov.bean.GovOrgInfo;
import com.lefuorgn.gov.bean.GovOrgInfoItem;
import com.lefuorgn.lefu.MainActivity;
import com.lefuorgn.util.DividerItemDecoration;
import com.lefuorgn.util.TLog;
import com.lefuorgn.widget.tree.adapter.TreeListViewAdapter;
import com.lefuorgn.widget.tree.bean.Node;

import java.util.ArrayList;
import java.util.List;


public class ChoseOrganizationActivity extends BaseActivity {

    RecyclerView mTree;
    ChoseOrgnizationAdapter mChoseOrganizationAdapter;
    private String mOrgInfo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chose_organization;
    }

    @Override
    protected void initView() {
        mTree = (RecyclerView) findViewById(R.id.chose_gov_recycle);
        mTree.setLayoutManager(new LinearLayoutManager(this));
        mTree.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST,
                getResources().getColor(R.color.recycler_view_item_division_color)));
        findViewById(R.id.seek_gov_org_activity).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        setToolBarTitle("机构选择");
        User user = AppContext.getInstance().getUser();
        if(user.isGroup()) {
            // 集团优先级最高
            loadOrg(user.getGroupOrg_id());
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
            loadOrg(user.getGovOrg_id());
        }
    }

    /**
     * 获取机构信息集合
     */
    public void loadOrg(long id){
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
            }
        });
    }

    /**
     * 初始化Adapter
     * @param list 要展示的数据
     */
    private void initAdapter(List<GovOrgInfoItem> list) {
        try{
            mChoseOrganizationAdapter = new ChoseOrgnizationAdapter(R.layout.gov_organization,mTree, list, 0);
            mChoseOrganizationAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
                @Override
                public void onClick(final Node node, View view, int position) {
                    if (node.isLeaf()) {
                        chose(node.getName(), node.getcId());
                    }
                }
            });
        }catch (Exception e){
            TLog.error(e.toString());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.seek_gov_org_activity :
                Intent intent = new Intent(getApplicationContext(), SeekOrgActivity.class);
                intent.putExtra("orgInfo",mOrgInfo);
                startActivityForResult(intent,100);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 600 && data != null) {
            long id = data.getLongExtra("id", 0);
            String orgName = data.getStringExtra("orgName");
            chose(orgName, id);
        }
    }

    /**
     * 选择要跳转的机构页面
     * @param orgName 机构名称
     * @param orgId 机构ID
     */
    private void chose(final String orgName, final long orgId){
        new AlertDialog()
                .setContent("是否进入\""+orgName+"\"管理平台")
                .setClickCallBack(new AlertDialog.ClickCallBack() {
                    @Override
                    public void cancel() {

                    }

                    @Override
                    public void confirm() {
                        skipMainActivity(orgId, orgName);
                    }
                }).show(getSupportFragmentManager(),"");
    }

    /**
     * 跳转到机构页面
     * @param id 机构ID
     * @param name 机构名称
     */
    private void skipMainActivity(final long id, final String name) {
        showLoadingDialog();
        GovApi.isHasSkipPermission(id, new RequestCallback<String>() {
            @Override
            public void onSuccess(String result) {
                hideLoadingDialog();
                Intent intent = new Intent(ChoseOrganizationActivity.this, MainActivity.class);
                intent.putExtra(MainActivity.FIRST_LEVEL_PAGE, false);
                intent.putExtra(MainActivity.INTENT_AGENCY_ID, id);
                intent.putExtra(MainActivity.INTENT_AGENCY_NAME, name);
                startActivity(intent);
            }

            @Override
            public void onFailure(ApiHttpException e) {
                showToast(e.getMessage());
                hideLoadingDialog();
            }
        });
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
