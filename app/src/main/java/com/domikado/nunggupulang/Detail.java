package com.domikado.nunggupulang;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Detail extends AppCompatActivity {

    private ImageView callPhone, callShare;
    public static Double latitude, longitude;


    private TextView studioName, studioAddress, studioPrice, studioHour, studioAlatMusik, studioUpdate;
    private RatingBar ratingBarAlat, ratingBarRec, ratingBarTmpt;

    public static String id, namaStudio, alamat, harga, jam, alatmusik, lastUpdate, gambar,
            callSave, ratingAlat, ratingRec, ratingTmpt;
    SliderLayout mDemoSlider;
    private static final String TAG = "ListDislay";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ratingBarAlat = (RatingBar) findViewById(R.id.rating_alatmusik);
        ratingBarRec = (RatingBar) findViewById(R.id.rating_recording);
        ratingBarTmpt = (RatingBar) findViewById(R.id.rating_tempat);

        ratingBarAlat.setRating(Float.parseFloat(ratingAlat));
        ratingBarRec.setRating(Float.parseFloat(ratingRec));
        ratingBarTmpt.setRating(Float.parseFloat(ratingTmpt));

        studioName = (TextView) findViewById(R.id.studioname);
        studioAddress = (TextView) findViewById(R.id.studio_address);
        studioPrice = (TextView) findViewById(R.id.studio_price);
        studioHour = (TextView) findViewById(R.id.studio_hour);
        studioAlatMusik = (TextView) findViewById(R.id.textalatmusik);
        studioUpdate = (TextView) findViewById(R.id.lastupdate);

        studioName.setText(namaStudio);
        studioAddress.setText(alamat);
        studioPrice.setText(harga);
        studioHour.setText(jam);
        studioAlatMusik.setText(alatmusik);
        studioUpdate.setText(lastUpdate);

        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        callPhone = (ImageView) findViewById(R.id.call_button);
        callShare = (ImageView) findViewById(R.id.share_button);

        getGambar();
        callButton();
        sendMessage();
    }

    private void getGambar(){

        String URL = "http://cloudofoasis.com/api/Ivan/getGambar.php?StudioMusik=" + id;
        Log.i(TAG, URL);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                int numData = response.length();
                if (numData == 0) mDemoSlider.setVisibility(View.GONE);
                else {
                    JSONObject slider_studio;
                    Log.i(TAG, "On Response Get Gambar");
                    String[] gambar_studio = new String[numData],
                             nama_studio = new String[numData];

                    HashMap<String, String> url_maps = new HashMap<String, String>();

                    for (int i = 0; i < numData; i++) {
                        try {
                            slider_studio = response.getJSONObject(i);
                            gambar_studio[i] = slider_studio.getString("gambar");
                            nama_studio[i] = slider_studio.getString("nama");
                            url_maps.put(nama_studio[i], "http://cloudofoasis.com/api/Ivan/slider_studio/" + gambar_studio[i]);
                        } catch (JSONException je) {
                            Toast.makeText(Detail.this, "JSON ERROR", Toast.LENGTH_SHORT).show();
                        }
                    }
                    for (String name : url_maps.keySet()) {
                        TextSliderView textSliderView = new TextSliderView(Detail.this);
                        textSliderView.description(name).image(url_maps.get(name)).setScaleType(BaseSliderView.ScaleType.Fit);
                        textSliderView.bundle(new Bundle());
                        textSliderView.getBundle().putString("extra", name);

                        mDemoSlider.addSlider(textSliderView);
                        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
                        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                        mDemoSlider.setDuration(4000);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(this).add(request);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, MapsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void callButton(){

        callPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert = new AlertDialog.Builder(Detail.this);
                alert.setMessage("call " + callSave + " ?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel: " + callSave));
                        try{
                            startActivity(intent);

                        } catch (SecurityException e){
                            Toast.makeText(Detail.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alert1 = alert.create();
                alert1.show();
            }
        });
    }

    private void sendMessage(){
        callShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        String shareBody = namaStudio + "\n\n"
                                + " "
                                + "Alamat:" + "\n" + alamat + "\n\n"
                                + " "
                                + "Jam Operasional:" + "\n" + jam + "\n\n"
                                + " "
                                + "Harga:" + "\n" + harga + "\n\n"
                                + " "
                                + "Alat Musik:" + "\n" + alatmusik + "\n\n"
                                + " "
                                + "No Telepon:" + "\n" + callSave + "\n\n"
                                + " "
                                + "Update Terakhir:" + "\n" + lastUpdate + "\n\n"
                                + " "
                                + "Lokasi Studio Musik:" + "\n" +
                                "http://maps.google.com/?q=" + latitude + "," + longitude + "\n\n"
                                + " "
                                + "'Find Your Music Studio!'";
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(sharingIntent, "Pilih Aplikasi"));
                    }
                });

            }
        });
    }
}
