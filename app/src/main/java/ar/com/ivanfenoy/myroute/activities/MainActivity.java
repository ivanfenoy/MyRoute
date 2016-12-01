package ar.com.ivanfenoy.myroute.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;

import ar.com.ivanfenoy.myroute.App;
import ar.com.ivanfenoy.myroute.R;
import ar.com.ivanfenoy.myroute.adapters.TripsAdapter;
import ar.com.ivanfenoy.myroute.fragments.NewTripFragment;
import ar.com.ivanfenoy.myroute.interfaces.ListCallback;
import ar.com.ivanfenoy.myroute.model.Trip;
import ar.com.ivanfenoy.myroute.utils.Utils;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.rv_trips) RecyclerView mRvTrips;
    @Bind(R.id.fab_add_trip) FloatingActionButton mFabTrips;
    @Bind(R.id.bar_toolbar) Toolbar mToolbar;
    @Bind(R.id.sw_trips) SwipeRefreshLayout mSwTrips;

    private TripsCallback onTripsCallback;
    TripsAdapter mAdapter;
    ArrayList<Trip> mListTrips = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        onTripsCallback = new TripsCallback();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initToolbar();
        initView();
    }

    private void initToolbar(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getString(R.string.app_name));
        getSupportActionBar().setHomeAsUpIndicator(new IconDrawable(this, FontAwesomeIcons.fa_bars)
                .colorRes(R.color.white)
                .actionBarSize());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initView(){
        mRvTrips.setHasFixedSize(true);
        mAdapter = new TripsAdapter(MainActivity.this, new ArrayList<Trip>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvTrips.setLayoutManager(layoutManager);
        mRvTrips.setAdapter(mAdapter);

        mFabTrips.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_plus)
                .colorRes(R.color.white)
                .actionBarSize());

        mSwTrips.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTrips();
            }
        });

        refreshTrips();
    }

    private void refreshTrips() {
        mSwTrips.setRefreshing(true);
        App.DB().getTrips(onTripsCallback);
    }

    public void openTrip(Trip pTrip){
        Intent wI = new Intent(MainActivity.this, TripActivity.class);
        wI.putExtra("trip", pTrip);
        startActivity(wI);
    }

    private class TripsCallback implements ListCallback {
        @Override
        public void done(Exception e, ArrayList<?> list) {
            mSwTrips.setRefreshing(false);
            if(e != null) {
                return;
            }
            tripSuccess((ArrayList<Trip>)list);
        }
    }

    private void tripSuccess(ArrayList<Trip> trips){
        if (trips != null) {
            mAdapter.addTrips(trips);
        }
    }

    @OnClick(R.id.fab_add_trip)
    public void addTrip(View view){
        FragmentManager wFm = getSupportFragmentManager();
        NewTripFragment wDialog = new NewTripFragment();
        wDialog.show(wFm, "");
    }

    public void addTrip(Trip pTrip){
        mListTrips.add(pTrip);
        mAdapter.addTrips(mListTrips);
        App.DB().saveNewTrip(pTrip);
    }

    public void deleteTrip(final Trip pTrip){
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (which == DialogInterface.BUTTON_POSITIVE){
                    mListTrips.remove(pTrip);
                    mAdapter.addTrips(mListTrips);
                    Utils.deteleImage(pTrip.image);
                    App.DB().deleteTrip(pTrip);
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.trips));
        builder.setCancelable(false);
        builder.setMessage(R.string.question_delete_trip);
        builder.setPositiveButton(R.string.yes, listener);
        builder.setNegativeButton(R.string.no, listener);
        builder.create().show();

    }
}
