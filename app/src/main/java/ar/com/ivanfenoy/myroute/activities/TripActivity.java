package ar.com.ivanfenoy.myroute.activities;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.util.ArrayList;
import ar.com.ivanfenoy.myroute.App;
import ar.com.ivanfenoy.myroute.R;
import ar.com.ivanfenoy.myroute.fragments.StepsViewFragment;
import ar.com.ivanfenoy.myroute.model.Step;
import ar.com.ivanfenoy.myroute.model.Trip;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TripActivity extends AppCompatActivity {
    @Bind(R.id.img_trip) ImageView mImgTrip;
    @Bind(R.id.trip_bar) Toolbar mTripBar;
    @Bind(R.id.frame) FrameLayout mFrame;
    @Bind(R.id.collapsing_toolbar_layout) CollapsingToolbarLayout mCollapsintToolbar;
    @Bind(R.id.fab_add_step) FloatingActionButton mFabStep;

    public Trip mTrip;
    private StepsViewFragment mStepFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        ButterKnife.bind(this);

        mTrip = getIntent().getExtras().getParcelable("trip");
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initToolbar();
        initView();
    }

    private void initToolbar(){
        setSupportActionBar(mTripBar);
        mTripBar.setTitleTextColor(getResources().getColor(R.color.white));
        mTripBar.setTitle(mTrip.name);
        mTripBar.setNavigationIcon(new IconDrawable(this, FontAwesomeIcons.fa_arrow_left)
                .colorRes(R.color.white)
                .actionBarSize());
        mTripBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Picasso.with(TripActivity.this).load(Uri.fromFile(new File(mTrip.image)))
                .into(mImgTrip);
    }

    private void initView(){
        mStepFragment = StepsViewFragment.newInstance(mTrip.listSteps);
        getSupportFragmentManager().beginTransaction().add(R.id.frame,mStepFragment).commit();

        mFabStep.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_plus)
                .colorRes(R.color.white)
                .actionBarSize());
    }

    public void addStep(Step pStep){
        if(mTrip.listSteps == null){
            mTrip.listSteps = new ArrayList<Step>();
        }
        mTrip.listSteps.add(pStep);
        App.DB().updateTrip(mTrip);
    }

    @OnClick(R.id.fab_add_step)
    public void newStep(View view){
        mStepFragment.newStep();
    }

    public void changeFragment(Fragment pFragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.frame,pFragment).addToBackStack("step").commit();
    }

    public void setTitle(String pTitle){
        mCollapsintToolbar.setTitle(pTitle);
    }

    public void hideFab(boolean pHide){
        if(pHide){
            mFabStep.setVisibility(View.GONE);
        }
        else{
            mFabStep.setVisibility(View.VISIBLE);
        }
    }
}
