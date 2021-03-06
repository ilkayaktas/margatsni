package com.ilkayaktas.margatsni.controller.strategy;

import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by iaktas on 28.08.2017.
 */

public class OAuthAccessTokenStrategy implements Strategy  {
    private OAuth10aService service;
    private OAuth1RequestToken requestToken;
    private String verifier;

    public OAuthAccessTokenStrategy(OAuth10aService service, OAuth1RequestToken requestToken, String verifier) {
        this.service = service;
        this.requestToken = requestToken;
        this.verifier = verifier;
    }

    @Override
    public String execute() {
        try {
            // get access token
            OAuth1AccessToken accessToken = service.getAccessToken(requestToken, verifier);

            System.out.println("(if your curious it looks like this: " + accessToken + ", 'rawResponse'='" + accessToken.getRawResponse() + "')");

            return accessToken.getToken();

//            final OAuthRequest request = new OAuthRequest(Verb.GET, AppConstants.FIVEHUNDREDPX_API_BASE_URL);
//
//            service.signRequest(accessToken, request);
//
//            final Response response = service.execute(request);
//
//            return response.toString();

        } catch (InterruptedException | IOException | ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }
}
