package com.github.aliakseikaraliou.shoplist.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.aliakseikaraliou.shoplist.R;
import com.github.aliakseikaraliou.shoplist.models.interfaces.IProduct;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final List<IProduct> productList;
    private OnProductClickListener productClickListener;


    @SuppressWarnings("AssignmentToCollectionOrArrayFieldFromParameter")
    public ProductAdapter(final Context context, final List<IProduct> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (productClickListener != null && view.getTag() != null) {
                    productClickListener.onClick((IProduct) view.getTag());
                }
            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final IProduct product = productList.get(position);
        viewHolder.name.setText(product.getName());
        viewHolder.itemView.setTag(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void setProductClickListener(final OnProductClickListener productClickListener) {
        this.productClickListener = productClickListener;
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private final TextView name;

        ViewHolder(final View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.item_product_name);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(final ContextMenu menu, final View view, final ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(ContextMenu.NONE, view.getId(), getAdapterPosition(), R.string.edit);
            menu.add(ContextMenu.NONE, view.getId(), getAdapterPosition(), R.string.activity_productlist_context_search);
        }
    }

    public interface OnProductClickListener {
        void onClick(IProduct product);
    }
}
