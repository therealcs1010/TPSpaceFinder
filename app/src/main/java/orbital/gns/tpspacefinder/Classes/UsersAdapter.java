package orbital.gns.tpspacefinder.Classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import orbital.gns.tpspacefinder.R;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> implements Filterable {

    Context context;
    ArrayList<String> locations;
    ArrayList<String> locationsfull;
    int [] images = {R.drawable.shortcircuit, R.drawable.breadboard, R.drawable.businesspark, R.drawable.designerpad, R.drawable.flavours, R.drawable.triplets, R.drawable.mcdonalds,
    R.drawable.subway, R.drawable.bistrolab, R.drawable.sugar, R.drawable.canopy, R.drawable.toptable};

    public UsersAdapter(Context ct, ArrayList<String> locations) {
        this.locations = locations;
        this.locationsfull = new ArrayList<>(locations);
        this.context = ct;
    }

    @NonNull
    @Override
    public UsersAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.location_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.MyViewHolder holder, int position) {
        String locationName = locations.get(position);
        holder.locationTextView.setText(locationName);
        switch(locationName) {
            case "The Short Circuit" :
               holder.locationImage.setImageResource(images[0]);
               break;
            case "The Bread Board" :
                holder.locationImage.setImageResource(images[1]);
                break;
            case "The Business Park" :
                holder.locationImage.setImageResource(images[2]);
                break;
            case "The Designer Pad" :
                holder.locationImage.setImageResource(images[3]);
                break;
            case "The Flavours" :
                holder.locationImage.setImageResource(images[4]);
                break;
            case "TripletS" :
                holder.locationImage.setImageResource(images[5]);
                break;
            case "McDonald's" :
                holder.locationImage.setImageResource(images[6]);
                break;
            case "Subway" :
                holder.locationImage.setImageResource(images[7]);
                break;
            case "Bistro Lab" :
                holder.locationImage.setImageResource(images[8]);
                break;
            case "Sugarloaf" :
                holder.locationImage.setImageResource(images[9]);
                break;
            case "Canopy Coffee Club Café" :
                holder.locationImage.setImageResource(images[10]);
                break;
            case "The Top Table" :
                holder.locationImage.setImageResource(images[11]);
                break;

        }

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
                filteredList.addAll(locationsfull);
            } else {
                String filterpattern = constraint.toString().toLowerCase().trim();
                for (String item : locationsfull) {
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView locationTextView;
        ImageView locationImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            locationTextView = itemView.findViewById(R.id.locationName);
            locationImage = itemView.findViewById(R.id.locationImageView);
        }
    }
}