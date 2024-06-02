package com.myclinic;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myclinic.http.Endpoints;
import com.myclinic.http.GetData;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AvailableServices extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ServiceAdapter serviceAdapter;
    private List<Service> services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_services);

        // STATUS BAR INVISIBLE
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // NAVIGATION BAR INVISIBLE
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize services list
        services = new ArrayList<>();

        // Fetch services from server
        fetchServices();

        // Set up adapter
        serviceAdapter = new ServiceAdapter(services);
        recyclerView.setAdapter(serviceAdapter);
    }

    private void fetchServices() {
        String endpoint = Endpoints.SERVICES_AVAILABLE;
        GetData getData = new GetData();
        try {
            JSONObject response = getData.execute(endpoint).get();
            if (response != null) {
                JSONArray servicesArray = response.getJSONArray("services");
                for (int i = 0; i < servicesArray.length(); i++) {
                    JSONObject serviceJson = servicesArray.getJSONObject(i);
                    String id = serviceJson.getString("id");
                    String name = serviceJson.getString("name");
                    double price = serviceJson.getDouble("price");
                    services.add(new Service(id, name, price));
                }
            }
        } catch (ExecutionException | InterruptedException e) {
            Log.e("AvailableServices", "Error fetching services", e);
        } catch (Exception e) {
            Log.e("AvailableServices", "Error parsing services", e);
        }
    }

    public void Back(View view) {
        finish();
    }
}