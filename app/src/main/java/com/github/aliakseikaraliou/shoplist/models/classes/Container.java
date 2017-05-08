package com.github.aliakseikaraliou.shoplist.models.classes;

import com.github.aliakseikaraliou.shoplist.models.interfaces.IContainer;
import com.github.aliakseikaraliou.shoplist.models.interfaces.IProductList;
import com.github.aliakseikaraliou.shoplist.models.interfaces.IUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Container implements IContainer {

    private final IProductList productList;
    private IUser sender;
    private final IUser receiver;

    public Container(final IProductList productList, final IUser receiver) {
        this.productList = productList;
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && user.getEmail() != null) {
            final String email = user.getEmail();
            this.sender = new User(email);
        }
        this.receiver = receiver;
    }

    @Override
    public IProductList getProductList() {
        return productList;
    }

    @Override
    public IUser getSender() {
        return sender;
    }

    @Override
    public IUser getReceiver() {
        return receiver;
    }
}
