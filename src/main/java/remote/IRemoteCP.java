package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IRemoteCP<T> extends Remote {
    public int commit(T item) throws RemoteException;

    /*
     * Given an index, the method returns messages starting from the index
     * */
    public List<T> poll(int index) throws RemoteException;
}
