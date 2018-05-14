package com.ratna.foosip.allUsersPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class allUsersRequestBean {

    @SerializedName("rid")
    @Expose
    private String rid;

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }


}
