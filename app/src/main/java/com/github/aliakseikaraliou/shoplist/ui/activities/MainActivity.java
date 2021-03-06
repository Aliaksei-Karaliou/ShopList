package com.github.aliakseikaraliou.shoplist.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.aliakseikaraliou.shoplist.R;
import com.github.aliakseikaraliou.shoplist.db.firebase.FirebaseDbHelper;
import com.github.aliakseikaraliou.shoplist.db.firebase.OnDataChanged;
import com.github.aliakseikaraliou.shoplist.models.classes.User;
import com.github.aliakseikaraliou.shoplist.models.interfaces.IProductList;
import com.github.aliakseikaraliou.shoplist.models.interfaces.IUser;
import com.github.aliakseikaraliou.shoplist.ui.UiConstants;
import com.github.aliakseikaraliou.shoplist.ui.adapters.ProductListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private List<IProductList> list;
    private RecyclerView recyclerView;
    private FirebaseDbHelper firebaseDbHelper;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list = new ArrayList<>();

        final FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {
                final EditText editText = new EditText(MainActivity.this);
                final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Enter product list name")
                        .setView(editText)
                        .setPositiveButton("Create", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(final DialogInterface dialog, final int which) {
                                final Intent intent = new Intent(MainActivity.this, ProductListActivity.class);
                                final String productListName = editText.getText().toString();
                                intent.putExtra(UiConstants.Strings.PRODUCT_LIST_NAME, productListName);
                                startActivityForResult(intent, UiConstants.Ids.PRODUCTLIST_CREATE);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(final DialogInterface dialog, final int which) {
                            }
                        })
                        .create();
                alertDialog.show();

            }
        });

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = ((RecyclerView) findViewById(R.id.activity_main_recycler));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        final ProductListAdapter adapter = new ProductListAdapter(this, list);
        adapter.setOnProductListClickListener(new ProductListAdapter.OnProductListClickListener() {

            @Override
            public void onClick(final int position) {
                final Intent intent = new Intent(MainActivity.this, ProductListActivity.class);
                intent.putExtra(UiConstants.Strings.PRODUCT_LIST, list.get(position));
                intent.putExtra(UiConstants.Strings.POSITION, position);
                startActivityForResult(intent, UiConstants.Ids.PRODUCTLIST_CHANGE);
            }
        });
        recyclerView.setAdapter(adapter);

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.activity_main_progress);

        firebaseDbHelper = new FirebaseDbHelper();
        firebaseDbHelper.setOnChangeListener(new OnDataChanged<List<IProductList>>() {

            @Override
            public void onChange(final List<IProductList> data) {
                list.clear();
                list.addAll(data);
                recyclerView.getAdapter().notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(final DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        // Handle navigation view item clicks here.
        final int id = item.getItemId();
        if (id == R.id.log_out) {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Succeed", Toast.LENGTH_SHORT).show();
            final Intent intent = new Intent(this, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            final IProductList productList = data.getParcelableExtra(UiConstants.Strings.PRODUCT_LIST);
            if (requestCode == UiConstants.Ids.PRODUCTLIST_CREATE) {
                firebaseDbHelper.push(productList);
            } else if (requestCode == UiConstants.Ids.PRODUCTLIST_CHANGE) {
                final int position = data.getIntExtra(UiConstants.Strings.POSITION, -1);
                if (productList.size() > 0) {
                    firebaseDbHelper.update(productList);
                } else {
                    firebaseDbHelper.delete(productList);
                }
            }
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        final int position = item.getOrder();
        if (item.getTitle().equals(getString(R.string.context_menu_productlist_change))) {
            final Intent intent = new Intent(this, ProductListActivity.class);
            intent.putExtra(UiConstants.Strings.PRODUCT_LIST, list.get(position));
            intent.putExtra(UiConstants.Strings.POSITION, position);
            startActivityForResult(intent, UiConstants.Ids.PRODUCTLIST_CHANGE);
        } else if (item.getTitle().equals(getString(R.string.context_menu_productlist_delete))) {
            final IProductList product = list.remove(position);
            firebaseDbHelper.delete(product);
            recyclerView.getAdapter().notifyItemRemoved(position);
        } else if (item.getTitle().equals(getString(R.string.context_menu_productlist_save))) {
            final EditText emailEditText = new EditText(this);
            emailEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            final AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.activity_main_send_title)
                    .setView(emailEditText)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {
                            final String email = String.valueOf(emailEditText.getText());
                            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                final IProductList list = MainActivity.this.list.get(position);
                                final IUser user = new User(email);
                                firebaseDbHelper.send(user, list);
                            }
                        }
                    })
                    .create();
            alertDialog.show();
        }
        return super.onContextItemSelected(item);
    }
}
