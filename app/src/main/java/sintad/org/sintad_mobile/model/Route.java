package sintad.org.sintad_mobile.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by TTR on 30/09/2017.
 */

public class Route {

    @SerializedName("viaje")
    private int id_viaje;

    @SerializedName("lat_P")
    private double latitud_origen;

    @SerializedName("lon_P")
    private double longitud_origen;

    @SerializedName("depoNom_P")
    private String nombre_depo_origen;

    @SerializedName("lat_L")
    private double latitud_fin;

    @SerializedName("lon_L")
    private double longitud_fin;

    @SerializedName("depoNom_L")
    private String nombre_depo_fin;

    public int getId_viaje() {
        return id_viaje;
    }

    public void setId_viaje(int id_viaje) {
        this.id_viaje = id_viaje;
    }

    public double getLatitud_origen() {
        return latitud_origen;
    }

    public void setLatitud_origen(double latitud_origen) {
        this.latitud_origen = latitud_origen;
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

    public String getNombre_depo_origen() {
        return nombre_depo_origen;
    }

    public void setNombre_depo_origen(String nombre_depo_origen) {
        this.nombre_depo_origen = nombre_depo_origen;
    }

    public String getNombre_depo_fin() {
        return nombre_depo_fin;
    }

    public void setNombre_depo_fin(String nombre_depo_fin) {
        this.nombre_depo_fin = nombre_depo_fin;
    }
}
