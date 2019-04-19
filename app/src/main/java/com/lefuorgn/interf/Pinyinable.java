package com.lefuorgn.interf;

/**
 * 设置内容字母索引、全拼以及首字母全拼的接口类
 */

public interface Pinyinable {

    /**
     * 获取要转换为拼音的内容
     * @return 内容
     */
    String getCharacters();

    /**
     * 获取当前内容的字母索引
     * @return 字母索引
     */
    String getSortLetters();

    /**
     * 设置当前字母索引
     * @param sortLetters 字母索引
     */
    void setSortLetters(String sortLetters);

    /**
     * 获取当前内容全拼
     * @return 内容全拼
     */
    String getFullPinYin();

    /**
     * 设置当前内容拼音
     * @param fullPinYin 字母全拼
     */
    void setFullPinYin(String fullPinYin);

    /**
     * 获取当前内容首字母拼音
     * @return 内容首字母全拼
     */
    String getInitial();

    /**
     * 设置内容首字母全拼
     * @param initial 内容首字母全拼
     */
    void setInitial(String initial);


}
