package coms.pacs.pacs.Views.AdapterHelper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import coms.pacs.pacs.R;

/**
 * Created by 不听话的好孩子 on 2018/2/6.
 */

public class RefreshHeaderAndFooterWrap extends HeaderAndFooterWrap {
    public RefreshHeaderAndFooterWrap(RecyclerView.Adapter adapter) {
        super(adapter);
    }

    public RefreshHeaderAndFooterWrap addHeaderLayout(int layoutid, ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(layoutid, parent, false);
        headers.add(inflate);
        return this;
    }

    public RefreshHeaderAndFooterWrap addfooterLayout(int layoutid, ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(layoutid, parent, false);
        footers.add(inflate);
        return this;
    }

    public RefreshHeaderAndFooterWrap attachView(ViewGroup parent){
        addHeaderLayout(R.layout.header_layout,parent);
        addfooterLayout(R.layout.footer_layout,parent);
        return this;
    }
}
