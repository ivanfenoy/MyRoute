package ar.com.ivanfenoy.myroute;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ar.com.ivanfenoy.myroute.interfaces.ObjectSelected;
import ar.com.ivanfenoy.myroute.model.Day;
import ar.com.ivanfenoy.myroute.model.Point;
import ar.com.ivanfenoy.myroute.model.Step;
import ar.com.ivanfenoy.myroute.model.Trip;
import ar.com.ivanfenoy.myroute.utils.Utils;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class StepDialogFragment extends DialogFragment {
    @Bind(R.id.til_step_name)TextInputLayout mTilStepName;
    @Bind(R.id.edt_step_name)EditText mEdtStepName;
    @Bind(R.id.til_place)TextInputLayout mTilPlace;
    @Bind(R.id.edt_place)EditText mEdtPlace;
    @Bind(R.id.til_days_number)TextInputLayout mTilDaysNumber;
    @Bind(R.id.edt_days_number)EditText mEdtDaysNumber;

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    private View mRootView;
    public Step mStep;
    public Trip mTrip;
    public ObjectSelected mListener;


    public static StepDialogFragment newInstance() {
        StepDialogFragment fragment = new StepDialogFragment();
        return fragment;
    }

    public StepDialogFragment(){}

    public StepDialogFragment(ObjectSelected pListener){
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
        mRootView = inflater.inflate(R.layout.fragment_step_dialog, container, false);
        ButterKnife.bind(this, mRootView);

        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(mStep != null) {
            mEdtStepName.setText(mStep.name);
        }
    }

    @OnClick({R.id.edt_place, R.id.til_place})
    public void openAutocompletePlace(View view){
        AutocompleteFilter wFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build();
        try {
            Intent wIntent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .setFilter(wFilter)
                            .build(getActivity());
            startActivityForResult(wIntent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place wPlace = PlaceAutocomplete.getPlace(getActivity(), data);
                mStep.point = new Point(wPlace.getName().toString(), wPlace.getLatLng().latitude, wPlace.getLatLng().longitude);
                mEdtPlace.setText(mStep.point.name);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
            } else if (resultCode == RESULT_CANCELED) {
            }
        }
    }

    @OnClick(R.id.btn_add_step)
    public void addTrip(View view){
        String wName = mEdtStepName.getText().toString().trim();
        if(wName.isEmpty()){
            Utils.sendToast(getActivity(), R.string.empty_step_name, Utils.TOAST_INFO);
            return;
        }
        int wDays = Integer.parseInt(mEdtDaysNumber.getText().toString().trim());
        if(wDays < 1){
            Utils.sendToast(getActivity(), R.string.empty_step_days, Utils.TOAST_INFO);
            return;
        }
        if(mStep.point == null){
            Utils.sendToast(getActivity(), R.string.empty_step_point, Utils.TOAST_INFO);
            return;
        }
        mStep.name = wName;
        Calendar wCal = Calendar.getInstance();
        wCal.setTimeInMillis(mTrip.getLastDate());
        int wLastDay = mTrip.getDaysCount();
        if(mStep.listDays == null){
            mStep.listDays = new ArrayList<Day>();
        }
        for (int i=0; i < wDays; i++){
            wCal.add(Calendar.HOUR_OF_DAY, 24);
            mStep.listDays.add(new Day((wLastDay+i), wCal.getTimeInMillis()));
        }
        mListener.select(mStep);
        this.dismiss();
    }

}
