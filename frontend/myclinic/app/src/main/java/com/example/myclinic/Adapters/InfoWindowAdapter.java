package com.example.myclinic.Adapters;

import android.app.Activity;
import android.view.View;


import com.example.garagerent.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Activity context;

    public InfoWindowAdapter(Activity context){
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view = context.getLayoutInflater().inflate(R.layout.card_spots_map, null);


        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }


}

