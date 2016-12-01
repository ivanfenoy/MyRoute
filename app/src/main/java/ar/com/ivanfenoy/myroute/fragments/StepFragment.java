package ar.com.ivanfenoy.myroute.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ar.com.ivanfenoy.myroute.R;
import ar.com.ivanfenoy.myroute.activities.MapActivity;
import ar.com.ivanfenoy.myroute.activities.TripActivity;
import ar.com.ivanfenoy.myroute.adapters.StepsAdapter;
import ar.com.ivanfenoy.myroute.model.Step;
import ar.com.ivanfenoy.myroute.model.Trip;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StepFragment extends Fragment {
    private static final String ARG_PARAM1 = "trip";
    private static final String ARG_PARAM2 = "step";

    private Trip mTrip;
    private Step mStep;

    public StepFragment() {
        // Required empty public constructor
    }

    public static StepFragment newInstance(Trip pTrip, Step pStep) {
        StepFragment fragment = new StepFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, pTrip);
        args.putParcelable(ARG_PARAM2, pStep);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTrip = getArguments().getParcelable(ARG_PARAM1);
            mStep = getArguments().getParcelable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View wView = inflater.inflate(R.layout.fragment_step, container, false);
        ButterKnife.bind(this, wView);
        return wView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TripActivity)getActivity()).setTitle(mStep.point.name);
        ((TripActivity)getActivity()).hideFab(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick(R.id.btn_road)
    public void openMap(View pview){
        Intent wI = new Intent(getActivity(), MapActivity.class);
        wI.putExtra("step", mStep);
        getActivity().startActivity(wI);
    }

    @OnClick(R.id.btn_bed)
    public void openBed(View pview){
        WhereSleepFragment wFragment = WhereSleepFragment.newInstance(mStep);
        ((TripActivity)getActivity()).changeFragment(wFragment);
    }

}
