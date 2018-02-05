package com.cryallen.common.function.comlib.adapter.common;

import android.content.Context;

import com.cryallen.common.function.comlib.adapter.delegate.ItemViewDelegate;
import com.cryallen.common.function.comlib.adapter.recycler.RecyclerViewHolder;

import java.util.List;

/**
 * 通用Adapter抽象类 常用于 listview,gridview等
 * Created by chenran3 on 2017/12/18.
 */

public abstract class CommonAdapter <T> extends MultiItemTypeAdapter<T> {

    public CommonAdapter(Context context, final int layoutId, List<T> datas)
    {
        super(context, datas);

        addItemViewDelegate(new ItemViewDelegate<T>()
        {
            @Override
            public int getItemViewLayoutId()
            {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position)
            {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position)
            {
                CommonAdapter.this.convert(holder, t, position);
            }

            @Override
            public void recyclerConvert(RecyclerViewHolder holder, T t, int position)
            {

            }
        });
    }

    @Override
    protected abstract void convert(ViewHolder viewHolder, T item, int position);
}
