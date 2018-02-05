package com.cryallen.common.function.comlib.adapter.delegate;

import com.cryallen.common.function.comlib.adapter.common.ViewHolder;
import com.cryallen.common.function.comlib.adapter.recycler.RecyclerViewHolder;

/**
 * Created by chenran3 on 2017/12/18.
 */

public interface ItemViewDelegate<T> {

    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(ViewHolder holder, T t, int position);

    void recyclerConvert(RecyclerViewHolder holder, T t, int position);
}
