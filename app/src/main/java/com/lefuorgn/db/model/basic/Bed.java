package com.lefuorgn.db.model.basic;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lefuorgn.db.model.interf.IDownloadable;

import java.math.BigDecimal;

/**
 * 床位基本信息类
 */
@DatabaseTable(tableName = "Bed")
public class Bed implements IDownloadable {

	@DatabaseField(generatedId = true)
	public long _id;// 数据id
	@DatabaseField(columnName = "id")
	private long bed_id; // 床位ID
	@DatabaseField
	private String floor_no; // 楼栋号
	@DatabaseField
	private String floor_layer; // 楼层
	@DatabaseField
	private String room_no; // 房间号
	@DatabaseField
	private String room_type; // 房间类型
	@DatabaseField
	private String face; // 朝向
	@DatabaseField
	private long bed_level_id; // 床位级别
	@DatabaseField
	private String bed_no; // 床位号
	@DatabaseField
	private String room_facilities; // 房间设施
	@DatabaseField
	private long agency_id; // 机构ID
	@DatabaseField
	private int bed_status; // 床位状态
	@DatabaseField(columnName = "createTime")
	private long create_time;
	@DatabaseField(columnName = "updateTime")
	private long update_time;
	@DatabaseField
	private String remark; // 描述
	@DatabaseField
	private String beds_no; // 多个床位号拼接成的字符串
	// 不进行数据存储，只进行前台数据展示
	@DatabaseField
	private String level_name; // 床位级别名称
	@DatabaseField
	private BigDecimal bed_price; // 床位价格
	@DatabaseField
	private int scode; // 0:没删，1:删除

	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public long getBed_id() {
		return bed_id;
	}

	public void setBed_id(long bed_id) {
		this.bed_id = bed_id;
	}

	public String getFloor_no() {
		return floor_no;
	}

	public void setFloor_no(String floor_no) {
		this.floor_no = floor_no;
	}

	public String getFloor_layer() {
		return floor_layer;
	}

	public void setFloor_layer(String floor_layer) {
		this.floor_layer = floor_layer;
	}

	public String getRoom_no() {
		return room_no;
	}

	public void setRoom_no(String room_no) {
		this.room_no = room_no;
	}

	public String getRoom_type() {
		return room_type;
	}

	public void setRoom_type(String room_type) {
		this.room_type = room_type;
	}

	public String getFace() {
		return face;
	}

	public void setFace(String face) {
		this.face = face;
	}

	public long getBed_level_id() {
		return bed_level_id;
	}

	public void setBed_level_id(long bed_level_id) {
		this.bed_level_id = bed_level_id;
	}

	public String getBed_no() {
		return bed_no;
	}

	public void setBed_no(String bed_no) {
		this.bed_no = bed_no;
	}

	public String getRoom_facilities() {
		return room_facilities;
	}

	public void setRoom_facilities(String room_facilities) {
		this.room_facilities = room_facilities;
	}

	public long getAgency_id() {
		return agency_id;
	}

	public void setAgency_id(long agency_id) {
		this.agency_id = agency_id;
	}

	public int getBed_status() {
		return bed_status;
	}

	public void setBed_status(int bed_status) {
		this.bed_status = bed_status;
	}

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

	public long getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(long update_time) {
		this.update_time = update_time;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getBeds_no() {
		return beds_no;
	}

	public void setBeds_no(String beds_no) {
		this.beds_no = beds_no;
	}

	public String getLevel_name() {
		return level_name;
	}

	public void setLevel_name(String level_name) {
		this.level_name = level_name;
	}

	public BigDecimal getBed_price() {
		return bed_price;
	}

	public void setBed_price(BigDecimal bed_price) {
		this.bed_price = bed_price;
	}

	public int getScode() {
		return scode;
	}

	public void setScode(int scode) {
		this.scode = scode;
	}

	@Override
	public long getC_id() {
		return _id;
	}

	@Override
	public void setC_id(long _id) {
		this._id = _id;
	}

	@Override
	public long getCId() {
		return bed_id;
	}

	@Override
	public long getCUpdateTime() {
		return update_time;
	}

	@Override
	public long getCCreateTime() {
		return create_time;
	}

	@Override
	public String toString() {
		return "Bed [_id=" + _id + ", bed_id=" + bed_id + ", floor_no="
				+ floor_no + ", floor_layer=" + floor_layer + ", room_no="
				+ room_no + ", room_type=" + room_type + ", face=" + face
				+ ", bed_level_id=" + bed_level_id + ", bed_no=" + bed_no
				+ ", room_facilities=" + room_facilities + ", agency_id="
				+ agency_id + ", bed_status=" + bed_status + ", create_time="
				+ create_time + ", update_time=" + update_time + ", remark="
				+ remark + ", beds_no=" + beds_no + ", level_name="
				+ level_name + ", bed_price=" + bed_price + ", scode=" + scode
				+ "]";
	}

}
