package com.lefuorgn.lefu.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.api.common.Json;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.ImageLoader;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.bean.User;
import com.lefuorgn.db.model.basic.DisplaySignOrNursingItem;
import com.lefuorgn.db.model.basic.OldPeople;
import com.lefuorgn.db.util.SignConfigManager;
import com.lefuorgn.dialog.ListDialog;
import com.lefuorgn.lefu.adapter.MeasurementTaskAdapter;
import com.lefuorgn.db.model.basic.AllocatingTypeTask;
import com.lefuorgn.lefu.bean.Staff;
import com.lefuorgn.util.DividerItemDecoration;
import com.lefuorgn.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 今日工作, 测量任务页面
 */

public class MeasurementTaskActivity extends BaseActivity {

    private ImageView mHeadImg;
    private TextView mOldPeopleNameView, mOldPeopleSexAgeView;
    private TextView mBedInfoView, mRoomInfoView;
    private TextView mFloorInfoView, mBuildInfoView;
    private RecyclerView mRecyclerView;
    private TextView mStaffView;

    private long mTime;
    private OldPeople mOldPeople;
    private MeasurementTaskAdapter mAdapter;
    private List<DisplaySignOrNursingItem> mItems;
    private long mAgencyId; // 机构ID
    private User mUser; // 用户信息
    private String oldMeasureEmployee; // 旧测量员工
    private String newMeasureEmployee; // 新的测量员工
    private String measureEmployeeName; // 测量员工名称

