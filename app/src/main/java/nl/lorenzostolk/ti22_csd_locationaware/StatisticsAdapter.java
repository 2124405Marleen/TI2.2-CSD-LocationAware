package nl.lorenzostolk.ti22_csd_locationaware;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.StatisticsViewHolder> {

    private ArrayList<WhenWhere> whenWhereList;
    public StatisticsAdapter(ArrayList<WhenWhere> whenWhereList){
        this.whenWhereList = whenWhereList;
    }

    @NonNull
    @Override
    public StatisticsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_item_statistics, parent, false);
        return new StatisticsViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull StatisticsViewHolder holder, int position) {
        holder.locationTextView.setText(whenWhereList.get(position).getLocation().toString());
        holder.timeTextView.setText(String.valueOf(whenWhereList.get(position).getTimeSpend()));
    }

    @Override
    public int getItemCount() {
        return this.whenWhereList.size();
    }

    public class StatisticsViewHolder extends RecyclerView.ViewHolder {
        private TextView locationTextView;
        private TextView timeTextView;

        public StatisticsViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.Detail_time_text);
            locationTextView = itemView.findViewById(R.id.Detail_location_text);
        }
    }
}
