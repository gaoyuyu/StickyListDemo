package com.gaoyy.stickylistdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import static com.gaoyy.stickylistdemo.R.id.minus;

public class MainActivity extends AppCompatActivity
{

    private List<Sample> data = new ArrayList<>();
    private List<Type> headerData = new ArrayList<>();

    private RelativeLayout activityMain;
    private RecyclerView left;
    private StickyListHeadersListView list;
    private LinearLayout linearLayout;
    private ImageView cart;

    SampleAdapter sampleAdapter;

    ReListAdapter reListAdapter;

    private void assignViews()
    {
        activityMain = (RelativeLayout) findViewById(R.id.activity_main);
        left = (RecyclerView) findViewById(R.id.left);
        list = (StickyListHeadersListView) findViewById(R.id.list);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        cart = (ImageView) findViewById(R.id.cart);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignViews();
        initData();

        sampleAdapter = new SampleAdapter(data, this);
        list.setAdapter(sampleAdapter);

        reListAdapter = new ReListAdapter(this, headerData);
        left.setAdapter(reListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        left.setLayoutManager(linearLayoutManager);
        left.setItemAnimator(new DefaultItemAnimator());

        reListAdapter.setOnItemClickListener(new ReListAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                int select = -1;
                for (int i = 0; i < data.size(); i++)
                {
                    if (Integer.valueOf(data.get(i).getGroupId()) == position)
                    {
                        select = i;
                        break;
                    }
                }
                updateTypeList(position);
                list.setSelection(select);

            }
        });

        list.setOnScrollListener(new AbsListView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
                Sample sample = data.get(firstVisibleItem);
                int groupId = Integer.valueOf(sample.getGroupId());
                left.smoothScrollToPosition(groupId);
                updateTypeList(groupId);
            }
        });

        sampleAdapter.setOnOperationClickListener(new SampleAdapter.OnOperationClickListener()
        {
            @Override
            public void onOperationClick(View parent, View view, int position)
            {
                ImageView add = (ImageView) parent.findViewById(R.id.add);
                ImageView miuns = (ImageView) parent.findViewById(minus);

                Sample sample = data.get(position);
                int count = sample.getCount();
                switch (view.getId())
                {
                    case R.id.add:
                        //添加时动画效果
                        createAnim(add);
                        count = count + 1;
                        data.get(position).setCount(count);

                        boolean tag = ((boolean) miuns.getTag());
                        if (tag)
                        {
                            minusAnim(miuns);
                        }
                        else
                        {
                            sampleAdapter.updateDataCount(data);
                        }
                        break;
                    case R.id.minus:
                        count = count - 1;
                        if (count < 0)
                        {
                            count = 0;
                        }
                        data.get(position).setCount(count);
                        sampleAdapter.updateDataCount(data);
                        break;
                }
            }

            /**
             * 添加时动画效果
             * @param add
             */
            private void createAnim(ImageView add)
            {
                //获取+号的坐标
                int[] addPoint = new int[2];
                add.getLocationInWindow(addPoint);

                //获取购物车的坐标
                int[] cartPoint = new int[2];
                cart.getLocationInWindow(cartPoint);

                //获父布局的坐标
                int[] parentPoint = new int[2];
                activityMain.getLocationInWindow(parentPoint);

                final ImageView newAdd = new ImageView(MainActivity.this);
                newAdd.setLayoutParams(new RelativeLayout.LayoutParams(dip2px(MainActivity.this, 25), dip2px(MainActivity.this, 25)));
                newAdd.setImageResource(R.mipmap.button_add);
                newAdd.setX(addPoint[0]);
                newAdd.setY(addPoint[1] - parentPoint[1]);
                activityMain.addView(newAdd);

                //X轴平移动画
                ValueAnimator x = ValueAnimator.ofInt((int) newAdd.getX(), cartPoint[0]);
                x.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
                {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator)
                    {
                        int value = (int) valueAnimator.getAnimatedValue();
                        newAdd.setTranslationX(value);
                    }
                });
                //设置线性插值器
                x.setInterpolator(new LinearInterpolator());

                //Y轴平移动画
                ValueAnimator y = ValueAnimator.ofInt((int) newAdd.getY(), cartPoint[1] - parentPoint[1]);
                y.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
                {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator)
                    {
                        int value = (int) valueAnimator.getAnimatedValue();
                        newAdd.setTranslationY(value);
                    }
                });
                //设置加速插值器
                y.setInterpolator(new AccelerateInterpolator());


                //新增ImageView加号的渐隐动画
                ObjectAnimator newAddAlpha = ObjectAnimator.ofFloat(newAdd, "Alpha", 1.0f, 0.0f);
                newAddAlpha.addListener(new AnimatorListenerAdapter()
                {
                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        super.onAnimationEnd(animation);
                        //动画结束移除view
                        activityMain.removeView(newAdd);
                    }
                });

                //动画集合
                AnimatorSet set = new AnimatorSet();
                set.playTogether(x, y, newAddAlpha);
                set.start();

                set.addListener(new AnimatorListenerAdapter()
                {
                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        super.onAnimationEnd(animation);
                        //购物车的放大动画
                        cartAnim();
                    }
                });
            }
        });

    }

    /**
     * 购物车放大动画
     */
    private void cartAnim()
    {
        ObjectAnimator cartScale = ObjectAnimator.ofFloat(cart, "ScaleXY", 0.6f, 1.0f);
        cartScale.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator)
            {
                float value = (float) valueAnimator.getAnimatedValue();
                cart.setScaleX(value);
                cart.setScaleY(value);
            }
        });
        cartScale.start();
    }

    /**
     * dp转为px
     *
     * @param context  上下文
     * @param dipValue dp值
     * @return
     */
    private int dip2px(Context context, float dipValue)
    {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dipValue, r.getDisplayMetrics());
    }

    public void minusAnim(final View minus)
    {
        ObjectAnimator translationXAnim = ObjectAnimator.ofFloat(minus, "TranslationX", 100, 0);
        ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(minus, "Alpha", 0f, 1f);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(translationXAnim, alphaAnim);
        set.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                super.onAnimationEnd(animation);
                sampleAdapter.updateDataCount(data);
            }
        });
        set.start();

    }

    private void updateTypeList(int position)
    {
        for (int i = 0; i < headerData.size(); i++)
        {
            Type type = headerData.get(i);

            if (position == i)
            {
                type.setStatus(1);
                headerData.remove(i);
                headerData.add(i, type);
            }
            else
            {
                type.setStatus(0);
                headerData.remove(i);
                headerData.add(i, type);
            }
        }
        reListAdapter.updateData(headerData);
    }

    private void initData()
    {
        int random = (int) (Math.random() * 100 + 1);
        for (int i = 0; i < 15; i++)
        {
            headerData.add(new Type(0, "种类" + i));
        }

        for (int i = 0; i < headerData.size(); i++)
        {
            for (int k = 0; k < 10; k++)
            {
                data.add(new Sample("" + k, "" + i, "种类" + i + "   分类" + k, (int) (Math.random() * 100 + 1) + "", "种类" + i, 0));
            }
        }
    }
}
