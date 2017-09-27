package sintad.org.sintad_mobile.activity;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sintad.org.sintad_mobile.R;
import sintad.org.sintad_mobile.adapter.ServiceOrderAdapter;
import sintad.org.sintad_mobile.interface_api.OrderAPI;
import sintad.org.sintad_mobile.model.ServiceOrder;
import sintad.org.sintad_mobile.util.APIClient;

/**
 * Created by TTR on 20/09/2017.
 */

public class WelcomeActivity extends Activity{

    TextView txtUserName;
    ServiceOrderAdapter orderAdapter;
    ListView listView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);

        Intent intent = getIntent();
        int id_empleado = intent.getIntExtra("user_id",0);
        String nombre_user = intent.getStringExtra("nombre_user");

        txtUserName = findViewById(R.id.txtUserName);
        txtUserName.setText(nombre_user);

        getServiceOrderList(id_empleado);
    }

    private void getServiceOrderList(final int id_empleado){
        OrderAPI mApiService = APIClient.getClient().create(OrderAPI.class);
        Call<List<ServiceOrder>> mService = mApiService.getList(id_empleado);
        mService.enqueue(new Callback<List<ServiceOrder>>() {
            @Override
            public void onResponse(Call<List<ServiceOrder>> call, Response<List<ServiceOrder>> response) {
                List<ServiceOrder> mLoginObject = response.body();
                listView = (ListView) findViewById(R.id.lv1List1);
                orderAdapter = new ServiceOrderAdapter(WelcomeActivity.this, R.layout.service_order_rowlayout, mLoginObject);
                listView.setAdapter(orderAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        TextView c = (TextView) findViewById(R.id.txt1RowLay1);
                        String nro_orden = c.getText().toString();
                        Intent intent = new Intent(WelcomeActivity.this, DetailActivity.class);
                        intent.putExtra("nro_ordern", nro_orden);
                        WelcomeActivity.this.startActivity(intent);
                    }
                });
            }
            @Override
            public void onFailure(Call<List<ServiceOrder>> call, Throwable t) {
                call.cancel();
            }
        });
    }
}
