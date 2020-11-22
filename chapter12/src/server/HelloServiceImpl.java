package server;

import rmi.HelloService;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

public class HelloServiceImpl extends UnicastRemoteObject implements HelloService {
    private String name;

    protected HelloServiceImpl() throws RemoteException {
    }

    public HelloServiceImpl(String name) throws RemoteException {
        this.name = name;
    }

    @Override
    public String echo(String message) throws RemoteException {
        System.out.println("服务端完成一些echo方法相关任务...");
        return "echo:" + message + "from " + name;
    }

    @Override
    public Date getTime() throws RemoteException {
        System.out.println("服务端完成getTime方法相关任务...");
        return new Date();
    }
}
