package com.example.lostandfoundapp.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lostandfoundapp.R;
import com.example.lostandfoundapp.data.Advert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdvertAdapter extends RecyclerView.Adapter<AdvertAdapter.AdvertViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Advert advert);
    }

    private final List<Advert> advertList;
    private final OnItemClickListener listener;

    public AdvertAdapter(List<Advert> advertList, OnItemClickListener listener) {
        this.advertList = advertList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdvertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_advert, parent, false);
        return new AdvertViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdvertViewHolder holder, int position) {
        Advert advert = advertList.get(position);
        holder.textDescription.setText(advert.type + ": " + advert.description);
        holder.textDate.setText("Date: " + advert.date);

        if (isOlderThanOneWeek(advert.date)) {
            holder.textDate.setTextColor(Color.RED);
        } else {
            holder.textDate.setTextColor(Color.parseColor("#777777")); // Default color
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(advert));
    }


    @Override
    public int getItemCount() {
        return advertList.size();
    }

    static class AdvertViewHolder extends RecyclerView.ViewHolder {
        TextView textDescription, textDate;

        public AdvertViewHolder(@NonNull View itemView) {
            super(itemView);
            textDescription = itemView.findViewById(R.id.textDescription);
            textDate = itemView.findViewById(R.id.textDate);
        }
    }

    private boolean isOlderThanOneWeek(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date itemDate = sdf.parse(dateString);
            Calendar oneWeekAgo = Calendar.getInstance();
            oneWeekAgo.add(Calendar.DAY_OF_YEAR, -7);
            return itemDate != null && itemDate.before(oneWeekAgo.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

}
