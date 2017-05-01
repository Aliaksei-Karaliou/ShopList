package com.github.aliakseikaraliou.shoplist.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.aliakseikaraliou.shoplist.R;
import com.github.aliakseikaraliou.shoplist.models.interfaces.IShopListProduct;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShopListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final List<IShopListProduct> productList;

    public ShopListAdapter(final Context context, final List<IShopListProduct> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_shoplist, parent, false);
        return new ViewHolder(view);
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
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView price;
        private final ImageView image;
        private final Button add;

        public ViewHolder(final View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.item_shoplist_name);
            price = (TextView) itemView.findViewById(R.id.item_shoplist_price);
            image = (ImageView) itemView.findViewById(R.id.item_shoplist_image);
            add = (Button) itemView.findViewById(R.id.item_shoplist_button);
        }
    }
}
