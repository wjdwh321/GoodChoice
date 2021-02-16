package com.example.testgc2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/*
 this program is made by 박정조,조성재
 this project provide restaurent search operation.
 the development date : from 2017-11-27 to 2017-12-06

 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnInfoWindowLongClickListener {


    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentLocationMarker;
    public static final int REQUEST_LOCATION_CODE = 99;
    double latitude,longitude;

    private AutoCompleteTextView textView;
    InputMethodManager imm;
    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this, R.array.거리별, android.R.layout.simple_list_item_activated_1);
        Spinner spinner1 = (Spinner) findViewById(R.id.Distancespinner);
        spinner1.setPrompt("거리별");
        spinner1.setAdapter(adapter1);

        ImageView imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView1.setImageResource(R.drawable.goodchoice_logo);

        Button imageButton = (Button) findViewById(R.id.imageButton);


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this , BookmarkActivity.class);
                startActivity(intent);
            }
        });




        myDB = new DatabaseHelper(this);
        textView = (AutoCompleteTextView)findViewById(R.id.autotext);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.showDropDown();

                setCompleteList();
            }
        });


        textView.setOnKeyListener(new View.OnKeyListener(){

            @Override
            public boolean onKey(View view, int keycode, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keycode == KeyEvent.KEYCODE_ENTER)) {
                    textView.dismissDropDown();
                    return  true;
                }

                return false;
            }
        });

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkLocationPermission();
        }

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

        }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode)
        {
            case REQUEST_LOCATION_CODE:
                if(grantResults.length> 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    //PERMISSION IS GRANTED
                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        if(client == null)
                        {
                            buildGoogleAPiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else //permission is denied
                {
                    Toast.makeText(this,"위치 권한이 필요합니다." , Toast.LENGTH_LONG).show();
                }

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
        {
            buildGoogleAPiClient();
            mMap.setMyLocationEnabled(true);
        }

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(this));
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnInfoWindowLongClickListener(this);
    }

    protected synchronized void buildGoogleAPiClient(){
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        client.connect();
    }

    @Override
    public void onLocationChanged(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        lastLocation = location;

        if(currentLocationMarker != null){
            currentLocationMarker.remove();
        }

        Log.d("lat = ",""+latitude);
        Log.d("lng = ",""+longitude);
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("현재 위치");
        markerOptions.snippet("현재 당신의 위치입니다.");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        currentLocationMarker = mMap.addMarker(markerOptions);


        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(10));

        if(client != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(client,this);
        }

    }
    public void onClick(View v)
    {

        String TEXT = textView.getText().toString();


        mMap.clear();
        String restaurant = "restaurant";
        String url = getUrl(latitude,longitude,restaurant);
        Object dataTransfer[] = new Object[2];
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;

        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        getNearbyPlacesData.execute(dataTransfer);
        Toast.makeText(MapsActivity.this,"주변 검색",Toast.LENGTH_LONG).show();



        if(textView.length()!=0)
        {
            AddData(TEXT);
            textView.setText("");
        }
        else
        {

        }

    }

    private String getUrl(double latitude , double longitude , String nearbyPlace) {

        String TEXT = textView.getText().toString();
        Spinner spinner = (Spinner)findViewById(R.id.Distancespinner);
        String Distance = spinner.getSelectedItem().toString();

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+Distance);
        googlePlaceUrl.append("&type="+nearbyPlace);
        googlePlaceUrl.append("&keyword="+TEXT);
        googlePlaceUrl.append("&key="+"AIzaSyAqJSqqIvd5QZLaSDLIw_UUXfcHUc3w3HA");

        Log.d("MapsActivity", "url = "+googlePlaceUrl.toString());

        return googlePlaceUrl.toString();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();

        locationRequest.setInterval(100);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }
    }
    public boolean checkLocationPermission(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
            }

            else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
        }

            return false;
        }

        else
            return true;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public void AddData(String newEntry)
    {

        boolean insertData = myDB.addData(newEntry);

        if(insertData == true)
        {
            //Toast.makeText(this,"Data successfully inserted!",Toast.LENGTH_LONG).show();
        }
        else
        {
           // Toast.makeText(this,"something is wrong",Toast.LENGTH_LONG).show();
        }

        Log.d("db","dat insert");
    }


    private void setCompleteList()
    {
        myDB = new DatabaseHelper(this);
        textView = (AutoCompleteTextView)findViewById(R.id.autotext);

        ArrayList<String> theList = new ArrayList<>();
        Cursor data = myDB.getListContents();

        if(data.getCount() == 0 )
        {
            Toast.makeText(this,"내용을 입력하세요",Toast.LENGTH_SHORT).show();
        }else
        {
            while(data.moveToNext()){
                theList.add(data.getString(1));
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,theList);
                textView.setAdapter(adapter);
            }

        }

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        if(marker.isInfoWindowShown()){
            marker.hideInfoWindow();
        }

    }
    @Override
    public void onInfoWindowLongClick(Marker marker) {
        String title = marker.getTitle();//이름
        if(title.equals("현재 위치")){
            marker.hideInfoWindow();
        }else{
        Toast.makeText(this, "즐겨찾기에 추가되었습니다.", Toast.LENGTH_SHORT).show();
        String vicinity;
        String rating;
        String latlng = marker.getPosition().toString();
        latlng.toString();
        String snippet =marker.getSnippet();//"주소 : "+vicinity+"\n"+"위도:"+lat+"\n경도:"+lng+"\n평점:"+rating"
        String result[] = snippet.split("\n");
        String result1[] = result[0].split(":");
        String ll[];
        vicinity = result1[1];
        result1 = result[3].split(":");
        rating = result1[1];
        ll = latlng.split(",");
        myDB.addData2(title,vicinity,ll[0],ll[1],rating);
        }

    }
}





