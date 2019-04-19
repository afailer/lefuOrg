package com.lefuorgn.lefu.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lefuorgn.R;
import com.lefuorgn.db.util.BedManager;
import com.lefuorgn.lefu.adapter.GridMenuRightAdapter;
import com.lefuorgn.lefu.base.BaseGridActivity;
import com.lefuorgn.lefu.bean.SearchConditionGrid;
import com.lefuorgn.util.DividerItemDecoration;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.util.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 */
public class GridMenuRightFragment extends Fragment implements OnClickListener{
	
	/**
	 * 主页面状态标记
	 */
	private final int MAIN_PAGE = 1;
	/**
	 * 选项页面状态标记
	 */
	private final int OPTIONS_PAGE = 2;
	/**
	 * 日期页面状态标记
	 */
	private final int DATE_PAGE = 3;

	private final String mFormat = "yyyy-MM-dd";

    /**
     * 楼栋选择状态标记
     */
	private final String BUILDING = "BUILDING";
    /**
     * 楼层选择状态标记
     */
	private final String UNIT = "UNIT";
    /**
     * 房间选择状态标记
     */
	private final String ROOM = "ROOM";
    /**
     * 记录当前选项的内容.item点击时使用;判断当前选择的是那个条件,如上面三个状态标记
     */
	private String OPTION = ""; 

	private LinearLayout mMainPage; // 主页面
	private LinearLayout mOptionsPage; // 选项页面
	private LinearLayout mDatePage; // 日期选择页面

	// 主页面控件初始化
	private BaseGridActivity mActivity;
    private TextView mBuildingView; // 楼栋号码
    private TextView mUnitView; // 楼层号码
    private TextView mRoomView; // 房间号号码
    private TextView mDateView; // 日期号码
    private ImageView mNameSortImg; // 按姓名排序是否选中状态图标
    private ImageView mRoomSortImg; // 按姓名排序是否选中状态图标

    private TextView mTitleView; // 选项页面标题控件
	private RecyclerView mRecyclerView; //

    private DatePicker mDatePicker; // 日期控件
	private GridMenuRightAdapter mAdapter;
	// 选项条件bean类
	private SearchConditionGrid mSearchCondition;
	private String mBufferDate; // 日历中用于缓存用户当前选择的日期
	// 创建日历数据变化监听事件对象
	private OnDateChangedListener mOnDateChangedListener;
	
