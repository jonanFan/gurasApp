package eus.ehu.tta.gurasapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class MultiplesActivity extends BaseActivity implements View.OnClickListener {

    private final static int MIN_VALUE = 1;
    private final static int BLUE_PLAYER = 0;
    private final static int RED_PLAYER = 1;
    private int redTeamValue;
    private int blueTeamValue;
    private int playerTurn;
    private int boardSize;
    private int lastValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiples);
    }

    public void chooseBoard(View view) {
        Log.d(GURASAPP_ACTIVITY_TAG, "Button is " + ((Button) view).getText());
        boardSize = Character.getNumericValue(((Button) view).getText().charAt(0));

        findViewById(R.id.boardSelector).setVisibility(View.GONE);
        findViewById(R.id.languageLayout).setVisibility(View.GONE);
        generateBoard(boardSize);
    }

    private void generateBoard(int size) {
        GridLayout layout = new GridLayout(this);

        LinearLayout.LayoutParams gridParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        gridParams.setMargins(0, (int) (50 * getResources().getDisplayMetrics().density), 0, 0);
        layout.setLayoutParams(gridParams);
        layout.setColumnCount(size);
        layout.setRowCount(size);
        // layout.setOrientation(GridLayout.HORIZONTAL);

        Random random = new Random();

        int value = MIN_VALUE;
        lastValue = random.nextInt((size - MIN_VALUE) + 1) + MIN_VALUE;
        playerTurn = random.nextInt(2);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Button boardButton = new Button(this);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 1f), GridLayout.spec(GridLayout.UNDEFINED, 1f));
                params.width = 0;
                //  params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                //  params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);

                boardButton.setLayoutParams(params);
                boardButton.setText(String.valueOf(value++));
                boardButton.setOnClickListener(this);
                layout.addView(boardButton);
            }
        }

        //layout.setVisibility(View.VISIBLE);
        ((LinearLayout) findViewById(R.id.multiplesContent)).addView(layout);

        TextView title = new TextView(this);
        title.setText(R.string.start_multiples_title);
        title.setGravity(Gravity.CENTER);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setCustomTitle(title);
        alertDialog.setMessage(String.format("%s: %s\n%s: %s", getResources().getString(R.string.start_multiples_player), getResources().getString(playerTurn == RED_PLAYER ? R.string.red : R.string.blue), getResources().getString(R.string.start_multiples_number), String.valueOf(lastValue)));
        alertDialog.setNeutralButton("OK", null);
        alertDialog.setCancelable(false);
        AlertDialog dialog = alertDialog.show();

        ((TextView) dialog.findViewById(android.R.id.message)).setGravity(Gravity.CENTER);
        Button button = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        button.setGravity(Gravity.CENTER_HORIZONTAL);
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


        if (value % lastValue == 0 || lastValue % value == 0) {
            //aumentar el marcador del pavo y cambiar de pavo, y pintar el boton
            if (playerTurn == RED_PLAYER) {

                v.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                ((TextView) findViewById(R.id.redTeam)).setText(String.valueOf(++redTeamValue));
            } else {
                v.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
                ((TextView) findViewById(R.id.blueTeam)).setText(String.valueOf(++blueTeamValue));
            }

            playerTurn = (playerTurn == RED_PLAYER ? BLUE_PLAYER : RED_PLAYER);

            lastValue = value;
            v.setEnabled(false);
        } else {
            TextView title = new TextView(this);
            title.setText(getResources().getString(R.string.game_over));
            title.setGravity(Gravity.CENTER);

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setCustomTitle(title);
            if (redTeamValue == blueTeamValue) {
                if (redTeamValue == 0) {
                    alertDialog.setMessage(getResources().getString(R.string.multiples_try_again));
                } else
                    alertDialog.setMessage(getResources().getString(R.string.tie));
            } else {
                alertDialog.setMessage(getResources().getString(R.string.winner) + ": " + getResources().getString((redTeamValue > blueTeamValue ? R.string.red : R.string.blue)));
            }
            alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            alertDialog.setCancelable(false);
            AlertDialog dialog = alertDialog.show();

            ((TextView) dialog.findViewById(android.R.id.message)).setGravity(Gravity.CENTER);
            Button button = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
            button.setGravity(Gravity.CENTER_HORIZONTAL);

            /*GridLayout layout=(GridLayout)v.getParent();
            for (int i = 0; i < layout.getChildCount(); i++) {
                View child = layout.getChildAt(i);
                child.setEnabled(false);
            }*/
        }

    }
}
