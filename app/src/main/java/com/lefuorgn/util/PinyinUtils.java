package com.lefuorgn.util;

import com.lefuorgn.interf.Pinyinable;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.List;

/**
 * 汉字转拼音工具类
 */
public class PinyinUtils {


    /**
     * 将集合中的bean类中的内容转换成拼音
     * @param list 待转换的内容
     */
    public static void convertedToPinyin(List<? extends Pinyinable> list) {
        if(list == null) {
            return;
        }
        for (Pinyinable pinyinable : list) {
            converted(pinyinable);
        }
    }

    /**
     * 将汉字转换成拼音
     * @param t 要转换的bean类
     * @param <T> 实现Pinyinable的接口类
     */
    private static <T extends Pinyinable> void converted(T t) {
        String name = t.getCharacters();
        if(StringUtils.isEmpty(name)) {
            t.setSortLetters("#");
            t.setFullPinYin("");
            t.setInitial("");
            return;
        }
        char[] cs = name.toCharArray();
        // 存放拼音全拼
        StringBuilder sbFull = new StringBuilder();
        // 存放内容首字母全拼
        StringBuilder sbFirst = new StringBuilder();
        for (char c : cs) {
            if(isChinese(c)) {
                // 是汉字
                String[] phonetics = PinyinHelper.toHanyuPinyinStringArray(c);
                if(phonetics == null) {
                    // 汉语符号
                    continue;
                }
                sbFull.append(phonetics[0].substring(0, phonetics[0].length() - 1));
                sbFirst.append(phonetics[0].substring(0, 1));
            }else {
                // 不是汉字
                sbFull.append(c);
                sbFirst.append(String.valueOf(c).toLowerCase());
            }
        }
        t.setFullPinYin(sbFull.toString());
        t.setInitial(sbFirst.toString());
        String sortString = sbFull.toString().substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortString.matches("[A-Z]")) {
            t.setSortLetters(sortString.toUpperCase());
        } else {
            t.setSortLetters("#");
        }
    }

    /**
     * 根据UNICODE编码完美的判断中文汉字和符号
     * @param c 待校验的字符
     * @return true : 是汉字;  false : 非汉字
     */
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION;
    }

}
