package com.example.appproject;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.appproject.model.Model;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FragmentMap extends Fragment implements OnMapReadyCallback {

    GoogleMap map;
    public FragmentMap() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initMap();
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    private void initMap(){
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        requireActivity().getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map=googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);
        Model.instance.getUserLocation();
        Model.instance.getLocations();
        subscribeToObservers();



        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //      String title = marker.getTitle();
                //      LatLng position = marker.getPosition();
                //     Toast.makeText(getActivity(), title+" "+position, Toast.LENGTH_LONG).show();
//                      marker.showInfoWindow();

                //      Navigation.findNavController(getView()).navigate(R.id.action_fragmentMaps_to_fragmetnUserDetails);
                return true;
            }
        });
    }

    private void subscribeToObservers() {

        Model.instance.userLocation.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                addUserLocationToMap(s);
            }
        });

        Model.instance.locationsList.observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                addLocationsToMap(strings);

            }
        });
    }

    private void addUserLocationToMap(String location) {
        List<Address> address=null;
        Geocoder coder = new Geocoder(this.getContext());
        List<MarkerOptions> markers = new ArrayList<MarkerOptions>();

        try{
            address=coder.getFromLocationName(location,1);
            if(address != null && address.size() > 0 ){
                Address loc = address.get(0);
                markers.add(new MarkerOptions()
                        .position(new LatLng(loc.getLatitude(),loc.getLongitude()))
                        .title(location)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        .snippet(location));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        setMarkers(markers);
//        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//            @Override
//            public void onInfoWindowClick(@NonNull Marker marker) {
//
//                //move to the user feed
//                marker.hideInfoWindow();
//            }
//        });

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                Log.d("TAG","marker is clicked"+marker.isInfoWindowShown());

                    marker.showInfoWindow();

                return true;
            }
        });
        map.moveCamera(CameraUpdateFactory.newLatLng(markers.get(0).getPosition()));
    }

    private void addLocationsToMap(List<String> locations) {
        List<Address> address=null;
        Geocoder coder = new Geocoder(this.getContext());
        List<MarkerOptions> markers = new ArrayList<MarkerOptions>();

        for(String location: locations){
            try{
                address=coder.getFromLocationName(location,1);
                if(address != null && address.size()>0 ){
                    Address loc = address.get(0);
                    markers.add(new MarkerOptions()
                            .position(new LatLng(loc.getLatitude(), loc.getLongitude()))
                            .title(location)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                            .snippet(location));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                marker.showInfoWindow();

                return true;
            }
        });
//        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//            @Override
//            public void onInfoWindowClick(@NonNull Marker marker) {
//
//                //move to the user feed
//                marker.hideInfoWindow();
//            }
//        });
        setMarkers(markers);
    }



    private void setMarkers(List<MarkerOptions> markers){
        for(MarkerOptions m: markers){
            map.addMarker(m);
        }
    }



}