package net.zebra.ontrack.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Zeb on 6/6/2017.
 */

public class UserManager {
    private static HashMap<String, User> users = new HashMap<String, User>();
    private static String currentUser;

    public static String createUser(String name){
        if (users.get(name) == null){
            users.put(name, new User(name));
            return "Created!";
        }
        else if (name.equals("")){
            return "Please specify a name";
        }
        else return "User already exists!";


    }

    public static void addUserMap(HashMap<String, User> u){
        for (int i = 0; i < u.size(); i++){
            users.putAll(u);
        }
    }

    public static void deleteUser(String selec){
        users.remove(selec);
    }

    public static void deleteAllUsers() {
        users.clear();
    }

    public static void deleteCurrentUser(){
        users.remove(getCurrentUser().getName());
    }

    public static void setCurrentUser(String selec){
        currentUser = selec;
    }

    public static User getCurrentUser(){
        return users.get(currentUser);
    }

    public static ArrayList<User> getUserList(){
        return new ArrayList<User>(users.values());
    }

    public static HashMap<String, User> getUserMap(){
        return users;
    }

    public static ArrayList<String> getUserNames(){
        return new ArrayList<String>(users.keySet());
    }

}
