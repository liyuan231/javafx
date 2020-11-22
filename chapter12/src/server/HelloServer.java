package server;

import rmi.HelloService;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class HelloServer {
    public static void main(String[] args) throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(1099);
        HelloService helloService = new HelloServiceImpl("李源的远程服务！");
        registry.rebind("helloService", helloService);
        System.out.println("发布了一个HelloService RMI远程服务！");

    }
}
