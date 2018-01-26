package eus.ehu.tta.gurasapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import eus.ehu.tta.gurasapp.presentation.Preferences;
import eus.ehu.tta.gurasapp.presentation.ProgressTask;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String username = data.getUsername();

        if (username != null) {
            ((EditText) findViewById(R.id.loginUsername)).setText(username);
        }
    }

    public void login(View view) {
        final String login = ((EditText) findViewById(R.id.loginUsername)).getText().toString();
        final String pass = ((EditText) findViewById(R.id.loginPassword)).getText().toString();
        final Boolean checked = ((CheckBox) findViewById(R.id.loginCheckbox)).isChecked();

        if (!login.isEmpty() && !pass.isEmpty()) {

            new ProgressTask<Boolean>(this, getString(R.string.wait_login)) {
                @Override
                protected Boolean background() throws Exception {
                    return business.login(login, pass);
                }

                @Override
                protected void onFinish(Boolean result) {
                    if (result) {
                        if (checked) {
                            Preferences.setLogin(context, login);
                            Preferences.setPassword(context, pass);
                        }

                        data.putUsername(login);
                        startBaseActivityWithFlags(MenuActivity.class, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //Ñapa para hacer que cuando le des a tras en el menu se salga la app
                    } else
                        Toast.makeText(context, getString(R.string.bad_login), Toast.LENGTH_SHORT).show();
                }
            }.execute();

        } else
            Toast.makeText(this, R.string.not_filled, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void changeLangToEs(View view) {
        super.changeLangToEs(view);
    }

    @Override
    public void changeLangToEu(View view) {
        super.changeLangToEu(view);
    }
}
