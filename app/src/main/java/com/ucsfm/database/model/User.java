package com.ucsfm.database.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.widget.ImageButton;

public class User {
    private int id;
    private String name;
    private String email;
    private String urlProfilePicture;
    private Boolean lastLoggedUser;

    public User() {
    }

    public User(String name, String email, String urlProfilePicture) {
        this.name = name;
        this.email = email;
        this.urlProfilePicture = urlProfilePicture;
    }

    public User(int id, String name, String email, String urlProfilePicture) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.urlProfilePicture = urlProfilePicture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getLastLoggedUser() {
        return lastLoggedUser;
    }

    public void setLastLoggedUser(Boolean lastLoggedUser) {
        this.lastLoggedUser = lastLoggedUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrlProfilePicture() {
        return urlProfilePicture;
    }

    public void setUrlProfilePicture(String urlProfilePicture) {
        this.urlProfilePicture = urlProfilePicture;
    }

    public Bitmap getProfilePicture() {
        if(urlProfilePicture == null) return null;
        Bitmap bitmap =
                BitmapFactory.decodeFile(urlProfilePicture);
        return bitmap;
    }
}
