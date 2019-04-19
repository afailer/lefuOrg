package com.lefuorgn.lefu.util;

import com.google.gson.reflect.TypeToken;
import com.lefuorgn.api.common.Json;
import com.lefuorgn.db.model.basic.AllocatingTypeTask;
import com.lefuorgn.lefu.bean.AllocatingTaskExecute;
import com.lefuorgn.lefu.bean.AllocatingTaskExecuteOption;
import com.lefuorgn.util.StringUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 今日工作, 接单请求数据处理工具类
 */

public class OrderTakingNursingUtils {

    /**
     * 获取接单中未接单数据, 或者已接单数据
     * @param json 带解析的json数据
     * @return list集合数据
     */
    public static List<AllocatingTaskExecute> getOrderTakingNursing(String json) {
        // 用于存放数据缓存
        List<AllocatingTaskExecute> list = new ArrayList<AllocatingTaskExecute>();
        if (!StringUtils.isEmpty(json)) {
            // 用于存放配置的缓存
            Map<String, String> config = new HashMap<String, String>();
            try {
                JSONArray array = new JSONArray(json);
                // 遍历数据数组
                for (int i = 0; i < array.length(); i++) {
                    // 将数据转换成Map集合
                    Map<String, String> map = jsonToMap(array.getJSONObject(i));
                    if (i == 0) {
                        // 获取的是字段解释类
                        config = map;
                    } else {
                        // 字段数据类
                        AllocatingTaskExecute task = new AllocatingTaskExecute();
                        task.setOptions(new ArrayList<AllocatingTaskExecuteOption>());
                        task.setTask_time(System.currentTimeMillis());
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            // 遍历数据配单
                            String key = entry.getKey();
                            String value = entry.getValue();
                            if ("0".equals(key)) {
                                // 姓名
                                task.setOld_people_name(value);
                            } else if ("id".equals(key)) {
                                // id
                                task.setOld_people_id(Long.parseLong(value));
                            } else {
                                // 值
                                String[] split = value.split("/");
                                if (split.length == 2 && !"0".equals(split[1])) {
                                    // 测量数为0的移除
                                    AllocatingTaskExecuteOption option = new AllocatingTaskExecuteOption();
                                    option.setNursing_item_id(Long.parseLong(key));
                                    option.setNursing_item_name(config.get(key));
                                    option.setPercentage(value);
                                    option.setComplete(Integer.parseInt(split[0]));
                                    option.setTotal(Integer.parseInt(split[1]));
                                    option.setSave_type(AllocatingTypeTask.TYPE_FROM_SERVICE);
                                    task.getOptions().add(option);
                                }
                            }
                        }
                        list.add(task);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;

    }

    /**
     * 将JSONObject转换成Map
     * @param object 数据JSONObject
     * @return Map数据集合
     */
    private static Map<String, String> jsonToMap(JSONObject object) {
        Type type = new TypeToken<Map<String, String>>() {}.getType();
        Map<String, String> map = Json.getGson().fromJson(object.toString(), type);
        if(map == null) {
            map = new HashMap<String, String>();
        }
        return map;
    }

}
