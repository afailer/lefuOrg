package com.lefuorgn.lefu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lefuorgn.AppContext;
import com.lefuorgn.AppLogin;
import com.lefuorgn.R;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.base.BaseFragment;
import com.lefuorgn.dialog.AlertDialog;
import com.lefuorgn.lefu.activity.SettingAboutCloudPlatformActivity;
import com.lefuorgn.lefu.activity.SettingAccountActivity;
import com.lefuorgn.lefu.activity.SettingPasswordUpdateActivity;
import com.lefuorgn.lefu.activity.SettingSyncActivity;
import com.lefuorgn.util.ShareUtils;
import com.lefuorgn.util.UpdateUtils;

/**
 * 设置页面
 */

public class SettingFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        AppContext context = AppContext.getInstance();
        ((TextView) view.findViewById(R.id.tv_fragment_setting_org_name))
                .setText(AppContext.getInstance().getAgencyName());
        ((TextView) view.findViewById(R.id.tv_fragment_setting_version))
                .setText(String.format("ver: %s", context.getPackageInfo().versionName));
        view.findViewById(R.id.tv_fragment_setting_account).setOnClickListener(this);
        view.findViewById(R.id.tv_fragment_setting_password_update).setOnClickListener(this);
        view.findViewById(R.id.tv_fragment_setting_sync).setOnClickListener(this);
        view.findViewById(R.id.tv_fragment_setting_clear_data).setOnClickListener(this);
        view.findViewById(R.id.tv_fragment_setting_check_for_updates).setOnClickListener(this);
        view.findViewById(R.id.tv_fragment_setting_about_cloud_platform).setOnClickListener(this);
        view.findViewById(R.id.tv_fragment_setting_share).setOnClickListener(this);
        view.findViewById(R.id.tv_fragment_setting_sign_out).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tv_fragment_setting_account:
                // 账户设置
                intent = new Intent(getActivity(), SettingAccountActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_fragment_setting_password_update:
                // 修改密码
                intent = new Intent(getActivity(), SettingPasswordUpdateActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_fragment_setting_sync:
                // 同步设置
                intent = new Intent(getActivity(), SettingSyncActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_fragment_setting_clear_data:
                // 清除数据
                AppContext.getInstance().cleanAllInfo();
                intent = new Intent(getActivity(), AppLogin.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.tv_fragment_setting_check_for_updates:
                // 检查更新
                UpdateUtils uu = new UpdateUtils((BaseActivity) getActivity(), true);
                uu.checkUpdate();
                break;
            case R.id.tv_fragment_setting_about_cloud_platform:
                // 关于云平台
                intent = new Intent(getActivity(), SettingAboutCloudPlatformActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_fragment_setting_share:
                // 分享
                ShareUtils.share((BaseActivity) getActivity(),"孝心无界·关爱随行","关注不在身边的至亲，尽在乐福App！","","",true);
                break;
            case R.id.tv_fragment_setting_sign_out:
                // 退出
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

                break;
        }
    }
}
