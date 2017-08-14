package com.ilkayaktas.margatsni.views.activities.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.ilkayaktas.margatsni.App;
import com.ilkayaktas.margatsni.R;
import com.ilkayaktas.margatsni.di.annotations.ActivityContext;
import com.ilkayaktas.margatsni.di.components.ActivityComponent;
import com.ilkayaktas.margatsni.di.components.DaggerActivityComponent;
import com.ilkayaktas.margatsni.di.modules.ActivityModule;

import javax.inject.Inject;

import butterknife.Unbinder;

/**
 * Created by ilkay on 09/03/2017.
 */

public class BaseActivity extends AppCompatActivity implements MvpView, BaseFragment.Callback{

	private ProgressDialog progressDialog;

	private ActivityComponent mActivityComponent;
	
	private Unbinder mUnBinder;

	@Inject
	public Typeface textFont;

	@Inject
	@ActivityContext
	public Typeface typeface;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivityComponent = DaggerActivityComponent.builder()
				.activityModule(new ActivityModule(this))
				.applicationComponent(((App) getApplication()).getAppComponent())
				.build();

		mActivityComponent.inject(this);

	}

	public ActivityComponent getActivityComponent() {
		return mActivityComponent;
	}

	@Override
	public void showLoading() {
		hideLoading();
//		progressDialog = CommonUtils.showLoadingDialog(this);
	}

	@Override
	public void hideLoading() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.cancel();
		}
	}

	@Override
	public void onError(String message) {
		if (message != null) {
			showSnackBar(message);
		} else {
			showSnackBar(getString(R.string.all_errormessage));
		}
	}

	private void showSnackBar(String message) {
		Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT);
		View sbView = snackbar.getView();
		TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
		textView.setTextColor(ContextCompat.getColor(this, R.color.mobss_color_white));
		snackbar.show();
	}

	@Override
	public void onError(@StringRes int resId) {
		onError(getString(resId));
	}

	@Override
	public void onFragmentAttached() {

	}

	@Override
	public void onFragmentDetached(String tag) {

	}
	
	public void setUnBinder(Unbinder unBinder) {
		mUnBinder = unBinder;
	}
	
	@Override
	protected void onDestroy() {
		
		if (mUnBinder != null) {
			mUnBinder.unbind();
		}
		super.onDestroy();
	}
	
	public void changeActionBarFont(){
		// action bar id'si alınıyor
		int titleId = getResources().getIdentifier("action_bar_title", "id", "android");

		// action bar yazı fontu ve rengi değiştiriliyor
		TextView myActionBar = (TextView) findViewById(titleId);
		if(myActionBar != null) {
			myActionBar.setTextColor(Color.WHITE);
			myActionBar.setTypeface(textFont);
			myActionBar.setTextSize(20);
		}
	}
	
	public void changeActionBarColor(int color){
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
	}
	
	public void startActivity(Class<?> newActivityClass){
		// intent oluştur
		Intent intent = new Intent(getBaseContext(), newActivityClass);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		
		// yeni Acitivity başlat
		startActivity(intent);
	}
}