	// 页面动画切换
	private Animation mLeftInAnimation; // 从左侧进入动画
	private Animation mLeftOutAnimation; // 从左侧移出动画
	private Animation mRightInAnimation; // 从右侧进入动画
	private Animation mRightOutAnimation; // 从右侧移出动画

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		mActivity = (BaseGridActivity) context;
		// 初始化动画
		mLeftInAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.slide_left_in);
		mLeftOutAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.slide_left_out);
		mRightInAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.slide_right_in);
		mRightOutAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.slide_right_out);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_grid_menu_right, container, false);
	}
	
	@SuppressLint("NewApi")
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			// 当前版本大于4.4
			view.setPadding(0, getStatusBarHeight(), 0, 0);
		}
		// 初始化主页面控件
		mMainPage = (LinearLayout) view.findViewById(R.id.ll_main_page_grid_menu);
		// 初始化选项页面
		mOptionsPage = (LinearLayout) view.findViewById(R.id.ll_options_page_grid_menu);
		// 影藏选项页面
		mOptionsPage.setVisibility(View.GONE);
		// 日期页面初始化
		mDatePage = (LinearLayout) view.findViewById(R.id.ll_date_page_grid_menu);
		// 影藏日期页面
		mDatePage.setVisibility(View.GONE);
		
		// 右侧拉主菜单控件对象初始化
        view.findViewById(R.id.tv_cancel_grid_menu).setOnClickListener(this);
        view.findViewById(R.id.tv_confirm_grid_menu).setOnClickListener(this);
        view.findViewById(R.id.rl_building_grid_menu).setOnClickListener(this);
        view.findViewById(R.id.rl_unit_grid_menu).setOnClickListener(this);
        view.findViewById(R.id.rl_room_grid_menu).setOnClickListener(this);
        view.findViewById(R.id.rl_date_grid_menu).setOnClickListener(this);
        view.findViewById(R.id.rl_name_sort_grid_menu).setOnClickListener(this);
        view.findViewById(R.id.rl_room_sort_grid_menu).setOnClickListener(this);
        view.findViewById(R.id.tv_clear_grid_menu).setOnClickListener(this);
		mBuildingView = (TextView) view.findViewById(R.id.tv_building_grid_menu_no);
		mUnitView = (TextView) view.findViewById(R.id.tv_unit_grid_menu_no);
		mRoomView = (TextView) view.findViewById(R.id.tv_room_grid_menu_no);
		mDateView = (TextView) view.findViewById(R.id.tv_date_grid_menu_no);
		mNameSortImg = (ImageView) view.findViewById(R.id.iv_name_img_grid_menu);
		// 解决有的手机可能俩个按钮同时显示的问题
        mNameSortImg.setImageResource(R.mipmap.select_right);
		mRoomSortImg = (ImageView) view.findViewById(R.id.iv_room_img_grid_menu);
		mRoomSortImg.setImageResource(0);

		// 初始化选项页面控件
        view.findViewById(R.id.tv_back_options_grid_menu).setOnClickListener(this);
		mTitleView = (TextView) view.findViewById(R.id.tv_title_options_grid_menu);

		mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_options_grid_menu);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST,
                getResources().getColor(R.color.recycler_view_item_division_color)));

		// 初始化日期页面控件
        view.findViewById(R.id.tv_back_date_grid_menu).setOnClickListener(this);
        view.findViewById(R.id.tv_confirm_date_grid_menu).setOnClickListener(this);
		mDatePicker = (DatePicker) view.findViewById(R.id.dp_date_grid_menu);

        // 设置日期改编事件
        String date = StringUtils.getFormatData(System.currentTimeMillis(), mFormat);
        int mYear = Integer.valueOf(date.substring(0, 4));
        int mMonth = Integer.valueOf(date.substring(5, 7)) - 1;
        int mDay = Integer.valueOf(date.substring(8, date.length()));
        mOnDateChangedListener = new OnDateChangedListener() {

            @SuppressLint("SimpleDateFormat")
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                // 获取一个日历对象，并初始化为当前选中的时间
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                // 缓存当前用户选择的日期
                mBufferDate = format.format(calendar.getTime());
            }
        };
        mDatePicker.setMaxDate(new Date().getTime());
        mDatePicker.init(mYear, mMonth, mDay, mOnDateChangedListener);

		initData();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
        mSearchCondition = new SearchConditionGrid();
		mSearchCondition.copySearchCondition(mActivity.getSearchCondition());
		// 初始化选项控件
		setOptionView(mSearchCondition);
		DrawerLayout drawerLayout = mActivity.getDrawerLayout();
		// 监听侧拉界面的状态
		drawerLayout.addDrawerListener(new DrawerListener() {
			
			@Override
			public void onDrawerStateChanged(int arg0) {
			}
			
			@Override
			public void onDrawerSlide(View arg0, float arg1) {
            }
			
			@Override
			public void onDrawerOpened(View arg0) {
            }
			
			@Override
			public void onDrawerClosed(View arg0) {
				// 侧拉界面关闭时。将侧拉界面置成主页面  并与activity中的选项条件同步
				// 点击空白地方关闭侧拉界面的情况进行的处理
				if(mMainPage.getVisibility() != View.VISIBLE) {
					mMainPage.setVisibility(View.VISIBLE);
					mOptionsPage.setVisibility(View.INVISIBLE);
					mDatePage.setVisibility(View.INVISIBLE);
				}
			}
		});
	}

    public boolean closePage() {
        // 返回键点击完成,处理相应的操作
        if(mMainPage.getVisibility() != View.VISIBLE) {
            // 侧拉菜单显示的不是主界面,则返回侧拉菜单主界面
            pageSwitching(MAIN_PAGE);
            return true;
        }else {
            // 当前显示的是主界面,返回activity的主页面
            return false;
        }
    }
	
	/**
	 * 初始化listView相关
	 */
	private void initData() {
		// 初始化listView中的数据
		mAdapter = new GridMenuRightAdapter(new ArrayList<String>());
		mRecyclerView.setAdapter(mAdapter);
		// 设置条目点击事件
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String no = (String) mAdapter.getData().get(position);
                if(BUILDING.equals(OPTION)) {
                    // 楼栋
                    // 显示选中的值
                    mBuildingView.setText(no);
                    // 设置楼层和房间号为空
                    mUnitView.setText("");
                    mRoomView.setText("");
                    // 记录当前的值
                    mSearchCondition.setBuildingNo(no);
                    mSearchCondition.setUnitNo("-1");
                    mSearchCondition.setRoomNo("-1");
                }else if(UNIT.equals(OPTION)) {
                    // 楼栋
                    // 显示选中的值
                    mUnitView.setText(no);
                    mRoomView.setText("");
                    // 记录当前的值
                    mSearchCondition.setUnitNo(no);
                    mSearchCondition.setRoomNo("-1");
                }else if(ROOM.equals(OPTION)) {
                    // 楼栋
                    // 显示选中的值
                    mRoomView.setText(no);
                    // 记录当前的值
                    mSearchCondition.setRoomNo(no);
                }
                // 切换到主页面
                pageSwitching(MAIN_PAGE);
            }
        });
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_cancel_grid_menu:
			// 侧拉菜单取消按钮
            // 还原数据,不进行保存
            mSearchCondition.copySearchCondition(mActivity.getSearchCondition());
            // 设置选项控件的值
            setOptionView(mSearchCondition);
			mActivity.closeDrawerLayout();
			break;
		case R.id.tv_confirm_grid_menu:
			// 侧拉菜单确定按钮
			// 将选项内容传递给activity
			mActivity.setInputDataOptions(mSearchCondition);
			// 关闭侧拉界面
			mActivity.closeDrawerLayout();
			break;
		case R.id.rl_building_grid_menu:
			// 侧拉菜单楼栋点击条目
			mTitleView.setText("选择楼栋号");
			// 记录选项的状态
			OPTION = BUILDING;
			// 加载数据
			loadingData(BUILDING, "", "");
			// 切换页面
			pageSwitching(OPTIONS_PAGE);
			break;
		case R.id.rl_unit_grid_menu:
			// 侧拉菜单楼层点击条目
			if("-1".equals(mSearchCondition.getBuildingNo())) {
				// 楼栋号不存在
				ToastUtils.show(mActivity, "请选择楼栋号");
			}else {
				mTitleView.setText("选择楼层号");
				OPTION = UNIT;
				loadingData(UNIT, mSearchCondition.getBuildingNo(), "");
				pageSwitching(OPTIONS_PAGE);
			}
			break;
		case R.id.rl_room_grid_menu:
			// 侧拉菜单房间点击条目
			if("-1".equals(mSearchCondition.getBuildingNo()) || "-1".equals(mSearchCondition.getUnitNo())) {
				// 楼栋号不存在
				ToastUtils.show(mActivity, "请选择楼栋号或楼层号");
			}else {
				mTitleView.setText("选择房间号");
				OPTION = ROOM;
				loadingData(ROOM, mSearchCondition.getBuildingNo(), mSearchCondition.getUnitNo());
				pageSwitching(OPTIONS_PAGE);
			}
			break;
		case R.id.rl_date_grid_menu:
			// 侧拉菜单日期点击条目
			String date = mSearchCondition.getDate();
			int mYear = Integer.valueOf(date.substring(0, 4));
			int mMonth = Integer.valueOf(date.substring(5, 7)) - 1;
			int mDay = Integer.valueOf(date.substring(8, date.length()));
			mDatePicker.init(mYear, mMonth, mDay, mOnDateChangedListener);
			pageSwitching(DATE_PAGE);
			break;
		case R.id.rl_name_sort_grid_menu:
			// 侧拉菜单按姓名排序点击条目
			if(mSearchCondition.getSort() != 0) {
				mSearchCondition.setSort(SearchConditionGrid.NAME);
				mNameSortImg.setImageResource(R.mipmap.select_right);
				mRoomSortImg.setImageResource(0);
			}
			break;
		case R.id.rl_room_sort_grid_menu:
			// 侧拉菜单按房间号排序点击条目
			if(mSearchCondition.getSort() != 1) {
				mSearchCondition.setSort(SearchConditionGrid.ROOM);
				mNameSortImg.setImageResource(0);
				mRoomSortImg.setImageResource(R.mipmap.select_right);
			}
			break;
		case R.id.tv_clear_grid_menu:
			// 清除选项点击事件处理
			// 重置数据
			mSearchCondition.setBuildingNo("-1");
			mSearchCondition.setUnitNo("-1");
			mSearchCondition.setRoomNo("-1");
			mSearchCondition.setDate(StringUtils.getFormatData(System.currentTimeMillis(), mFormat));
			// 默认是按床位号排序
			mSearchCondition.setSort(BaseGridActivity.SORT_ORDER_DEFAULT);
			setOptionView(mSearchCondition);
			break;
			
		// 选项页面控件的点击事件处理
		case R.id.tv_back_options_grid_menu:
			// 从选项页面返回主页面
			pageSwitching(MAIN_PAGE);
			break;
		// 日期页面控件的点击事件处理
		case R.id.tv_back_date_grid_menu:
			// 从选项页面返回主页面
			pageSwitching(MAIN_PAGE);
			break;
		// 日期选择事件处理
		case R.id.tv_confirm_date_grid_menu:
			// 设置新的日期
			mSearchCondition.setDate(mBufferDate);
			mDateView.setText(mSearchCondition.getDate());
			pageSwitching(MAIN_PAGE);
			break;
			
		default:
			break;
		}
		
	}
	/**
	 * 选项页面中加载数据
	 * @param options 选项:楼栋、楼层、房间号
	 * @param buildingNo 楼栋号
	 * @param unitNo 楼层号
	 */
	private void loadingData(String options, String buildingNo, String unitNo) {
		// 根据不同的选项加载新的数据
		if(BUILDING.equals(options)) {
			// 楼栋
			mAdapter.setNewData(BedManager.findAllBuildingNos());
		}else if(UNIT.equals(options)) {
			// 楼层
			mAdapter.setNewData(BedManager.findAllUnitNos(buildingNo));
		}else if(ROOM.equals(options)) {
			// 房间
			mAdapter.setNewData(BedManager.findAllRoomNos(buildingNo, unitNo));
		}
	}

	/**
	 * 设置选择条件的值
	 * @param options 条件
	 */
	private void setOptionView(SearchConditionGrid options) {
		// 设置楼栋号的值
		if("-1".equals(options.getBuildingNo())) {
			mBuildingView.setText("");
		}else {
			mBuildingView.setText(options.getBuildingNo());
		}
		// 设置楼层号的值
		if("-1".equals(options.getUnitNo())) {
			mUnitView.setText("");
		}else {
			mUnitView.setText(options.getUnitNo());
		}
		// 设置房间号的值
		if("-1".equals(options.getRoomNo())) {
			mRoomView.setText("");
		}else {
			mRoomView.setText(options.getRoomNo());
		}
		// 设置日期的值
		mDateView.setText(options.getDate());
		// 设置是按名字排序还是按房间号排序
		if(options.getSort() == SearchConditionGrid.NAME) {
			// name
			mNameSortImg.setImageResource(R.mipmap.select_right);
			mRoomSortImg.setImageResource(0);
		}else if(options.getSort() == SearchConditionGrid.ROOM) {
			// room
			mNameSortImg.setImageResource(0);
			mRoomSortImg.setImageResource(R.mipmap.select_right);
		}
	}
	/**
	 * 主页面和选项页面以及主页面和日期页面的切换
	 * @param page 页面
	 */
	private void pageSwitching(int page) {
		if(page == MAIN_PAGE) {
			mMainPage.setVisibility(View.VISIBLE);
			if(mOptionsPage.getVisibility() == View.VISIBLE) {
				// 从选项页面切换
				mOptionsPage.setVisibility(View.GONE);
				// 添加切换动画
				mOptionsPage.startAnimation(mRightOutAnimation);
				mMainPage.startAnimation(mLeftInAnimation);
			}else {
				// 从日期页面切换
				mDatePage.setVisibility(View.GONE);
				// 添加切换动画
				mDatePage.startAnimation(mRightOutAnimation);
				mMainPage.startAnimation(mLeftInAnimation);
			}
		}else if(page == OPTIONS_PAGE) {
			// 切换到选项页面
			mMainPage.setVisibility(View.GONE);
			mOptionsPage.setVisibility(View.VISIBLE);
			// 添加切换动画
			mMainPage.startAnimation(mLeftOutAnimation);
			mOptionsPage.startAnimation(mRightInAnimation);
		}else if(page == DATE_PAGE) {
			// 切换到日期页面
			mMainPage.setVisibility(View.GONE);
			mDatePage.setVisibility(View.VISIBLE);
			// 添加切换动画
			mMainPage.startAnimation(mLeftOutAnimation);
			mDatePage.startAnimation(mRightInAnimation);
		}
	}

	/**
	 * 获取状态栏的高度
	 * @return 状态栏的高度
	 */
	protected int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
	
}
