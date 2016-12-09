package ar.com.ivanfenoy.myroute.activities;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bumptech.glide.util.Util;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.squareup.picasso.Picasso;

import java.io.File;

import ar.com.ivanfenoy.myroute.App;
import ar.com.ivanfenoy.myroute.R;
import ar.com.ivanfenoy.myroute.fragments.MapCategoryDialogFragment;
import ar.com.ivanfenoy.myroute.fragments.NewTripFragment;
import ar.com.ivanfenoy.myroute.fragments.StepsViewFragment;
import ar.com.ivanfenoy.myroute.interfaces.ObjectCallback;
import ar.com.ivanfenoy.myroute.model.PhotonPlaces;
import ar.com.ivanfenoy.myroute.model.SleepPlace;
import ar.com.ivanfenoy.myroute.model.Step;
import ar.com.ivanfenoy.myroute.utils.Utils;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    @Bind(R.id.bar_toolbar) Toolbar mToolbar;
    @Bind(R.id.fab_search) FloatingActionButton mFabSearch;

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
        mFabSearch.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_search)
                .colorRes(R.color.white)
                .actionBarSize());

        SupportMapFragment mMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        View wIconMarker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_marker, null);
        if(mStep != null) {
            LatLng mPoint = new LatLng(mStep.point.latitude, mStep.point.longitude);
            mMap.addMarker(new MarkerOptions()
                    .position(mPoint)
                    .title(mStep.point.name)
                    .icon(BitmapDescriptorFactory.fromBitmap(Utils.createDrawableFromView(this, wIconMarker)))
            );
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mPoint, 12));
        }
        if(mSleepPlace != null) {
            LatLng mPoint = new LatLng(mSleepPlace.latitude, mSleepPlace.longitude);
            mMap.addMarker(new MarkerOptions()
                    .position(mPoint)
                    .title(mSleepPlace.name)
                    .icon(BitmapDescriptorFactory.fromBitmap(Utils.createDrawableFromView(this, wIconMarker)))
            );
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mPoint, 17));
        }
    }

    @OnClick(R.id.fab_search)
    public void openSearchDialog(View view){
        FragmentManager wFm = getSupportFragmentManager();
        MapCategoryDialogFragment wDialog = new MapCategoryDialogFragment();
        wDialog.show(wFm, "");
    }

    public void getPoints(String wFilter){
        App.api().getPlaces(mStep.point.name, wFilter, new PlacesCallback());
    }

    private class PlacesCallback implements ObjectCallback {
        @Override
        public void done(Exception e, Object object) {
            if(e != null) {
                return;
            }
            drawPoints((PhotonPlaces) object);
        }
    }

    public void drawPoints(PhotonPlaces pPoints){
        if(pPoints.listFeatures.size() < 1){
            Utils.sendToast(this, R.string.not_find_point, Utils.TOAST_INFO);
            return;
        }
        mMap.clear();
        LatLngBounds.Builder wBounds = new LatLngBounds.Builder();
        View wIconMarker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_marker, null);
        for (PhotonPlaces.Feature wFeature : pPoints.listFeatures) {
            LatLng wPoint = new LatLng(wFeature.point.coordinates.get(1), wFeature.point.coordinates.get(0));
            mMap.addMarker(new MarkerOptions()
                    .position(wPoint)
                    .title(wFeature.place.name)
                    .icon(BitmapDescriptorFactory.fromBitmap(Utils.createDrawableFromView(this, wIconMarker)))
            );
            wBounds.include(wPoint);
        }

        LatLngBounds bounds = wBounds.build();
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
    }
}
