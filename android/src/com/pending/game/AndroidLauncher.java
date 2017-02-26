package com.pending.game;

import android.os.Bundle;
import android.view.View;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new GameMain(), config);
	}



	protected View getAdView(){

		AdView adView = new AdView(this);

		adView.setAdSize(AdSize.BANNER);
		adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);

		return adView;
	}
}
