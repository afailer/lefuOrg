package com.lefuorgn.oa.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lefuorgn.R;
import com.lefuorgn.base.BaseFragment;
import com.lefuorgn.oa.activity.SelectStuffActivity;
import com.lefuorgn.oa.adapter.StuffChoseAdapter;
import com.lefuorgn.oa.adapter.StuffDeptAdapter;
import com.lefuorgn.oa.bean.Department;
import com.lefuorgn.oa.bean.DepartmentList;
import com.lefuorgn.oa.bean.DeptNode;
import com.lefuorgn.oa.bean.OaUser;
import com.lefuorgn.oa.interf.StuffInterface;
import com.lefuorgn.util.DividerItemDecoration;
import com.lefuorgn.util.TLog;
import com.lefuorgn.widget.tree.adapter.TreeListViewAdapter;
import com.lefuorgn.widget.tree.bean.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuting on 2017/4/1.
 */

public class SelectStuffInDeptFragment extends BaseFragment implements StuffInterface{
    RecyclerView mTree;
    TreeListViewAdapter stuffAdapter;
    TextView confirm;
    @Override
    protected int getLayoutId() {
        return R.layout.select_stuff_with_dept;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        mTree= (RecyclerView) view.findViewById(R.id.stuff_tree);
        mTree.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTree.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST,
                getResources().getColor(R.color.recycler_view_item_division_color)));
        confirm = (TextView) view.findViewById(R.id.stuff_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stuffAdapter!=null){
                    String checkedNodes = ((StuffDeptAdapter)stuffAdapter).getCheckedNodes();
                    TLog.error(checkedNodes);
                    Intent intent=new Intent();
                    intent.putExtra("stuff",checkedNodes);
                    getActivity().setResult(500,intent);
                    getActivity().finish();
                }
            }
        });
    }

    @Override
    public void setStuff(List<DepartmentList> listDepartmentList,boolean isCopy) {
        if(listDepartmentList==null){
            return;
        }
        for(int j=0;j<listDepartmentList.size();j++) {
                DepartmentList departmentList=listDepartmentList.get(j);
            try {
                List<Department> departments = new ArrayList<Department>();
                for (int i = 0; i < departmentList.getDepts().size(); i++) {
                    if (departmentList.getDepts().get(i).getDept_id() != 0) {
                        departments.add(departmentList.getDepts().get(i));
                    }
                }
                deptNodes.add(new DeptNode(0, 0 - departmentList.getAgency_id(), departmentList.getAgency_name(), 5,"","",0));
                getDeptNodes(departmentList.getDepts(),0 - departmentList.getAgency_id());
                if (isCopy) {
                    stuffAdapter = new StuffDeptAdapter(R.layout.stuff_item, mTree, deptNodes, 0);
                } else {
                    confirm.setVisibility(View.GONE);
                    stuffAdapter = new StuffChoseAdapter(R.layout.stuff_item, mTree, deptNodes, 0);
                    ((StuffChoseAdapter) stuffAdapter).setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
                        @Override
                        public void onClick(Node node, View view, int position) {
                            if (node.getNum() < 0) {
                                StringBuilder sb = new StringBuilder();
                                sb.append(node.getId() + "," + node.getName() + SelectStuffActivity.DIVIDE_STR);
                                Intent intent = new Intent();
                                if("attendance".equals(getArguments().getString("attendance"))){
                                    intent.putExtra("user_id",node.getId());
                                    intent.putExtra("user_icon",node.getExtra2());
                                    intent.putExtra("user_name",node.getName());
                                    intent.putExtra("gender",node.getIntExtra1());
                                    TLog.error(node.toString());
                                }
                                Log.e("oaUser",node.toString());
                                intent.putExtra("stuff", sb.toString());
                                getActivity().setResult(800, intent);
                                getActivity().finish();
                            }
                        }
                    });
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
    List<DeptNode> deptNodes=new ArrayList<DeptNode>();
    private void getDeptNodes(List<Department> departments,long rootId){
        if(departments!=null) {
            for (int i = 0; i < departments.size(); i++) {
                if(departments.get(i).getDept_id()!=0){
                    if(departments.get(i).getPid()==0) {
                        deptNodes.add(new DeptNode(rootId, departments.get(i).getDept_id(), departments.get(i).getDept_name(),5,"","",0));
                    }else{
                        deptNodes.add(new DeptNode(departments.get(i).getPid(), departments.get(i).getDept_id(), departments.get(i).getDept_name(),5,"","",0));
                    }
                }

                if (departments.get(i).getUsers() != null) {
                    for (int j = 0; j < departments.get(i).getUsers().size(); j++) {
                        OaUser oaUser = departments.get(i).getUsers().get(j);
                        if(oaUser.getDept_id()==0){
                            deptNodes.add(new DeptNode(rootId, oaUser.getUser_id(), oaUser.getUser_name(),-3,oaUser.getUser_icon(),oaUser.getPost_name(),oaUser.getGender()));
                        }else{
                            deptNodes.add(new DeptNode(oaUser.getDept_id(), oaUser.getUser_id(), oaUser.getUser_name(),-3,oaUser.getUser_icon(),oaUser.getPost_name(),oaUser.getGender()));
                        }

                    }
                }
                List<Department> depts = departments.get(i).getChildDepts();
                getDeptNodes(depts,rootId);
            }
        }
    }
}
