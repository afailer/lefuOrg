package com.lefuorgn.gov.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.lefuorgn.AppContext;
import com.lefuorgn.AppLogin;
import com.lefuorgn.R;
import com.lefuorgn.api.remote.ImageLoader;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.base.BaseFragment;
import com.lefuorgn.dialog.AlertDialog;
import com.lefuorgn.gov.adapter.SettingAdapter;
import com.lefuorgn.gov.bean.SettingItem;
import com.lefuorgn.lefu.activity.SettingAboutCloudPlatformActivity;
import com.lefuorgn.lefu.activity.SettingAccountActivity;
import com.lefuorgn.lefu.activity.SettingPasswordUpdateActivity;
import com.lefuorgn.lefu.activity.SettingSyncActivity;
import com.lefuorgn.util.DividerGridItemDecoration;
import com.lefuorgn.util.ShareUtils;
import com.lefuorgn.util.UpdateUtils;
import com.lefuorgn.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 政府页面个人设置
 */

public class SettingFragment extends BaseFragment{
    RecyclerView recyclerView;
    TextView settingName,versionName;
    RelativeLayout settingUser;
    SettingAdapter adapter;
    AppContext appContext=AppContext.getInstance();
    CircleImageView headImg;

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        recyclerView= (RecyclerView) view.findViewById(R.id.gov_setting_grid);
        recyclerView.addItemDecoration(new DividerGridItemDecoration(Color.parseColor("#d8d8d8"),1));
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(gridLayoutManager);
        versionName= (TextView) view.findViewById(R.id.version_name);
        settingName= (TextView) view.findViewById(R.id.setting_name);
        settingUser= (RelativeLayout) view.findViewById(R.id.set_user);
        headImg= (CircleImageView) view.findViewById(R.id.headImg);
        settingUser.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        ImageLoader.loadImgForUserDefinedView(appContext.getUser().getIcon(),headImg);
        versionName.setText(String.format("当前版本：%s", appContext.getPackageInfo().versionName));
        settingName.setText(appContext.getUser().getUser_name());
        adapter=new SettingAdapter(R.layout.grid_item,getSettingData());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.set_user:
                startActivity(new Intent(getActivity(), SettingAccountActivity.class));
                break;
        }
    }

    private List<SettingItem> getSettingData(){
        List<SettingItem> items=new ArrayList<SettingItem>();
        items.add(new SettingItem(R.mipmap.setting_gov_sync,"同步设置") {
            @Override
            public void click() {
                startActivity(new Intent(getActivity(),
                        SettingSyncActivity.class));
            }
        });
        items.add(new SettingItem(R.mipmap.setting_password,"修改密码") {
            @Override
            public void click() {
                startActivity(new Intent(getActivity(),
                        SettingPasswordUpdateActivity.class));
            }
        });
        items.add(new SettingItem(R.mipmap.setting_cleardata,"清除数据") {
            @Override
            public void click() {
                AppContext.getInstance().cleanAllInfo();
                Intent intent = new Intent(getActivity(), AppLogin.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        items.add(new SettingItem(R.mipmap.setting_gov_share,"分享") {
            @Override
            public void click() {
                ShareUtils.share((BaseActivity) getActivity(),"孝心无界·关爱随行","关注不在身边的至亲，尽在乐福App！","","",true);
            }
        });
        items.add(new SettingItem(R.mipmap.setting_lefu,"关于云平台") {
            @Override
            public void click() {
                // 关于云平台
                Intent intent = new Intent(getActivity(), SettingAboutCloudPlatformActivity.class);
                startActivity(intent);
            }
        });
        items.add(new SettingItem(R.mipmap.setting_check_update,"检查更新") {
            @Override
            public void click() {
                // 检查更新
                UpdateUtils uu = new UpdateUtils((BaseActivity) getActivity(), true);
                uu.checkUpdate();
            }
        });
        items.add(new SettingItem(R.mipmap.setting_exit,"退出") {
            @Override
            public void click() {
                new AlertDialog().setTitle("退出当前账号?")
                        .setContent("亲,退出时会清除数据,请先同步!")
                        .setClickCallBack(new AlertDialog.ClickCallBack() {
                            @Override
                            public void cancel() {}

                            @Override
                            public void confirm() {
                                AppContext.getInstance().cleanLoginInfo();
                                Intent intent = new Intent(getActivity(), AppLogin.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }).show(getFragmentManager(), "AlertDialog");
            }
        });

        return items;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.gov_fragment_setting;
    }
}
