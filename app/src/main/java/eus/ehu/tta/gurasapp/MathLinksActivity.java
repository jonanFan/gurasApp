package eus.ehu.tta.gurasapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import eus.ehu.tta.gurasapp.model.Tests;
import eus.ehu.tta.gurasapp.presentation.ProgressTask;

public class MathLinksActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_links);

        TextView textView = (TextView) findViewById(R.id.linkTitle);
        textView.setText(R.string.subject_1_topic);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.playLayout);

        Button buttonTest = new Button(this);
        buttonTest.setText(R.string.test);
        buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toTest(v);
            }
        });
        buttonTest.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);


        Button buttonMultiplos = new Button(this);
        buttonMultiplos.setText(R.string.multiples);
        buttonMultiplos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMultiplos(v);
            }
        });
        buttonMultiplos.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

        linearLayout.addView(buttonTest);
        linearLayout.addView(buttonMultiplos);
    }

    public void toTest(View view) {


        new ProgressTask<Tests>(this, getString(R.string.loading_test)) {
            @Override
            protected Tests background() throws Exception {
                return business.getTest(data.getUsername());
            }

            @Override
            protected void onFinish(Tests result) {
                if (result != null) {
                    data.putTests(result);
                    startBaseActivity(TestActivity.class);
                } else
                    Toast.makeText(context, getString(R.string.no_test), Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    public void toMultiplos(View view) {
        startBaseActivity(MultiplesActivity.class);
    }


    public void toLink1(View view) {
        openNav(getString(R.string.link1_math));
    }

    public void toLink2(View view) {
        openNav(getString(R.string.link2_math));
    }

    private void openNav(String url) {
        if (URLUtil.isHttpsUrl(url) || URLUtil.isHttpUrl(url)) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
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
