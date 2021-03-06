package com.yzk.brain.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.yzk.brain.R;
import com.yzk.brain.activity.RemeberPracticeEnterActivity;
import com.yzk.brain.activity.TwentyOneEnterActivity;
import com.yzk.brain.bean.PracticeEntity;

import java.util.List;

/**
 * Created by android on 11/17/16.
 */

public class GridAdapter extends BaseAdapter {

    private final Context mContext;
    private List<PracticeEntity> mDataList;
    private ViewHolder viewHolder;
    private final LayoutInflater mInflater;

    public GridAdapter(Context context) {

        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setData(List<PracticeEntity> dataList) {
        this.mDataList = dataList;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return null!=mDataList?mDataList.size():0;
    }

    @Override
    public Object getItem(int i) {
        return null!=mDataList?mDataList.get(i):null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (null == view) {
            viewHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.layout_grid_item, null);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.imageView);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final PracticeEntity practiceEntity = mDataList.get(i);

        if (practiceEntity.locked) {
            viewHolder.imageView.setImageResource(practiceEntity.normalResId);
            viewHolder.imageView.setEnabled(false);
        } else {
            viewHolder.imageView.setImageResource(practiceEntity.unlockResId);
            viewHolder.imageView.setEnabled(true);
        }
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PracticeEntity practiceEntity1 = mDataList.get(i);
                Intent intent = null;
                if (0 == practiceEntity1.index) {
                    intent = new Intent(mContext, RemeberPracticeEnterActivity.class);
                    intent.putExtra("isTest", true);
                    mContext.startActivity(intent);
                } else {
                    intent = new Intent(mContext, TwentyOneEnterActivity.class);
                    mContext.startActivity(intent);
                }
            }
        });

        int width = viewHolder.imageView.getWidth();


        return view;
    }

    private static class ViewHolder {
        ImageView imageView;
    }

}
