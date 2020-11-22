package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

public interface HelloService extends Remote {
    public String echo(String message) throws RemoteException;

    public Date getTime() throws RemoteException;
}
