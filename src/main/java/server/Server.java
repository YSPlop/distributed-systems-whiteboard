package server;

import client.Command;
import remote.RemoteCP;
import remote.RemoteUserList;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    private int serverPort;

    private String IPaddress;
    private static final String remoteUserListServerIdentifier = "RemoteUserList";
    private static final String remoteRemoteCommandsIdentifier = "RemoteCommands";
    private static final String remoteRemoteMessagesIdentifier = "RemoteMessages";

    public Server(String IPaddress, int serverPort){
        this.IPaddress = IPaddress;
        this.serverPort = serverPort;

        try {

            // this calls in a remote object
            RemoteUserList remoteUserList = new RemoteUserList();
            RemoteCP<Command> remoteCommands = new RemoteCP<>("RemoteCPCommand");
            RemoteCP<String> remoteMessages = new RemoteCP<String>("RemoteCPMessage");

            Registry registry = LocateRegistry.createRegistry(serverPort);



            String remoteUserListUrl = "rmi://" + IPaddress + ":" +
                    String.valueOf(serverPort) + "/" + remoteUserListServerIdentifier;
            String remoteCommandsUrl = "rmi://" + IPaddress + ":" +
                    String.valueOf(serverPort) + "/" + remoteRemoteCommandsIdentifier;
            String remoteMessagesUrl = "rmi://" + IPaddress + ":" +
                    String.valueOf(serverPort) + "/" + remoteRemoteMessagesIdentifier;

            System.out.println(remoteUserListUrl);

            registry.rebind(remoteUserListUrl, remoteUserList);
            registry.rebind(remoteCommandsUrl, remoteCommands);
            registry.rebind(remoteMessagesUrl, remoteMessages);

            System.out.println("Server is ready");


        } catch (RemoteException e) {

            System.out.println("Server RemoteException: " + e.getMessage());

        }
    }

    public static void main(String[] args) {
        Server myServer = new Server(args[0], Integer.valueOf(args[1]));


    }
}
