package sintad.org.sintad_mobile.model;

import java.util.Date;

/**
 * Created by TTR on 21/09/2017.
 */

public class ServiceOrder {
    private String idOrden;
    private Date fechaOrden;
    private String ruc;
    private String nombreCliente;
    private String tipoServicio;

    public ServiceOrder(String idOrden, Date fechaOrden, String ruc, String nombreCliente, String tipoServicio) {
        this.idOrden = idOrden;
        this.fechaOrden = fechaOrden;
        this.ruc = ruc;
        this.nombreCliente = nombreCliente;
        this.tipoServicio = tipoServicio;
    }

    public String getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(String idOrden) {
        this.idOrden = idOrden;
    }

    public Date getFechaOrden() {
        return fechaOrden;
    }

    public void setFechaOrden(Date fechaOrden) {
        this.fechaOrden = fechaOrden;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }
}
