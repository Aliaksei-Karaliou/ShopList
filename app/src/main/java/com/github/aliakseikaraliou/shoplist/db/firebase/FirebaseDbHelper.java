package com.github.aliakseikaraliou.shoplist.db.firebase;

import android.support.annotation.NonNull;

import com.github.aliakseikaraliou.shoplist.models.classes.ProductList;
import com.github.aliakseikaraliou.shoplist.models.interfaces.IProduct;
import com.github.aliakseikaraliou.shoplist.models.interfaces.IProductList;
import com.github.aliakseikaraliou.shoplist.models.interfaces.IUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FirebaseDbHelper {

    private static final String LIST = "list";
    private static final String SENDER = "sender";
    private FirebaseUser firebaseUser;

    public FirebaseDbHelper() {
        this.firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void push(@NonNull final IProductList productList) {
        if (firebaseUser != null && firebaseUser.getEmail() != null && productList.getId() == null) {
            final DatabaseReference emailReference = FirebaseDatabase.getInstance().getReference("").child(firebaseUser.getEmail().toLowerCase().replace('.', ','));
            final DatabaseReference keyReference = emailReference.push();
            productList.setId(keyReference.getKey());
            keyReference.setValue(productList);
        }
    }

    public void setOnChangeListener(@NonNull final OnDataChanged<List<IProductList>> productListOnDataChanged) {
        if (firebaseUser != null && firebaseUser.getEmail() != null) {
            final DatabaseReference emailReference = FirebaseDatabase.getInstance().getReference("").child(firebaseUser.getEmail().replace('.', ','));
            emailReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(final DataSnapshot snapshot) {
                    final List<IProductList> productLists = new ArrayList<>();
                    final GenericTypeIndicator<Map<String, ProductList>> productListTypeIndicator = new GenericTypeIndicator<Map<String, ProductList>>() {
                    };

                    final Map<String, ProductList> map = snapshot.getValue(productListTypeIndicator);
                    final Set<String> keySet = map.keySet();
                    for (final String key : keySet) {
                        final ProductList productList = map.get(key);
                        productList.setId(key);
                        productLists.add(productList);
                    }
                    productListOnDataChanged.onChange(productLists);
                }

                @Override
                public void onCancelled(final DatabaseError error) {
                    productListOnDataChanged.onError(error);
                }
            });
        }
    }

    public void send(@NonNull final IUser user, @NonNull final IProductList productList) {
        final DatabaseReference emailReference = FirebaseDatabase.getInstance().getReference("").child(user.getEmail().toLowerCase().replace('.', ','));
        emailReference.push().setValue(productList);
    }

    public void update(final IProductList productList) {
        if (productList.getId() != null && firebaseUser.getEmail() != null) {
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("").child(firebaseUser.getEmail().toLowerCase().replace('.', ',')).child(productList.getId());
            reference.setValue(productList);
        }
    }

    public void delete(final IProductList productList) {
        if (productList.getId() != null && firebaseUser.getEmail() != null) {
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("").child(firebaseUser.getEmail().toLowerCase().replace('.', ',')).child(productList.getId());
            reference.removeValue();
        }
    }
}
