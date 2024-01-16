package com.abam.qrscanner;

import android.Manifest;
import android.view.MenuItem;
import android.os.Bundle;
import android.widget.*;
import android.view.*;
import android.content.pm.*;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity
{
    
	private BottomNavigationView bottomNav;
	private DrawerLayout mDrawer;
	private NavigationView mNav;
	private ActionBarDrawerToggle mToogle;
	private androidx.appcompat.widget.Toolbar mToolbar;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		initDrawer();
		initBottomNav();
		getPermission();
		itemSelection(R.id.navigation_item1);
    }
    
	
	
	
	
	
	public void initDrawer(){
		mDrawer = (DrawerLayout) findViewById(R.id.mainActivitydrawer_layout);
		mNav = (NavigationView) findViewById(R.id.mainActivity_drawer);
		mToolbar = (Toolbar) findViewById(R.id.mainActivitytoolbar);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		mNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
			@Override public boolean onNavigationItemSelected(MenuItem item){
				itemSelection(item.getItemId());
				mDrawer.closeDrawers();
				return true;
			}
			
		});
		mNav.inflateHeaderView(R.layout.drawer_head);
		mToogle = new ActionBarDrawerToggle(this,mDrawer,mToolbar,R.string.drawer_open,R.string.drawer_close);
		mDrawer.setDrawerListener(mToogle);
		mToogle.syncState();
	}
	
	public void itemSelection(int item){
		switch(item){
			case android.R.id.home:
				mDrawer.openDrawer(GravityCompat.START);
				break;
			case R.id.drawer_navigation_item1:
			case R.id.navigation_item1:
				bottomNav.getMenu().findItem(R.id.navigation_item1).setChecked(true);
				//bottomNav.getMenu().findItem(R.id.navigation_item2).setChecked(false);
				//bottomNav.getMenu().findItem(R.id.navigation_item3).setChecked(false);
				//bottomNav.getMenu().findItem(R.id.navigation_item4).setChecked(false);
				Fragment frag1;
				FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
				frag1 = new HomeFragmet(this);
				transaction1.replace(R.id.frame_container,frag1);
				transaction1.commit();
				break;
			case R.id.drawer_navigation_item2:
			case R.id.navigation_item2:
				Fragment frag2;
				FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
				//bottomNav.getMenu().findItem(R.id.navigation_item1).setChecked(false);
				bottomNav.getMenu().findItem(R.id.navigation_item2).setChecked(true);
				//bottomNav.getMenu().findItem(R.id.navigation_item3).setChecked(false);
				//bottomNav.getMenu().findItem(R.id.navigation_item4).setChecked(false);
				
				frag2 = new QRscanner(this);
				transaction2.replace(R.id.frame_container,frag2);
				transaction2.commit();
				break;
			case R.id.drawer_navigation_item3:
			case R.id.navigation_item3:
				Fragment frag3;
				FragmentTransaction transaction3 = getSupportFragmentManager().beginTransaction();
				//bottomNav.getMenu().findItem(R.id.navigation_item1).setChecked(false);
				//bottomNav.getMenu().findItem(R.id.navigation_item2).setChecked(false);
				bottomNav.getMenu().findItem(R.id.navigation_item3).setChecked(true);
				//bottomNav.getMenu().findItem(R.id.navigation_item4).setChecked(false);
				
				frag3 = new QRgenerator(this);
				transaction3.replace(R.id.frame_container,frag3);
				transaction3.commit();
				break;
			case R.id.drawer_navigation_item4:
			case R.id.navigation_item4:
				Fragment frag4;
				FragmentTransaction transaction4 = getSupportFragmentManager().beginTransaction();
				//bottomNav.getMenu().findItem(R.id.navigation_item1).setChecked(false);
				//bottomNav.getMenu().findItem(R.id.navigation_item2).setChecked(false);
				//bottomNav.getMenu().findItem(R.id.navigation_item3).setChecked(false);
				bottomNav.getMenu().findItem(R.id.navigation_item4).setChecked(true);
				
				frag4 = new AboutFragmet(this);
				transaction4.replace(R.id.frame_container,frag4);
				transaction4.commit();
				break;
		}
	}
	
	public void getPermission(){
		if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
			
		}else{
			requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},6099);
		}
	}
	
	public void initBottomNav(){
		bottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
			@Override public boolean onNavigationItemSelected(MenuItem item){
				itemSelection(item.getItemId());
				mDrawer.closeDrawers();
				return true;
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		itemSelection(item.getItemId());
		return true;
	}
	
}
