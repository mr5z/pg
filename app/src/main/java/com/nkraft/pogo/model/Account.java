package com.nkraft.pogo.model;

/**
 * Created by mark on 21/07/2016.
 */
public class Account {

    private long googleId;
    private String email;
    private String displayName;
    private boolean isSignedIn;

    private Account() {

    }

    public long getGoogleId() {
        return googleId;
    }

    public void setGoogleId(long googleId) {
        this.googleId = googleId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isSignedIn() {
        return isSignedIn;
    }

    public void setSignedIn(boolean signedIn) {
        isSignedIn = signedIn;
    }

    public static class Builder {
        private Account account = new Account();

        public Builder googleId(long value) {
            account.setGoogleId(value);
            return this;
        }

        public Builder email(String value) {
            account.setEmail(value);
            return this;
        }

        public Builder displayName(String value) {
            account.setDisplayName(value);
            return this;
        }

        public Builder signedIn(boolean value) {
            account.setSignedIn(value);
            return this;
        }

        public Account build() {
            return account;
        }
    }
}
