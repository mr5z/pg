package com.nkraft.pogo.controller.mapper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.nkraft.pogo.controller.database.AccountDatabase;
import com.nkraft.pogo.model.Account;

import java.util.Locale;

/**
 * Created by mark on 21/07/2016.
 */
public class AccountMapper {

    private static volatile AccountMapper instance;
    private AccountDatabase database;

    private AccountMapper(Context context) {
        database = new AccountDatabase(context);
    }

    public static AccountMapper getInstance(Context context) {
        if (instance == null) {
            synchronized (AccountMapper.class) {
                if (instance == null) {
                    instance = new AccountMapper(context);
                }
            }
        }
        return instance;
    }

    public void getSignedInAccount(final AccountListener listener) {
        new AsyncTask<Void, Void, Account>() {

            @Override
            protected Account doInBackground(Void... voids) {
                return getSignedInAccount();
            }

            @Override
            protected void onPostExecute(Account account) {
                listener.onAccountRetrieve(account);
            }
        }.execute();
    }

    private Account getSignedInAccount() {
        SQLiteDatabase db = database.getReadableDatabase();
        String[] columns = {
                AccountDatabase.COLUMN_GOOGLE_ID,
                AccountDatabase.COLUMN_EMAIL,
                AccountDatabase.COLUMN_DISPLAY_NAME,
                AccountDatabase.COLUMN_SIGNED_IN
        };
        String selection = String.format(Locale.ROOT, "%s=%d", AccountDatabase.COLUMN_SIGNED_IN, 1);
        Cursor cursor = db.query(AccountDatabase.TABLE_NAME, columns, selection, null, null, null, null);
        if (cursor.moveToFirst()) {
            long googleId = cursor.getLong(cursor.getColumnIndexOrThrow(
                    AccountDatabase.COLUMN_GOOGLE_ID));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(
                    AccountDatabase.COLUMN_EMAIL));
            String displayName = cursor.getString(cursor.getColumnIndexOrThrow(
                    AccountDatabase.COLUMN_DISPLAY_NAME));
            boolean signedIn = cursor.getInt(cursor.getColumnIndexOrThrow(
                    AccountDatabase.COLUMN_SIGNED_IN)) != 0;
            return new Account.Builder()
                    .googleId(googleId)
                    .email(email)
                    .displayName(displayName)
                    .signedIn(signedIn)
                    .build();
        }

        return null;

    }

    public interface AccountListener {
        void onAccountRetrieve(Account account);
    }
}
