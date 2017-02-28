package com.github.aliakseikaraliou.shoplist.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.aliakseikaraliou.shoplist.R;
import com.github.aliakseikaraliou.shoplist.models.interfaces.IProduct;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final List<IProduct> productList;

    public ProductListAdapter(final Context context, final List<IProduct> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final IProduct product = productList.get(position);
        viewHolder.name.setText(product.getName());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;

        public ViewHolder(final View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.item_product_name);
        }
    }
}
