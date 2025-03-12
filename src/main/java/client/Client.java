package client;

import remote.IRemoteCP;

import remote.IRemoteUserList;

import javax.swing.*;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * The main class in package @see COMP90015G72.client
 * Boots up several threads for texting, drawing, etc.
 */
public class Client {

    private static final String remoteUserListName = "RemoteUserList";
    private static final String remoteCommandsIdentifier = "RemoteCommands";
    private static final String remoteMessagesIdentifier = "RemoteMessages";
    //private static int serverPort = 5859;

    private String userName;

    private String userID;

    private int serverPort;

    private String serverIP;

    private ClientUserList clientUserList;

    private ClientMessenger clientMessenger;

    private ClientDrawer clientDrawer;


    public static void main(String[] args){

        Client myClient =new Client(args[0], Integer.parseInt(args[1]), args[2]);


    }

    private Remote lookupRemote(String remoteName){
        String IPaddress = "127.0.0.1";
        try{
            String remoteUserListURL =
                    "rmi://" + IPaddress +
                            ":" +
                            String.valueOf(serverPort) + "/" + remoteName;

            System.out.println(remoteUserListURL);
            Registry registry = LocateRegistry.getRegistry(serverPort);
            Remote remote = registry.lookup(remoteUserListURL);

            if(remote!=null) {
                System.out.println("Successfully retrieved remote user list");
                return remote;
            }

        } catch (NotBoundException | RemoteException e) {
            System.err.println("Exception retrieving rmi remote: " + e);
        }

        return null;
    }

    public Client(String serverIP, int port, String name){
        this.serverIP = serverIP;
        this.serverPort = port;
        this.userName =name;

        IRemoteUserList remoteUserList = (IRemoteUserList) lookupRemote(remoteUserListName);
        IRemoteCP<String> remoteMessages = (IRemoteCP<String>) lookupRemote(remoteMessagesIdentifier);
        IRemoteCP<Command> remoteCommand = (IRemoteCP<Command>) lookupRemote(remoteCommandsIdentifier);


        //The order of constructing is intertwined...

        DefaultListModel listModel = new DefaultListModel();
        System.out.println(userName);
        this.clientUserList = new ClientUserList(listModel, remoteUserList);

        try {
            userID = clientUserList.register(userName);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        ClientUI myUI = new ClientUI(userID, clientUserList);
        clientUserList.setClientUI(myUI);
        this.clientDrawer = new ClientDrawer(myUI, remoteCommand);
        myUI.setClientDrawer(clientDrawer);

        Thread userUpdator = new Thread(new UserThread(clientUserList));
        userUpdator.setDaemon(true);
        userUpdator.start();

        this.clientMessenger = myUI.getClientMessenger();
        this.clientMessenger.setIRemoteMessages(remoteMessages);
        Thread messengerUpdator = new Thread(new MessengerThread(clientMessenger));
        messengerUpdator.setDaemon(true);
        messengerUpdator.start();

        Thread drawingUpdator = new Thread(new DrawingThread(clientDrawer));
        drawingUpdator.setDaemon(true);
        drawingUpdator.start();


    }

}
