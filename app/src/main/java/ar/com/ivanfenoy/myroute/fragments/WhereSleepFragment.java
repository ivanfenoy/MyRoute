package ar.com.ivanfenoy.myroute.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import ar.com.ivanfenoy.myroute.R;
import ar.com.ivanfenoy.myroute.StepDialogFragment;
import ar.com.ivanfenoy.myroute.activities.TripActivity;
import ar.com.ivanfenoy.myroute.interfaces.ObjectSelected;
import ar.com.ivanfenoy.myroute.model.SleepPlace;
import ar.com.ivanfenoy.myroute.model.Step;
import butterknife.Bind;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public class WhereSleepFragment extends Fragment {
    @Bind(R.id.txt_place_name) TextView mTxtPlaceName;
    @Bind(R.id.txt_place_address) TextView mTxtPlaceAddress;

    private static final String ARG_PARAM1 = "sleepPlace";

    private Step mStep;
    private int PLACE_PICKER_REQUEST = 1;

    public WhereSleepFragment() {
        // Required empty public constructor
    }

    public static WhereSleepFragment newInstance(Step pStep) {
        WhereSleepFragment fragment = new WhereSleepFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, pStep);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStep = getArguments().getParcelable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View wView = inflater.inflate(R.layout.fragment_where_sleep, container, false);
        ButterKnife.bind(this, wView);

        wView.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    //your code here


                }
                return false;
            }
        } );


        return wView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(mStep.sleepPlace == null){
            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (which == DialogInterface.BUTTON_POSITIVE){
                        LatLng wPoint = new LatLng(mStep.point.latitude, mStep.point.longitude);
                        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder().setLatLngBounds(LatLngBounds.builder().include(wPoint).build());
                        try {
                            startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                        } catch (GooglePlayServicesRepairableException e) {
                            e.printStackTrace();
                        } catch (GooglePlayServicesNotAvailableException e) {
                            e.printStackTrace();
                        }
                    }
                    else{

                        FragmentManager wFm = getActivity().getSupportFragmentManager();
                        SleepPlaceDialogFragment wDialog = new SleepPlaceDialogFragment(mNewSleepPlace);
                        wDialog.show(wFm, "");
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getResources().getString(R.string.btn_bed));
            builder.setCancelable(false);
            builder.setMessage(getResources().getString(R.string.dialog_select_where_sleep));
            builder.setPositiveButton(R.string.map, listener);
            builder.setNegativeButton(R.string.manually, listener);
            builder.create().show();
        }
        else{
            initView();
        }
    }

    private void initView() {
        mTxtPlaceAddress.setText(mStep.sleepPlace.address);
        mTxtPlaceName.setText(mStep.sleepPlace.name);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(getActivity(), data);
                mStep.sleepPlace = new SleepPlace();
                mStep.sleepPlace.name = place.getName().toString();
                mStep.sleepPlace.address = place.getAddress().toString();
                mStep.sleepPlace.latitude = place.getLatLng().latitude;
                mStep.sleepPlace.longitude = place.getLatLng().longitude;
                initView();
            }
        }
    }


    ObjectSelected mNewSleepPlace = new ObjectSelected() {
        @Override
        public void select(Object object) {
            mStep.sleepPlace = (SleepPlace)object;
            initView();
        }
    };

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                return true;
        }
        return false;
    }

}
