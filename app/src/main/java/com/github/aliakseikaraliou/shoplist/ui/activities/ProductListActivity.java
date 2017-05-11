package com.github.aliakseikaraliou.shoplist.ui.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aliakseikaraliou.shoplist.R;
import com.github.aliakseikaraliou.shoplist.models.classes.Product;
import com.github.aliakseikaraliou.shoplist.models.classes.ProductList;
import com.github.aliakseikaraliou.shoplist.models.interfaces.IProduct;
import com.github.aliakseikaraliou.shoplist.models.interfaces.IProductList;
import com.github.aliakseikaraliou.shoplist.models.interfaces.IShopListProduct;
import com.github.aliakseikaraliou.shoplist.ui.UiConstants;
import com.github.aliakseikaraliou.shoplist.ui.adapters.ProductAdapter;

import org.jsoup.helper.StringUtil;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

public class ProductListActivity extends AppCompatActivity {

    private IProductList productList;
    private RecyclerView recyclerView;
    private Stack<Pair<Integer, IProduct>> deletedProducts;
    private int position = -1;
    private boolean changed;
    private TextView totalPrice;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productlist);

        changed = false;

        final Intent intent = getIntent();

        if (intent.hasExtra(UiConstants.Strings.PRODUCT_LIST)) {
            productList = intent.getParcelableExtra(UiConstants.Strings.PRODUCT_LIST);
            position = intent.getIntExtra(UiConstants.Strings.POSITION, -1);
        } else if (intent.hasExtra(UiConstants.Strings.PRODUCT_LIST_NAME)) {
            final String productListName = intent.getStringExtra(UiConstants.Strings.PRODUCT_LIST_NAME);
            productList = new ProductList(productListName);
        }

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(productList.getTitle());
        }

        deletedProducts = new Stack<>();

        recyclerView = (RecyclerView) findViewById(R.id.activity_productlist_recycler);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        final ProductAdapter adapter = new ProductAdapter(this, productList.getList());
        adapter.setProductClickListener(new ProductAdapter.OnProductClickListener() {
            @Override
            public void onClick(final IProduct product) {
                final View view = getLayoutInflater().inflate(R.layout.item_product_dialog_view, null);
                final TextView nameEditText = (TextView) view.findViewById(R.id.item_product_dialog_name);
                nameEditText.setText(product.getName());
                final TextView descriptionEditText = (TextView) view.findViewById(R.id.item_product_dialog_description);
                descriptionEditText.setText(product.getDescription());
                final TextView priceEditText = (TextView) view.findViewById(R.id.item_product_dialog_price);
                priceEditText.setText(String.valueOf(product.getPrice()));
                final TextView quantityEditText = (TextView) view.findViewById(R.id.item_product_dialog_quantity);
                quantityEditText.setText(String.valueOf(product.getQuantity()));
                final AlertDialog alertDialog = new AlertDialog.Builder(ProductListActivity.this)
                        .setView(view)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, final int which) {

                            }
                        })
                        .create();
                alertDialog.show();
            }
        });
        recyclerView.setAdapter(adapter);
        final DividerItemDecoration divider = new DividerItemDecoration(this, layoutManager.getOrientation());
        recyclerView.addItemDecoration(divider);
        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, final int direction) {
                final int position = viewHolder.getAdapterPosition();
                final IProduct deletedProduct = productList.remove(position);
                recyclerView.getAdapter().notifyItemRemoved(position);
                setTotalPrice();
                deletedProducts.push(new Pair<>(position, deletedProduct));
                changed = true;
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        totalPrice = (TextView) findViewById(R.id.activity_productlist_total);

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        final MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_productlist, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();
        final String[] items = new String[]{getString(R.string.productlist_dialog_typeproduct), getString(R.string.product_list_dialog_shop)};
        if (id == R.id.menu_activity_productlist_add) {
            final AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setItems(items, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(final DialogInterface dialogInterface, final int which) {
                            if (items[which].equals(getString(R.string.productlist_dialog_typeproduct))) {
                                @SuppressLint("InflateParams")
                                final View view = getLayoutInflater().inflate(R.layout.item_product_dialog_edit, null);
                                final AlertDialog dialog = new AlertDialog.Builder(ProductListActivity.this)
                                        .setTitle(R.string.productlist_dialog_typeproduct)
                                        .setView(view)
                                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(final DialogInterface dialog, final int which) {
                                                final String name = ((TextView) view.findViewById(R.id.item_product_dialog_name)).getText().toString();
                                                final String description = ((TextView) view.findViewById(R.id.item_product_dialog_description)).getText().toString();
                                                double price;
                                                try {
                                                    price = Double.parseDouble(((TextView) view.findViewById(R.id.item_product_dialog_price)).getText().toString());
                                                } catch (final NumberFormatException e) {
                                                    price = 0;
                                                }
                                                double quantity;
                                                try {
                                                    quantity = Double.parseDouble(((TextView) view.findViewById(R.id.item_product_dialog_quantity)).getText().toString());
                                                } catch (final NumberFormatException e) {
                                                    quantity = 1;
                                                }
                                                final IProduct product = new Product.Builder(name)
                                                        .setPrice(price)
                                                        .setDescription(description)
                                                        .setQuantity(quantity)
                                                        .setPrice(price)
                                                        .build();
                                                productList.add(product);
                                                changed = true;
                                                recyclerView.getAdapter().notifyDataSetChanged();
                                                setTotalPrice();
                                            }
                                        })
                                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(final DialogInterface dialog, final int which) {

                                            }
                                        })
                                        .create();
                                dialog.show();
                            } else if (items[which].equals(getString(R.string.productlist_dialog_barcode))) {
                                final Intent intent = new Intent(ProductListActivity.this, BarcodeActivity.class);
                                startActivity(intent);
                            } else if (items[which].equals(getString(R.string.product_list_dialog_shop))) {

                                final EditText editText = new EditText(ProductListActivity.this);
                                final AlertDialog dialog = new AlertDialog.Builder(ProductListActivity.this)
                                        .setTitle(R.string.productlist_dialog_typeproduct)
                                        .setView(editText)
                                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(final DialogInterface dialog, final int which) {
                                                final Intent intent = new Intent(ProductListActivity.this, ShopListActivity.class);
                                                intent.putExtra(UiConstants.Strings.PRODUCT_TITLE, editText.getText().toString());
                                                startActivityForResult(intent, UiConstants.Ids.SHOP_LIST);
                                            }
                                        })
                                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(final DialogInterface dialog, final int which) {

                                            }
                                        })
                                        .create();
                                dialog.show();
                            }
                        }
                    }).create();
            alertDialog.show();
        } else if (id == R.id.menu_activity_productlist_restore) {
            if (!deletedProducts.empty()) {
                final Pair<Integer, IProduct> productPair = deletedProducts.pop();
                final Integer position = productPair.first;
                final IProduct product = productPair.second;
                productList.add(position, product);
                recyclerView.getAdapter().notifyItemInserted(position);
                setTotalPrice();
            } else {
                Toast.makeText(this, R.string.activity_productlist_noproductstorestore, Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.menu_activity_productlist_restoreall) {
            if (!deletedProducts.empty()) {
                Pair<Integer, IProduct> productPair;
                Integer position;
                IProduct product;
                while (!deletedProducts.empty()) {
                    productPair = deletedProducts.pop();
                    position = productPair.first;
                    product = productPair.second;
                    productList.add(position, product);
                    recyclerView.getAdapter().notifyItemInserted(position);
                    setTotalPrice();
                }
            } else {
                Toast.makeText(this, R.string.activity_productlist_noproductstorestore, Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.menu_activity_productlist_save) {
            save();
            finish();
        } else if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void save() {
        final Intent intent = new Intent();
        intent.putExtra(UiConstants.Strings.PRODUCT_LIST, productList);
        intent.putExtra(UiConstants.Strings.POSITION, position);
        setResult(UiConstants.Ids.PRODUCTLIST_CREATE, intent);
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        final int position = item.getOrder();
        final IProduct product = productList.get(position);
        if (item.getTitle().equals(getString(R.string.activity_productlist_context_delete))) {
            final IProduct deletedProduct = productList.remove(position);
            recyclerView.getAdapter().notifyItemRemoved(position);
            setTotalPrice();
            deletedProducts.push(new Pair<>(position, deletedProduct));
            changed = true;
        } else if (item.getTitle().equals(getString(R.string.edit))) {
            changed = true;
            @SuppressLint("InflateParams")
            final View dialogView = getLayoutInflater().inflate(R.layout.item_product_dialog_edit, null);
            final TextView nameEditText = (TextView) dialogView.findViewById(R.id.item_product_dialog_name);
            nameEditText.setText(product.getName());
            final TextView descriptionEditText = (TextView) dialogView.findViewById(R.id.item_product_dialog_description);
            descriptionEditText.setText(product.getDescription());
            final TextView priceEditText = (TextView) dialogView.findViewById(R.id.item_product_dialog_price);
            priceEditText.setText(String.valueOf(product.getPrice()));
            final TextView quantityEditText = (TextView) dialogView.findViewById(R.id.item_product_dialog_quantity);
            quantityEditText.setText(String.valueOf(product.getQuantity()));
            final AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.edit)
                    .setView(dialogView)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {
                            try {
                                final NumberFormat numberFormat = NumberFormat.getInstance();
                                final String name = nameEditText.getText().toString();
                                final String description = descriptionEditText.getText().toString();
                                final double quantity;
                                final double price;
                                if (!StringUtil.isBlank(quantityEditText.getText().toString())) {
                                    quantity = numberFormat.parse(quantityEditText.getText().toString()).doubleValue();
                                } else {
                                    quantity = 1;
                                }
                                if (!StringUtil.isBlank(priceEditText.getText().toString())) {
                                    price = numberFormat.parse(priceEditText.getText().toString()).doubleValue();
                                } else {
                                    price = 0;
                                }
                                final IProduct product = new Product.Builder(name)
                                        .setDescription(description)
                                        .setPrice(price)
                                        .setQuantity(quantity)
                                        .build();
                                productList.set(position, product);
                                recyclerView.getAdapter().notifyDataSetChanged();
                                setTotalPrice();
                            } catch (final Throwable e) {
                                Toast.makeText(ProductListActivity.this, R.string.error_default, Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .create();
            alertDialog.show();
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.activity_productlist_dialog_savechanges)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        save();
                        ProductListActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        setResult(UiConstants.Ids.PRODUCTLIST_CREATE, null);
                        ProductListActivity.super.onBackPressed();
                    }
                })
                .setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {

                    }
                })
                .create();
        if (changed) {
            alertDialog.show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (intent != null) {
            final List<IShopListProduct> shopList = intent.getParcelableArrayListExtra(UiConstants.Strings.SHOP_LIST);
            for (final IShopListProduct shopListProduct : shopList) {
                productList.add(shopListProduct.getProduct());
            }
            recyclerView.getAdapter().notifyDataSetChanged();
            setTotalPrice();
            changed = true;
        }
    }

    private void setTotalPrice() {
        final List<IProduct> list = productList.getList();
        double sum = 0;
        for (final IProduct product : list) {
            sum += product.getPrice();
        }
        totalPrice.setText(String.format(Locale.getDefault(), "Total price: %f", sum));
    }
}
