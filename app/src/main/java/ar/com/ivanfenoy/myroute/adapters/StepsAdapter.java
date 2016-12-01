package ar.com.ivanfenoy.myroute.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconTextView;

import java.util.ArrayList;

import ar.com.ivanfenoy.myroute.R;
import ar.com.ivanfenoy.myroute.interfaces.ObjectSelected;
import ar.com.ivanfenoy.myroute.model.Step;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ivanj on 4/11/2016.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {

    private ArrayList<Step> mSteps = new ArrayList<>();
    private Context mContext;
    private ObjectSelected mOpenObject;
    private ObjectSelected mDeleteObject;

    public StepsAdapter(Context context, ArrayList<Step> pSteps, ObjectSelected openObject, ObjectSelected deleteObject) {
        this.mContext = context;
        this.mSteps = pSteps;
        this.mOpenObject = openObject;
        this.mDeleteObject = deleteObject;
    }

    public void addSteps(ArrayList<Step> pStep) {
        mSteps.clear();
        for(int i=0; i<pStep.size();i++) {
            this.mSteps.add(pStep.get(i));
        }
        notifyDataSetChanged();
    }

    public void clear(){
        this.mSteps = new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_step, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Step wStep = mSteps.get(position);

        holder.mItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOpenObject.select(wStep);
            }
        });
        holder.mItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mDeleteObject.select(wStep);
                return false;
            }
        });
        if(position == 0){
            holder.mIcon.setText("{fa-flag-o}");
        }
        else if(position == (mSteps.size()-1)){
            holder.mIcon.setText("{fa-flag-checkered}");
        }
        else{
            holder.mIcon.setText("{mdi-map-marker-radius}");
        }

        holder.mStepName.setText(wStep.name);
        holder.mStepLocation.setText(wStep.point.name);
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_step) CardView mItem;
        @Bind(R.id.txt_step_name) TextView mStepName;
        @Bind(R.id.txt_step_location) TextView mStepLocation;
        @Bind(R.id.img_marker) IconTextView mIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}