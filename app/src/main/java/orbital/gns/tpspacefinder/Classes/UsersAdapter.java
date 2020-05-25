package orbital.gns.tpspacefinder.Classes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import orbital.gns.tpspacefinder.Activities.LocationDetailsActivity;
import orbital.gns.tpspacefinder.R;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> implements Filterable {

    private FirebasePackage firebase;
    private LocationPackage locationPackage;

    private Context context;
    private ArrayList<String> locations;
    private ArrayList<String> locationsFull;
    private Location currentLocationPicked;

    public UsersAdapter(Context ct, ArrayList<String> locations) {
        this.locations = locations;
        this.locationsFull = new ArrayList<>(locations);
        this.context = ct;
        locationPackage = new LocationPackage();
        firebase = new FirebasePackage();
        currentLocationPicked = new Location();
    }

    @NonNull
    @Override
    public UsersAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.location_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UsersAdapter.MyViewHolder holder, final int position) {
        final String locationName = locations.get(position);
        holder.locationTextView.setText(locationName);
        holder.locationImage.setImageResource(locationPackage.allLocationsDrawable.get(locationName));

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 final Intent intent = new Intent(context, LocationDetailsActivity.class);
                 final Bundle bundle = new Bundle();
                 firebase.database.collection("Locations").document(locationName).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                     @Override
                     public void onSuccess(DocumentSnapshot documentSnapshot) {
                         currentLocationPicked = documentSnapshot.toObject(Location.class);
                         Log.d("debug", currentLocationPicked.getName());
                         bundle.putSerializable("CurrentLocationDetails", currentLocationPicked);
                         intent.putExtras(bundle);
                         context.startActivity(intent);
                     }
                 });
             }
        });

    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<String> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(locationsFull);
            } else {
                String filterpattern = constraint.toString().toLowerCase().trim();
                for (String item : locationsFull) {
                    if (item.toLowerCase().contains(filterpattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            locations.clear();
            locations.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView locationTextView;
        ImageView locationImage;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            locationTextView = itemView.findViewById(R.id.locationName);
            locationImage = itemView.findViewById(R.id.locationImageView);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}