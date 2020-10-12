package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GroupServer {
    private static final int port = 8008;
    private static final String ip = "127.0.0.1";
    private static ExecutorService executorService;
    private static Set<Student> students;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        executorService = Executors.newFixedThreadPool(12);
        students = new HashSet<>();
        while (true) {
            Socket socket = serverSocket.accept();
            executorService.submit(() -> {
                try {
                    PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                    String[] usernameAndNumber = bufferedReader.readLine().split("&");
//                    String username = usernameAndNumber[0];
//                    String number = usernameAndNumber[1];
                    students.add(new Student("number", "username", socket));
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        if (line.trim().equals("bye")) {
                            break;
                        }
//                        printWriter.println("From 服务器：");
//                        printWriter.println(line);
                        sendToAllMembers(line);
                    }
                    printWriter.println("连接结束！");
                    removeSocket(socket);
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private static void removeSocket(Socket socket) {
//        Socket socket1
    }

    public static void sendToAllMembers(String message) throws IOException {
        Iterator<Student> iterator = students.iterator();
        String hostName;
        String hostAddress;
        OutputStream outputStream;
        PrintWriter printWriter = null;
        while (iterator.hasNext()) {
            Student student = iterator.next();
            hostName = student.getSocket().getInetAddress().getHostName();
            hostAddress = student.getSocket().getInetAddress().getHostAddress();
            outputStream = student.getSocket().getOutputStream();
            printWriter = new PrintWriter(new OutputStreamWriter(outputStream), true);
            printWriter.println(hostAddress + ":" + message);
        }
    }


}

class Student {
    private String number;
    private String name;
    private Socket socket;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Student(String number, String name, Socket socket) {
        this.number = number;
        this.name = name;
        this.socket = socket;
    }
}
