package com.example.appproject;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.appproject.feed.FragmentFeedDirections;
import com.example.appproject.model.Model;
import com.example.appproject.model.user.User;
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
import java.util.HashMap;
import java.util.List;


public class FragmentMap extends Fragment implements OnMapReadyCallback {

    GoogleMap map;
    HashMap<String,String> nameToId = new HashMap<>();
    public FragmentMap() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.main_menu_settings).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        Model.instance.refreshList();
        subscribeToObservers();




        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                      marker.showInfoWindow();
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

           LiveData<List<User>> users=Model.instance.getUsers();
           User selectedUser=null;
           for(User user:users.getValue()) {
               if (user.getAddress().equals(location)) {
                   selectedUser = user;
                   nameToId.put(selectedUser.getName(),selectedUser.getUid());
               }
           }
            address=coder.getFromLocationName(location,1);
            if(address != null && address.size() > 0 ){
                Address loc = address.get(0);
                markers.add(new MarkerOptions()
                        .position(new LatLng(loc.getLatitude(),loc.getLongitude()))
                        .title(selectedUser.getName())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        .snippet(location));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        setMarkers(markers);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(markers.get(0).getPosition(),15));
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(@NonNull Marker marker) {
                Navigation.findNavController(getView()).navigate(FragmentMapDirections.actionFragmentMapToFragmentUserDisplayDetails(nameToId.get(marker.getTitle())));
            }
        });
    }

    private void addLocationsToMap(List<String> locations) {
        List<Address> address=null;
        Geocoder coder = new Geocoder(this.getContext());
        List<MarkerOptions> markers = new ArrayList<MarkerOptions>();

        for(String location: locations){
            try{
                LiveData<List<User>> users=Model.instance.getUsers();
                User selectedUser=null;
                for(User user:users.getValue()) {
                    if (user.getAddress().equals(location)) {
                        selectedUser = user;
                        nameToId.put(selectedUser.getName(),selectedUser.getUid());
                    }
                }
                address=coder.getFromLocationName(location,1);
                if(address != null && address.size()>0 ){
                    Address loc = address.get(0);
                    markers.add(new MarkerOptions()
                            .position(new LatLng(loc.getLatitude(), loc.getLongitude()))
                            .title(selectedUser.getName())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                            .snippet(location));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        setMarkers(markers);
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(@NonNull Marker marker) {
                Navigation.findNavController(getView()).navigate(FragmentMapDirections.actionFragmentMapToFragmentUserDisplayDetails(nameToId.get(marker.getTitle())));
            }
        });
    }


    private void setMarkers(List<MarkerOptions> markers){
        for(MarkerOptions m: markers){
            map.addMarker(m);
        }
    }



}