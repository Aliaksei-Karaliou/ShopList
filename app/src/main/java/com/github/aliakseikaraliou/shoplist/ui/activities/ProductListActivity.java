package com.github.aliakseikaraliou.shoplist.ui.activities;

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
import android.widget.EditText;
import android.widget.Toast;

import com.github.aliakseikaraliou.shoplist.R;
import com.github.aliakseikaraliou.shoplist.models.classes.Product;
import com.github.aliakseikaraliou.shoplist.models.classes.ProductList;
import com.github.aliakseikaraliou.shoplist.models.interfaces.IProduct;
import com.github.aliakseikaraliou.shoplist.models.interfaces.IProductList;
import com.github.aliakseikaraliou.shoplist.ui.UiConstants;
import com.github.aliakseikaraliou.shoplist.ui.adapters.ProductAdapter;

import java.util.Stack;

public class ProductListActivity extends AppCompatActivity {

    private IProductList productList;
    private RecyclerView recyclerView;
    private Stack<Pair<Integer, IProduct>> deletedProducts;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productlist);

        final Intent intent = getIntent();
        final String productListName = intent.getStringExtra(UiConstants.Strings.PRODUCT_LIST_NAME);
        productList = new ProductList(productListName);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(productList.getTitle());
        }

        deletedProducts = new Stack<>();

        recyclerView = (RecyclerView) findViewById(R.id.fragment_productlist_recycler);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        final ProductAdapter adapter = new ProductAdapter(this, productList);
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
                deletedProducts.push(new Pair<>(position, deletedProduct));
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
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
        final String[] items = new String[]{getString(R.string.productlist_dialog_typeproduct),
                getString(R.string.productlist_dialog_barcode), getString(R.string.product_list_dialog_shop)};
        if (id == R.id.menu_activity_productlist_add) {
            final AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setItems(items, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(final DialogInterface dialogInterface, final int which) {
                            if (items[which].equals(getString(R.string.productlist_dialog_typeproduct))) {
                                final EditText editText = new EditText(ProductListActivity.this);
                                final AlertDialog dialog = new AlertDialog.Builder(ProductListActivity.this)
                                        .setTitle(R.string.productlist_dialog_typeproduct)
                                        .setView(editText)
                                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(final DialogInterface dialog, final int which) {
                                                final IProduct product = new Product.Builder(editText.getText().toString())
                                                        .build();
                                                productList.add(product);
                                                recyclerView.getAdapter().notifyItemInserted(productList.size() - 1);
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
                            } else if (items[which].equals(getString(R.string.product_list_dialog_shop))){
                                final Intent intent = new Intent(ProductListActivity.this,ShopListActivity.class);
                                startActivity(intent);
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
                }
            } else {
                Toast.makeText(this, R.string.activity_productlist_noproductstorestore, Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.menu_activity_productlist_save) {
            final Intent intent = new Intent();
            intent.putExtra(UiConstants.Strings.PRODUCT_LIST, productList);
            setResult(UiConstants.Ids.PRODUCTLIST_CREATE, intent);
            finish();
        } else if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        final IProduct product = productList.get(item.getOrder());
        if (item.getTitle().equals(getString(R.string.activity_productlist_context_search))) {
            final Intent intent = new Intent(this, ShopListActivity.class);
            intent.putExtra(UiConstants.Strings.PRODUCT_TITLE, product.getName());
            startActivity(intent);
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setResult(UiConstants.Ids.PRODUCTLIST_CREATE, null);
        super.onBackPressed();
    }
}
