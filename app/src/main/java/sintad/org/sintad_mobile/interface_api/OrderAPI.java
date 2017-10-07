package sintad.org.sintad_mobile.interface_api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import sintad.org.sintad_mobile.model.Route;
import sintad.org.sintad_mobile.model.ServiceOrder;

/**
 * Created by TTR on 21/09/2017.
 */

public interface OrderAPI {
    @GET("/service_order/getList/{id_empleado}")
    Call<List<ServiceOrder>> getList(@Path("id_empleado") int id_empleado);

    @GET("/service_order_deta/getRoute/{id_order}")
    Call<List<Route>> getRouteList(@Path("id_order") String id_order);

    @FormUrlEncoded
    @POST("/service_order_deta/registerStar")
    Call<Route> saveStage(@Field("id_order") String id_order,
                                @Field("latitud") double latitud,
                                @Field("longitud") double longitud,
                                @Field("tipo") int tipo);
}
