package com.example.tugasbesarandhika;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tugasbesarandhika.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ArrayList<Meal> meals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // back button
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        // swipe to refresh
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            load();
            new Handler().postDelayed(() -> binding.swipeRefreshLayout.
                    setRefreshing(false), 1000); // Delay in millis
        });

        binding.swipeRefreshLayout.
                setColorSchemeResources(android.R.color.holo_blue_bright,
                        android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_red_light);

        // ambil data dari internet
        load();
    }

    private void showRecyclerGrid() {
        // deteksi mode ponsel: potrait atau landscape
        if (this.getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {
            binding.recyclerView.setLayoutManager(new
                    GridLayoutManager(this, 2));
        } else {
            binding.recyclerView.setLayoutManager(new
                    GridLayoutManager(this, 4));
        }

        RecyclerViewAdapter mAdapter = new RecyclerViewAdapter(this, meals);
        binding.recyclerView.setAdapter(mAdapter);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void load() {
        binding.progressBar.setVisibility(ProgressBar.VISIBLE);
        RequestQueue queue = Volley.
                newRequestQueue(getApplicationContext());
        String url = "https://www.themealdb.com/api/json/v1/1/filter.php?c=Dessert";

        //Ambil JSON dari internet
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    String id, meal, photo;
                    meals = new ArrayList<>();
                    try {
                        //ambil objek meals
                        JSONArray jsonArray = response.getJSONArray("meals");
                        meals.clear();

                        //masukkan kedalam Arraylist
                        if (jsonArray.length() != 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.
                                        getJSONObject(i);
                                id = data.getString("idMeal").trim();
                                meal = data.getString("strMeal").trim();
                                photo = data.getString("strMealThumb").
                                        trim();
                                //masukkan kedalam Arraylist
                                meals.add(new Meal(id, meal, photo));
                            }
                            showRecyclerGrid();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    binding.progressBar.setVisibility(ProgressBar.GONE);
                }, error -> {
            binding.progressBar.setVisibility(ProgressBar.GONE);
            Toast.makeText(this, "Connection problem !", Toast.LENGTH_SHORT).show();
        });
        queue.add(jsObjRequest);
    }
}