package com.buildproject.rebuy;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

class User {
    private String firstName;
    private String lastName;


    public User(GoogleSignInAccount account) {
        firstName = account.getGivenName();
        lastName = account.getFamilyName();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
