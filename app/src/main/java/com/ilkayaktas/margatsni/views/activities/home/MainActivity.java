package com.ilkayaktas.margatsni.views.activities.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ilkayaktas.margatsni.R;
import com.ilkayaktas.margatsni.utils.AppConstants;
import com.ilkayaktas.margatsni.views.activities.base.BaseActivity;
import com.ilkayaktas.margatsni.views.widgets.dialogs.rateme.Config;
import com.ilkayaktas.margatsni.views.widgets.dialogs.rateme.RateMe;

import javax.inject.Inject;

import butterknife.ButterKnife;
import lib.ilkayaktas.instagram.Instagram;
import lib.ilkayaktas.instagram.InstagramRequest;
import lib.ilkayaktas.instagram.InstagramSession;
import lib.ilkayaktas.instagram.http.Verbs;
import lib.ilkayaktas.instagram.model.api.Endpoints;
import lib.ilkayaktas.instagram.model.api.Scope;
import lib.ilkayaktas.instagram.model.entity.users.basicinfo.UserInfo;

public class MainActivity extends BaseActivity implements MainMvpView {

	private InstagramSession mInstagramSession;
	private Instagram mInstagram;

	private ProgressBar mLoadingPb;
	private GridView mGridView;

	private static final String CLIENT_ID = AppConstants.INSTAGRAM_CLIENT_ID;
	private static final String CLIENT_SECRET = AppConstants.INSTAGRAM_CLIENT_SECRET;
	private static final String REDIRECT_URI = AppConstants.INSTAGRAM_CALBACK_URL;

	@Inject
	MainMvpPresenter<MainMvpView> mPresenter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getActivityComponent().inject(this);
		
		setUnBinder(ButterKnife.bind(this));
		
		RateMe.init(new Config(5, 10)); // 5 gün ya da 10 defa uygulama başlattıktan sonra
		
		// Attach presenter
		mPresenter.onAttach(MainActivity.this);

		instagram();
	}

	private void instagram() {

		mInstagram  		= new Instagram(this, CLIENT_ID, CLIENT_SECRET, REDIRECT_URI, Scope.ALL);
		mInstagramSession	= mInstagram.getSession();

		mInstagram.authorize(mAuthListener);

	}


	private Instagram.InstagramAuthListener mAuthListener = new Instagram.InstagramAuthListener() {
		@Override
		public void onSuccess(UserInfo user) {
			finish();
			Toast.makeText(MainActivity.this, user.accessToken, Toast.LENGTH_SHORT).show();

			InstagramRequest request = new InstagramRequest(user.accessToken);

				request.createRequest(Verbs.GET, Endpoints.USERS_SELF, null, new InstagramRequest.InstagramRequestListener(){

					@Override
					public void onSuccess(String response) {
						System.out.println(response);
					}

					@Override
					public void onError(String error) {

					}
				});

			mInstagram.resetSession();
		}

		@Override
		public void onError(String error) {
			Toast.makeText(MainActivity.this, "Error! "+error, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onCancel() {
			Toast.makeText(MainActivity.this, "Cancelled!", Toast.LENGTH_SHORT).show();
		}
	};

	@Override
	protected void onStart() {
		super.onStart();
		RateMe.onStart(this);
		RateMe.showRateDialogIfNeeded(this);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		mPresenter.onDetach();
		super.onDestroy();
	}
	
	public static Intent getStartIntent(Context context) {
		Intent intent = new Intent(context, MainActivity.class);
		return intent;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			
			finish();
			
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
}
