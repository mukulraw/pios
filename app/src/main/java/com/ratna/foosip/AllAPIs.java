package com.ratna.foosip;

import com.ratna.foosip.allUsersPOJO.allUsersBean;
import com.ratna.foosip.allUsersPOJO.allUsersRequestBean;

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



}
