package com.github.aliakseikaraliou.shoplist.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
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
    @SuppressWarnings("FieldCanBeLocal")
    private final String COUNT_TEMPLATE = "%d\nitems";
    private OnProductListClickListener onProductListClickListener;

    @SuppressWarnings("AssignmentToCollectionOrArrayFieldFromParameter")
    public ProductListAdapter(final Context context, final List<IProductList> productLists) {
        this.context = context;
        this.productLists = productLists;
    }

    public void setOnProductListClickListener(final OnProductListClickListener onProductListClickListener) {
        this.onProductListClickListener = onProductListClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_productlist, parent, false);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                if (onProductListClickListener != null && view.getTag() != null) {
                    onProductListClickListener.onClick((Integer) view.getTag());
                }
            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final IProductList productList = productLists.get(position);
        holder.itemView.setTag(position);
        ((ViewHolder) holder).nameTextView.setText(productList.getTitle());
        ((ViewHolder) holder).countTextView.setText(String.format(Locale.getDefault(), COUNT_TEMPLATE, productList.size()));
    }



    @Override
    public int getItemCount() {
        return productLists.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private final TextView countTextView;
        private final TextView nameTextView;

        ViewHolder(final View itemView) {
            super(itemView);
            countTextView = (TextView) itemView.findViewById(R.id.item_productlist_count);
            nameTextView = (TextView) itemView.findViewById(R.id.item_productlist_name);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(final ContextMenu menu, final View view, final ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(0, view.getId(), super.getLayoutPosition(), R.string.context_menu_productlist_change);
            menu.add(0, view.getId(), super.getLayoutPosition(), R.string.context_menu_productlist_delete);
            menu.add(0, view.getId(), super.getLayoutPosition(), R.string.context_menu_productlist_save);
        }
    }

    public interface OnProductListClickListener {

        void onClick(int position);
    }
}
