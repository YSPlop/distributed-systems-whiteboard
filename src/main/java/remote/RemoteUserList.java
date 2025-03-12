package remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RemoteUserList extends UnicastRemoteObject implements IRemoteUserList{

    private Lock userLock;

    private String managerName = null;

    private List<String> userNames = null;

    private int userCounter;

    // mapping for user: clientsID list
    private HashMap<String, Integer> usersIDList;

    // whiteboard managers ID
    private String managerID = null;

    // list of candidate users (don't know the purpose of this yet)
    // none of the candidate user functions will be implemented
    private List<String> candidateUsers;

    public RemoteUserList() throws RemoteException{
        userNames = new ArrayList<>();
        userLock = new ReentrantLock();

        usersIDList = new HashMap<>();
        candidateUsers = new ArrayList<>();
        userCounter = 0;
    }


    /*
     *  Add user of username to user list if manager does not exist and if the manager exists then assigns username to
     * manager.
     * */
    synchronized public String registerUser(String userName){

        System.out.println("Register user function entered");
        userLock.lock();

        String userID = String.format("%s (%d)", userName, userCounter);
        userCounter++;
        try{

            if (managerID == null){
                System.out.println(" this is a manager registered");
                managerID = userID;
                setManagerName(managerID);
                addUser(userID);
            }else{
                System.out.println("This is a client registered");
                addUser(userID);
            }

        }catch (RemoteException e){
            System.out.println("UserManager class addUser method Remote Exception: " + e.getMessage());
            userLock.unlock();
        }
        userLock.unlock();
        return userID;

    }

    /*
    * Adds user to a list
    * only called by register
    * */
    synchronized private void addUser(String userName) throws RemoteException {
        userNames.add(userName);
        System.out.println(Arrays.toString(userNames.toArray()));
    }

    synchronized public void removeUser(String userName) throws RemoteException {
        userLock.lock();
        userNames.remove(userName);
        userLock.unlock();
        System.out.println(Arrays.toString(userNames.toArray()));
    }

    synchronized public void removeAll() throws RemoteException {
        userLock.lock();
        userNames.clear();
        userLock.unlock();
        System.out.println(Arrays.toString(userNames.toArray()));
    }

    @Override
    public List<String> getUserNames() throws RemoteException {
        return userNames;
    }

    @Override
    public String getManagerName() throws RemoteException {
        return managerName;
    }

    private void setManagerName(String managerName) throws RemoteException {
        this.managerName = managerName;
    }
}
