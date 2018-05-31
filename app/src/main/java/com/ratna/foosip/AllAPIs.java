package com.ratna.foosip;

import com.ratna.foosip.allUsersPOJO.allUsersBean;
import com.ratna.foosip.allUsersPOJO.allUsersRequestBean;
import com.ratna.foosip.profilePOJO.picRequestBean;
import com.ratna.foosip.profilePOJO.profileBean;
import com.ratna.foosip.profilePOJO.profileRequestBean;
import com.ratna.foosip.profilePOJO.profileUpdateBean;
import com.ratna.foosip.profilePOJO.statusRequestBean;

import java.util.List;
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

    // Core APIs

    @Multipart
    @POST("api/get_groups.php")
    Call<List<groupListBean>> getGroupList(
            @Part("user_id") String user_id,
            @Part("rid") String rid
    );

    @Multipart
    @POST("api/create_group.php")
    Call<createGroupBean> createGroup(
            @Part("user_id") String user_id,
            @Part("rid") String rid,
            @Part("group_name") String groupName,
            @Part("group_users") String groupUsers
    );


    @Multipart
    @POST("api/send_message.php")
    Call<createGroupBean> sendMessage(
            @Part("group_id") String groupId,
            @Part("sender_id") String senderId,
            @Part("message") String message
    );

    @Multipart
    @POST("api/updateFirebase.php")
    Call<Integer> updateFirebase(
            @Part("user_id") String userId,
            @Part("fid") String fid
    );


}
