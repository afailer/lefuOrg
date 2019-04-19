package com.lefuorgn.gov.bean;

public class NewsType {

    private long id;
	private long organize_id;
	private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrganize_id() {
        return organize_id;
    }

    public void setOrganize_id(long organize_id) {
        this.organize_id = organize_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "NewsType{" +
                "id=" + id +
                ", organize_id=" + organize_id +
                ", name='" + name + '\'' +
                '}';
    }
}
