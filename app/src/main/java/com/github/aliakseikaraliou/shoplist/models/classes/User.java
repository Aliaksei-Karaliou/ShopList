package com.github.aliakseikaraliou.shoplist.models.classes;

import com.github.aliakseikaraliou.shoplist.models.interfaces.IUser;

public class User implements IUser {

    private final String email;

    public User(final String email) {
        this.email = email;
    }

    @Override
    public String getEmail() {
        return email;
    }


}
