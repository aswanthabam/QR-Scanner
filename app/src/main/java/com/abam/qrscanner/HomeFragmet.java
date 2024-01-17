package com.abam.qrscanner;

import android.view.*;
import android.os.*;
import android.support.v7.app.*;
import android.content.*;
import android.widget.*;
import android.view.animation.*;
public class HomeFragmet extends Fragment
{
	private MainActivity activity;
	private Context context;
	private LinearLayout lay1;
	private LinearLayout lay2;
	private LinearLayout lay3;
	private View v;
	private Animation anim1;
	private Animation anim2;
	private Animation anim3;
	public HomeFragmet(MainActivity a){
		activity = a;
		context = a.getApplicationContext();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		v = inflater.inflate(R.layout.home_fragment,container,false);
		anim1 = AnimationUtils.loadAnimation(activity,R.anim.left_to_right_1);
		anim2 = AnimationUtils.loadAnimation(activity,R.anim.right_to_left_1);
		anim3 = AnimationUtils.loadAnimation(activity,R.anim.bottom_to_top_1);
		lay1 = v.findViewById(R.id.home_fragmentButton1);
		lay2 = v.findViewById(R.id.home_fragmentButton2);
		lay3 = v.findViewById(R.id.home_fragmentButton3);
		anim1.setDuration(800);
		anim2.setDuration(800);
		anim3.setDuration(800);
		lay1.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){
				activity.itemSelection(R.id.navigation_item2);
			}
		});
		lay2.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){
				activity.itemSelection(R.id.navigation_item3);
			}
		});
		lay3.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){
				activity.itemSelection(R.id.navigation_item4);
			}
		});
		lay1.setAnimation(anim1);
		lay2.setAnimation(anim2);
		lay3.setAnimation(anim3);
		anim1.start();
		anim2.start();
		anim3.start();
		return v;
	}
	
}
