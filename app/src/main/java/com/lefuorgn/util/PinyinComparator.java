package com.lefuorgn.util;

import com.lefuorgn.interf.Pinyinable;

import java.util.Comparator;

/**
 * 拼音排序比较器
 */

public class PinyinComparator<T extends Pinyinable> implements Comparator<T> {

    @Override
    public int compare(T lhs, T rhs) {
        if(lhs == null || rhs == null || lhs.getSortLetters() == null || rhs.getSortLetters() == null){
            return 0;
        }
        if (lhs.getSortLetters().equals("@")
                || rhs.getSortLetters().equals("#")) {
            return -1;
        } else if (lhs.getSortLetters().equals("#")
                || rhs.getSortLetters().equals("@")) {
            return 1;
        }else {
            return lhs.getFullPinYin().compareTo(rhs.getFullPinYin());
        }
    }
}
