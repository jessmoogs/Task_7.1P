package com.example.lostandfoundapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.lostandfoundapp.adapters.AdvertAdapter;
import com.example.lostandfoundapp.data.Advert;
import com.example.lostandfoundapp.data.AdvertDatabase;
import com.example.lostandfoundapp.databinding.ActivityItemListBinding;

import java.util.List;

public class ItemListActivity extends AppCompatActivity {

    private ActivityItemListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 🔙 Back Button
        binding.btnBack.setOnClickListener(v -> finish());

        // Get all adverts (lost & found together)
        List<Advert> allAdverts = AdvertDatabase.getInstance(this).advertDao().getAllAdverts();

        AdvertAdapter adapter = new AdvertAdapter(allAdverts, advert -> {
            Intent intent = new Intent(ItemListActivity.this, ItemDetailActivity.class);
            intent.putExtra("advertId", advert.id);
            startActivity(intent);
        });

        binding.recyclerAll.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerAll.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<Advert> updatedList = AdvertDatabase.getInstance(this).advertDao().getAllAdverts();

        if (updatedList.isEmpty()) {
            binding.recyclerAll.setVisibility(View.GONE);
            binding.promptRedItems.setVisibility(View.GONE);
            binding.emptyMessage.setVisibility(View.VISIBLE);
        } else {
            binding.recyclerAll.setVisibility(View.VISIBLE);
            binding.promptRedItems.setVisibility(View.VISIBLE);
            binding.emptyMessage.setVisibility(View.GONE);

            AdvertAdapter adapter = new AdvertAdapter(updatedList, advert -> {
                Intent intent = new Intent(ItemListActivity.this, ItemDetailActivity.class);
                intent.putExtra("advertId", advert.id);
                startActivity(intent);
            });

            binding.recyclerAll.setLayoutManager(new LinearLayoutManager(this));
            binding.recyclerAll.setAdapter(adapter);
        }
    }


}
