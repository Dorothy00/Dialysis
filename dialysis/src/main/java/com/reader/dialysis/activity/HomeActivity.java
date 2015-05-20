package com.reader.dialysis.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.avos.avoscloud.AVUser;
import com.reader.dialysis.fragment.ReadProgressFragment;
import com.reader.dialysis.fragment.UserBookFragment;
import com.reader.dialysis.fragment.WordsFragment;
import com.reader.dialysis.view.DrawerView;

import test.dorothy.graduation.activity.R;


public class HomeActivity extends AppCompatActivity implements DrawerView
        .OnDrawerItemClickListener {

    private Toolbar toolbar;
    private DrawerView mDrawView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawView = (DrawerView) findViewById(R.id.drawer_view);
        mDrawView.setOnDrawerItemClickListener(this);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string
                .navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        AVUser avUser = AVUser.getCurrentUser();
        if (avUser != null) {
            mDrawView.setUserName(avUser.getUsername());
        }

        renderPanel(DrawerView.BOOK_TAG);
        //  AVService.uploadContents(this);

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        mDrawerToggle.syncState();
    }

    @Override
    public void renderPanel(String tag) {
        mDrawerLayout.closeDrawer(mDrawView);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            if (tag.equals(DrawerView.BOOK_TAG)) {
                fragment = new UserBookFragment();
            } else if (tag.equals(DrawerView.WORD_TAG)) {
                fragment = new WordsFragment();
            } else if (tag.equals(DrawerView.PROGRESS_TAG)) {
                fragment = new ReadProgressFragment();
            }
        }
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
}

