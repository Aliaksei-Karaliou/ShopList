package com.github.aliakseikaraliou.shoplist.db.firebase;

import com.github.aliakseikaraliou.shoplist.models.classes.ProductList;
import com.github.aliakseikaraliou.shoplist.models.interfaces.IProductList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirebaseDbHelper {

    private static final String LIST = "list";
    private static final String SENDER = "sender";

    public void push(final IProductList productList) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null && firebaseUser.getEmail() != null) {
            final DatabaseReference emailReference = FirebaseDatabase.getInstance().getReference("").child(firebaseUser.getEmail().replace('.', ','));
            emailReference.push().setValue(productList);
        }
    }

    public void setOnChangeListener(final OnDataChanged<List<IProductList>> productListOnDataChanged) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null && firebaseUser.getEmail() != null) {
            final DatabaseReference emailReference = FirebaseDatabase.getInstance().getReference("").child(firebaseUser.getEmail().replace('.', ','));
            emailReference.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(final DataSnapshot snapshot) {
                    final List<IProductList> productLists = new ArrayList<>();
                    final GenericTypeIndicator<Map<String, ProductList>> productListTypeIndicator = new GenericTypeIndicator<Map<String, ProductList>>() {
                    };
                    productLists.addAll(snapshot.getValue(productListTypeIndicator).values());
                    productListOnDataChanged.onChange(productLists);
                }

                @Override
                public void onCancelled(final DatabaseError error) {
                    productListOnDataChanged.onError(error);
                }
            });
        }
    }
}
