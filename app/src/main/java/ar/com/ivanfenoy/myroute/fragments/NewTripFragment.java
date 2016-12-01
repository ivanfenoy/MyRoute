package ar.com.ivanfenoy.myroute.fragments;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import ar.com.ivanfenoy.myroute.App;
import ar.com.ivanfenoy.myroute.R;
import ar.com.ivanfenoy.myroute.activities.MainActivity;
import ar.com.ivanfenoy.myroute.model.Day;
import ar.com.ivanfenoy.myroute.model.Step;
import ar.com.ivanfenoy.myroute.model.Trip;
import ar.com.ivanfenoy.myroute.utils.Utils;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gun0912.tedbottompicker.TedBottomPicker;

public class NewTripFragment extends DialogFragment {
    @Bind(R.id.til_trip_name)TextInputLayout mTilTripName;
    @Bind(R.id.edt_trip_name)EditText mEdtTripName;
    @Bind(R.id.til_start_date)TextInputLayout mTilStartDate;
    @Bind(R.id.edt_start_date)EditText mEdtStartDate;
    @Bind(R.id.til_trip_img)TextInputLayout mTilTripImg;
    @Bind(R.id.edt_trip_img)EditText mEdtTripImg;

    private View mRootView;
    private Date mStartDate;
    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private Uri mImageUri;

    public static NewTripFragment newInstance(String param1, String param2) {
        NewTripFragment fragment = new NewTripFragment();
        return fragment;
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
        mRootView = inflater.inflate(R.layout.fragment_new_trip, container, false);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick({R.id.edt_start_date, R.id.til_start_date})
    public void openDatePicker(View view){
        final Calendar wCal = Calendar.getInstance();
        DatePickerDialog wDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                Calendar wStartCal = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                mEdtStartDate.setText(mSimpleDateFormat.format(wStartCal.getTimeInMillis()));
                mStartDate = wStartCal.getTime();
            }
        }, wCal.get(Calendar.YEAR), wCal.get(Calendar.MONTH), wCal.get(Calendar.DAY_OF_MONTH));
        wDatePicker.setTitle(getResources().getString(R.string.msg_enter_start_date));
        wDatePicker.show();
    }

    @OnClick(R.id.btn_add_trip)
    public void addTrip(View view){
        if(mEdtTripName.getText().toString().isEmpty()){
            Utils.sendToast(getActivity(), R.string.empty_trip_name, Utils.TOAST_INFO);
            return;
        }
        if(mEdtStartDate.getText().toString().isEmpty()){
            Utils.sendToast(getActivity(), R.string.empty_start_date, Utils.TOAST_INFO);
            return;
        }
        if(mImageUri == null){
            Utils.sendToast(getActivity(), R.string.empty_image, Utils.TOAST_INFO);
            return;
        }

        String wImgPath = copyImage();

//        Day wDay = new Day(1, mStartDate.getTime());
//        Step wStep = new Step(getActivity().getString(R.string.first_step_name), null);
        Trip wTrip = new Trip(mEdtTripName.getText().toString().trim(), mStartDate.getTime(), wImgPath);

        ((MainActivity)getActivity()).addTrip(wTrip);
        this.dismiss();
    }

    @OnClick({R.id.edt_trip_img, R.id.til_trip_img})
    public void openImagePicker(View view){
        TedBottomPicker wTedBottomPicker = new TedBottomPicker.Builder(getActivity())
                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        if (uri != null){
                            mImageUri = uri;
                            mEdtTripImg.setText(mImageUri.getLastPathSegment());
                        }
                    }
                })
                .create();

        wTedBottomPicker.show(getActivity().getSupportFragmentManager());
    }

    private String copyImage(){
        try {
            Bitmap wBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mImageUri);
            return Utils.copyImage(Bitmap.createScaledBitmap(wBitmap,(int)(wBitmap.getWidth()*0.4), (int)(wBitmap.getHeight()*0.4), false), mImageUri.getLastPathSegment());
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
