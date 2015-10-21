package com.hammersmith.john.service.app;

import android.app.Dialog;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hammersmith.john.service.R;
import com.hammersmith.john.service.gpsTracker.GpsTracker;

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

    LocationManager lm;

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
    public void onResume() {
        super.onResume();
//            GpsTracker gpsTracker = new GpsTracker(getActivity());
            LatLng latLng = new LatLng(11.5565195, 104.9198001);
            if (mMap == null){
                mMap = supportMapFragment.getMap();
                mMap.addMarker(new MarkerOptions().position(latLng).title("Brown"));
                CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//                if (gpsTracker.canGetLocation()){
//                    LatLng latLng = new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude());
//                    LatLng endP = new LatLng(11.5565195, 104.9198001);
//                    mMap.addMarker(new MarkerOptions().position(latLng).title("My location"));
//                    CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15).build();
//                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//                    dis = CalculationByDistance(latLng,endP);
//                    Toast.makeText(getActivity(),"Dis : "+ dis,Toast.LENGTH_LONG).show();
//                }
//                else {
//                    gpsTracker.showSettingsAlert();
//                }
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
