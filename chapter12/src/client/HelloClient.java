package client;

import rmi.HelloService;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class HelloClient {
    public static void main(String[] args) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
        HelloService helloService = (HelloService) registry.lookup("helloService");
        System.out.println(helloService.echo("12345"));
        System.out.println(helloService.getTime());

    }
}
