package ar.com.ivanfenoy.myroute.activities;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.squareup.picasso.Picasso;

import java.io.File;

import ar.com.ivanfenoy.myroute.R;
import ar.com.ivanfenoy.myroute.fragments.StepsViewFragment;
import ar.com.ivanfenoy.myroute.model.SleepPlace;
import ar.com.ivanfenoy.myroute.model.Step;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    @Bind(R.id.bar_toolbar) Toolbar mToolbar;

    private GoogleMap mMap;
    private Step mStep = null;
    private SleepPlace mSleepPlace = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);

        if (getIntent().hasExtra("step")) {
            mStep = getIntent().getExtras().getParcelable("step");
        }
        if (getIntent().hasExtra("place")) {
            mSleepPlace = getIntent().getExtras().getParcelable("place");
        }

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initToolbar();
        initView();
    }

    private void initToolbar(){
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        if(mStep != null) {
            mToolbar.setTitle(mStep.point.name);
        }
        else{
            mToolbar.setTitle(getString(R.string.map));
        }
        mToolbar.setNavigationIcon(new IconDrawable(this, FontAwesomeIcons.fa_arrow_left)
                .colorRes(R.color.white)
                .actionBarSize());
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initView(){
        SupportMapFragment mMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(mStep != null) {
            LatLng mPoint = new LatLng(mStep.point.latitude, mStep.point.longitude);
            mMap.addMarker(new MarkerOptions().position(mPoint).title(mStep.point.name));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mPoint, 12));
        }
        if(mSleepPlace != null) {
            LatLng mPoint = new LatLng(mSleepPlace.latitude, mSleepPlace.longitude);
            mMap.addMarker(new MarkerOptions().position(mPoint).title(mSleepPlace.name));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mPoint, 17));
        }
    }
}
