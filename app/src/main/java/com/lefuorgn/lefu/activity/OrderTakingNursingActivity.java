package com.lefuorgn.lefu.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.api.common.Json;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.db.base.BaseDao;
import com.lefuorgn.db.model.basic.AllocatingTypeTask;
import com.lefuorgn.db.model.basic.OldPeople;
import com.lefuorgn.db.util.AllocatingTypeTaskManager;
import com.lefuorgn.db.util.DaoHelper;
import com.lefuorgn.db.util.OldPeopleManager;
import com.lefuorgn.dialog.AlertDialog;
import com.lefuorgn.lefu.bean.AllocatingTaskExecute;
import com.lefuorgn.lefu.bean.AllocatingTaskExecuteOption;
import com.lefuorgn.lefu.fragment.OrderTakingNursingFragment;

import java.util.ArrayList;
import java.util.List;

import static com.lefuorgn.db.util.AllocatingTypeTaskManager.deleteAllocatingTypeTaskById;

/**
 * 护理服务接单页面
 */

public class OrderTakingNursingActivity extends BaseActivity {

    // 当前显示用户接单页面
    public final String ACCEPT_FRAGMENT = "ACCEPT";
    // 当前显示用户配单页面
    public final String RETURN_FRAGMENT = "RETURN";
    // 记录当前被点击按钮的id
    private int mBtnId;
    private TextView mMenuView;
    private FragmentManager mFManager;
    // 接单、退还配单以及actionBar上的提交按钮
    private TextView mAcceptBtn, mReturnBtn;
    // 展示接单页面的fragment
    private OrderTakingNursingFragment mAcceptFragment;
    // 展示退还配单页面的fragment
    private OrderTakingNursingFragment mReturnFragment;

