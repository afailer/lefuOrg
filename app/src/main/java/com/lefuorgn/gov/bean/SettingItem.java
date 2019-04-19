package com.lefuorgn.gov.bean;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class SettingItem {
	int settingImg;
	String settingText;
	public SettingItem(int settingImg,String settingText){
		this.settingImg=settingImg;
		this.settingText=settingText;
	}

	public int getSettingImg() {
		return settingImg;
	}

	public void setSettingImg(int settingImg) {
		this.settingImg = settingImg;
	}

	public String getSettingText() {
		return settingText;
	}

	public void setSettingText(String settingText) {
		this.settingText = settingText;
	}

	public void doView(ImageView settingImg, TextView settingText, View view){
		settingImg.setImageResource(this.settingImg);
		settingText.setText(this.settingText);
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				click();
			}
		});
	}
	public abstract void click();
}
