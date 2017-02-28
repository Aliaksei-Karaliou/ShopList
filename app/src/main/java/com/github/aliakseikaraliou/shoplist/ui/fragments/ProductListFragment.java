package com.github.aliakseikaraliou.shoplist.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.github.aliakseikaraliou.shoplist.R;
import com.github.aliakseikaraliou.shoplist.models.classes.Product;
import com.github.aliakseikaraliou.shoplist.models.interfaces.IProduct;
import com.github.aliakseikaraliou.shoplist.ui.adapters.ProductListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProductListFragment extends Fragment {

    private Context context;
    private List<IProduct> productList;
    private RecyclerView recyclerView;

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getContext();
        setHasOptionsMenu(true);
        productList = new ArrayList<>();
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.fragment_productlist_recycler);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        final ProductListAdapter adapter = new ProductListAdapter(context, productList);
        recyclerView.setAdapter(adapter);
        final DividerItemDecoration divider = new DividerItemDecoration(context, layoutManager.getOrientation());
        recyclerView.addItemDecoration(divider);
        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, final int direction) {
                final int position = viewHolder.getAdapterPosition();
                productList.remove(position);
                recyclerView.getAdapter().notifyItemRemoved(position);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_productlist, container, false);
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.fragment_productlist, menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();
        final String[] items = {getString(R.string.fragment_productlist_dialog_typeproduct),
                getString(R.string.fragment_productlist_dialog_barcode)};
        if (id == R.id.menu_fragment_productlist_add) {
            final AlertDialog alertDialog = new AlertDialog.Builder(context)
                    .setItems(items, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(final DialogInterface dialogInterface, final int which) {
                            if (items[which].equals(getString(R.string.fragment_productlist_dialog_typeproduct))) {
                                final EditText editText = new EditText(context);
                                final AlertDialog dialog = new AlertDialog.Builder(context)
                                        .setTitle(R.string.fragment_productlist_dialog_typeproduct)
                                        .setView(editText)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(final DialogInterface dialog, final int which) {
                                                final IProduct product = new Product(editText.getText().toString());
                                                productList.add(product);
                                                recyclerView.getAdapter().notifyItemInserted(productList.size() - 1);
                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(final DialogInterface dialog, final int which) {

                                            }
                                        })
                                        .create();
                                dialog.show();
                            } else if (items[which].equals(getString(R.string.fragment_productlist_dialog_barcode))) {
                                Toast.makeText(context, "Barcode", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).create();
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }
}
