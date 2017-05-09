package com.github.aliakseikaraliou.shoplist.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.aliakseikaraliou.shoplist.R;
import com.github.aliakseikaraliou.shoplist.models.interfaces.IShopListProduct;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ShopListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final List<IShopListProduct> productList;
    private final List<Integer> checkedItems;

    public ShopListAdapter(final Context context, final List<IShopListProduct> productList) {
        this.context = context;
        this.productList = productList;
        this.checkedItems = new ArrayList<>();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_shoplist, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (view.getTag() != null) {
                    final Integer position = (Integer) view.getTag();
                    if (checkedItems.contains(position)) {
                        checkedItems.remove(position);
                        viewHolder.check.setChecked(false);
                        viewHolder.check.setVisibility(View.GONE);
                    } else {
                        checkedItems.add(position);
                        viewHolder.check.setChecked(true);
                        viewHolder.check.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        return viewHolder;
    }

    public List<Integer> getCheckedItems() {
        return checkedItems;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final IShopListProduct shopListProduct = productList.get(position);

        viewHolder.name.setText(shopListProduct.getProduct().getName());
        viewHolder.price.setText(String.valueOf(shopListProduct.getProduct().getPrice()));
        Picasso.with(context)
                .load(shopListProduct.getImageUrl())
                .into(viewHolder.image);
        if (checkedItems.contains(position)) {
            viewHolder.check.setChecked(true);
            viewHolder.check.setVisibility(View.VISIBLE);
        } else {
            viewHolder.check.setChecked(false);
            viewHolder.check.setVisibility(View.GONE);
        }

        viewHolder.itemView.setTag(position);

    }



    @Override
    public int getItemCount() {
        return productList.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView price;
        private final ImageView image;
        private final CheckBox check;

        public ViewHolder(final View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.item_shoplist_name);
            price = (TextView) itemView.findViewById(R.id.item_shoplist_price);
            image = (ImageView) itemView.findViewById(R.id.item_shoplist_image);
            check = (CheckBox) itemView.findViewById(R.id.item_shoplist_check);
        }
    }
}
