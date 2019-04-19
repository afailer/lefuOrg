package com.lefuorgn.lefu.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.lefuorgn.R;
import com.lefuorgn.api.remote.common.RemoteUtil;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.db.model.basic.OldPeople;
import com.lefuorgn.util.ShareUtils;
import com.lefuorgn.util.StringUtils;

import java.util.Hashtable;

/**
 * 生成二维码页面
 */

public class BuildZxingCodeActivity extends BaseActivity {

    private ImageView mZxingCodeView;
    private OldPeople mOldPeople;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_build_zxing_code;
    }

    @Override
    protected void initView() {
        mZxingCodeView = (ImageView) findViewById(R.id.iv_activity_build_zxing_code);
        findViewById(R.id.tv_activity_build_zxing_code).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        setToolBarTitle("二维码");
        Intent intent = getIntent();
        mOldPeople = (OldPeople) intent.getSerializableExtra("oldPeople");
        String number = StringUtils.randomBuildNumber(mOldPeople.getId());
        // 初始化二维码的宽和高
        int value = (int) getResources().getDimension(R.dimen.zxing_width_height);
        Bitmap bitmap = buildZxingCode(number, value, value);
        if(bitmap == null) {
            showToast("二维码生成失败");
            return;
        }
        mZxingCodeView.setImageBitmap(bitmap);
    }

    @Override
    public void onClick(View v) {
        // 分享二维码
        String msg = "扫一扫二维码,关注您的家人" + mOldPeople.getElderly_name();
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/socialPeopleCtr/toShareOldPeople");
        url = url + "?oid=" + mOldPeople.getId();
        ShareUtils.share(this, "孝心无界·关爱随行", msg, "", url, true);
    }

    /**
     *  * 生成一个二维码图像
     * @param str 传入的字符串
     * @param QR_WIDTH
     * 				宽度（像素值px）
     * @param QR_HEIGHT
     * 				高度（像素值px）
     * @return 返回图片的bitmap
     */
    private Bitmap buildZxingCode(String str, int QR_WIDTH, int QR_HEIGHT) {
        try {
            // 判断str合法性
            if (str == null || "".equals(str) || str.length() < 1) {
                return null;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(str,
                    BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            // 显示到一个ImageView上面
            // sweepIV.setImageBitmap(bitmap);
            return bitmap;
        }catch (WriterException e) {
            return null;
        }
    }

    @Override
    protected boolean hasToolBar() {
        return true;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }
}
