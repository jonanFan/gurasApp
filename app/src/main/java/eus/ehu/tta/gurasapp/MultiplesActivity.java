package eus.ehu.tta.gurasapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MultiplesActivity extends BaseActivity implements View.OnClickListener {

    private final static int BLUE_PLAYER = 0;
    private final static int RED_PLAYER = 1;
    private int redTeamValue;
    private int blueTeamValue;
    private int playerTurn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiples);


        TextView redTeam = (TextView) findViewById(R.id.redTeam);
        TextView blueTeam = (TextView) findViewById(R.id.blueTeam);

        redTeam.setText(String.valueOf(redTeamValue));
        blueTeam.setText(String.valueOf(blueTeamValue));
    }

    public void chooseBoard(View view) {
        Log.d(GURASAPP_ACTIVITY_TAG, "Button is " + ((Button) view).getText());
        int size = Character.getNumericValue(((Button) view).getText().charAt(0));

        generateBoard(size);
        findViewById(R.id.boardSelector).setVisibility(View.GONE);
    }

    private void generateBoard(int size) {
        GridLayout layout = new GridLayout(this);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(params);
        layout.setColumnCount(size);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Button boardButton = new Button(this);
                boardButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                boardButton.setText(String.valueOf(j));
                boardButton.setOnClickListener(this);
                layout.addView(boardButton);
            }
        }

        layout.setVisibility(View.VISIBLE);
        ((LinearLayout) findViewById(R.id.multiplesContent)).addView(layout);
    }

    @Override
    public void changeLangToEs(View view) {
        super.changeLangToEs(view);
    }

    @Override
    public void changeLangToEu(View view) {
        super.changeLangToEu(view);
    }

    @Override
    public void onClick(View v) {
        int value = Integer.parseInt(((Button) v).getText().toString());
        v.setEnabled(false);
        Toast.makeText(this, "El valor es " + value, Toast.LENGTH_SHORT).show();

    }
}
