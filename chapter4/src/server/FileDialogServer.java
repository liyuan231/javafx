package server;

import java.io.*;
import java.math.RoundingMode;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;

/**
 * 文件会话服务器，就是给你显示有什么文件可以下载
 */
public class FileDialogServer {
    private static String path = "C:\\Users\\Administrator\\Desktop";
    private static ServerSocket serverSocket;

    public static void main(String[] args) throws IOException {
        serverSocket = new ServerSocket(9009);
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println(socket.getInetAddress() + " is connected!");
            new Thread(() -> {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        if (line.trim().equals("fileList")) {
                            printFileListToClient(printWriter);
                        } else {
                            printWriter.println("Response from server:");
                            printWriter.println(line);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }


    public void setPath(String path) {
        this.path = path;
    }

    public static void printFileListToClient(PrintWriter printWriter) {
        File filePath = new File(path);
        if (!filePath.exists()) {
            throw new NullPointerException("服务器存储的文件夹目录不存在!");
        }
        if (!filePath.isDirectory()) {
            throw new IllegalStateException("服务器存储的文件夹应当是一个目录!");
        }
        String[] list = filePath.list();
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(0);
        decimalFormat.setRoundingMode(RoundingMode.CEILING);
        File tmpFile;
        for (String s : list) {
            tmpFile = new File(filePath + "/" + s);
            if (tmpFile.isFile()) {
                printWriter.println(s + " (" + decimalFormat.format(tmpFile.length() / 1024.0) + "KB)");
            }
        }
    }
}
