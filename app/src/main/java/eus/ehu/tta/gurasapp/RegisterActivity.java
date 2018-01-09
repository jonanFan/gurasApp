package eus.ehu.tta.gurasapp;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }


    public void register(View view) {
        String username = ((EditText) findViewById(R.id.registerName)).getText().toString();
        String email = ((EditText) findViewById(R.id.registerEmail)).getText().toString();
        String pass = ((EditText) findViewById(R.id.registerPassword)).getText().toString();


        if (username != null && !username.isEmpty() && email != null && !email.isEmpty() && pass != null && !pass.isEmpty()) {
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                String login = business.register(username, email, pass);
                if (login != null) {
                    data.putUsername(login);
                    startBaseActivity(LoginActivity.class);
                }
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
