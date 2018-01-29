package eus.ehu.tta.gurasapp.model;

/**
 * Created by jontx on 07/01/2018.
 */

public interface BusinessInterface {
    String register(String username, String email, String password) throws Exception;

    boolean login(String login, String password) throws Exception;

    Tests getTest(String login) throws Exception;

    Forums getForums(String login, int date) throws Exception;

    void getForumQuestion(String storagePath, String filename) throws Exception;
}
