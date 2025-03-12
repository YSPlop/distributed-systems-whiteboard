package client;

import remote.IRemoteUserList;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;

public class ClientUserList {

    private ClientUI clientUI;

    String userID;

    private Lock userlistLock;

    //acquire when modifying listModel
    private DefaultListModel listModel;
    private IRemoteUserList remoteUserList;

    public void setClientUI(ClientUI clientUI) {
        this.clientUI = clientUI;
    }

    public DefaultListModel getListModel(){
        return listModel;
    }

    public void setListModel(DefaultListModel listModel){
        this.listModel = listModel;
    }

    public ClientUserList(DefaultListModel listModel, IRemoteUserList remoteUserList){
        if(listModel!=null) {
            this.listModel = listModel;
        }else{
            this.listModel = new DefaultListModel();
        }
        this.remoteUserList = remoteUserList;
        this.userlistLock = new ReentrantLock();

    }

    /**
     * Only call once when log into server, after first update()
     * @param name
     * @throws RemoteException
     */
    public String register(String name) throws RemoteException {
        userID = remoteUserList.registerUser(name);
        /*
        userlistLock.lock();
        listModel.addElement(userID);
        userlistLock.unlock();
         */
        return userID;
    }

    public void removeElement(String name) throws RemoteException {
        userlistLock.lock();
        listModel.removeElement(name);
        userlistLock.unlock();
        remoteUserList.removeUser(name);
    }

    public void removeAll() throws RemoteException {
        /*
        userlistLock.lock();
        listModel.removeAllElements();
        userlistLock.unlock();

        */
        remoteUserList.removeAll();
    }



    /**
     * Only invoked by updating thread, not by user's event
     * @throws RemoteException
     */
    public void update() throws RemoteException {
        List<String> users = remoteUserList.getUserNames();
        userlistLock.lock();
        for(Object currentUser: listModel.toArray()){
            if(!users.contains(currentUser)){
                listModel.removeElement(currentUser);
            }
            else{
                users.remove(currentUser);
            }
        }

        listModel.addAll(users);

        userlistLock.unlock();

        if(!listModel.contains(userID)){
            System.out.println("You're kicked out");
            JOptionPane.showMessageDialog(clientUI.getContentPane(),
                    "You're kicked out.",
                    "sad",
                    JOptionPane.PLAIN_MESSAGE);
            System.exit(0);
        }
    }
}
