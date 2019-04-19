package com.lefuorgn.api.remote;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.api.remote.common.CircleTransform;
import com.lefuorgn.api.remote.common.RemoteUtil;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.util.TLog;

import java.io.File;

public class ImageLoader {

	/**
	 * 
	 * @param uri 图片的uri地址
	 * @param target 图片显示目标
	 */
	public static void loadImg(String uri, ImageView target) {
		loadImgByNormalImg(uri, R.mipmap.img_default, R.mipmap.img_default,
				target);
	}

    /**
     *
     * @param id mipmap中的图片id
     * @param target 对应的ImageView
     */
    public static void loadImg(int id, ImageView target) {
        Glide.with(AppContext.getInstance()).load(id)
                .centerCrop().into(target);
    }

	/**
	 * 加载图片失败显示指定的图片
	 * 
	 * @param uri
	 *            图片的uri地址
	 * @param beforeImg
	 *            加载前显示的图片
	 * @param errorImg
	 *            加载后显示的图片
	 * @param target
	 *            目标ImageView
	 */
	public static void loadImgByNormalImg(String uri, int beforeImg,
			int errorImg, ImageView target) {
		if(beforeImg==0||errorImg==0){
			Glide.with(AppContext.getInstance()).load(RemoteUtil.IMG_URL + uri).into(target);
		}
			Glide.with(AppContext.getInstance()).load(RemoteUtil.IMG_URL + uri).placeholder(beforeImg)
					.error(errorImg).into(target);
	}

	/**
	 * 加载本地图片
	 * @param uri 本地磁盘中的地址
	 * @param target 对应的ImageView
	 */
	public static void loadLocalImg(String uri,ImageView target){
		loadLocalImg(uri, R.mipmap.picture_default, target);
	}

    /**
     * 加载本地图片
     * @param uri 本地磁盘中的地址
     * @param errorResId 加载失败显示的图片资源ID
     * @param target 目标ImageView
     */
	public static void loadLocalImg(String uri, int errorResId, ImageView target) {
        Glide.with(AppContext.getInstance()).load(new File(uri))
				.fitCenter().diskCacheStrategy(DiskCacheStrategy.NONE)
				.error(errorResId).into(target);
	}

    /**
     * 加载圆角图片
     * @param uri 图片的uri地址
     * @param target 目标ImageView
     */
	public static void loadCircleImg(String uri, ImageView target) {
        Glide.with(AppContext.getInstance())
                .load(RemoteUtil.IMG_URL + uri)
                .transform(new CircleTransform(AppContext.getInstance()))
                .placeholder(R.mipmap.img_head_default)
                .error(R.mipmap.img_head_default).crossFade().into(target);
	}

    /**
     * 加载圆角图片给自定义控件(具有边框)
     * @param uri 图片的uri地址
     * @param target 目标ImageView
     */
    public static void loadImgForUserDefinedView(String uri, final ImageView target) {
        TLog.error("加载圆角图片");
        Glide.with(AppContext.getInstance())
                .load(RemoteUtil.IMG_URL + uri)
                .placeholder(R.mipmap.img_head_default)
                .error(R.mipmap.img_head_default)
                .crossFade()
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> a) {
                        target.setImageDrawable(resource);
                    }
                });
    }

	/**
	 * 加载OA小图标
	 * @param uri 图片uri
	 * @param target 图片显示目标控件
     */
	public static void loadOAImage(String uri, ImageView target) {
		if(StringUtils.isEmpty(uri)) {
            target.setImageResource(R.mipmap.map_default);
			return;
		}
		int res = getImageRes(uri);
		if(res > 0) {
			target.setImageResource(res);
		}else {
            loadImgByNormalImg(uri, R.mipmap.map_default, R.mipmap.map_default, target);
		}
	}

	/**
	 * 加载OA小图标(通过类型)
	 * @param type 图片类型
	 * @param target 图片显示目标控件
	 */
	public static void loadOAImageByType(long type, ImageView target) {
		int res = getImageRes((int) type);
        target.setImageResource(res);
	}

	/**
	 * OA根据uri获取指定的图片
	 * @param uri 加载图片uri
	 * @return 0: 可能是网络图片; 其他:本地图片
     */
	private static int getImageRes(String uri) {
		if("car".equals(uri)) {
			return R.mipmap.map_car;
		}else if("seal".equals(uri)) {
			return R.mipmap.map_seal;
		}else if("namecard".equals(uri)) {
			return R.mipmap.map_namecard;
		}else if("recruit".equals(uri)) {
			return R.mipmap.map_recruit;
		}else if("Entry".equals(uri)) {
			return R.mipmap.map_entry;
		}else if("promotion".equals(uri)) {
			return R.mipmap.map_promotion;
		}else if("leave".equals(uri)) {
			return R.mipmap.map_leave;
		}else if("buy".equals(uri)) {
			return R.mipmap.map_buy;
		}else if("expense".equals(uri)) {
			return R.mipmap.map_expense;
		}else if("loan".equals(uri)) {
			return R.mipmap.map_loan;
		}else if("payment".equals(uri)) {
			return R.mipmap.map_payment;
		}else if("btrip".equals(uri)) {
			return R.mipmap.map_btrip;
		}else if("license".equals(uri)) {
			return R.mipmap.map_license;
		}
		return 0;
	}

	/**
	 * OA根据uri获取指定的图片
	 * @param type 加载图片类型
	 * @return 0: 可能是网络图片; 其他:本地图片
	 */
	private static int getImageRes(int type) {
        int result;
		switch (type) {
            case 1:
                // 年假
                result = R.mipmap.clock_annual_leave;
                break;
            case 2:
                // 调休
                result = R.mipmap.clock_off;
                break;
            case 3:
                // 加班
                result = R.mipmap.clock_overtime;
                break;
            case 4:
                // 出差
                result = R.mipmap.clock_business_travel;
                break;
            case 5:
                // 外出
                result = R.mipmap.clock_go_out;
                break;
            case 6:
                // 事假
                result = R.mipmap.clock_private_affair_leave;
                break;
            case 7:
                // 病假
                result = R.mipmap.clock_sick_leave;
                break;
            case 8:
                // 婚假
                result = R.mipmap.clock_marriage_holiday;
                break;
            case 9:
                // 产假
                result = R.mipmap.clock_maternity_leave;
                break;
            case 10:
                // 丧假
                result = R.mipmap.clock_funeral;
                break;
            default:
                // 默认
                result = R.mipmap.clock_default;
                break;
        }
		return result;
	}

}
