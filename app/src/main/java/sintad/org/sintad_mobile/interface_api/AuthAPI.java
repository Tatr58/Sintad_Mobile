package sintad.org.sintad_mobile.interface_api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import sintad.org.sintad_mobile.model.User;

/**
 * Created by TTR on 17/09/2017.
 */

public interface AuthAPI {
    @GET ("/auth/login/{username}/{password}")
    Call<List<User>> authLogin(@Path("username") String user, @Path("password") String password);
}
