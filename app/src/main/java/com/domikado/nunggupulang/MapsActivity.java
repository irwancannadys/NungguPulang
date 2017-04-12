package com.domikado.nunggupulang;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private String[] nama, alamat, harga, jam, update, alatmusik, call, id,
            gambar, ratingalatmusik, ratingrecording, ratingtempat, distance;
    int numData;
    LatLng latLng[];
    Boolean markerD[];
    private Double[] latitude, longitude;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getLokasi();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
    }

    private void getLokasi(){
        String URL = "http://cloudofoasis.com/api/Ivan/getStudio.php";
        JsonArrayRequest request =
                new JsonArrayRequest(Request.Method.GET, URL, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        numData = response.length();
                        Log.d("DEBUG","Parse Json");
                        latLng = new LatLng[numData];
                        markerD = new Boolean[numData];
                        nama = new String[numData];
                        alamat = new String[numData];
                        harga = new String[numData];
                        jam = new String[numData];
                        call = new String[numData];
                        update = new String[numData];
                        alatmusik = new String[numData];
                        ratingalatmusik = new String[numData];
                        ratingrecording = new String[numData];
                        ratingtempat = new String[numData];
                        id = new String[numData];
                        gambar = new String[numData];
                        latitude = new Double[numData];
                        longitude = new Double[numData];

                        for (int i=0; i<numData; i++){
                            try {
                                JSONObject data = response.getJSONObject(i);
                                id[i] = data.getString("id");
                                latLng[i] = new LatLng(data.getDouble("latitude"), data.getDouble("longitude"));
                                nama[i] = data.getString("nama");
                                alamat[i] = data.getString("alamat");
                                harga[i] = data.getString("harga");
                                jam[i] = data.getString("jam");
                                call[i] = data.getString("call");
                                update[i] = data.getString("lastupdate");
                                alatmusik[i] = data.getString("alatmusik");
                                ratingalatmusik[i] = data.getString("ratingalatmusik");
                                ratingrecording[i] = data.getString("ratingrecording");
                                ratingtempat[i] = data.getString("ratingtempat");
                                gambar[i] = data.getString("gambar");
                                latitude[i] = data.getDouble("latitude");
                                longitude[i] = data.getDouble("longitude");

                                markerD[i] = false;
                                mMap.addMarker(new MarkerOptions().position(latLng[i])
                                        .title(nama[i])
                                        .snippet(alamat[i])
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.markermap)));


                            } catch (JSONException e){

                            }

                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng[i], 15.5f));
                        }

                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                Log.d("DEBUG_", "Marker clicked");
                                for (int i = 0; i < numData; i++) {

                                    if (marker.getTitle().equals(nama[i])) {
                                        if (markerD[i]) {
                                            Log.d("DEBUG_", "panggil activity");
                                            Detail.id = id[i];
                                            Detail.namaStudio = nama[i];
                                            Detail.alamat = alamat[i];
                                            Detail.harga = harga[i];
                                            Detail.jam = jam[i];
                                            Detail.callSave = call[i];
                                            Detail.alatmusik = alatmusik[i];
                                            Detail.lastUpdate = update[i];
                                            Detail.ratingAlat = ratingalatmusik[i];
                                            Detail.ratingRec = ratingrecording[i];
                                            Detail.ratingTmpt = ratingtempat[i];
                                            Detail.gambar = gambar[i];
                                            Detail.latitude = latitude[i];
                                            Detail.longitude = longitude[i];

                                            Intent intent = new Intent(MapsActivity.this, Detail.class);
                                            startActivity(intent);
                                            markerD[i] = false;
                                        } else {
                                            Log.d("DEBUG_", "show info");
                                            // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15.5f));
                                            markerD[i] = true;
                                            marker.showInfoWindow();
                                            Toast ts = Toast.makeText(MapsActivity.this,"Tap once again on marker, for detail Studio Music",Toast.LENGTH_LONG);
                                            TextView v = (TextView) ts.getView().findViewById(android.R.id.message);
                                            if( v != null)
                                                v.setGravity(Gravity.CENTER);
                                            ts.show();
                                        }
                                    } else {
                                        markerD[i] = false;
                                    }
                                }
                                return false;
                            }

                        });
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                        builder.setTitle("Error!");
                        builder.setMessage("No Internet Connection");
                        builder.setIcon(android.R.drawable.ic_dialog_alert);
                        builder.setPositiveButton("Refresh", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getLokasi();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });

        Volley.newRequestQueue(this).add(request);
    }
}

