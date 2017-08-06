package com.gaoyy.stickylistdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by gaoyy on 2017/8/4 0004.
 */

public class ReListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{


    private LayoutInflater inflater;
    private Context context;
    private List<Type> data;

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.onItemClickListener = listener;
    }

    public ReListAdapter(Context context, List<Type> data)
    {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View rootView = inflater.inflate(R.layout.item_left, parent, false);
        LeftViewHolder leftViewHolder = new LeftViewHolder(rootView);
        return leftViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {

        LeftViewHolder leftViewHolder = ((LeftViewHolder) holder);
        leftViewHolder.tv.setText(data.get(position).getType());
        leftViewHolder.tv.setTag(position);

        if(data.get(position).getStatus() == 1)
        {
            leftViewHolder.layout.setBackgroundColor(context.getResources().getColor(android.R.color.white));
            leftViewHolder.tv.setTextColor(context.getResources().getColor(android.R.color.black));
        }
        if(data.get(position).getStatus() == 0)
        {
            leftViewHolder.layout.setBackgroundColor(context.getResources().getColor(R.color.gray));
            leftViewHolder.tv.setTextColor(context.getResources().getColor(R.color.gray_text));
        }


        if (onItemClickListener != null)
        {
            leftViewHolder.tv.setOnClickListener(new BasicOnClickListener(leftViewHolder));
        }
    }

    public void updateData(List<Type> data)
    {
        this.data = data;
        notifyDataSetChanged();
    }

    private class BasicOnClickListener implements View.OnClickListener
    {
        private LeftViewHolder leftViewHolder;

        public BasicOnClickListener(LeftViewHolder leftViewHolder)
        {
            this.leftViewHolder = leftViewHolder;
        }

        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.left_tv:
                    onItemClickListener.onItemClick(leftViewHolder.tv, leftViewHolder.getLayoutPosition());
                    break;
            }
        }
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }


    public static class LeftViewHolder extends RecyclerView.ViewHolder
    {
        public TextView tv;
        public LinearLayout layout;

        public LeftViewHolder(View itemView)
        {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.left_tv);
            layout = (LinearLayout) itemView.findViewById(R.id.layout);
        }
    }
}
