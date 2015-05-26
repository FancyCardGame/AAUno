package at.fancycardgame.aauno;

/**
 * Created by Thomas on 26.05.2015.
 */
public class User {

    private static String username;
    private static String email;
    private static String pwd;
    private static Boolean loggedIn;

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        User.email = email;
    }

    public static void login() {
        User.loggedIn = true;
    }

    public static void logout() {
        User.loggedIn = false;
    }

    public static String getPwd() {
        return pwd;
    }

    public static void setPwd(String pwd) {
        User.pwd = pwd;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        User.username = username;
    }
}
