package com.abam.qrscanner;

import android.os.*;
import android.content.*;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		new Handler().postDelayed(new Runnable(){
			@Override public void run(){
				startActivity(new Intent(SplashActivity.this,MainActivity.class));
				finish();
			}
		},500);
	}
	
}
