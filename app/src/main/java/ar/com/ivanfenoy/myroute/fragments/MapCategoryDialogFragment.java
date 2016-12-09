package ar.com.ivanfenoy.myroute.fragments;

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

import ar.com.ivanfenoy.myroute.R;
import ar.com.ivanfenoy.myroute.activities.MapActivity;
import ar.com.ivanfenoy.myroute.interfaces.ObjectSelected;
import ar.com.ivanfenoy.myroute.model.SleepPlace;
import ar.com.ivanfenoy.myroute.utils.Utils;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapCategoryDialogFragment extends DialogFragment {
    private View mRootView;

    public MapCategoryDialogFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.DialogTheme);

    }

    public void onResume()
    {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout((int)(getResources().getDisplayMetrics().widthPixels*((float)0.9)), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.dialog_map_category, container, false);
        ButterKnife.bind(this, mRootView);

        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @OnClick({R.id.btn_info, R.id.btn_attraction, R.id.btn_hotel, R.id.btn_park, R.id.btn_view_point, R.id.btn_camping})
    public void addTrip(View view){
        String wFilter = "";
        switch (view.getId()){
            case R.id.btn_info:
                wFilter = "tourism:information";
                break;
            case R.id.btn_attraction:
                wFilter = "tourism:attraction";
                break;
            case R.id.btn_hotel:
                wFilter = "tourism:hotel";
                break;
            case R.id.btn_park:
                wFilter = "tourism:theme_park";
                break;
            case R.id.btn_view_point:
                wFilter = "tourism:viewpoint";
                break;
            case R.id.btn_camping:
                wFilter = "tourism:camp_site";
                break;
        }
        ((MapActivity)getActivity()).getPoints(wFilter);
        this.dismiss();
    }

}
