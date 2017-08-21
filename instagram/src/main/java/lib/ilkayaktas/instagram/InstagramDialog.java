package lib.ilkayaktas.instagram;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Authentication and authorization dialog.
 * 
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 *
 */
@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
public class InstagramDialog extends Dialog {
	private ProgressDialog mSpinner;
	private WebView mWebView;
	private LinearLayout mContent;
	private TextView mTitle;
	
	private String mAuthUrl;
	private String mRedirectUri;
	
	private InstagramDialogListener mListener;
	
	static final FrameLayout.LayoutParams FILL = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
			ViewGroup.LayoutParams.MATCH_PARENT);
	
	static final String TAG = "Instagram-Android";
	
	public InstagramDialog(Context context, String authUrl, String redirectUri, InstagramDialogListener listener) {
		super(context);
		
		mAuthUrl = authUrl;
		mListener = listener;
		mRedirectUri = redirectUri;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mSpinner = new ProgressDialog(getContext());
	        
		mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mSpinner.setMessage("Loading...");

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
		mListener.onCancel();
		
	}
	private class InstagramWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.d(TAG, "Redirecting URL " + url);
	        	
			if (url.startsWith(mRedirectUri)) {

				Uri uri = Uri.parse(url);
				String code= null;
				try {
					code = URLDecoder.decode(uri.getQueryParameter("code"), "UTF-8");
					mListener.onSuccess(code);
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
	      
			mListener.onError(description);
	            
			InstagramDialog.this.dismiss();
			
			Log.d(TAG, "Page error: " + description);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			
			mSpinner.show();
			
			Log.d(TAG, "Loading URL: " + url);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			
			mSpinner.dismiss();
		}
	}
	
	public interface InstagramDialogListener {
		void onSuccess(String code);
		void onCancel();
		void onError(String error);
	}
}