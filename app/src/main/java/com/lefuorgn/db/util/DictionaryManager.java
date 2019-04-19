package com.lefuorgn.db.util;

import com.lefuorgn.db.base.BaseDao;
import com.lefuorgn.db.model.basic.Dictionary;

/**
 * 字典管理类
 */
public class DictionaryManager {

    /**
     * 通过ID获取字典中指定的内容值
     * @param id 主键ID
     * @return 内容
     */
    public static String getContent(long id) {
        if(id <= 0) {
            return "";
        }
        BaseDao<Dictionary, Long> dao = DaoHelper.getInstance().getDao(Dictionary.class);
        Dictionary dictionary = dao.query("id", id);
        if(dictionary != null) {
            return dictionary.getContent();
        }
        return "";
    }

}