    private List<Staff> staffList; // 员工列表
    // 员工选择Dialog
    private ListDialog<Staff> mDialog;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_measurement_task;
    }

    @Override
    protected void initView() {
        mHeadImg = (ImageView) findViewById(R.id.iv_activity_measurement_task);
        mOldPeopleNameView = (TextView) findViewById(R.id.tv_activity_measurement_task_name);
        mOldPeopleSexAgeView = (TextView) findViewById(R.id.tv_activity_measurement_task_sex_age);
        mBedInfoView = (TextView) findViewById(R.id.tv_activity_measurement_task_bed);
        mRoomInfoView = (TextView) findViewById(R.id.tv_activity_measurement_task_room);
        mFloorInfoView = (TextView) findViewById(R.id.tv_activity_measurement_task_floor);
        mBuildInfoView = (TextView) findViewById(R.id.tv_activity_measurement_task_build);
        findViewById(R.id.ll_activity_measurement_task).setOnClickListener(this);
        findViewById(R.id.btn_activity_measurement_task).setOnClickListener(this);
        mStaffView = (TextView) findViewById(R.id.tv_activity_measurement_task_staff);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_activity_measurement_task);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST,
                getResources().getColor(R.color.recycler_view_item_division_color)));
    }

    @Override
    protected void initData() {
        setToolBarTitle("测量任务");
        mTime = getIntent().getLongExtra("time", 0);
        mOldPeople = (OldPeople) getIntent().getSerializableExtra("oldPeople");
        mItems = SignConfigManager.getNursingItem();
        AppContext context = AppContext.getInstance();
        mAgencyId = context.getAgencyId();
        mUser = context.getUser();
        refreshOldPeopleInfoView();
        showWaitDialog();
        // 获取员工信息
        LefuApi.getStaffs(mAgencyId, new RequestCallback<List<Staff>>() {
            @Override
            public void onSuccess(List<Staff> result) {
                staffList = result;
            }

            @Override
            public void onFailure(ApiHttpException e) {
                staffList = new ArrayList<Staff>();
                showToast(e.getMessage());
            }
        });

        // 获取配单情况
        LefuApi.getAllocatingTypeTask(mOldPeople.getId(), mTime, new RequestCallback<List<AllocatingTypeTask>>() {
            @Override
            public void onSuccess(List<AllocatingTypeTask> result) {
                hideWaitDialog();
                List<Long> list = new ArrayList<Long>();
                List<Long> allList = new ArrayList<Long>();
                for (DisplaySignOrNursingItem item : mItems) {
                    allList.add(item.getType());
                }
                if(result != null) {
                    for (AllocatingTypeTask task : result) {
                        list.add(task.getNursing_item_id());
                    }
                    if(result.size() > 0) {
                        oldMeasureEmployee = result.get(0).getCare_workers();
                        measureEmployeeName = result.get(0).getWorker_name();
                        mStaffView.setText(StringUtils.isEmpty(measureEmployeeName) ? "请选择" : measureEmployeeName);
                    }
                }else {
                    result = new ArrayList<AllocatingTypeTask>();
                }
                allList.removeAll(list);
                for (Long l : allList) {
                    AllocatingTypeTask task = new AllocatingTypeTask();
                    task.setAgency_id(mAgencyId);
                    task.setCare_workers(oldMeasureEmployee);
                    task.setWorker_name(measureEmployeeName);
                    task.setOld_people_id(mOldPeople.getId());
                    task.setOld_people_name(mOldPeople.getElderly_name());
                    task.setHead_nurse_id(mUser.getUser_id());
                    task.setNursing_item_id(l);
                    task.setContent(SignConfigManager.getNursingName(l));
                    task.setNumber_nursing(0);
                    task.setNumber_current(0);
                    task.setTask_time(mTime);
                    task.setTask_state(0);
                    task.setCreate_time(System.currentTimeMillis());
                    task.setUpdate_time(System.currentTimeMillis());
                    result.add(task);
                }
                initAdapter(result, "这里还没有内容哦~");
            }

            @Override
            public void onFailure(ApiHttpException e) {
                initAdapter(new ArrayList<AllocatingTypeTask>(), "数据加载失败");
                hideWaitDialog();
                showToast(e.getMessage());
            }
        });
    }

    /**
     * 刷新老人基本信息控件
     */
    private void refreshOldPeopleInfoView() {
        if(mOldPeople != null) {
            ImageLoader.loadCircleImg(mOldPeople.getIcon(), mHeadImg);
            mOldPeopleNameView.setText(mOldPeople.getElderly_name());
            mOldPeopleSexAgeView.setText(mOldPeople.getGender() == 15 ? "女  " : "男  "
                    + mOldPeople.getAge() + "岁");
            mBedInfoView.setText(String.format("床位: %s", mOldPeople.getBed_no()));
            mRoomInfoView.setText(String.format("房间: %s", mOldPeople.getRoom_no()));
            mFloorInfoView.setText(String.format("楼层: %s", mOldPeople.getFloor_layer()));
            mBuildInfoView.setText(String.format("楼栋: %s", mOldPeople.getFloor_no()));
        }
    }

    /**
     * 初始化适配器
     * @param list 适配器待添加数据
     */
    private void initAdapter(List<AllocatingTypeTask> list, String msg) {
        mAdapter = new MeasurementTaskAdapter(list);
        View view = getLayoutInflater().inflate(R.layout.item_recyclerview_empty,
                (ViewGroup) mRecyclerView.getParent(), false);
        TextView textView = (TextView) view.findViewById(R.id.item_recycler_view_item);
        textView.setText(msg);
        mAdapter.setEmptyView(view);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_activity_measurement_task:
                // 选择员工
                showStaffDialog();
                break;
            case R.id.btn_activity_measurement_task:
                // 提交按钮
                submit();
                break;
        }
    }

    /**
     * 显示员工Dialog
     */
    private void showStaffDialog() {
        if(mDialog == null) {
            mDialog = new ListDialog<Staff>();
            mDialog.setTitle("员工列表")
                    .setGravity(ListDialog.LEFT)
                    .setEmptyNote("没有员工");
            mDialog.setData(staffList);
            mDialog.setCallBack(new ListDialog.Callback<Staff>() {
                        @Override
                        public void convert(TextView view, Staff staff) {
                            view.setText(staff.getStaff_name());
                        }

                        @Override
                        public void onItemClick(View view, Staff staff) {
                            newMeasureEmployee = "," + staff.getUser_id() + ",";
                            measureEmployeeName = staff.getStaff_name();
                            mStaffView.setText(StringUtils.isEmpty(measureEmployeeName) ? "请选择" : measureEmployeeName);
                            List<AllocatingTypeTask> editData =  mAdapter.getData();
                            for (AllocatingTypeTask task : editData) {
                                task.setCare_workers(newMeasureEmployee);
                                task.setWorker_name(measureEmployeeName);
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    });
        }
        mDialog.show(getSupportFragmentManager(), "ListDialog");
    }

    /**
     * 提交数据
     */
    private void submit() {
        if(!StringUtils.isEmpty(newMeasureEmployee) || !StringUtils.isEmpty(oldMeasureEmployee)) {
            // 存在测量员工, 再判断测量员工是否发生变化
            // 保存要提交的数据
            List<AllocatingTypeTask> confirmList = new ArrayList<AllocatingTypeTask>();
            // 获取修改后的数据
            List<AllocatingTypeTask> editData =  mAdapter.getData();
            // 获取要提交的数据
            if(!StringUtils.isEmpty(newMeasureEmployee) && !newMeasureEmployee.equals(oldMeasureEmployee)) {
                // 员工发生变化
                for (AllocatingTypeTask task : editData) {
                    if(task.getNumber_nursing() != 0) {
                        // 测量数据不为0
                        confirmList.add(task);
                    }
                }
            }else {
                // 员工没有发生变化
                for (int i = 0; i < editData.size(); i++) {
                    AllocatingTypeTask task = editData.get(i);
                    if(mAdapter.getOldData().get(i) != task.getNumber_nursing()) {
                        confirmList.add(task);
                    }
                }
            }
            if(confirmList.size() > 0) {
                // 提交数据
                LefuApi.editAllocatingTypeTask(getJson(confirmList), new RequestCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        // 保存成功
                        showToast("保存成功");
                        setResult(200);
                        finish();
                    }

                    @Override
                    public void onFailure(ApiHttpException e) {
                        // 保存失败
                        showToast(e.getMessage());
                    }
                });
            }else {
                showToast("没有添加或修改的数据");
            }
        }else {
            showToast("请先选择员工");
        }
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
