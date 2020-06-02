package com.constantinpricope.cpnote;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import static androidx.core.content.ContextCompat.getSystemService;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DataTime#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DataTime extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView textView;
    Context context ;
    String textLocation = "LocationNotFound";
    LocationManager locationManager;
    private static  final int REQUEST_LOCATION=1;
    String latitude,longitude;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DataTime() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DataTime.
     */
    // TODO: Rename and change types and number of parameters
    private static DataTime newInstance(String param1, String param2) {
        DataTime fragment = new DataTime();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
//        textView = (TextView) getView().findViewById(R.id.textView2);
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        Date date = new Date();
//        System.out.println(formatter.format(date));
//        textView.setText("Astazi suntem in : " + formatter.format(date));
        //Add permission

//        ActivityCompat.requestPermissions(this,new String[]
//                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
//
//        showLocationTxt=findViewById(R.id.show_location);
//        getlocationBtn=findViewById(R.id.getLocation);
//
//        getlocationBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {

                locationManager=(LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                //Check gps is enable or not

                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                {
                    //Write Function To enable gps

                    OnGPS();
                }
                else
                {
                    //GPS is already On then

                    getLocation();
                }
//            }
//        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_data_time, container, false);
         textView = v.findViewById(R.id.textView2);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        System.out.println(formatter.format(date));
//        LocationInterface locationInterface = new LocationInterface(savedInstanceState);

//        LocationManager locationmanager= (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        textView.setText("Astazi suntem in : " + formatter.format(date) +"\n Locatia este: " + textLocation);

        // Inflate the layout for this fragment

        return v;
    }

    ///////////////////////////////
    private void getLocation() {

        //Check Permissions again

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),

                Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(),new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else
        {
            Location LocationGps= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive=locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (LocationGps !=null)
            {
                double lat=LocationGps.getLatitude();
                double longi=LocationGps.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);

                textLocation = (" "+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude);
            }
            else if (LocationNetwork !=null)
            {
                double lat=LocationNetwork.getLatitude();
                double longi=LocationNetwork.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);

                textLocation = (" "+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude);
            }
            else if (LocationPassive !=null)
            {
                double lat=LocationPassive.getLatitude();
                double longi=LocationPassive.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);

                textLocation = (" "+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude);
            }
//            else
//            {
//                Toast.makeText(this, "Can't Get Your Location", Toast.LENGTH_SHORT).show();
//            }

            //Thats All Run Your App
        }

    }

    private void OnGPS() {

        final AlertDialog.Builder builder= new AlertDialog.Builder(context);

        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        final AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
    ///////////
}
