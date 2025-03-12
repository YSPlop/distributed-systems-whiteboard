package client;

import java.rmi.RemoteException;
import java.util.concurrent.TimeUnit;

public class MessengerThread implements Runnable{
    private ClientMessenger clientMessenger;

    public MessengerThread(ClientMessenger clientMessenger){
        this.clientMessenger = clientMessenger;
    }

    public void run(){
        while(true){
            try{
                clientMessenger.poll();
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                System.err.println("InterruptedException when MessengerThread poll: " + e);
            } catch (RemoteException e) {
                System.err.println("RemoteException when MessengerThread poll: " + e);
            }
        }
    }
}
