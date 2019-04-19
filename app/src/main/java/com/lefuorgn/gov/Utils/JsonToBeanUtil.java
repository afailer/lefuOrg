package com.lefuorgn.gov.Utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lefuorgn.api.common.Json;
import com.lefuorgn.gov.bean.GovOrgInfo;
import com.lefuorgn.gov.bean.GovOrgInfoDecorate;
import com.lefuorgn.gov.bean.GovOrgInfoItem;
import com.lefuorgn.gov.bean.Organization;
import com.lefuorgn.gov.bean.RegionInfo;
import com.lefuorgn.gov.bean.Street;
import com.lefuorgn.util.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuting on 2016/12/29.
 */

public class JsonToBeanUtil {
     Gson gson= Json.getGson();
    static JsonToBeanUtil jsonToBeanUtil;
    private JsonToBeanUtil(){}
    public static JsonToBeanUtil getInstance(){
        if(jsonToBeanUtil==null){
            jsonToBeanUtil=new JsonToBeanUtil();
        }
        return jsonToBeanUtil;
    }


    /**
     * 将字符串转换成机构搜索列表所能使用的数据结构
     * @return
     */
    public List<GovOrgInfoItem> getGovOrgInfoItem(GovOrgInfo orgInfos) {
        // 为街道重新分配ID,街道的ID都为负数
        int id = -2;
        // 存放所有的条目信息
        List<GovOrgInfoItem> list = new ArrayList<GovOrgInfoItem>();
        // 存放街道信息
        List<GovOrgInfoItem> streetList = new ArrayList<GovOrgInfoItem>();
        //GovOrgInfo govOrgInfo = getGovOrgInfo(json);
        GovOrgInfoDecorate govOrgInfoDecorate = getGovOrgInfoDecorate(orgInfos);
        // 转换成树形结构所需要的结构
        for (RegionInfo provinces : govOrgInfoDecorate.getProvinces()) {
            // 遍历省的信息
            if("".equals(provinces.getRegion_name())){
                continue;
            }
            list.add(createGovOrgInfoItem(provinces));
        }
        for (RegionInfo city : govOrgInfoDecorate.getCities()) {
            // 遍历市的信息
            if("".equals(city.getRegion_name())){
                continue;
            }
            list.add(createGovOrgInfoItem(city));
        }
        for (RegionInfo area : govOrgInfoDecorate.getAreas()) {
            // 遍历区的信息
            if("".equals(area.getRegion_name())){
                continue;
            }
            list.add(createGovOrgInfoItem(area));
        }
        // 自定义ID从-3开始, 因为-2已经被机构的id占用, -1在树形结构中已经使用, 这里不可以使用
        for (Street street : govOrgInfoDecorate.getStreet()) {
            if("".equals(street.getRegion_name())){
                continue;
            }
            // 遍历街道的信息
            GovOrgInfoItem item = new GovOrgInfoItem();
            item.setId(--id);
            item.setpId(street.getPid());
            item.setName(street.getRegion_name());
            list.add(item);
            streetList.add(item);
        }
        for (Organization orgInfo : govOrgInfoDecorate.getAgencyInfos()) {
            GovOrgInfoItem item = new GovOrgInfoItem();
            // -1在树形结构中已经使用, 不可以使用
            item.setId(-2);
            item.setmId(orgInfo.getAgency_id());
            item.setName(orgInfo.getAgency_name());
            if(StringUtils.isEmpty(orgInfo.getStreet())) {
                item.setpId(orgInfo.getArea_id());
            }else {
                for (GovOrgInfoItem g : streetList) {
                    if(g.getName().equals(orgInfo.getStreet())) {
                        item.setpId(g.getId());
                        break;
                    }
                }
                // 所有的街道ID都小于-1,如果大于等于-1则,使其父ID为其区域ID
                if(item.getpId() >= -2) {
                    item.setpId(orgInfo.getArea_id());
                }
            }
            list.add(item);
        }
        return list;
    }
    private GovOrgInfoItem createGovOrgInfoItem(RegionInfo r) {
        GovOrgInfoItem item = new GovOrgInfoItem();
        item.setId(r.getId());
        item.setpId(r.getPid());
        item.setName(r.getRegion_name());
        return item;
    }
    /**
     * 将第一层提取的数据,再次提取,将字符串转换成list集合的形式
     * @param govOrgInfo 第一层提取的bean类
     * @return
     */
    public GovOrgInfoDecorate getGovOrgInfoDecorate(GovOrgInfo govOrgInfo) {
        GovOrgInfoDecorate govOrgInfoDecorate = new GovOrgInfoDecorate();
        Type oType = new TypeToken<List<Organization>>() {
        }.getType();
        govOrgInfoDecorate.setAgencyInfos(jsonToBean(new ArrayList<Organization>(),
                govOrgInfo.getAgencyInfos(), oType));
        Type rType = new TypeToken<List<RegionInfo>>() {
        }.getType();
        govOrgInfoDecorate.setAreas(jsonToBean(new ArrayList<RegionInfo>(),
                govOrgInfo.getAreas(), rType));
        govOrgInfoDecorate.setCities(jsonToBean(new ArrayList<RegionInfo>(),
                govOrgInfo.getCities(), rType));
        govOrgInfoDecorate.setProvinces(jsonToBean(new ArrayList<RegionInfo>(),
                govOrgInfo.getProvinces(), rType));
        Type sType = new TypeToken<List<Street>>() {
        }.getType();
        govOrgInfoDecorate.setStreet(jsonToBean(new ArrayList<Street>(),
                govOrgInfo.getStreet(), sType));
        return govOrgInfoDecorate;
    }
    /**
     * 将字符串转换成指定的集合类型
     * @param t 内容为空的集合类型
     * @param str 字符创
     * @param type 集合所存放的数据类型
     * @return
     */
    public <T> T jsonToBean(T t, String str, Type type) {
        if (StringUtils.isEmpty(str)) {
            // 机构集合
            return t;
        } else {
            t = (T)jsonToList(str, type);
        }
        return t;
    }

    /**
     * 把json字符串转成list集合
     * @param jsonStr
     * @param type new TypeToken<List<Person>>(){}.getType()
     * @return
     */
    public List<?> jsonToList(String jsonStr, java.lang.reflect.Type type) {
        List<?> objList = null;
        if (gson != null) {
            objList = gson.fromJson(jsonStr, type);
        }
        return objList;
    }

    /**
     * 将json数据简单的提取,即第一层提取
     * @param json
     * @return
     */
    public GovOrgInfo getGovOrgInfo(String json) {
        if (StringUtils.isEmpty(json)) {
            return new GovOrgInfo();
        }
        String json1 = getdataJson(json);
        GovOrgInfo bean = (GovOrgInfo)jsonToBean(json1,
                GovOrgInfo.class);
        return bean;
    }
        /**
         * 把json转成list
         * @param jsonStr
         * @return
         */
        public Object jsonToBean(String jsonStr, Class<?> cl) {
            Object obj = null;
            if (gson != null) {
                obj = gson.fromJson(jsonStr, cl);
            }
            return obj;
        }
    public String getdataJson(String json){
        String str="";
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            str=jsonObject.optString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return str;
    }
}