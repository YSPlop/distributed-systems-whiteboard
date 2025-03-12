package client;

import java.rmi.RemoteException;
import java.util.concurrent.TimeUnit;

/**
 * set to daemon
 */
public class UserThread implements Runnable{

    private ClientUserList clientUserList;

    public UserThread(ClientUserList clientUserList){
        this.clientUserList = clientUserList;
    }

    @Override
    public void run() {

        while(true){
            try{
                clientUserList.update();
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                System.err.println("InterruptedException when UserThread update: " + e);
            } catch (RemoteException e) {
                System.err.println("RemoteException when UserThread update: " + e);
            }
        }
    }
}
