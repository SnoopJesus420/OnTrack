package net.zebra.ontrack.tools;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Zeb on 6/6/2017.
 */

public class UserManager {
    private static HashMap<String, User> users = new HashMap<String, User>();
    private static String currentUser;
    private static String selectedUser;
    private static int checkForValidUser;

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

    public static String deleteSelectedUser(){
        String oldName;
        if (users.size() > 0) {
            oldName = getSelectedUser().getName();
            users.remove(oldName);
            return "Deleted "+ oldName;
        }
        else
            return "No users to delete!";


    }

    public static void setCurrentUser(String selec){
        currentUser = selec;
    }

    public static void setCurrentUser(int pos){
        if (users.size() > 0) {
            currentUser = getUserList().get(pos).getName();
        }
    }

    public static void selectUser(String selec){
        for (int i = 0; i < getUserList().size(); i++){
            if (selec.equals(getUserNames().get(i))){
                checkForValidUser++;
            }
        }
        if (checkForValidUser > 0 && users.size() > 0) {
            selectedUser = selec;
            checkForValidUser = 0;
        }
    }
    public static void selectUser(int pos){
        if (users.size() > 0) {
            selectedUser = getUserList().get(pos).getName();
        }
    }

    public static User getCurrentUser(){
        return users.get(currentUser);
    }

    public static User getSelectedUser(){
        return users.get(selectedUser);
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
