package com.lefuorgn.gov.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.lefuorgn.R;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.gov.Utils.JsonToBeanUtil;
import com.lefuorgn.gov.adapter.SeekAdapter;
import com.lefuorgn.gov.bean.Organization;
import com.lefuorgn.util.DividerItemDecoration;
import com.lefuorgn.widget.ClearEditText;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SeekOrgActivity extends BaseActivity {
    RecyclerView recycleView;
    SeekAdapter seekAdapter;
    ClearEditText editText;
    private List<Organization> mList;

    @Override
    protected void initView() {
        super.initView();
        editText= (ClearEditText) findViewById(R.id.cet_seek_org_activity);
        recycleView= (RecyclerView) findViewById(R.id.seek_recycle);
        seekAdapter=new SeekAdapter(R.layout.item_seek_org,new ArrayList<Organization>());
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST,
                getResources().getColor(R.color.recycler_view_item_division_color)));
        recycleView.setAdapter(seekAdapter);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                //filterData(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        seekAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Organization organization = seekAdapter.getItem(i);
                Intent data = new Intent();
                data.putExtra("id", organization.getAgency_id());
                data.putExtra("orgName", organization.getAgency_name());
                setResult(600, data);
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        Intent intent = getIntent();
        String json = intent.getStringExtra("orgInfo");
        Type oType = new TypeToken<List<Organization>>() {}.getType();
        mList = JsonToBeanUtil.getInstance().jsonToBean(new ArrayList<Organization>(), json, oType);
        seekAdapter.setNewData(mList);
        filledData();
    }
    /**
     * 为ListView填充数据
     * @return
     */
    private void filledData() {

        for (Organization org : mList) {
            // 汉字转换成拼音
            switchToPinyin(org);
        }
    }
    /**
     * 将汉字转换成拼音
     * @return
     */
    @SuppressLint("DefaultLocale")
    private void switchToPinyin(Organization org) {
        String orgName = org.getAgency_name();
        if(orgName == null || "".equals(orgName)) {
            org.setFullFirstChar("");
            org.setFullPinYIn("");
            return;
        }
        char[] cs = orgName.toCharArray();
        StringBuilder sbFull = new StringBuilder();
        StringBuilder sbFirst = new StringBuilder();
        for (char c : cs) {
            if(isChinese(c)) {
                // 是汉字
                String[] phonetics = PinyinHelper.toHanyuPinyinStringArray(c);
                sbFull.append(phonetics[0].substring(0, phonetics[0].length() - 1));
                sbFirst.append(phonetics[0].substring(0, 1));
            }else {
                // 不是汉字
                sbFull.append(c);
                sbFirst.append(String.valueOf(c).toLowerCase());
            }
        }
        org.setFullFirstChar(sbFirst.toString());
        org.setFullPinYIn(sbFull.toString());
    }

    // 根据UNICODE编码完美的判断中文汉字和符号
    private boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    @SuppressLint("DefaultLocale")
    private void filterData(String filterStr) {
        List<Organization> filterDateList = new ArrayList<Organization>();
        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = mList;
        } else {
            filterDateList.clear();
            filterStr = filterStr.toLowerCase();
            for (Organization org : mList) {
                if (org.getAgency_name().contains(filterStr) ||
                        org.getFullFirstChar().contains(filterStr) ||
                        org.getFullPinYIn().contains(filterStr)) {
                    filterDateList.add(org);
                }
            }
        }
        seekAdapter.setNewData(filterDateList);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_seek_org;
    }
}
