package net.zebra.ontrack.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Zeb on 6/6/2017.
 */

public class UserHandler {
    private static Map<String, User> users = new HashMap<String, User>();
    private static String currentUser;

    public static void createUser(String name){
        users.put(name, new User(name));
    }

    public static void deleteUser(String selec){
        users.remove(selec);
    }
    public static void setCurrentUser(String selec){
        currentUser = selec.toLowerCase();
    }

    public static User getCurrentUser(){
        return users.get(currentUser);
    }

    public static ArrayList<User> getUserList(){
        return new ArrayList<User>(users.values());
    }

    public static ArrayList<String> getUserNames(){
        return new ArrayList<String>(users.keySet());
    }

}
