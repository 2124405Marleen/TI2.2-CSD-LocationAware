package nl.lorenzostolk.ti22_csd_locationaware.Controller;

import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import nl.lorenzostolk.ti22_csd_locationaware.View.WeekOverview;
import nl.lorenzostolk.ti22_csd_locationaware.Model.WorkingWeek;
import nl.lorenzostolk.ti22_csd_locationaware.R;

import java.util.ArrayList;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.StatisticsViewHolder> {

    private ArrayList<WorkingWeek> weeks;

    public StatisticsAdapter(ArrayList<WorkingWeek> weeks){
        this.weeks = weeks;
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
        holder.textHours.setText(String.valueOf(weeks.get(position).getTotalSeconds()));
        holder.textYear.setText(String.valueOf(weeks.get(position).getYear()));
        holder.textWeekNumber.setText(String.valueOf(weeks.get(position).getWeekNumber()));
    }

    @Override
    public int getItemCount() {
        return this.weeks.size();
    }


    public class StatisticsViewHolder extends RecyclerView.ViewHolder {
        private TextView textHours;
        private TextView textWeekNumber;
        private TextView textYear;

        public StatisticsViewHolder(@NonNull final View itemView) {
            super(itemView);
            textHours = itemView.findViewById(R.id.Statistic_overview_text_hours);
            textWeekNumber = itemView.findViewById(R.id.Statistic_overview_text_weeknumber);
            textYear = itemView.findViewById(R.id.Statistic_overview_textyear);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), WeekOverview.class);
                    Log.i("DETAILHOURS", "" + StatisticsViewHolder.super.getAdapterPosition());
                    WorkingWeek workingWeek = weeks.get(StatisticsViewHolder.super.getAdapterPosition());
                    intent.putExtra("WEEK", workingWeek);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
