package com.lefuorgn.lefu.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.lefu.bean.Trajectory;
import com.lefuorgn.util.StringUtils;

import java.util.List;

/**
 * 老人移动轨迹点数据适配器
 */

public class TrajectoryPlaybackAdapter extends BaseQuickAdapter<Trajectory> {

    public TrajectoryPlaybackAdapter(List<Trajectory> data) {
        super(R.layout.item_activity_trajectory_playback, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Trajectory trajectory) {
        holder.setText(R.id.tv_item_activity_trajectory_playback_place, trajectory.getAddress())
                .setText(R.id.tv_item_activity_trajectory_playback_time,
                        StringUtils.getFormatData(trajectory.getTime(), "yyyy-MM-dd HH:mm"));
        holder.getView(R.id.iv_item_activity_trajectory_playback).setSelected(trajectory.isSelect());
    }
}