    private long mUserId;
    private BaseDao<AllocatingTypeTask, Long> mDao;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_taking_nursing;
    }

    @Override
    protected void initView() {
        // 初始化按钮控件
        mAcceptBtn = (TextView) findViewById(R.id.btn_activity_order_taking_nursing_accept);
        mReturnBtn = (TextView) findViewById(R.id.btn_activity_order_taking_nursing_cancel);
        mAcceptBtn.setOnClickListener(this);
        mReturnBtn.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mMenuView = setMenuTextView("接收");
        mUserId = AppContext.getInstance().getUser().getUser_id();
        mDao = DaoHelper.getInstance().getDao(AllocatingTypeTask.class);
        // 管理所有的fragment
        addAllFragment();
        // 设置默认的Fragment
        setFragment(ACCEPT_FRAGMENT);
    }

    /**
     * 添加所有的fragment到FragmentManager中
     */
    private void addAllFragment() {
        if(mFManager == null) {
            mFManager = getSupportFragmentManager();
        }
        mAcceptFragment = (OrderTakingNursingFragment) Fragment
                .instantiate(this, OrderTakingNursingFragment.class.getName(), getBundle(1));
        mReturnFragment = (OrderTakingNursingFragment) Fragment
                .instantiate(this, OrderTakingNursingFragment.class.getName(), getBundle(2));
        FragmentTransaction transaction = mFManager.beginTransaction();
        transaction.add(R.id.fl_activity_order_taking_nursing, mAcceptFragment, ACCEPT_FRAGMENT);
        transaction.add(R.id.fl_activity_order_taking_nursing, mReturnFragment, RETURN_FRAGMENT);
        transaction.commit();
    }

    private Bundle getBundle(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(OrderTakingNursingFragment.BUNDLE_ORDER_TASK_TYPE, type);
        return bundle;
    }

    /**
     * 进行fragment切换
     * @param tag 当前页面标记
     */
    private void setFragment(String tag) {
        if(mAcceptFragment == null) {
            // 如果引用被置为空,则获取到其原有对象的引用, 防止对象引用被置空引起页面出现重叠的现象
            mAcceptFragment = (OrderTakingNursingFragment) mFManager
                    .findFragmentByTag(ACCEPT_FRAGMENT);
        }
        if(mReturnFragment == null) {
            mReturnFragment = (OrderTakingNursingFragment) mFManager
                    .findFragmentByTag(RETURN_FRAGMENT);
        }
        FragmentTransaction transaction = mFManager.beginTransaction();
        // 显示指定页面,影藏对应的页面
        if(ACCEPT_FRAGMENT.equals(tag)) {
            mBtnId = R.id.btn_activity_order_taking_nursing_accept;
            transaction.show(mAcceptFragment);
            transaction.hide(mReturnFragment);
            // 设置标题
            setToolBarTitle("护理任务-接单");
            mMenuView.setText("接收");
            // 按钮被选中的状态
            mAcceptBtn.setTextColor(Color.WHITE);
            mAcceptBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            // 按钮的默认状态
            mReturnBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
            mReturnBtn.setBackgroundColor(Color.parseColor("#D9D9D9"));

        }else if(RETURN_FRAGMENT.equals(tag)) {
            mBtnId = R.id.btn_activity_order_taking_nursing_cancel;
            transaction.show(mReturnFragment);
            transaction.hide(mAcceptFragment);
            setToolBarTitle("护理任务-退还派单");
            mMenuView.setText("退还");
            mAcceptBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
            mAcceptBtn.setBackgroundColor(Color.parseColor("#D9D9D9"));
            mReturnBtn.setTextColor(Color.WHITE);
            mReturnBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_activity_order_taking_nursing_accept:
                // 接收派单按钮
                if(mBtnId == R.id.btn_activity_order_taking_nursing_accept) {
                    // 如果页面没有进行切换则返回
                    return;
                }
                // 切换Fragment
                setFragment(ACCEPT_FRAGMENT);
                break;
            case R.id.btn_activity_order_taking_nursing_cancel:
                // 退还派单按钮
                if(mBtnId == R.id.btn_activity_order_taking_nursing_cancel) {
                    // 如果页面没有进行切换则返回
                    return;
                }
                // 切换Fragment
                setFragment(RETURN_FRAGMENT);
                break;
        }
    }

    @Override
    protected void onMenuClick(View v) {
        // 接单或者配单
        if(mBtnId == R.id.btn_activity_order_taking_nursing_accept) {
            // 当前页面是接单页面
            showToast("接收配单");
            if(mAcceptFragment == null) {
                // 如果引用被置为空,则获取到其原有对象的引用, 防止对象引用被置空引起页面出现重叠的现象
                mAcceptFragment = (OrderTakingNursingFragment) mFManager
                        .findFragmentByTag(ACCEPT_FRAGMENT);
            }
            List<AllocatingTaskExecute> list = mAcceptFragment.getOrderTakingNursing();
            if(list.size() == 0) {
                showToast("没有您的配单");
                return;
            }
            acceptAllocatingTask(list);
        }else if(mBtnId == R.id.btn_activity_order_taking_nursing_cancel) {
            // 退还配单
            showToast("退还配单");
            if(mReturnFragment == null) {
                mReturnFragment = (OrderTakingNursingFragment) mFManager
                        .findFragmentByTag(RETURN_FRAGMENT);
            }
            List<AllocatingTaskExecute> list = mReturnFragment.getOrderTakingNursing();
            if(list.size() == 0) {
                showToast("没有退还配单");
                return;
            }
            returnAllocatingTask(list);
        }
    }

    /**
     * 接收配单
     */
    private void acceptAllocatingTask(List<AllocatingTaskExecute> list) {
        List<AllocatingTaskExecute> data = filterAllocatingTask(list);
        if(data.size() > 0) {
            if(filterOldPeople(data)) {
                AllocatingTypeTaskManager.deleteAllocatingTypeTask(data);
                LefuApi.receiveAllocatingTypeTask(mUserId, getIds(data)
                        , new RequestCallback<List<AllocatingTypeTask>>() {
                    @Override
                    public void onSuccess(List<AllocatingTypeTask> result) {
                        mDao.insertList(result);
                        showToast("接单成功");
                        if(mAcceptFragment == null) {
                            mAcceptFragment = (OrderTakingNursingFragment) mFManager
                                    .findFragmentByTag(ACCEPT_FRAGMENT);
                        }
                        if(mReturnFragment == null) {
                            mReturnFragment = (OrderTakingNursingFragment) mFManager
                                    .findFragmentByTag(RETURN_FRAGMENT);
                        }
                        mAcceptFragment.updateData();
                        mReturnFragment.updateData();
                    }

                    @Override
                    public void onFailure(ApiHttpException e) {
                        showToast(e.getMessage());
                    }
                });
            }else {
                // 老人不匹配
                new AlertDialog()
                        .setContent("检测到您所接单的老人，本地数据库中没有，请先同步数据后再接单")
                        .setCancelBtnText("取消")
                        .setConfirmBtnText("确认")
                        .isCancelOutside(false)
                        .setClickCallBack(new AlertDialog.ClickCallBack() {
                            @Override
                            public void cancel() {}

                            @Override
                            public void confirm() {
                                // TODO 这里进行数据同步
                                showToast("进行数据同步");
                            }
                        })
                        .show(getSupportFragmentManager(), "AlertDialog");
            }
        }else {
            showToast("请选择要接收的派单");
        }
    }

    /**
     * 退还配单
     */
    private void returnAllocatingTask(List<AllocatingTaskExecute> list) {
        List<AllocatingTaskExecute> data = filterAllocatingTask(list);
        if(data.size() > 0) {
            showToast("存在退还配单");
            // 获取要提交的数据
            final List<AllocatingTypeTask> confirmList = getAllocatingTypeTask(data);
            LefuApi.submitAllocatingTypeTask(getJson(confirmList), new RequestCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    // 保存成功
                    showToast("派单退还成功");
                    // 删除数据库中指定的配单
                    deleteAllocatingTypeTaskById(confirmList);
                    if(mAcceptFragment == null) {
                        mAcceptFragment = (OrderTakingNursingFragment) mFManager
                                .findFragmentByTag(ACCEPT_FRAGMENT);
                    }
                    if(mReturnFragment == null) {
                        mReturnFragment = (OrderTakingNursingFragment) mFManager
                                .findFragmentByTag(RETURN_FRAGMENT);
                    }
                    mAcceptFragment.updateData();
                    mReturnFragment.updateData();
                }

                @Override
                public void onFailure(ApiHttpException e) {
                    // 保存失败
                    showToast(e.getMessage());
                }
            });
        }else {
            showToast("请选择退还派单");
        }
    }

    /**
     * 获取要提交的配单信息
     */
    private List<AllocatingTypeTask> getAllocatingTypeTask(List<AllocatingTaskExecute> data) {
        List<AllocatingTypeTask> resultData = new ArrayList<AllocatingTypeTask>();
        long time = System.currentTimeMillis();
        for (AllocatingTaskExecute execute : data) {
            for (AllocatingTaskExecuteOption option : execute.getOptions()) {
                AllocatingTypeTask task = new AllocatingTypeTask();
                // 设置必要的参数
                task.set_id(option.get_id());
                task.setId(option.getId());
                task.setNumber_current(option.getComplete());
                // 将其置成未接单状态
                task.setTask_state(1);
                task.setCare_worker(mUserId);
                task.setUpdate_time(time);
                task.setRemark(option.getRemark());
                // 将当前bean添加到集合中
                resultData.add(task);
            }
        }
        return resultData;
    }

    /**
     * 获取配单中老人id集合
     * @param data 配单信息集合
     */
    private String getIds(List<AllocatingTaskExecute> data) {
        List<Long> taskIdList = new ArrayList<Long>();
        for (AllocatingTaskExecute execute : data) {
            taskIdList.add(execute.getOld_people_id());
        }
        String ids = taskIdList.toString();
        return ids.substring(1, ids.length() - 1);
    }

    /**
     * 过滤老人数据, 配单老人数据和本地数据库的老人数据过滤
     * @param data 配单信息集合
     */
    private boolean filterOldPeople(List<AllocatingTaskExecute> data) {
        List<Long> taskIdList = new ArrayList<Long>();
        List<Long> idList = new ArrayList<Long>();
        // 获取所有的老人
        List<OldPeople> list = OldPeopleManager.getOldPeople(false);
        for (OldPeople oldPeople : list) {
            idList.add(oldPeople.getId());
        }
        for (AllocatingTaskExecute execute : data) {
            taskIdList.add(execute.getOld_people_id());
        }
        return idList.containsAll(taskIdList);
    }

    /**
     * 筛选被选中的条目配单
     */
    private List<AllocatingTaskExecute> filterAllocatingTask(List<AllocatingTaskExecute> list) {
        List<AllocatingTaskExecute> data = new ArrayList<AllocatingTaskExecute>();
        for (AllocatingTaskExecute task : list) {
            if(task.isChecked()) {
                data.add(task);
            }
        }
        return data;
    }

    /**
     * 将配单任务列表转换成json数据
     * @param list 要转换的列表
     * @return json数据格式
     */
    protected String getJson(List<AllocatingTypeTask> list) {
        return Json.getGson().toJson(list);
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
