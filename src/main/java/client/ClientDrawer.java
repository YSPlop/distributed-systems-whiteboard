package client;

import remote.IRemoteCP;

import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ClientDrawer {

    private ClientUI clientUI;
    private IRemoteCP<Command> iRemoteCommand;

    private Lock graphicsLock;

    private int ack;

    public ClientDrawer(ClientUI clientUI, IRemoteCP<Command> iRemoteCommand){
        this.clientUI = clientUI;
        this.iRemoteCommand = iRemoteCommand;
        graphicsLock = new ReentrantLock();
        ack = 0;
    }

    public void commit(Command command) throws RemoteException {
        /*
        chatAreaLock.lock();
        String text = chatArea.getText() + msg + "\n";
        chatArea.setText(text);
        chatAreaLock.unlock();
        */
        int ack_ = iRemoteCommand.commit(command);
        // in case of exception
        ack = ack_;
    }

    public void poll() throws RemoteException {
        List<Command> commandList = iRemoteCommand.poll(ack);
        if(commandList == null){
            return;
        }
        graphicsLock.lock();

        for(Command s: commandList){
            clientUI.draw(s);
        }

        ack += commandList.size();
        graphicsLock.unlock();
    }
}
