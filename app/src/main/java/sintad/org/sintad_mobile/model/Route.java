package sintad.org.sintad_mobile.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by TTR on 30/09/2017.
 */

public class Route {

    @SerializedName("viaje")
    private int id_viaje;

    @SerializedName("lat_P")
    private double lattitud_origen;

    @SerializedName("lon_P")
    private double longitud_origen;

    @SerializedName("lat_L")
    private double latitud_fin;

    @SerializedName("lon_L")
    private double longitud_fin;

    public int getId_viaje() {
        return id_viaje;
    }

    public void setId_viaje(int id_viaje) {
        this.id_viaje = id_viaje;
    }

    public double getLattitud_origen() {
        return lattitud_origen;
    }

    public void setLattitud_origen(double lattitud_origen) {
        this.lattitud_origen = lattitud_origen;
    }

    public double getLongitud_origen() {
        return longitud_origen;
    }

    public void setLongitud_origen(double longitud_origen) {
        this.longitud_origen = longitud_origen;
    }

    public double getLatitud_fin() {
        return latitud_fin;
    }

    public void setLatitud_fin(double latitud_fin) {
        this.latitud_fin = latitud_fin;
    }

    public double getLongitud_fin() {
        return longitud_fin;
    }

    public void setLongitud_fin(double longitud_fin) {
        this.longitud_fin = longitud_fin;
    }
}
