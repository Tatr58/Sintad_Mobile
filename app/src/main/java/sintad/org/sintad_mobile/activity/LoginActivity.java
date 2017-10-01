package sintad.org.sintad_mobile.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sintad.org.sintad_mobile.R;
import sintad.org.sintad_mobile.interface_api.AuthAPI;
import sintad.org.sintad_mobile.model.User;
import sintad.org.sintad_mobile.util.APIClient;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "LoginActivity";
    EditText txvUsuario;
    EditText txvPassword;
    Button btnIngresar;
    String user_value;
    String password_value;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txvUsuario =(EditText) this.findViewById(R.id.ediText1Act1);
        txvPassword =(EditText) this.findViewById(R.id.ediText2Act1);
        btnIngresar = (Button) this.findViewById(R.id.btn1Act1);
        progress = (ProgressBar) this.findViewById(R.id.progressBarLogin);
        btnIngresar.setEnabled(true);

        progress.setVisibility(View.GONE);
        btnIngresar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1Act1:
                user_value = txvUsuario.getText().toString();
                password_value = txvPassword.getText().toString();

                if(user_value.matches("") || password_value.matches("")){
                    Toast.makeText(getBaseContext(),"Ingreasr los campos", Toast.LENGTH_LONG).show();
                } else {
                    btnIngresar.setEnabled(false);
                    progress.setVisibility(View.VISIBLE);
                    loginProcessWithRetrofit(user_value, password_value);
                }
                break;
        }

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        overridePendingTransition(R.animator.activity_open_scale,R.animator.activity_close_translate);
    }

    private void loginProcessWithRetrofit(final String user, String password){
        AuthAPI mApiService = APIClient.getClient().create(AuthAPI.class);
        Call<List<User>> mService = mApiService.authLogin(user, password);
        mService.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                progress.setVisibility(View.GONE);
                btnIngresar.setEnabled(true);
                if (response.isSuccessful()){
                    Log.d(TAG, response.body().toString());
                    int id_user;
                    String nombre_user;
                    List<User> mLoginObject = response.body();
                    if (mLoginObject.size() > 0) {
                        id_user = mLoginObject.get(0).getIdUser();
                        nombre_user = mLoginObject.get(0).getNombre();
                        Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                        intent.putExtra("user_id", id_user);
                        intent.putExtra("nombre_user", nombre_user);
                        ActivityOptions options =
                                ActivityOptions.makeCustomAnimation(LoginActivity.this,
                                        R.animator.activity_open_translate,R.animator.activity_close_scale);
                        LoginActivity.this.startActivity(intent, options.toBundle());
                    } else {
                        Toast.makeText(getBaseContext(),"Usuario o Contraseña incorrectos", Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {Log.e(TAG, response.errorBody().string());} catch (IOException ignored) {}
                    Toast.makeText(getBaseContext(),"Usuario no Existente", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                btnIngresar.setEnabled(true);
                progress.setVisibility(View.GONE);
                Log.e(TAG, t.toString());
                Toast.makeText(getBaseContext(),"Error de Conexión", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }
}
