package ar.com.ivanfenoy.myroute.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ar.com.ivanfenoy.myroute.R;
import ar.com.ivanfenoy.myroute.StepDialogFragment;
import ar.com.ivanfenoy.myroute.activities.TripActivity;
import ar.com.ivanfenoy.myroute.adapters.StepsAdapter;
import ar.com.ivanfenoy.myroute.interfaces.ObjectSelected;
import ar.com.ivanfenoy.myroute.model.Step;
import butterknife.Bind;
import butterknife.ButterKnife;

public class StepsViewFragment extends Fragment {
    private static final String ARG_PARAM1 = "steps";
    private ArrayList<Step> mSteps;

    @Bind(R.id.rv_steps) RecyclerView mRvSteps;

    private StepsAdapter mAdapter;

    public StepsViewFragment() {
    }

    public static StepsViewFragment newInstance(ArrayList<Step> pSteps) {
        StepsViewFragment fragment = new StepsViewFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM1, pSteps);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSteps = getArguments().getParcelableArrayList(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View wView = inflater.inflate(R.layout.fragment_steps_view, container, false);
        ButterKnife.bind(this, wView);
        return wView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRvSteps.setHasFixedSize(true);
        mAdapter = new StepsAdapter(getActivity(), (mSteps == null)? new ArrayList<Step>(): mSteps, mOpenStep, mDeleteStep);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvSteps.setLayoutManager(layoutManager);
        mRvSteps.setAdapter(mAdapter);

        if(mSteps == null){
            newStep();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TripActivity)getActivity()).setTitle(((TripActivity)getActivity()).mTrip.name);
        ((TripActivity)getActivity()).hideFab(false);
    }

    ObjectSelected mSaveStep = new ObjectSelected() {
        @Override
        public void select(Object object) {
            ((TripActivity)getActivity()).addStep((Step) object);
            mAdapter.notifyDataSetChanged();
        }
    };

    ObjectSelected mOpenStep = new ObjectSelected() {
        @Override
        public void select(Object object) {
            StepFragment wStepFragment = StepFragment.newInstance(((TripActivity)getActivity()).mTrip, (Step)object);
            ((TripActivity)getActivity()).changeFragment(wStepFragment);
        }
    };

    ObjectSelected mDeleteStep = new ObjectSelected() {
        @Override
        public void select(Object object) {
        }
    };

    public void newStep(){
        FragmentManager wFm = getActivity().getSupportFragmentManager();
        StepDialogFragment wDialog = new StepDialogFragment(mSaveStep);
        wDialog.mStep = new Step();
        wDialog.mTrip = ((TripActivity)getActivity()).mTrip;
        wDialog.show(wFm, "");
    }

}
