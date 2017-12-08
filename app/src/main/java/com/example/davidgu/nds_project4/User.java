package com.example.davidgu.nds_project4;

import android.net.Uri;

/**
 * Created by davidgu on 12/8/17.
 */

class User {
    public int availability;
    public String img_Url;
    public String Discription;

    public User(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(int availability, String img_Url, String Discription) {
        this.availability = availability;
        this.img_Url = img_Url;
        this.Discription = Discription;
    }

}
