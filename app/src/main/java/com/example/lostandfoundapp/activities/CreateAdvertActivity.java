package com.example.lostandfoundapp.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lostandfoundapp.R;
import com.example.lostandfoundapp.data.Advert;
import com.example.lostandfoundapp.data.AdvertDatabase;
import com.example.lostandfoundapp.databinding.ActivityCreateAdvertBinding;

import java.util.Calendar;

public class CreateAdvertActivity extends AppCompatActivity {

    private ActivityCreateAdvertBinding binding;
    private boolean isEditMode;
    private int editAdvertId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAdvertBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 🔙 Back Button
        binding.btnBack.setOnClickListener(v -> finish());

        isEditMode = getIntent().getBooleanExtra("editMode", false);
        editAdvertId = getIntent().getIntExtra("advertId", -1);

        if (isEditMode && editAdvertId != -1) {
            Advert existing = null;
            for (Advert a : AdvertDatabase.getInstance(this).advertDao().getAllAdverts()) {
                if (a.id == editAdvertId) {
                    existing = a;
                    break;
                }
            }

            if (existing != null) {
                if (existing.type.equalsIgnoreCase("Lost")) {
                    binding.radioGroup.check(R.id.radioLost);
                } else {
                    binding.radioGroup.check(R.id.radioFound);
                }
                binding.editName.setText(existing.name);
                binding.editPhone.setText(existing.phone);
                binding.editDescription.setText(existing.description);
                binding.editDate.setText(existing.date);
                binding.editLocation.setText(existing.location);
            }
        }

        // 📅 Date Picker
        binding.editDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(
                    this,
                    (DatePicker view, int year, int month, int dayOfMonth) -> {
                        String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                        binding.editDate.setText(date);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            dialog.show();
        });

        // ✅ Save / Update Button
        binding.btnSave.setOnClickListener(v -> {
            String type = ((RadioButton) findViewById(binding.radioGroup.getCheckedRadioButtonId())).getText().toString();
            String name = binding.editName.getText().toString().trim();
            String phone = binding.editPhone.getText().toString().trim();
            String description = binding.editDescription.getText().toString().trim();
            String date = binding.editDate.getText().toString().trim();
            String location = binding.editLocation.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty() || description.isEmpty() || date.isEmpty() || location.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isEditMode && editAdvertId != -1) {
                Advert updated = new Advert(type, name, phone, description, date, location);
                updated.id = editAdvertId;
                AdvertDatabase.getInstance(this).advertDao().delete(updated);
                AdvertDatabase.getInstance(this).advertDao().insert(updated);
                Toast.makeText(this, "Advert updated!", Toast.LENGTH_SHORT).show();
            } else {
                Advert advert = new Advert(type, name, phone, description, date, location);
                AdvertDatabase.getInstance(this).advertDao().insert(advert);
                Toast.makeText(this, "Advert saved!", Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent(this, ItemListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        });
    }
}
