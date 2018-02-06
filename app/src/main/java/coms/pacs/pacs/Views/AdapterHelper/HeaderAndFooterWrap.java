package coms.pacs.pacs.Views.AdapterHelper;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.ck.hello.nestrefreshlib.View.Adpater.Base.BaseAdapter;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.SAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 不听话的好孩子 on 2018/2/6.
 */

public class HeaderAndFooterWrap extends RecyclerView.Adapter {
    RecyclerView.Adapter adapter;

    List<View> headers = new ArrayList<>();
    List<View> footers = new ArrayList<>();
    private static int HEADERBASE = 99999999;
    private static int FOOTERBASE = -99999999;

    public HeaderAndFooterWrap(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    public HeaderAndFooterWrap addHeader(View Header) {
        headers.add(Header);
        return this;
    }

    public HeaderAndFooterWrap addFooter(View footer) {
        footers.add(footer);
        return this;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType >= HEADERBASE) {
            int position = viewType - HEADERBASE;
            return new RecyclerView.ViewHolder(headers.get(position)) {
                @Override
                public String toString() {
                    return super.toString();
                }
            };
        } else if (viewType <= FOOTERBASE) {
            int position = FOOTERBASE - viewType;
            return new RecyclerView.ViewHolder(footers.get(position)) {
                @Override
                public String toString() {
                    return super.toString();
                }
            };
        }

        return adapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        if (adapter instanceof SAdapter) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                GridLayoutManager manager = (GridLayoutManager) layoutManager;
                final int spanCount = manager.getSpanCount();
                manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    public int getSpanSize(int position) {
                        int itemViewType = getItemViewType(position);
                        if (itemViewType <= FOOTERBASE || itemViewType >= HEADERBASE) {
                            return spanCount;
                        }
                        return ((SAdapter) adapter).setIfGridLayoutManagerSpan(itemViewType, position - headers.size(), spanCount);
                    }
                });
            }
        } else {
            adapter.onAttachedToRecyclerView(recyclerView);
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        if(adapter instanceof SAdapter){
            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
            if(params instanceof android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams) {
                int itemViewType = holder.getItemViewType();
                if (itemViewType <= FOOTERBASE || itemViewType >= HEADERBASE) {
                    ((android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams)params).setFullSpan(true);
                }else {
                    ((android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams) params).setFullSpan(((SAdapter) adapter).setIfStaggedLayoutManagerFullspan(itemViewType));
                }
            }
        }else{
            adapter.onViewAttachedToWindow(holder);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position < headers.size()) {
            onBindHeader(headers.get(position), position);
        } else if (position < headers.size() + adapter.getItemCount()) {
            adapter.onBindViewHolder(holder, position - headers.size());
        } else {
            onBindHeader(footers.get(position - headers.size() - adapter.getItemCount()), position);
        }
    }

    public void onBindHeader(View view, int position) {
    }

    public void onBindFooter(View view, int position) {
    }

    @Override
    public int getItemCount() {
        return headers.size() + adapter.getItemCount() + footers.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < headers.size()) {
            return HEADERBASE + position;
        } else if (position < headers.size() + adapter.getItemCount()) {
            return adapter.getItemViewType(position - headers.size());
        } else {
            return FOOTERBASE - position;
        }
    }
}
