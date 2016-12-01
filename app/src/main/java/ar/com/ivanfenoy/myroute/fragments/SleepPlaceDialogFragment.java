package ar.com.ivanfenoy.myroute.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.util.ArrayList;
import java.util.Calendar;

import ar.com.ivanfenoy.myroute.R;
import ar.com.ivanfenoy.myroute.interfaces.ObjectSelected;
import ar.com.ivanfenoy.myroute.model.Day;
import ar.com.ivanfenoy.myroute.model.Point;
import ar.com.ivanfenoy.myroute.model.SleepPlace;
import ar.com.ivanfenoy.myroute.model.Step;
import ar.com.ivanfenoy.myroute.model.Trip;
import ar.com.ivanfenoy.myroute.utils.Utils;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class SleepPlaceDialogFragment extends DialogFragment {
    @Bind(R.id.til_place_name)TextInputLayout mTilPlaceName;
    @Bind(R.id.edt_place_name)EditText mEdtPlaceName;
    @Bind(R.id.til_place_address)TextInputLayout mTilPlaceAddress;
    @Bind(R.id.edt_place_address)EditText mEdtPlaceAdress;
    @Bind(R.id.til_place_info)TextInputLayout mTilPlaceInfo;
    @Bind(R.id.edt_place_info)EditText mEdtPlaceInfo;

    private View mRootView;
    public ObjectSelected mListener;


    public static SleepPlaceDialogFragment newInstance() {
        SleepPlaceDialogFragment fragment = new SleepPlaceDialogFragment();
        return fragment;
    }

    public SleepPlaceDialogFragment(){}

    public SleepPlaceDialogFragment(ObjectSelected pListener){
        this.mListener = pListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.DialogTheme);

    }

    public void onResume()
    {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout((int)(getResources().getDisplayMetrics().widthPixels*((float)0.75)), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_sleep_place_dialog, container, false);
        ButterKnife.bind(this, mRootView);

        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @OnClick(R.id.btn_add_place)
    public void addTrip(View view){
        String wName = mEdtPlaceName.getText().toString().trim();
        if(wName.isEmpty()){
            Utils.sendToast(getActivity(), R.string.empty_place_name, Utils.TOAST_INFO);
            return;
        }
        String wAddress = mEdtPlaceAdress.getText().toString().trim();
        if(wName.isEmpty()){
            Utils.sendToast(getActivity(), R.string.empty_place_address, Utils.TOAST_INFO);
            return;
        }
        String wInfo = mEdtPlaceInfo.getText().toString().trim();
        if(wName.isEmpty()){
            Utils.sendToast(getActivity(), R.string.empty_place_info, Utils.TOAST_INFO);
            return;
        }

        SleepPlace wSleepPlace = new SleepPlace();
        wSleepPlace.name = wName;
        wSleepPlace.address = wAddress;
        wSleepPlace.info = wInfo;
        mListener.select(wSleepPlace);
        this.dismiss();
    }

}
