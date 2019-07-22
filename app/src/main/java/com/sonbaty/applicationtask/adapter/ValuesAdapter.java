package com.sonbaty.applicationtask.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.sonbaty.applicationtask.R;
import com.sonbaty.applicationtask.data.model.getValues.GetValuesData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.sonbaty.applicationtask.utils.Constant.IMAGE;

public class ValuesAdapter extends RecyclerView.Adapter<ValuesAdapter.ViewHolder> {

    private Context context;
    private List<GetValuesData> valuesDataList = new ArrayList<>();

    public ValuesAdapter(Context context, List<GetValuesData> valuesDataList) {
        this.context = context;
        this.valuesDataList = valuesDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_values,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (valuesDataList.get(position).getType().equals(IMAGE)) {
            holder.itemValuesImageViewView.setVisibility(View.VISIBLE);
            holder.itemValuesVideoViewView.setVisibility(View.GONE);

            Glide.with(context)
                    .load(valuesDataList.get(position).getValue())
                    .into(holder.itemValuesImageViewView);

        } else {
            holder.itemValuesImageViewView.setVisibility(View.GONE);
            holder.itemValuesVideoViewView.setVisibility(View.VISIBLE);

            holder.itemValuesVideoViewView.setVideoPath(valuesDataList.get(position).getValue());
            holder.itemValuesVideoViewView.setMediaController(new MediaController(context));
            holder.itemValuesVideoViewView.requestFocus();
            holder.itemValuesVideoViewView.start();

        }
    }

    @Override
    public int getItemCount() {
        return valuesDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_values_imageView_view)
        ImageView itemValuesImageViewView;
        @BindView(R.id.item_values_videoView_view)
        VideoView itemValuesVideoViewView;
        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}
