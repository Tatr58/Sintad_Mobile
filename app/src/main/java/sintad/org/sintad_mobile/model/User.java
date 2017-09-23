package sintad.org.sintad_mobile.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by TTR on 17/09/2017.
 */

public class User {

    @SerializedName("id_user")
    private int idUser;

    @SerializedName("id_empleado")
    private int idEmpleado;

    @SerializedName("id_role")
    private int idRole;

    @SerializedName("nombre")
    private String nombre;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public int getIdRole() {
        return idRole;
    }

    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }
}
