package com.example.appproject;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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

//        List<MarkerOptions> markerOptionsList = demoMarkers("Simta Plonit 2 Tel Aviv","Israel");
//
//        if(markerOptionsList != null) {
//            setMarkers(markerOptionsList);
//            map.moveCamera(CameraUpdateFactory.newLatLng(markerOptionsList.get(0).getPosition()));
//        }




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




    private List<MarkerOptions> demoMarkers(String city,String street){
        Geocoder coder = new Geocoder(this.getContext());
        List<Address> addresses=null;
        LatLng point = null;

         String address = city+","+street;

        try{
            addresses=coder.getFromLocationName(address,1);

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(addresses==null)
            return null;

            Address location = addresses.get(0);
            point=new LatLng(location.getLatitude(),location.getLongitude());




        List<MarkerOptions> markers = new ArrayList<MarkerOptions>();
        MarkerOptions marker = new MarkerOptions().position(point).title(address);
        markers.add(marker);
//        LatLng sydney = new LatLng(-33.852, 151.211);
//        LatLng sydney2 = new LatLng(-33.842, 151.211);
//        LatLng melbourne = new LatLng(-37.813, 144.962);
//        MarkerOptions marker1 = new MarkerOptions().position(sydney).title("Sydney");
//        MarkerOptions marker2 = new MarkerOptions().position(sydney2).title("Sydney2");
//        MarkerOptions marker3 = new MarkerOptions().position(melbourne).title("Melbourne");
//        markers.add(marker1);
//        markers.add(marker2);
//        markers.add(marker3);
        return markers;
    }

    private void setMarkers(List<MarkerOptions> markers){
        for(MarkerOptions m: markers){
            map.addMarker(m);
        }
    }



}