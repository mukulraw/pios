package com.ratna.foosip.profilePOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class profileUpdateBean {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("interest")
    @Expose
    private String interest;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


}
