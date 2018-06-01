package com.ratna.foosip;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class postListbean {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("rid")
    @Expose
    private String rid;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("post")
    @Expose
    private String post;
    @SerializedName("total_likes")
    @Expose
    private String totalLikes;
    @SerializedName("total_comments")
    @Expose
    private String totalComments;
    @SerializedName("post_type")
    @Expose
    private String postType;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("sender_image")
    @Expose
    private String senderImage;
    @SerializedName("sender_name")
    @Expose
    private String senderName;
    @SerializedName("user_id")
    @Expose
    private String user_id;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(String totalLikes) {
        this.totalLikes = totalLikes;
    }

    public String getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(String totalComments) {
        this.totalComments = totalComments;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getSenderImage() {
        return senderImage;
    }

    public void setSenderImage(String senderImage) {
        this.senderImage = senderImage;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
