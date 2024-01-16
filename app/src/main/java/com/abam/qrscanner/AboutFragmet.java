/*
Made by Aswanth Vc
******************
Copy righted content. Dont edit or republish.
©ABAM
*/
package com.abam.qrscanner;
import android.view.*;
import android.os.*;
import android.content.*;
import android.widget.*;
import android.text.method.*;
import android.animation.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class AboutFragmet extends Fragment
{
	private AppCompatActivity ACTIVITY;
	private Context CONTEXT;
	public AboutFragmet(AppCompatActivity a){
		ACTIVITY = a;
		CONTEXT = a.getApplicationContext();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.about_fragment,container,false);
		TextView txt1 = v.findViewById(R.id.about_fragmentTextView1);
		TextView txt2 = v.findViewById(R.id.about_fragmentTextView2);
		TextView txt3 = v.findViewById(R.id.about_fragmentTextView3);
		TextView txt4 = v.findViewById(R.id.about_fragmentTextView4);
		TextView txt5 = v.findViewById(R.id.about_fragmentTextView5);
		RatingBar rate = v.findViewById(R.id.about_fragmentRatingBar);
		txt1.setMovementMethod(LinkMovementMethod.getInstance());
		txt2.setMovementMethod(LinkMovementMethod.getInstance());
		txt3.setMovementMethod(LinkMovementMethod.getInstance());
		txt4.setMovementMethod(LinkMovementMethod.getInstance());
		txt5.setMovementMethod(LinkMovementMethod.getInstance());
		txt1.setText(R.string.appunderlined);
		txt2.setText(R.string.appinfo);
		txt3.setText(R.string.madebyaswanth);
		txt4.setText(R.string.abam);
		txt5.setText(R.string.github);
		ObjectAnimator progressAnimator;
		progressAnimator = ObjectAnimator.ofInt(rate, "progress", 0,10);
		progressAnimator.setDuration(700);
		progressAnimator.start();
		return v;
	}
}
