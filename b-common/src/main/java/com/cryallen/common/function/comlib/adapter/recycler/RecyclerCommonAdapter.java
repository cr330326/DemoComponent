package com.cryallen.common.function.comlib.adapter.recycler;

import android.content.Context;
import android.view.LayoutInflater;

import com.cryallen.common.function.comlib.adapter.common.ViewHolder;
import com.cryallen.common.function.comlib.adapter.delegate.ItemViewDelegate;

import java.util.List;

/**
 * Created by chenran3 on 2017/12/20.
 */

public abstract class RecyclerCommonAdapter<T> extends RecyclerMultiItemTypeAdapter<T> {

    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    public RecyclerCommonAdapter(final Context context, final int layoutId, List<T> datas)
    {
        super(context, datas);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;

        addItemViewDelegate(new ItemViewDelegate<T>()
        {
            @Override
            public int getItemViewLayoutId()
            {
                return layoutId;
            }

            @Override
            public boolean isForViewType( T item, int position)
            {
                return true;
            }

            @Override
            public void recyclerConvert(RecyclerViewHolder holder, T t, int position)
            {
                RecyclerCommonAdapter.this.recyclerConvert(holder, t, position);
            }

            @Override
            public void convert(ViewHolder holder, T t, int position)
            {

            }
        });
    }

    protected abstract void recyclerConvert(RecyclerViewHolder holder, T t, int position);
}
