package com.example.finalsample1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.util.Objects;

public class BookDetails {

    private String profileImage;
    private Bitmap decodedByte;

    public Bitmap getDecodedByte() {
        return decodedByte;
    }

    public void setDecodedByte(Bitmap decodedByte) {
        this.decodedByte = decodedByte;
    }

    public BookDetails() {
    }

    public BookDetails(String profileImage, Bitmap decodedByte) {
        this.profileImage = profileImage;
        this.decodedByte = decodedByte;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProfileImage(), getDecodedByte());
    }

    public String getProfileImage() {
        return profileImage;
    }

    public Bitmap convertToImage(String profileImage)
    {
        byte[] decodedString = Base64.decode(profileImage, Base64.DEFAULT);
        decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
}
