package com.example.myclinic.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myclinic.Adapters.MainListAdapter;
import com.example.myclinic.Details;
import com.example.myclinic.Downloader.JSONarrayDownloader;
import com.example.myclinic.Maps.GPSUtils;
import com.example.myclinic.Models.MainListModel;
import com.example.myclinic.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class FragmentExplore extends Fragment {

    private LocationManager locationManager;
    private Location mLastLocation;
    private Marker mCurrLocationMarker=null;
    private GoogleMap mGoogleMap;

    private String URL = "https://garage-rent-api.azurewebsites.net/api/Garage/GetAllGarages";

    public JSONObject jsonObject, jsonObject1;
    public JSONArray jsonArray, jsonArray1;

    RecyclerView recyclerView;
    List<MainListModel> mainListModelList;
    MainListAdapter mainListAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        //LISTA PRINCIPAL
        recyclerView = view.findViewById(R.id.recyclerViewMain);
        mainListModelList = new ArrayList<>();

        JSONarrayDownloader task = new JSONarrayDownloader();
        try {
            jsonArray = task.execute(URL).get();

            if (jsonArray.length() != 0){
                for (int i=0 ; jsonArray.length()>i ; i++){
                    jsonObject = jsonArray.getJSONObject(i);


                    mainListModelList.add(new MainListModel(R.drawable.image_empty_back,
                            jsonObject.getString("id"),
                            jsonObject.getString("name"),
                            jsonObject.getString("address"),
                            jsonObject.getString("price"),
                            "Available"));

                }

            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            jsonArray = null;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        mainListAdapter = new MainListAdapter(getContext(),mainListModelList);
        recyclerView.setAdapter(mainListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);



        GPSUtils.getInstance().findDeviceLocation(getActivity());

        //CUSTOM LOCATION PIN - USER LOCATION
        BitmapDrawable bitmapdrawUser = (BitmapDrawable) getResources().getDrawable(R.drawable.map_pin_user);
        Bitmap smallMarkerUser = Bitmap.createScaledBitmap(bitmapdrawUser.getBitmap(), 80, 80, false);

        //CUSTOM LOCATION PIN - AVAILABLE GARAGES
        BitmapDrawable bitmapdrawAvailable = (BitmapDrawable) getResources().getDrawable(R.drawable.map_pin_available);
        Bitmap smallMarkerAvailable = Bitmap.createScaledBitmap(bitmapdrawAvailable.getBitmap(), 80, 80, false);

        //CUSTOM LOCATION PIN - GARAGES THAT WILL BE AVAILABLE SOON
        BitmapDrawable bitmapdrawSoon = (BitmapDrawable) getResources().getDrawable(R.drawable.map_pin_soon);
        Bitmap smallMarkerSoon = Bitmap.createScaledBitmap(bitmapdrawSoon.getBitmap(), 80, 80, false);

        //CUSTOM LOCATION PIN - GARAGES NOT AVAILABLE
        BitmapDrawable bitmapdrawNot = (BitmapDrawable) getResources().getDrawable(R.drawable.map_pin_not);
        Bitmap smallMarkerNot = Bitmap.createScaledBitmap(bitmapdrawNot.getBitmap(), 80, 80, false);


        //MAP
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.googlemaps);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                //MAP STYLE
                googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(container.getContext(), R.raw.style_json));

                //MAP START POSITION
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(38.74, -9.17)));

                //MAP START ZOOM
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(11));

                //MAP MAX ZOOM
                googleMap.setMinZoomPreference(10.0f);

                //MAP MAX SCROLL
                final LatLngBounds MAX = new LatLngBounds(
                        new LatLng(38.55, -9.50), new LatLng(39, -9.05));
                googleMap.setLatLngBoundsForCameraTarget(MAX);


                //LOCATION PIN POSITION
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(38.74, -9.17))
                        .title("Garage n1")
                        .snippet("Click to view details")
                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarkerAvailable)));


                //Set Custom InfoWindow Adapter
                //InfoWindowAdapter adapter = new InfoWindowAdapter((Activity) view.getContext());
                //googleMap.setInfoWindowAdapter(adapter);

                JSONarrayDownloader task = new JSONarrayDownloader();
                try {
                    jsonArray = task.execute(URL).get();

                    if (jsonArray.length() != 0){
                        for (int i=0 ; jsonArray.length()>i ; i++){
                            jsonObject = jsonArray.getJSONObject(i);
                            double Lat = Double.parseDouble(jsonObject.getString("latitude"));
                            double Lng = Double.parseDouble(jsonObject.getString("longitude"));

                            googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(Lat, Lng))
                                    .title(jsonObject.getString("name"))
                                    .snippet(jsonObject.getString("address"))
                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarkerAvailable)));
                        }

                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                    jsonArray = null;
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


                //CURRENT LOCATION MARKER
                double temp1 = Double.parseDouble(GPSUtils.getInstance().getLatitude());
                double temp2 = Double.parseDouble(GPSUtils.getInstance().getLongitude());
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(temp1, temp2))
                        .title("You")
                        .snippet("Your location")
                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarkerUser)));

                //CLICKABLE INFO WINDOW
                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        String markerId = marker.getSnippet();
                        if (!markerId.equals("Your location")) {
                            Intent intent = new Intent(getActivity(), Details.class);
                            startActivity(intent);
                        }
                    }
                });
            }



        });



        TextView title = view.findViewById(R.id.textTitleGen);
        EditText search = view.findViewById(R.id.search);
        ImageButton filter = view.findViewById(R.id.filter);
        ImageButton chat = view.findViewById(R.id.chat);
        ImageButton settings = view.findViewById(R.id.settings);
        ConstraintLayout list = view.findViewById(R.id.layoutMain);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(supportMapFragment.getView().getVisibility() == View.VISIBLE){
                    supportMapFragment.getView().setVisibility(View.GONE);
                    fab.setImageResource(R.drawable.ic_map);
                    title.setText("List");
                    filter.setVisibility(View.VISIBLE);
                    chat.setVisibility(View.GONE);
                    settings.setVisibility(View.GONE);
                    search.setVisibility(View.VISIBLE);
                    list.setVisibility(View.VISIBLE);

                }else {
                    supportMapFragment.getView().setVisibility(View.VISIBLE);
                    fab.setImageResource(R.drawable.ic_list);
                    title.setText("Map");
                    filter.setVisibility(View.GONE);
                    chat.setVisibility(View.VISIBLE);
                    settings.setVisibility(View.VISIBLE);
                    search.setVisibility(View.GONE);
                    list.setVisibility(View.GONE);
                }

            }
        });




        return view;
    }



}