package com.ilkayaktas.margatsni.views.activities.another;

import android.os.Bundle;
import android.view.KeyEvent;

import com.ilkayaktas.margatsni.R;
import com.ilkayaktas.margatsni.views.activities.base.BaseActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by ilkay on 02/08/2017.
 */

public class AnotherActivity extends BaseActivity implements AnotherMvpView {
	
	@Inject
	AnotherMvpPresenter<AnotherMvpView> mPresenter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_another);
		
		getActivityComponent().inject(this);
		
		setUnBinder(ButterKnife.bind(this));
		
		// Attach presenter
		mPresenter.onAttach(AnotherActivity.this);
		
	}
	
	@Override
	protected void onDestroy() {
		mPresenter.onDetach();
		super.onDestroy();
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
