package com.huanshi.game;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
//		initialize(new GameMain(), config);
		View gameView = initializeForView(new GameMain(), config);

		NativeExpressAdView nativeExpressAdView	= getNativeExpressAdView();
		nativeExpressAdView.setVisibility(View.VISIBLE);

		RelativeLayout layout = new RelativeLayout(this);

		layout.addView(gameView);

		RelativeLayout.LayoutParams adParams =
				new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		layout.addView(nativeExpressAdView, adParams);

		setContentView(layout);
	}

	protected View getAdView(){

		AdView adView = new AdView(this);

		adView.setAdSize(AdSize.BANNER);
		adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);

		return adView;
	}

	protected NativeExpressAdView getNativeExpressAdView(){

		ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);

		AdSize adSize = new AdSize(320, 150);

		NativeExpressAdView nativeExpressAdView	= new NativeExpressAdView(this);
		nativeExpressAdView.setLayoutParams(layoutParams);
		nativeExpressAdView.setAdUnitId("ca-app-123123123123/123123123");
		nativeExpressAdView.setAdSize(adSize);

		AdRequest request = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR) // 测试广告
				.build();
		nativeExpressAdView.loadAd(request);

		return nativeExpressAdView;
	}
}
