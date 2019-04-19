package com.lefuorgn.lefu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 评估表问题信息
 */

public class Question implements Serializable {

    private String question; // 题目内容
    private int type; // 题目类型  1是单选2是多选
    private List<Option> options; // 题目选项

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "Question{" +
                "question='" + question + '\'' +
                ", type=" + type +
                ", options=" + options +
                '}';
    }
}
