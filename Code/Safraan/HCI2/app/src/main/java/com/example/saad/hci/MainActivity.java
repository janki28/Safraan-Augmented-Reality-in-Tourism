package com.example.saad.hci;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Objects;

public class MainActivity extends Activity implements BaseSliderView.OnSliderClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    Class_MyApplication app;
    SliderLayout mDemoSliderPopular;
    int backButtonCount;
    boolean displaySearch;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Location mCurrentLocation;
    LocationRequest mLocationRequest;
    private static final int LOCATION_PERMISSION_CODE = 100;
    public  String permission;
    public int requestCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        backButtonCount = 0;
        displaySearch = false;

        // reference the MyApplication variable to access global variables.
        app = (Class_MyApplication) getApplication();

        // Slider
        mDemoSliderPopular = findViewById(R.id.main_slider);
        setupSlider();
        PagerIndicator pagerIndicator = findViewById(R.id.custom_indicator);
        mDemoSliderPopular.setCustomIndicator(pagerIndicator);
        mDemoSliderPopular.setDuration(3);
        mDemoSliderPopular.setCurrentPosition(app.all_HeritageSites.size() - 1);

        // Search Bar
        setupSearch();

        // Title
        TextView textView = findViewById(R.id.main_title);
        textView.setText(R.string.Safraan);

        // GPS
        buildGoogleApiClient();
        createLocationRequest();

        //PERMISSIONS
        checkPermission("android.permission.ACCESS_FINE_LOCATION",200);
    }

    public void setupSlider() {
        for (Class_HeritageSite heritageSite : app.all_HeritageSites) {
            int image_id = getResources().getIdentifier(heritageSite.getImageHor(), "drawable", getPackageName());
            Class_MyTextSliderView textSliderView = new Class_MyTextSliderView(this);
            textSliderView.description(heritageSite.getName()).image(image_id).setOnSliderClickListener(this);
            mDemoSliderPopular.addSlider(textSliderView);
        }
    }

    @Override
    public void onSliderClick(BaseSliderView baseSliderView) {
        Toast.makeText(this, baseSliderView.getDescription(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, SiteDisplayActivity.class);
        intent.putExtra("NAME", baseSliderView.getDescription());
        startActivity(intent);
        mDemoSliderPopular.stopAutoCycle();
    }

    public void setupSearch() {
        AutoCompleteTextView searchbar = findViewById(R.id.main_search);
        String[] s = new String[app.all_HeritageSites.size()];

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, app.all_searchables.toArray(s));
        searchbar.setAdapter(adapter);
        searchbar.setOnEditorActionListener(new AutoCompleteTextView.OnEditorActionListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch(null);
                    return true;
                }
                return false;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void performSearch(View v) {
        if (!displaySearch) {
            displaySearch = true;
            AutoCompleteTextView textView = findViewById(R.id.main_search);
            textView.setVisibility(View.VISIBLE);
            (findViewById(R.id.main_title)).setVisibility(View.INVISIBLE);
            if (textView.requestFocus()) {
                ((InputMethodManager) Objects.requireNonNull(getSystemService(Context.INPUT_METHOD_SERVICE))).showSoftInput(textView, InputMethodManager.SHOW_IMPLICIT);
            }
        } else {
            AutoCompleteTextView textView = findViewById(R.id.main_search);
            String text = textView.getText().toString();
            if (app.all_searchables.contains(text)) {
                Intent intent;
                if (app.isTag(text)) {
                    intent = new Intent(this, ListDisplayActivity.class);
                    intent.putExtra("MODE", "SEARCH");
                    intent.putExtra("NAME", text);
                    Toast.makeText(this, "Tag Found", Toast.LENGTH_SHORT).show();
                } else if (app.isTrail(text)) {
                    intent = new Intent(this, TrailDisplayActivity.class);
                    intent.putExtra("NAME", text);
                    Toast.makeText(this, "Trail Found", Toast.LENGTH_SHORT).show();
                } else {
                    intent = new Intent(this, SiteDisplayActivity.class);
                    intent.putExtra("NAME", text);
                    Toast.makeText(this, "Site Found", Toast.LENGTH_SHORT).show();
                }
                startActivity(intent);
                mDemoSliderPopular.stopAutoCycle();
            } else {
                Toast.makeText(this, "Does Not Exist", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void redirectNearest(View v) {
        Toast.makeText(this, "Redirected to Nearest Page", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ListDisplayActivity.class);
        intent.putExtra("MODE", "NEAREST");
        startActivity(intent);
        mDemoSliderPopular.stopAutoCycle();
    }

    public void redirectPopular(View v) {
        Toast.makeText(this, "Redirected to Popular Page", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ListDisplayActivity.class);
        intent.putExtra("MODE", "POPULAR");
        startActivity(intent);
        mDemoSliderPopular.stopAutoCycle();
    }

    public void redirectTrails(View v) {
        Toast.makeText(this, "Redirected to Trails Page", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ListDisplayActivity.class);
        intent.putExtra("MODE", "TRAIL");
        startActivity(intent);
        mDemoSliderPopular.stopAutoCycle();
    }

    public void redirectFavourites(View v) {
        Intent intent = new Intent(this, ListDisplayActivity.class);
        intent.putExtra("MODE", "FAVOURITES");
        startActivity(intent);
        mDemoSliderPopular.stopAutoCycle();
    }

    public void goToTrail(View v) {
        TextView textView = v.findViewById(R.id.imagebox_text);
        Toast.makeText(this, "Redirected to " + textView.getText(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, TrailDisplayActivity.class);
        intent.putExtra("NAME", textView.getText());
        startActivity(intent);
        mDemoSliderPopular.stopAutoCycle();
    }

    public void goHome(View v) {
        hideSearchBar();
    }

    public void performAugmented(View v) {
        Intent intent = new Intent(this,SampleCamActivity.class);
        startActivity(intent);
        mDemoSliderPopular.stopAutoCycle();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (backButtonCount >= 1) {
            saveFavourites();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        backButtonCount = 0;
        mDemoSliderPopular.startAutoCycle();
        hideSearchBar();
        mGoogleApiClient.connect();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void hideSearchBar() {
        displaySearch = false;
        AutoCompleteTextView textView = findViewById(R.id.main_search);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ((InputMethodManager) Objects.requireNonNull(getSystemService(Context.INPUT_METHOD_SERVICE))).hideSoftInputFromWindow(textView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        textView.setVisibility(View.GONE);
        textView.setText("");
        findViewById(R.id.main_title).setVisibility(View.VISIBLE);
    }

    public void saveFavourites() {
        SharedPreferences.Editor e = app.sharedPreferences.edit();
        e.clear();
        e.apply();
        e.putInt("num", app.all_favourites.size());
        for (int i = 0; i < app.all_favourites.size(); i++) {
            String num = String.format("%s", i);
            e.putString(num, app.all_favourites.get(i));
        }
        e.commit();
    }

    public void checkPermission( String  permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission)
                == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(MainActivity.this,"Thanks Permission granted",Toast.LENGTH_SHORT).show();

            // Requesting the permission
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{permission},
                    requestCode);


        } else {
            Toast.makeText(MainActivity.this,"Permission already granted",Toast.LENGTH_SHORT).show();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super
                .onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);

        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this,"Camera Permission Granted",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this,"Camera Permission Denied",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void startLocationUpdates() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODOoo: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        app.setLoc(new float[]{(float) mLastLocation.getLatitude(), (float) mLastLocation.getLongitude()});
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODOoo: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            app.setLoc(new float[] {(float)mLastLocation.getLatitude(), (float)mLastLocation.getLongitude()});
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Error: Could not connect to GPS", Toast.LENGTH_LONG).show();
    }
}