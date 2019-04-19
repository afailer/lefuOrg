package com.lefuorgn.oa.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.base.BaseContactFragment;
import com.lefuorgn.oa.activity.SelectStuffActivity;
import com.lefuorgn.oa.interf.StuffInterface;
import com.lefuorgn.oa.bean.Department;
import com.lefuorgn.oa.bean.DepartmentList;
import com.lefuorgn.oa.bean.OaUser;
import com.lefuorgn.util.PinyinComparator;
import com.lefuorgn.util.PinyinUtils;
import com.lefuorgn.util.TLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by liuting on 2017/4/4.
 */

public class SelectStuffByNameFragment extends BaseContactFragment<OaUser> implements StuffInterface{
    private List<OaUser> oaUsers=new ArrayList<OaUser>();
    private List<OaUser> selectedUsers=new ArrayList<OaUser>();
    boolean isCopy;
    View confirm;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_base_contact_staff;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        confirm=view.findViewById(R.id.stuff_confirm_container);
        view.findViewById(R.id.stuff_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder sb=new StringBuilder();
                for(int i=0;i<selectedUsers.size();i++){
                    sb.append(selectedUsers.get(i).getUser_id()+","+selectedUsers.get(i).getUser_name()+ SelectStuffActivity.DIVIDE_STR);
                }
                Intent intent=new Intent();
                intent.putExtra("stuff",sb.toString());
                getActivity().setResult(500,intent);
                getActivity().finish();
            }
        });
    }

    @Override
    public void setStuff(List<DepartmentList> departmentList,boolean isCopy) {
        this.isCopy=isCopy;
        if(departmentList == null) {
            return;
        }
        List<Department> departments=new ArrayList<Department>();
        for(int i=0;i<departmentList.size();i++){
            departments.addAll(departmentList.get(i).getDepts());
        }
        setOaUsers(departments);
        // 将名称转换成拼音
        PinyinUtils.convertedToPinyin(oaUsers);
        // 排序
        Collections.sort(oaUsers, new PinyinComparator<OaUser>());
        setData(oaUsers);
        if(isCopy){
            mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int i) {
                    OaUser oaUser = (OaUser) mAdapter.getData().get(i);
                    if(selectedUsers.contains(oaUser)){
                        ((ImageView)view.findViewById(R.id.stuff_select)).setImageResource(R.mipmap.gov_normal);
                        selectedUsers.remove(oaUser);
                    }else{
                        ((ImageView)view.findViewById(R.id.stuff_select)).setImageResource(R.mipmap.gov_select);
                        selectedUsers.add(oaUser);
                    }
                }
            });
        }else{
            confirm.setVisibility(View.GONE);
            mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int i) {

                    OaUser oaUser = (OaUser) mAdapter.getData().get(i);
                        StringBuilder sb=new StringBuilder();
                        sb.append(oaUser.getUser_id()+","+oaUser.getUser_name()+SelectStuffActivity.DIVIDE_STR);
                        Intent intent=new Intent();
                        if("attendance".equals(getArguments().getString("attendance"))){
                            intent.putExtra("user_id",oaUser.getUser_id());
                            intent.putExtra("user_icon",oaUser.getUser_icon());
                            intent.putExtra("user_name",oaUser.getUser_name());
                            intent.putExtra("gender",oaUser.getGender());
                            TLog.error(oaUser.toString());
                        }
                        intent.putExtra("stuff",sb.toString());
                        getActivity().setResult(800,intent);
                        getActivity().finish();
                }
            });
        }
    }

    @Override
    protected void setData(List<OaUser> data) {
        mData.addAll(data);
        mAdapter.addData(data);
        updateQuickSideBarLetters(data);
    }

    public void setOaUsers(List<Department> departmentList){
        for(Department department:departmentList){
            for(OaUser oaUser:department.getUsers()){
                oaUser.setDept_name(department.getDept_name());
            }
            oaUsers.addAll(department.getUsers());
            if(department.getChildDepts()!=null){
                setOaUsers(department.getChildDepts());
            }
        }
    }
    @Override
    protected void initData() {
        super.initData();

    }
    @Override
    protected int getItemLayoutId() {
        return R.layout.stuff_item;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, OaUser oaUser, boolean flag) {
        baseViewHolder.setText(R.id.stuff_name,oaUser.getUser_name())
                .setText(R.id.tv_item_fragment_contact_letter, oaUser.getSortLetters())
                .setVisible(R.id.open_dept,false);
        if(oaUser.getDept_name()==null||"".equals(oaUser.getDept_name())){
           baseViewHolder.getView(R.id.dept_name).setVisibility(View.GONE);
        }else{
            baseViewHolder.getView(R.id.dept_name).setVisibility(View.VISIBLE);
            baseViewHolder.setText(R.id.dept_name,oaUser.getDept_name());
        }
        if(oaUser.getPost_name()==null || "".equals(oaUser.getPost_name())){
            baseViewHolder.getView(R.id.pos_name).setVisibility(View.GONE);
        }else{
            baseViewHolder.getView(R.id.pos_name).setVisibility(View.VISIBLE);
            baseViewHolder.setText(R.id.pos_name,oaUser.getPost_name());
        }
        if(selectedUsers.contains(oaUser)){
            baseViewHolder.setImageResource(R.id.stuff_select,R.mipmap.gov_select);
        }else{
            baseViewHolder.setImageResource(R.id.stuff_select,R.mipmap.gov_normal);
        }

        if(!isCopy){
            baseViewHolder.getView(R.id.stuff_select).setVisibility(View.GONE);
        }
        baseViewHolder.getView(R.id.tv_item_fragment_contact_letter).setVisibility(flag ? View.VISIBLE : View.GONE);
    }
}
