package com.lefuorgn.lefu.bean;

import java.io.Serializable;

/**
 * 评估表选项信息
 */

public class Option implements Serializable {

    private int type; // 选项类型
    private String content; // 选项内容
    private int status; // 选项的状态,0为未被选中;1为被选中
    private String answers; // 填空题的答案;多个时,封装规则为用逗号分隔
    private int score; // 当前选项的分值

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Option{" +
                "type=" + type +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", answers='" + answers + '\'' +
                ", score=" + score +
                '}';
    }
}
