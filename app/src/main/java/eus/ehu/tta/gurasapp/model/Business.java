package eus.ehu.tta.gurasapp.model;

/**
 * Created by jontx on 07/01/2018.
 */

public class Business implements BusinessInterface {

    @Override
    public String register(String username, String email, String password) {
        String login = null;
        if (username == null || email == null || password == null)
            return null;

        if (password.compareTo("1234") == 0)
            return "jj0";
        else
            return null;
    }

    @Override
    public boolean login(String login, String password) {

        return !(login == null || password == null) && login.compareTo("jj0") == 0 && password.compareTo("1234") == 0;

    }
}
