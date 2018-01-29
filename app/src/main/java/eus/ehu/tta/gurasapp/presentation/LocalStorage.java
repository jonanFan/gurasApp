package eus.ehu.tta.gurasapp.presentation;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import eus.ehu.tta.gurasapp.model.Forums;

/**
 * Created by jontx on 28/01/2018.
 */

public class LocalStorage {
    protected final static String GURASAPP_LOCALSTORATE_TAG = "gurasAppLocalStorageTag";
    private final static String FORUMS = "forums";

    public static void putForums(Context context, Forums forums) {
        try {
            FileOutputStream fos = context.openFileOutput(FORUMS, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(forums);
            os.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Forums getForums(Context context) {
        Forums forums = null;

        try {
            FileInputStream fis = context.openFileInput(FORUMS);
            ObjectInputStream is = new ObjectInputStream(fis);
            forums = (Forums) is.readObject();
            is.close();
            fis.close();
        } catch (FileNotFoundException e) {
            Log.d(GURASAPP_LOCALSTORATE_TAG, "No existe el fichero de forums");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return forums;
    }

    public static void deleteForums(Context context) {
        File file = context.getFileStreamPath(FORUMS);
        if (file != null && file.exists()) {
            Log.d(GURASAPP_LOCALSTORATE_TAG, "Se ha borrado el fichero de forums");
            context.deleteFile(FORUMS);
        }
    }
}
