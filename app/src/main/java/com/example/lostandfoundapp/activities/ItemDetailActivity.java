package com.example.lostandfoundapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lostandfoundapp.data.Advert;
import com.example.lostandfoundapp.data.AdvertDatabase;
import com.example.lostandfoundapp.databinding.ActivityItemDetailBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ItemDetailActivity extends AppCompatActivity {

    private ActivityItemDetailBinding binding;
    private Advert advert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Back button
        binding.btnBack.setOnClickListener(v -> finish());

        int advertId = getIntent().getIntExtra("advertId", -1);
        if (advertId == -1) {
            Toast.makeText(this, "Invalid advert ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        advert = getAdvertById(advertId);

        if (advert != null) {
            binding.textType.setText(advert.type + ": " + advert.description);
            binding.textDate.setText("Date: " + advert.date);
            if (isOlderThanOneWeek(advert.date)) {
                binding.textDate.setTextColor(Color.RED);
            } else {
                binding.textDate.setTextColor(Color.parseColor("#777777"));
            }
            binding.textLocation.setText("Location: " + advert.location);
        }

        // Edit button
        binding.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateAdvertActivity.class);
            intent.putExtra("editMode", true);
            intent.putExtra("advertId", advert.id);
            startActivity(intent);
        });

        // Remove item
        binding.btnRemove.setOnClickListener(v -> {
            AdvertDatabase.getInstance(this).advertDao().delete(advert);
            Toast.makeText(this, "Advert removed", Toast.LENGTH_SHORT).show();
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        });
    }

    private Advert getAdvertById(int id) {
        for (Advert a : AdvertDatabase.getInstance(this).advertDao().getAllAdverts()) {
            if (a.id == id) return a;
        }
        return null;
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
