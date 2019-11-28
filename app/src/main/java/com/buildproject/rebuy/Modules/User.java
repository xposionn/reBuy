package com.buildproject.rebuy.Modules;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.List;

public class User {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    List<ListOfItems> itemList;


    public String getUserId() {
        return userId;
    }

    public User(GoogleSignInAccount account) {
        userId = account.getId();
        firstName = account.getGivenName();
        lastName = account.getFamilyName();
        email = account.getEmail();
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
