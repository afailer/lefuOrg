package com.lefuorgn.gov.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.api.remote.ImageLoader;
import com.lefuorgn.gov.Utils.GovUtils;
import com.lefuorgn.gov.bean.LeaderNews;
import com.lefuorgn.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by liuting on 2016/12/28.
 */

public class NewsAdapter extends BaseQuickAdapter<LeaderNews>{

    public NewsAdapter(int layoutResId, List<LeaderNews> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, LeaderNews leaderNews) {
        ImageView img = baseViewHolder.getView(R.id.item_img);
        TextView title = baseViewHolder.getView(R.id.item_title);
        TextView time = baseViewHolder.getView(R.id.item_holdtime);
        TextView type=baseViewHolder.getView(R.id.item_type);
        type.setText(leaderNews.getType_name());
        type.setBackgroundColor(GovUtils.getInstance().getColor(leaderNews.getType()));//(Color.parseColor(colorSets[leaderNews.getType()%colorSets.length]));
        ImageLoader.loadImg(leaderNews.getPicture(),img);
        title.setText(leaderNews.getTheme());
        time.setText(StringUtils.getFormatData(leaderNews.getCreate_dt(), "yyyy年MM月dd日"));
    }
}