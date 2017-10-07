package sintad.org.sintad_mobile.activity;
import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sintad.org.sintad_mobile.R;
import sintad.org.sintad_mobile.util.DataParser;
import sintad.org.sintad_mobile.util.LocationUtils;
import com.google.maps.android.SphericalUtil;

/**
 * Created by TTR on 1/10/2017.
 */

public class RouteActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = RouteActivity.class.getSimpleName();
    private static final int padding = 200;
    private static final double min_distance = 20;

    GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;
    ArrayList<MarkerOptions> mMarkerArray = new ArrayList<>();
    String id_order;
    Boolean is_principal = false;
    ArrayList<Polyline> principal_line = new ArrayList<>();
    BottomSheetBehavior bottomRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_onprogress);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.punto_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        bottomRoute = BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayout));
        bottomRoute.setState(BottomSheetBehavior.STATE_HIDDEN);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMarkerArray = getIntent().getParcelableArrayListExtra("markers");
        id_order = getIntent().getStringExtra("nro_orden");
        int counter = 0;
        is_principal = false;

        for (MarkerOptions marker: mMarkerArray) {
            marker.position(marker.getPosition());
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            mMap.addMarker(marker);
        }

        for (MarkerOptions marker: mMarkerArray.subList(1, mMarkerArray.size())) {
            MarkerOptions marker_ini = mMarkerArray.get(counter);
            LatLng origen_ruta = new LatLng(marker_ini.getPosition().latitude,
                    marker_ini.getPosition().longitude);
            LatLng destino_ruta = new LatLng(marker.getPosition().latitude,
                    marker.getPosition().longitude);

            String url = LocationUtils.getUrl(origen_ruta, destino_ruta);
            Log.d("loadRoute", url);
            FetchUrl FetchUrl = new RouteActivity.FetchUrl();
            FetchUrl.execute(url);
            counter++;
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }

    }

    LocationCallback mLocationCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                Log.d(TAG, "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                onLocationChanged(locationResult.getLastLocation());
            }
        }
    };

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        drawPrincipalLine(location);
    }

    public void drawPrincipalLine(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        double distance_mc;

        is_principal = true;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Mi Ubicación");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.location));
        builder.include(latLng);

        mCurrLocationMarker = mMap.addMarker(markerOptions);
        MarkerOptions marker_dest = mMarkerArray.get(0);

        LatLng destino_activo = new LatLng(marker_dest.getPosition().latitude,
                marker_dest.getPosition().longitude);
        builder.include(destino_activo);

        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.moveCamera(cu);

        distance_mc = getDistance(latLng,destino_activo);
        Log.d(TAG + " Active Distance", " " + distance_mc);

        if(distance_mc != 0 && distance_mc < min_distance) {
            Toast info_ruta = Toast.makeText(getBaseContext(),"Se encuentra cerca del punto destino, registrar " +
                    "etapa", Toast.LENGTH_LONG);
            info_ruta.setGravity(Gravity.CENTER, 0,0);
            info_ruta.show();
            bottomRoute.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            bottomRoute.setState(BottomSheetBehavior.STATE_HIDDEN);
        }

        String url = LocationUtils.getUrl(latLng, destino_activo);
        Log.d("loadRoute Activa", url);
        FetchUrl FetchUrl = new RouteActivity.FetchUrl();
        FetchUrl.execute(url);
    }

    private double getDistance(LatLng inicio, LatLng fin) {
        double distance = SphericalUtil.computeDistanceBetween(inicio, fin);
        return distance;
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Permisos de Localización Requeridas")
                        .setMessage("Esta aplicación necesita acceso a su ubicación")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(RouteActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }


    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = LocationUtils.downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new RouteActivity.ParserTask();
            parserTask.execute(result);

        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask",jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                routes = parser.parse(jObject);
                Log.d("ParserTask","Executing routes");
                Log.d("ParserTask",routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask",e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (Polyline line: principal_line) {
                    line.remove();
                }

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(10);

                if (is_principal) {
                    lineOptions.color(Color.RED);
                } else {
                    lineOptions.color(Color.BLUE);
                }

                lineOptions.geodesic(true);

                Log.d("onPostExecute","onPostExecute lineoptions decoded");

            }

            if(lineOptions != null) {
                if(is_principal) {
                    principal_line.add(mMap.addPolyline(lineOptions));
                    is_principal = false;
                } else {
                    mMap.addPolyline(lineOptions);
                }
            }
            else {
                Log.d("onPostExecute","without Polylines drawn");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                        mMap.getUiSettings().setZoomControlsEnabled(true);
                    }

                } else {

                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}
