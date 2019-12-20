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

import static nl.lorenzostolk.ti22_csd_locationaware.R.id.Detail_location_text;

public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.WeekViewHolder> {

    private ArrayList<WhenWhere> whenWhereList;

    public WeekAdapter(ArrayList<WhenWhere> whenWheres){
        this.whenWhereList = whenWheres;
    }
    @NonNull
    @Override
    public WeekViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_item_statistics_detail, parent, false);
        return new WeekViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull WeekViewHolder holder, int position) {
        WhenWhere whenWhere = whenWhereList.get(position);
        holder.textLocation.setText(whenWhere.getTimeSpend());
        holder.textTime.setText(String.valueOf(whenWhere.getLocation()));
        holder.textArrivalAndDeparture.setText(whenWhere.getTimeArrivalAndDeparture());

    }

    @Override
    public int getItemCount() {
        return whenWhereList.size();
    }

    public class WeekViewHolder extends RecyclerView.ViewHolder {

        private TextView textTime;
        private TextView textLocation;
        private TextView textArrivalAndDeparture;

        public WeekViewHolder(@NonNull View itemView) {
            super(itemView);

            this.textTime = itemView.findViewById(R.id.Detail_time_text);
            this.textLocation = itemView.findViewById(Detail_location_text);
            this.textArrivalAndDeparture = itemView.findViewById(R.id.Detail_text_timesArrDep);

        }
    }
}
