package com.example.finalsample1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.util.Objects;

public class UserDetails {

    private String profileImage;
    private String profileName;
    private String profileLocation;
    private Bitmap decodedByte;
    private String userUid;

    public Bitmap getDecodedByte() {
        return decodedByte;
    }

    public void setDecodedByte(Bitmap decodedByte) {
        this.decodedByte = decodedByte;
    }

    public UserDetails() {
    }

    public UserDetails(String profileImage, String profileName, String profileLocation, Bitmap decodedByte, String userUid) {
        this.profileImage = profileImage;
        this.profileName = profileName;
        this.profileLocation = profileLocation;
        this.decodedByte = decodedByte;
        this.userUid = userUid;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProfileImage(), getProfileName(), getProfileLocation(), getDecodedByte(), getUserUid());
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

    public String getProfileLocation() {
        return profileLocation;
    }

    public void setProfileLocation(String profileLocation) {
        this.profileLocation = profileLocation;
    }
    public Bitmap convertToImage(String profileImage)
    {
        byte[] decodedString = Base64.decode(profileImage, Base64.DEFAULT);
        decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    @Override
    public boolean equals(Object object)
    {
        boolean sameSame = false;

        if (object != null && object instanceof UserDetails)
        {
            sameSame = this.userUid == ((UserDetails) object).userUid;
        }

        return sameSame;
    }
}
