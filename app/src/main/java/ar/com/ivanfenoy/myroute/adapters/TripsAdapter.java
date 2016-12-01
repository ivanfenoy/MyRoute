package ar.com.ivanfenoy.myroute.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import ar.com.ivanfenoy.myroute.R;
import ar.com.ivanfenoy.myroute.activities.MainActivity;
import ar.com.ivanfenoy.myroute.model.Trip;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ivanj on 4/11/2016.
 */

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.ViewHolder> {

    private ArrayList<Trip> mTrips = new ArrayList<>();
    private Context context;
    private View.OnClickListener mClick;

    public TripsAdapter(Context context, ArrayList<Trip> pTrips) {
        this.context = context;
        this.mTrips = pTrips;
    }

    public void addTrips(ArrayList<Trip> pTrips) {
        mTrips.clear();
        for(int i=0; i<pTrips.size();i++) {
            this.mTrips.add(pTrips.get(i));
        }
        notifyDataSetChanged();
    }

    public void clear(){
        this.mTrips = new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Trip wTrip = mTrips.get(position);

        holder.mItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)context).openTrip(wTrip);
            }
        });
        holder.mItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((MainActivity)context).deleteTrip(wTrip);
                return false;
            }
        });
        holder.mTripName.setText(wTrip.name);
        Picasso.with(context).load(Uri.fromFile(new File(wTrip.image)))
                .into(holder.mImageTrip);
    }

    @Override
    public int getItemCount() {
        return mTrips.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_trip) CardView mItem;
        @Bind(R.id.txt_trip_name) TextView mTripName;
        @Bind(R.id.img_trip) ImageView mImageTrip;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}