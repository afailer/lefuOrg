package com.lefuorgn.lefu.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 评估表信息
 */

public class Estimate implements Serializable {

    private long id; // 评估答题表ID
    private int state; // 评估答题表的状态
    private long createTime; // 创建时间
    private long updateTime; // 修改时间
    private long examination_paper_id; // 试卷ID
    private String old_people_card_number; // 老人身份证
    private long inspectTime; // 录入时间
    private int nursing_level; // 护理级别
    private long agency_id; // 机构ID
    private String title; // 试卷名称
    private long inspect_user_id; // 录入人员ID
    private String inspect_user_name; // 录入人员姓名
    private long old_people_id; // 老人ID
    private String old_people_name; // 老人姓名
    private String reserved; // 概述/备注
    private String desc; // 注意事项
    private int sum; // 总分数
    private String cellphone; // 老人的联系方式
    private String content; // 评估表问题

    private List<Question> questions;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getExamination_paper_id() {
        return examination_paper_id;
    }

    public void setExamination_paper_id(long examination_paper_id) {
        this.examination_paper_id = examination_paper_id;
    }

    public String getOld_people_card_number() {
        return old_people_card_number;
    }

    public void setOld_people_card_number(String old_people_card_number) {
        this.old_people_card_number = old_people_card_number;
    }

    public long getInspectTime() {
        return inspectTime;
    }

    public void setInspectTime(long inspectTime) {
        this.inspectTime = inspectTime;
    }

    public int getNursing_level() {
        return nursing_level;
    }

    public void setNursing_level(int nursing_level) {
        this.nursing_level = nursing_level;
    }

    public long getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(long agency_id) {
        this.agency_id = agency_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getInspect_user_id() {
        return inspect_user_id;
    }

    public void setInspect_user_id(long inspect_user_id) {
        this.inspect_user_id = inspect_user_id;
    }

    public String getInspect_user_name() {
        return inspect_user_name;
    }

    public void setInspect_user_name(String inspect_user_name) {
        this.inspect_user_name = inspect_user_name;
    }

    public long getOld_people_id() {
        return old_people_id;
    }

    public void setOld_people_id(long old_people_id) {
        this.old_people_id = old_people_id;
    }

    public String getOld_people_name() {
        return old_people_name;
    }

    public void setOld_people_name(String old_people_name) {
        this.old_people_name = old_people_name;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Question> getQuestions() {
        if(questions == null) {
            questions = new ArrayList<Question>();
        }
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "Estimate{" +
                "id=" + id +
                ", state=" + state +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", examination_paper_id=" + examination_paper_id +
                ", old_people_card_number='" + old_people_card_number + '\'' +
                ", inspectTime=" + inspectTime +
                ", nursing_level=" + nursing_level +
                ", agency_id=" + agency_id +
                ", title='" + title + '\'' +
                ", inspect_user_id=" + inspect_user_id +
                ", inspect_user_name='" + inspect_user_name + '\'' +
                ", old_people_id=" + old_people_id +
                ", old_people_name='" + old_people_name + '\'' +
                ", reserved='" + reserved + '\'' +
                ", desc='" + desc + '\'' +
                ", sum=" + sum +
                ", cellphone='" + cellphone + '\'' +
                ", content='" + content + '\'' +
                ", questions=" + questions +
                '}';
    }
}
