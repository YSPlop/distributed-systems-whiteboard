package client;

import remote.IRemoteCP;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Only poll modifies chatArea's text, locking is redundant for now
 */
public class ClientMessenger {
    IRemoteCP<String> iRemoteMessages;
    private Lock chatAreaLock;

    private String messages;

    private int ack;

    private JTextArea chatArea;

    public void setIRemoteMessages(IRemoteCP<String> iRemoteMessages) {
        this.iRemoteMessages = iRemoteMessages;
    }

    public ClientMessenger(JTextArea chatArea){
        messages = "";
        int ack = 0;
        this.chatArea = chatArea;
        chatAreaLock = new ReentrantLock();
    }

    public void commit(String msg) throws RemoteException {
        /*
        chatAreaLock.lock();
        String text = chatArea.getText() + msg + "\n";
        chatArea.setText(text);
        chatAreaLock.unlock();
        */
        int ack_ = iRemoteMessages.commit(msg);
        // in case of exception
        ack = ack_;
    }

    public void poll() throws RemoteException {
        List<String> messageList = iRemoteMessages.poll(ack);
        if(messageList == null){
            return;
        }
        chatAreaLock.lock();
        String text = chatArea.getText();
        for(String s: messageList){
            text += s + "\n";
        }
        chatArea.setText(text);
        ack += messageList.size();
        chatAreaLock.unlock();
    }


}
