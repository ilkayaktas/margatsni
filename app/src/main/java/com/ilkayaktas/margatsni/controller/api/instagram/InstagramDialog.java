package com.ilkayaktas.margatsni.controller.api.instagram;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.ilkayaktas.margatsni.controller.api.instagram.http.InstagramAuthenticationService;
import com.ilkayaktas.margatsni.controller.api.instagram.model.entity.users.basicinfo.UserInfo;
import com.ilkayaktas.margatsni.utils.AppConstants;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import io.reactivex.Single;


/**
 * Authentication and authorization dialog.
 * 
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 *
 */
@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
public class InstagramDialog extends Dialog {
	private WebView mWebView;
	private LinearLayout mContent;
	private String mAuthUrl;
	private InstagramAuthenticationService instagramAuthenticationService;
	private OnInstagramAuthentication onInstagramAuthentication;


	static final FrameLayout.LayoutParams FILL = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
			ViewGroup.LayoutParams.MATCH_PARENT);
	
	static final String TAG = "Instagram-Android";
	
	public InstagramDialog(Context context, String authUrl, InstagramAuthenticationService instagramAuthenticationService, OnInstagramAuthentication onInstagramAuthentication) {
		super(context);
		this.instagramAuthenticationService = instagramAuthenticationService;
		this.onInstagramAuthentication = onInstagramAuthentication;
		mAuthUrl = authUrl;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mContent = new LinearLayout(getContext());
		mContent.setOrientation(LinearLayout.VERTICAL);
	        
		setUpTitle();
		setUpWebView();
	        
		Display display = getWindow().getWindowManager().getDefaultDisplay();
		Point outSize = new Point();
		
		int width = 0;
		int height = 0;
		
		double[] dimensions = new double[2];
		        
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			display.getSize(outSize);
			
			width	= outSize.x;
			height	= outSize.y;
		} else {
			width	= display.getWidth();
			height	= display.getHeight();
		}
		
		if (width < height) {
			dimensions[0]	= 0.87 * width;
	        dimensions[1]	= 0.82 * height;
		} else {
			dimensions[0]	= 0.75 * width;
			dimensions[1]	= 0.75 * height;	        
		}
	        
		addContentView(mContent, new FrameLayout.LayoutParams((int) dimensions[0], (int) dimensions[1]));
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		mWebView.destroy();
	}
	
	private void setUpTitle() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	        
	}

	private void setUpWebView() {
		mWebView = new WebView(getContext());
	        
		mWebView.setVerticalScrollBarEnabled(false);
		mWebView.setHorizontalScrollBarEnabled(false);
		mWebView.setWebViewClient(new InstagramWebViewClient());
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadUrl(mAuthUrl);
		mWebView.setLayoutParams(FILL);
	        
		WebSettings webSettings = mWebView.getSettings();
		
		webSettings.setSavePassword(false);
		webSettings.setSaveFormData(false);
		
		mContent.addView(mWebView);
	}

	public void clearCache() {
		mWebView.clearCache(true);
		mWebView.clearHistory();
		mWebView.clearFormData();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();

	}
	
	private class InstagramWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.d(TAG, "Redirecting URL " + url);
	        	
			if (url.startsWith(AppConstants.INSTAGRAM_CALBACK_URL)) {

				Uri uri = Uri.parse(url);
				String code= null;
				try {
					code = URLDecoder.decode(uri.getQueryParameter("code"), "UTF-8");
					
					Single<UserInfo> user = instagramAuthenticationService.authenticate(AppConstants.INSTAGRAM_CLIENT_ID,
							AppConstants.INSTAGRAM_CLIENT_SECRET, "authorization_code",
							AppConstants.INSTAGRAM_CALBACK_URL, code);
					
					onInstagramAuthentication.onSucces(user);

				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

	        	InstagramDialog.this.dismiss();
	        		
	        	return true;
			}

			return false;
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {	
			super.onReceivedError(view, errorCode, description, failingUrl);

			InstagramDialog.this.dismiss();
			
			Log.d(TAG, "Page error: " + description);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			
			Log.d(TAG, "Loading URL: " + url);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			
			Log.d(TAG, "Finished: " + url);
		}
	}
	
	public interface OnInstagramAuthentication{
		void onSucces(Single<UserInfo> user);
	}
}