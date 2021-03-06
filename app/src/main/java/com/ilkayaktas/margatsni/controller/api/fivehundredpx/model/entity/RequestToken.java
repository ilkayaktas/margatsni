package com.ilkayaktas.margatsni.controller.api.fivehundredpx.model.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by iaktas on 24.08.2017.
 */

public class RequestToken {

    @SerializedName("oauth_token")
    public String oauth_token;

    @SerializedName("oauth_token_secret")
    public String oauth_token_secret;

    @Override
    public String toString() {
        return String.format("RequestToken [oauth_token=%s, oauth_token_secret=%s]", oauth_token, oauth_token_secret);
    }
}
