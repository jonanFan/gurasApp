package eus.ehu.tta.gurasapp.presentation;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Created by jontx on 12/01/2018.
 */

public abstract class ProgressTask<T> extends AsyncTask<Void, Void, T> {

    protected final Context context;
    private ProgressDialog dialog = null;
    private Exception e;


    public ProgressTask(Context context, String dialogText) {
        this.context = context;
        if (dialogText != null && !dialogText.isEmpty()) {
            dialog = new ProgressDialog(context);
            dialog.setMessage(dialogText);
        }
    }

    @Override
    protected void onPreExecute() {
        if (dialog != null)
            dialog.show();
    }

    @Override
    protected T doInBackground(Void... voids) {
        T result = null;

        try {
            result = background();
        } catch (Exception e) {
            this.e = e;
        }

        return result;
    }

    @Override
    protected void onPostExecute(T result) {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();

        if (e != null)
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        else
            onFinish(result);
    }

    @Override
    protected void onCancelled() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    protected abstract T background() throws Exception;

    protected abstract void onFinish(T result);
}
