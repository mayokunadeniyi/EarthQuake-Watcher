package com.mayokun.earthquakewatcher.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mayokun.earthquakewatcher.Model.EarthQuake;
import com.mayokun.earthquakewatcher.R;
import com.mayokun.earthquakewatcher.UI.CustomInfoWindow;
import com.mayokun.earthquakewatcher.Util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private RequestQueue requestQueue;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private BitmapDescriptor[] iconColors;
    private Button showListBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        requestQueue = Volley.newRequestQueue(this);
        showListBtn = (Button) findViewById(R.id.showListBtn);

        showListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(MapsActivity.this,EarthQuakeList.class));
            }
        });

        iconColors = new BitmapDescriptor[]{
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA),
                //BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)
        };

        //Get the EarthQuakes
        getEarthQuakes();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new CustomInfoWindow(getApplicationContext()));
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerClickListener(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    private void getEarthQuakes() {

        final EarthQuake earthQuake = new EarthQuake();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.BASE_URL,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray feature = response.getJSONArray("features");
                    for (int i = 0; i < Constants.LIMIT; i++) {

                        //Get the properties
                        JSONObject properties = feature.getJSONObject(i).getJSONObject("properties");

                        //Get the geometry
                        JSONObject geometry = feature.getJSONObject(i).getJSONObject("geometry");
                        //Get the coordinates
                        JSONArray coordinates = geometry.getJSONArray("coordinates");

                        //Get the longitute and latitude of the coordinates
                        double lon = coordinates.getDouble(0);
                        double lat = coordinates.getDouble(1);

                        earthQuake.setPlace(properties.getString("place"));
                        earthQuake.setType(properties.getString("type"));
                        earthQuake.setTime(properties.getLong("time"));
                        earthQuake.setMagnitude(properties.getDouble("mag"));
                        earthQuake.setDetailLink(properties.getString("detail"));
                        earthQuake.setLatitude(lat);
                        earthQuake.setLongitude(lon);

                        //Get and format the date for the earthquake
                        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                        String formattedDate = dateFormat.format(new Date(Long.valueOf(properties.getLong("time"))).getTime());

                        //Create Marker Options and populate to the Map
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.icon(iconColors[Constants.randomInt(iconColors.length,0)]);
                        markerOptions.title(earthQuake.getPlace());
                        markerOptions.position(new LatLng(earthQuake.getLatitude(), earthQuake.getLongitude()));
                        markerOptions.snippet("Magnitude: " + earthQuake.getMagnitude()
                                + "\n" + "Date: " + formattedDate);

                        //Earthquake with magnitude equal or greater than 2
                        if (earthQuake.getMagnitude() >= 2.0){
                            CircleOptions circleOptions = new CircleOptions();
                            circleOptions.center(new LatLng(earthQuake.getLatitude(),earthQuake.getLongitude()));
                            circleOptions.radius(30000);
                            circleOptions.strokeWidth(3.6f);
                            circleOptions.fillColor(Color.RED);
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                            mMap.addCircle(circleOptions);
                        }

                        Marker marker = mMap.addMarker(markerOptions);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 1));
                        marker.setTag(earthQuake.getDetailLink());

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        getEarthQuakeDetail(marker.getTag().toString());

    }


    private void getEarthQuakeDetail(String toString) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, toString, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String detailUrl = "";

                        try {
                            JSONObject properties = response.getJSONObject("properties");
                            JSONObject products = properties.getJSONObject("products");
                            JSONArray geoserve = products.getJSONArray("geoserve");

                            for (int i = 0; i < geoserve.length(); i++) {
                                JSONObject geoserveObj = geoserve.getJSONObject(i);
                                JSONObject contents = geoserveObj.getJSONObject("contents");
                                JSONObject geoserveJson = contents.getJSONObject("geoserve.json");


                                detailUrl = geoserveJson.getString("url");
                            }
                            getMoreDetails(detailUrl);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    public void getMoreDetails(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        builder = new AlertDialog.Builder(MapsActivity.this);
                        View view = getLayoutInflater().inflate(R.layout.layout_popup, null);

                        //Set up layout popup widget

                        Button dismissBtn = (Button) view.findViewById(R.id.dismissPop);
                        Button dismissBtnTop = (Button) view.findViewById(R.id.dismissPopup);
                        TextView popList = (TextView) view.findViewById(R.id.popList);
                        WebView webView = (WebView) view.findViewById(R.id.webViewID);
                        StringBuilder stringBuilder = new StringBuilder();

                        try {
                            if (response.has("tectonicSummary") && response.getString("tectonicSummary") != null) {
                                JSONObject tectonic = response.getJSONObject("tectonicSummary");

                                if (tectonic.has("text") && tectonic.getString("text") != null) {

                                    String text = tectonic.getString("text");
                                    webView.loadDataWithBaseURL(null, text, "text/html", "UTF-8", null);
                                }
                            }
                            JSONArray cities = response.getJSONArray("cities");
                            for (int i = 0; i < cities.length(); i++) {

                                JSONObject cityObj = cities.getJSONObject(i);

                                stringBuilder.append("City: " + cityObj.getString("name") +
                                        "\n" + "Distance: " + cityObj.getString("distance") +
                                        "\n" + "Population: " + cityObj.get("population"));
                                stringBuilder.append("\n\n");

                            }

                            popList.setText(stringBuilder);
                            dismissBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
                                }
                            });
                            dismissBtnTop.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
                                }
                            });
                            builder.setView(view);
                            alertDialog = builder.create();
                            alertDialog.show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
