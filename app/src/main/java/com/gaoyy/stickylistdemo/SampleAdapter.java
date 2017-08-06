package com.gaoyy.stickylistdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

import static com.gaoyy.stickylistdemo.R.layout.item;

/**
 * Created by gaoyy on 2017/8/4 0004.
 */

public class SampleAdapter extends BaseAdapter implements StickyListHeadersAdapter
{

    private LayoutInflater inflater;
    private List<Sample> data;
    private Context context;


    private OnOperationClickListener onOperationClickListener;

    public interface OnOperationClickListener
    {
        void onOperationClick(View parent,View view, int position);
    }

    public void setOnOperationClickListener(OnOperationClickListener listener)
    {
        this.onOperationClickListener = listener;
    }


    public SampleAdapter(List<Sample> data, Context context)
    {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount()
    {
        return data.size();
    }

    @Override
    public Object getItem(int position)
    {
        return data.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ItemViewHolder itemViewHolder;

        if (convertView == null)
        {
            itemViewHolder = new ItemViewHolder();
            convertView = inflater.inflate(item, parent, false);
            itemViewHolder.title = (TextView) convertView.findViewById(R.id.title);
            itemViewHolder.desc = (TextView) convertView.findViewById(R.id.desc);
            itemViewHolder.add = (ImageView) convertView.findViewById(R.id.add);
            itemViewHolder.miuns = (ImageView) convertView.findViewById(R.id.minus);
            itemViewHolder.count = (TextView) convertView.findViewById(R.id.count);
            itemViewHolder.parent = (RelativeLayout) convertView.findViewById(R.id.item_parent);
            convertView.setTag(itemViewHolder);
        }
        else
        {
            itemViewHolder = (ItemViewHolder) convertView.getTag();
        }


        itemViewHolder.title.setText(data.get(position).getTitle());
        itemViewHolder.desc.setText(data.get(position).getDesc());


        if(data.get(position).getCount() == 0)
        {

            itemViewHolder.miuns.setAlpha(0f);
            itemViewHolder.count.setAlpha(0f);
            itemViewHolder.miuns.setTag(true);
        }
        else
        {
            itemViewHolder.count.setAlpha(1f);
            itemViewHolder.miuns.setAlpha(1f);
            itemViewHolder.count.setText(data.get(position).getCount()+"");
            itemViewHolder.miuns.setTag(false);
        }

        if(onOperationClickListener != null)
        {
            itemViewHolder.add.setOnClickListener(new BasicOnClickListener(itemViewHolder,position));
            itemViewHolder.miuns.setOnClickListener(new BasicOnClickListener(itemViewHolder,position));
        }


        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent)
    {
        HeaderViewHolder headerViewHolder;
        if (convertView == null)
        {
            headerViewHolder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.header, parent, false);
            headerViewHolder.header = (TextView) convertView.findViewById(R.id.header);
            convertView.setTag(headerViewHolder);
        }
        else
        {
            headerViewHolder = (HeaderViewHolder) convertView.getTag();
        }
        headerViewHolder.header.setText(data.get(position).getGroupTitle());
        return convertView;
    }

    @Override
    public long getHeaderId(int position)
    {
        //return the first character of the country as ID because this is what headers are based upon
        return Long.parseLong(data.get(position).getGroupId());
    }

    class HeaderViewHolder
    {
        TextView header;
    }

    class ItemViewHolder
    {
        TextView title;
        TextView desc;

        ImageView add;
        ImageView miuns;
        TextView count;

        RelativeLayout parent;

    }


    public class BasicOnClickListener implements View.OnClickListener
    {
        ItemViewHolder itemViewHolder;
        int position;

        public BasicOnClickListener(ItemViewHolder itemViewHolder,int position)
        {
            this.itemViewHolder = itemViewHolder;
            this.position = position;
        }

//        @Override
//        public void onClick(View view)
//        {
//            int id = view.getId();
//            Sample sample = data.get(position);
//            int count = sample.getCount();
//            switch (id)
//            {
//                case R.id.add:
//                    Toast.makeText(context,"add =="+position,Toast.LENGTH_SHORT).show();
//                    count = count +1;
//                    data.get(position).setCount(count);
//                    notifyDataSetChanged();
//                    break;
//                case R.id.minus:
//                    Toast.makeText(context,"minus =="+position,Toast.LENGTH_SHORT).show();
//                    count = count -1;
//                    if(count < 0 )
//                    {
//                        count = 0;
//                    }
//                    data.get(position).setCount(count);
//                    notifyDataSetChanged();
//                    break;
//            }
//        }
        @Override
        public void onClick(View view)
        {
            switch (view.getId())
            {
                case R.id.add:
                    onOperationClickListener.onOperationClick(itemViewHolder.parent,itemViewHolder.add,position);
                    break;
                case R.id.minus:
                    onOperationClickListener.onOperationClick(itemViewHolder.parent,itemViewHolder.miuns,position);
                    break;
            }
        }
    }


    public void updateDataCount(List<Sample> data)
    {
        this.data = data;
        notifyDataSetChanged();
    }
}
