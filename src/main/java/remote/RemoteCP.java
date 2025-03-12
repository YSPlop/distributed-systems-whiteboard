package remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RemoteCP<T> extends UnicastRemoteObject implements IRemoteCP<T>{
    private String name;
    private ArrayList<T> itemArray;

    private Lock itemLock;

    public RemoteCP() throws RemoteException {
        super();
        name = "RemoteCP";
        itemLock = new ReentrantLock();
        itemArray = new ArrayList<T>();
    }

    public RemoteCP(String name) throws RemoteException {
        super();
        this.name = name;
        itemLock = new ReentrantLock();
        itemArray = new ArrayList<T>();
    }

    public int commit(T item) throws RemoteException {
        System.out.println("Adding elements to message Queue");
        itemLock.lock();
        itemArray.add(item);
        int index = itemArray.size() - 1;
        itemLock.unlock();

        //return the index
        return index;
    }

    public List<T> poll(int index) throws RemoteException {

        System.out.println(name + "Polling " + index);

        if (index >= itemArray.size()) {
            System.out.println(name + "Poll: Index already updated");
            return null;
        }
        ArrayList<T> res = new ArrayList<T>(itemArray.subList(index, itemArray.size()));
        return res;
    }
}
