package client;

import java.rmi.RemoteException;
import java.util.concurrent.TimeUnit;

public class DrawingThread implements Runnable{
    private final int SLEEP_SEC = 1;

    private ClientDrawer clientDrawer;

    public DrawingThread(ClientDrawer clientDrawer){
        this.clientDrawer = clientDrawer;
    }

    @Override
    public void run() {
        while(true){
            try{
                clientDrawer.poll();
                TimeUnit.SECONDS.sleep(SLEEP_SEC);
            } catch (InterruptedException e) {
                System.err.println("InterruptedException when DrawingThread : " + e);
            } catch (RemoteException e) {
                System.err.println("RemoteException when DrawingThread : " + e);
            }
        }
    }
}
