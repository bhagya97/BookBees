package com.example.finalsample1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.Serializable;
import java.util.Objects;

public class RegisterUserDetails implements Serializable {

    private String profileImage;
    private String profileName;
    private String profileAboutMe;
    private Bitmap decodedByte;
    private String userUid;
    private String userGenre;

    public Bitmap getDecodedByte() {
        return decodedByte;
    }

    public void setDecodedByte(Bitmap decodedByte) {
        this.decodedByte = decodedByte;
    }

    public RegisterUserDetails() {
    }

    public RegisterUserDetails(String profileImage, String profileName, String profileAboutMe, Bitmap decodedByte, String userUid, String userGenre) {
        this.profileImage = profileImage;
        this.profileName = profileName;
        this.profileAboutMe = profileAboutMe;
        this.decodedByte = decodedByte;
        this.userUid = userUid;
        this.userGenre = userGenre;
    }

    public String getUserGenre() {
        return userGenre;
    }

    public void setUserGenre(String userGenre) {
        this.userGenre = userGenre;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProfileImage(), getProfileName(), getProfileAboutMe(), getDecodedByte(), getUserUid());
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProfileAboutMe() {
        return profileAboutMe;
    }

    public void setProfileAboutMe(String profileAboutMe) {
        this.profileAboutMe = profileAboutMe;
    }
    public Bitmap convertToImage(String profileImage)
    {
        byte[] decodedString = Base64.decode(profileImage, Base64.DEFAULT);
        decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }


}
