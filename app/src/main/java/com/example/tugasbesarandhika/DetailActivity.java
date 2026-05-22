package com.example.tugasbesarandhika;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.tugasbesarandhika.databinding.ActivityDetailBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {

    ActivityDetailBinding binding;
    String meal, photo, instruction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // binding
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // terima idMeal dari Main Activity
        Intent i = getIntent();
        String id = i.getStringExtra("i_idMeal");
        load(id);

        // back button
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void load(String id) {
        binding.progressBar.setVisibility(ProgressBar.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://www.themealdb.com/api/json/v1/1/lookup.php?i=" + id;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.
                Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("meals");
                    JSONObject data = jsonArray.getJSONObject(0);
                    meal = data.getString("strMeal");
                    instruction = data.getString("strInstructions");
                    photo = data.getString("strMealThumb");

                    Glide.with(getApplicationContext()).load(photo).
                            into(binding.ivImage);

                    binding.tvName.setText(meal);
                    binding.tvInstruction.setText(instruction);

                    binding.toolbar.setTitle(meal);
                    binding.toolbar.setTitleTextColor(Color.WHITE);

                    binding.progressBar.setVisibility(ProgressBar.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> {
            Toast.makeText(getApplicationContext(), "Please check your connection", Toast.LENGTH_SHORT).show();
        });
        queue.add(jsObjRequest);
    }
}