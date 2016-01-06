package com.harkanson.android.resume;

import android.animation.ValueAnimator;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.harkanson.android.resume.databinding.ActivityMainBinding;
import com.harkanson.android.resume.databinding.NavHeaderMainBinding;
import com.harkanson.android.resume.ui.AboutFragment;
import com.harkanson.android.resume.ui.EducationFragment;
import com.harkanson.android.resume.ui.ExperienceFragment;
import com.harkanson.android.resume.ui.ExpertiseFragment;
import com.harkanson.android.resume.ui.HobbiesFragment;
import com.harkanson.android.resume.ui.MissionFragment;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MissionFragment.OnContactsClickedListener {

    private ActivityMainBinding binding;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private FloatingActionButton fab;
    private FloatingActionButton fabCall;
    private FloatingActionButton fabText;
    private FloatingActionButton fabEmail;
    private Map<String,ValueAnimator> fabAnimators;
    private NavigationView navigationView;
    private View content;

    private int state;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Bind all the things.
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        drawer = binding.drawerLayout;
        toolbar = binding.appBarMain.toolbar;
        fab = binding.appBarMain.fab;
        fabCall = binding.appBarMain.fabCall;
        fabText = binding.appBarMain.fabText;
        fabEmail = binding.appBarMain.fabEmail;
        navigationView = binding.navView;
        content = binding.appBarMain.content;

        //Set up toolbar and drawer.
        initToolbarAndDrawer();

        //Set up the FABs.
        initFabs();

        //Bind the navigation item listener.
        navigationView.setNavigationItemSelectedListener(this);

        //Restore state.
        if (savedInstanceState == null) {
            state = R.id.nav_mission;
            onNavigationItemSelected(navigationView.getMenu().findItem(state));
        } else {
            state = savedInstanceState.getInt("state");
            title = savedInstanceState.getString("title");
            toolbar.setSubtitle(title);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("state", state);
        outState.putString("title", title);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            //Close the drawer if it is open.
            drawer.closeDrawer(GravityCompat.START);
        } else if (fabCall.getVisibility() == View.VISIBLE) {
            //Close the FAB menu if it is open.
            fabClick();
        } else if (title.equals(getString(R.string.action_about))) {
            //Exit About if it is open.
            onNavigationItemSelected(navigationView.getMenu().findItem(state));
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment fragment = null;

        //Select options menu item.
        switch (item.getItemId()){
            case R.id.action_about:
                title = getString(R.string.action_about);
                fragment = new AboutFragment();
                break;
        }

        toolbar.setSubtitle(title);

        getSupportFragmentManager().beginTransaction()
                .replace(content.getId(), fragment)
                .commit();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        boolean preserveState = false;

        //Select nav drawer item.
        switch (item.getItemId()) {
            case R.id.nav_mission:
                title = getString(R.string.nav_mission);
                fragment = new MissionFragment();
                break;

            case R.id.nav_experience:
                title = getString(R.string.nav_experience);
                fragment = new ExperienceFragment();
                break;

            case R.id.nav_education:
                title = getString(R.string.nav_education);
                fragment = new EducationFragment();
                break;

            case R.id.nav_expertise:
                title = getString(R.string.nav_expertise);
                fragment = new ExpertiseFragment();
                break;

            case R.id.nav_hobbies:
                title = getString(R.string.nav_hobbies);
                fragment = new HobbiesFragment();
                break;

            case R.id.nav_resume_pdf:
                preserveState = true;

                DownloadManager.Request request = new DownloadManager.Request(
                        Uri.parse("https://dl.dropboxusercontent.com/u/1712106/russ/resume.pdf"));

                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "resume.pdf")
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .allowScanningByMediaScanner();

                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                break;
        }

        if(!preserveState) {
            item.setChecked(true);
            state = item.getItemId();
            toolbar.setSubtitle(title);

            getSupportFragmentManager().beginTransaction()
                    .replace(content.getId(), fragment)
                    .commit();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initToolbarAndDrawer() {
        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );

        drawer.setDrawerListener(toggle);

        NavHeaderMainBinding navHeader = NavHeaderMainBinding.inflate(LayoutInflater.from(navigationView.getContext()));
        navigationView.addHeaderView(navHeader.getRoot());

        Picasso.with(this)
                .load(R.drawable.russell)
                .resize(128, 128)
                .transform(new CropCircleTransformation())
                .into(navHeader.ivAvatar);
    }

    private void initFabs() {
        //Set up the click listeners.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabClick();
            }
        });
        fabCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabCallClick();
            }
        });
        fabText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabTextClick();
            }
        });
        fabEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabEmailClick();
            }
        });

        //Prepare the animations.
        fabAnimators = new HashMap<>();
        final CoordinatorLayout.LayoutParams fabCallParams = (CoordinatorLayout.LayoutParams) fabCall.getLayoutParams();
        final CoordinatorLayout.LayoutParams fabTextParams = (CoordinatorLayout.LayoutParams) fabText.getLayoutParams();
        final CoordinatorLayout.LayoutParams fabEmailParams = (CoordinatorLayout.LayoutParams) fabEmail.getLayoutParams();

        int start = fabCallParams.bottomMargin;
        fabAnimators.put("showFabCall", generateAnimator(fabCall, fabCallParams, start, (start / 2) * 15));
        fabAnimators.put("hideFabCall", generateAnimator(fabCall, fabCallParams, (start / 2) * 15, start));

        start = fabTextParams.bottomMargin;
        fabAnimators.put("showFabText", generateAnimator(fabText, fabTextParams, start, (start / 2) * 11));
        fabAnimators.put("hideFabText", generateAnimator(fabText, fabTextParams, (start / 2) * 11, start));

        start = fabEmailParams.bottomMargin;
        fabAnimators.put("showFabEmail", generateAnimator(fabEmail, fabEmailParams, start, (start/2)*7));
        fabAnimators.put("hideFabEmail", generateAnimator(fabEmail, fabEmailParams, (start/2)*7, start));
    }

    private ValueAnimator generateAnimator(final View view,
                                           final CoordinatorLayout.LayoutParams params,
                                           int start, int finish) {

        ValueAnimator animator = ValueAnimator.ofInt(start, finish);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                params.bottomMargin = (Integer) valueAnimator.getAnimatedValue();
                view.requestLayout();
            }
        });
        animator.setDuration(300);

        return animator;
    }

    private void fabClick() {
        //FAB clicked, show/hide mini FABs.
        if (fabCall.getVisibility() == View.VISIBLE) {
            fabAnimators.get("hideFabCall").start();
            fabAnimators.get("hideFabText").start();
            fabAnimators.get("hideFabEmail").start();

            fabCall.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fabCall.setVisibility(View.GONE);
                }
            }, 300);
            fabText.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fabText.setVisibility(View.GONE);
                }
            }, 300);
            fabEmail.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fabEmail.setVisibility(View.GONE);
                }
            }, 300);

        } else {
            fabCall.setVisibility(View.VISIBLE);
            fabText.setVisibility(View.VISIBLE);
            fabEmail.setVisibility(View.VISIBLE);

            fabAnimators.get("showFabCall").start();
            fabAnimators.get("showFabText").start();
            fabAnimators.get("showFabEmail").start();
        }
    }

    private void fabCallClick() {
        //FAB call clicked.
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+11234567890"));
        startActivity(Intent.createChooser(intent, getString(R.string.call_with)));
    }

    private void fabTextClick() {
        //FAB text clicked.
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:+11234567890"));
        startActivity(Intent.createChooser(intent, getString(R.string.text_with)));
    }

    private void fabEmailClick() {
        //FAB email clicked.
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:rharkanson@gmail.com"));
        startActivity(Intent.createChooser(intent, getString(R.string.email_with)));
    }

    @Override
    public void onContactsClicked() {
        if (fabCall.getVisibility() != View.VISIBLE) {
            fabClick();
        }
    }
}
