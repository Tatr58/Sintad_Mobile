package sintad.org.sintad_mobile.util;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sintad.org.sintad_mobile.model.Route;

/**
 * Created by TTR on 7/10/2017.
 */

public class LocationUtils {
    public static Boolean isLocationFree(LatLng marker_destino, ArrayList<MarkerOptions> mMarkerArray) {
        Boolean exists = true;
        for (MarkerOptions marker: mMarkerArray) {
            if (marker_destino.latitude == marker.getPosition().latitude &&
                    marker_destino.longitude == marker.getPosition().longitude) {
                exists = false;
            }
        }
        return exists;
    }


    public static  Boolean isSameOrigin(List<Route> routeList) {

        final Set<LatLng> set_routes = new HashSet<>();

        for (Route route : routeList) {
            LatLng tmp_latlng = new LatLng(route.getLatitud_origen(), route.getLongitud_origen());
            if (!set_routes.add(tmp_latlng)) {
                return true;
            }
        }
        return false;
    }

    public static String getUrl(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        String output = "json";
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
    }

    public static String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data);
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}
