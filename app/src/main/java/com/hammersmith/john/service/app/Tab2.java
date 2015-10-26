package com.hammersmith.john.service.app;

import android.location.Location;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hammersmith.john.service.R;
import com.hammersmith.john.service.gpsTracker.GPSService;

import java.text.DecimalFormat;

/**
 * Created by John on 8/24/2015.
 */
public class Tab2 extends Fragment {

    private GoogleMap mMap;
    private SupportMapFragment supportMapFragment;
    MapView mMapView;
    Marker mMarker;
    private static View view;
    GPSService mGPSService;

    double dis;
//    public Tab2(){}
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view= inflater.inflate(R.layout.tab_2,container,false);
            mGPSService = new GPSService(getActivity());
        }catch (InflateException e){
            /* map is already there, just return view as it is */
        }

//        mMapView = (MapView) v.findViewById(R.id.map);
//        mMapView.onCreate(savedInstanceState);
//        mMapView.onResume(); // needed to get the map to display immediately
////       MapsInitializer.initialize(getActivity());
//        mMap = mMapView.getMap();
//        mMap.setBuildingsEnabled(true);
//
//        // Latitude Logtitude
//        LatLng latLng = new LatLng(11.5565195, 104.9198001);
//////        // Create Maker
//        mMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Brown Coffee"));
//        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15).build();
//        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//        GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
//            @Override
//            public void onMyLocationChange(Location location) {
//                LatLng loc = new LatLng(location.getLatitude(),location.getLongitude());
//                mMarker = mMap.addMarker(new MarkerOptions().position(loc));
//                if (mMap != null){
//                    CameraPosition cameraPosition = new CameraPosition.Builder().target(loc).zoom(15).build();
//                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//                }
//            }
//        };
//
//        mMap.setOnMyLocationChangeListener(myLocationChangeListener);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        supportMapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (supportMapFragment == null){
            supportMapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map,supportMapFragment).commit();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mMap == null){
            mMap = supportMapFragment.getMap();
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    mGPSService.getLocation();
                    return false;
                }
            });
            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title("My location"));
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            });
        }
    }


    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }
}
