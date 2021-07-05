package com.ucsfm.database.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ucsfm.database.BDSQLiteHelper;
import com.ucsfm.database.model.User;

import java.util.ArrayList;

public class UserRepository extends BDSQLiteHelper {
    private static final String TABELA_USER = "user";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String URL_PROFILE_PICTURE = "urlProfilePicture";
    private static final String LAST_LOGGED_USER = "lastLoggedUser";

    private static final String[] COLUNAS = {ID, NAME, EMAIL, LAST_LOGGED_USER, URL_PROFILE_PICTURE};

    public UserRepository(Context context) {
        super(context);
    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, user.getName());
        values.put(EMAIL, user.getEmail());
        values.put(LAST_LOGGED_USER, user.getLastLoggedUser());
        values.put(URL_PROFILE_PICTURE, user.getUrlProfilePicture());
        db.insert(TABELA_USER, null, values);
        db.close();
    }

    public User getUserById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABELA_USER,
                COLUNAS,
                " id = ?",
                new String[] { String.valueOf(id) },
                null,
                null,
                null,
                null);
        if (cursor == null) {
            return null;
        } else {
            cursor.moveToFirst();
            User user = cursorToUser(cursor);
            return user;
        }
    }

    public User getLastLoggedUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABELA_USER,
                COLUNAS,
                " lastLoggedUser = 1",
                null,
                null,
                null,
                null);
        if (cursor == null) {
            return null;
        } else {
            cursor.moveToFirst();
            User user = cursorToUser(cursor);
            return user;
        }
    }

    private User cursorToUser(Cursor cursor) {
        User user = new User();
        user.setId(Integer.parseInt(cursor.getString(0)));
        user.setName(cursor.getString(1));
        user.setEmail(cursor.getString(2));
        user.setLastLoggedUser(cursor.getInt(3) > 0);
        user.setUrlProfilePicture(cursor.getString(4));
        return user;
    }

    public ArrayList<User> getAllUsers() {
        ArrayList<User> listaUsers = new ArrayList<User>();
        String query = "SELECT * FROM " + TABELA_USER + " ORDER BY " + NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                User user = cursorToUser(cursor);
                listaUsers.add(user);
            } while (cursor.moveToNext());
        }
        return listaUsers;
    }

    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, user.getName());
        values.put(EMAIL, user.getEmail());
        values.put(LAST_LOGGED_USER, user.getLastLoggedUser());
        values.put(URL_PROFILE_PICTURE, user.getUrlProfilePicture());
        int i = db.update(TABELA_USER,
                values,
                ID+" = ?",
                new String[] { String.valueOf(user.getId()) });
        db.close();
        return i;
    }

    public int deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(TABELA_USER,
                ID+" = ?",
                new String[] { String.valueOf(user.getId()) });
        db.close();
        return i;
    }
}
