package com.github.aliakseikaraliou.shoplist.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.aliakseikaraliou.shoplist.R;
import com.github.aliakseikaraliou.shoplist.models.interfaces.IProductList;

import java.util.List;
import java.util.Locale;

public class ProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final List<IProductList> productLists;
    private final String COUNT_TEMPLATE = "%d\nitems";

    public ProductListAdapter(final Context context, final List<IProductList> productLists) {
        this.context = context;
        this.productLists = productLists;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_productlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final IProductList productList = productLists.get(position);
        ((ViewHolder) holder).nameTextView.setText(productList.getTitle());
        ((ViewHolder) holder).countTextView.setText(String.format(Locale.getDefault(), COUNT_TEMPLATE, productList.size()));
    }

    @Override
    public int getItemCount() {
        return productLists.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView countTextView;
        private final TextView nameTextView;

        public ViewHolder(final View itemView) {
            super(itemView);
            countTextView = (TextView) itemView.findViewById(R.id.item_productlist_count);
            nameTextView = (TextView) itemView.findViewById(R.id.item_productlist_name);
        }
    }
}
