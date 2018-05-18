package com.ratna.foosip;

import com.ratna.foosip.allUsersPOJO.allUsersBean;
import com.ratna.foosip.allUsersPOJO.allUsersRequestBean;
import com.ratna.foosip.profilePOJO.picRequestBean;
import com.ratna.foosip.profilePOJO.profileBean;
import com.ratna.foosip.profilePOJO.profileRequestBean;
import com.ratna.foosip.profilePOJO.profileUpdateBean;
import com.ratna.foosip.profilePOJO.statusRequestBean;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AllAPIs {





    @POST("usersapi/active_users")
    Call<allUsersBean> getAllUsers(
            @Body allUsersRequestBean body , @HeaderMap Map<String , String> map
            );


    @POST("usersapi/profile")
    Call<profileBean> getProfile(
            @Body profileRequestBean body , @HeaderMap Map<String , String> map
    );

    @POST("usersapi/editprofile")
    Call<profileBean> updatePic(
            @Body picRequestBean body , @HeaderMap Map<String , String> map
    );

    @POST("usersapi/editprofile")
    Call<profileBean> updateStatus(
            @Body statusRequestBean body , @HeaderMap Map<String , String> map
    );

    @POST("usersapi/editprofile")
    Call<profileBean> updateProfile(
            @Body profileUpdateBean body , @HeaderMap Map<String , String> map
    );

}
