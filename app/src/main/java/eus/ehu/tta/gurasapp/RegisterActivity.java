package eus.ehu.tta.gurasapp;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import eus.ehu.tta.gurasapp.presentation.NetworkChecker;
import eus.ehu.tta.gurasapp.presentation.ProgressTask;

public class RegisterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }


    public void register(View view) {
        final String username = ((EditText) findViewById(R.id.registerName)).getText().toString();
        final String email = ((EditText) findViewById(R.id.registerEmail)).getText().toString();
        final String pass = ((EditText) findViewById(R.id.registerPassword)).getText().toString();


        if (!username.isEmpty() && !email.isEmpty() && !pass.isEmpty()) {
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                int connType = NetworkChecker.getConnType(this);
                if (connType != -1) {

                    if (connType != ConnectivityManager.TYPE_WIFI) {
                        Toast.makeText(this, R.string.no_wifi_warning, Toast.LENGTH_SHORT).show();
                    }

                    new ProgressTask<String>(this, getString(R.string.wait_register)) {
                        @Override
                        protected String background() throws Exception {

                            return business.register(username, email, pass);
                        }

                        @Override
                        protected void onFinish(String result) {
                            if (result != null) {
                                data.putUsername(result);
                                startBaseActivity(LoginActivity.class);
                            } else
                                Toast.makeText(context, R.string.already_user, Toast.LENGTH_SHORT).show();
                        }
                    }.execute();

                } else
                    Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, R.string.incorrect_email, Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, R.string.not_filled, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void changeLangToEu(View view) {
        super.changeLangToEu(view);
    }

    @Override
    public void changeLangToEs(View view) {
        super.changeLangToEs(view);
    }
}
