package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 文件传送服务器，一直监视某一个端口用于文件下载传送
 */
public class FileDataServer {
    private static ServerSocket serverSocket;

    static {
        try {
            serverSocket = new ServerSocket(8081);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(() -> {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    OutputStream outputStream = socket.getOutputStream();
                    PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream), true);

                    String downloadFile = bufferedReader.readLine();//读取不到文件名
                    System.out.println("要下载的文件为:" + downloadFile);
                    String filePath = "C:\\Users\\Administrator\\Desktop";
                    if (downloadFile == null || !isValidFileName(downloadFile, filePath)) {
//                printWriter.println("文件不存在！");
                        socket.close();
                        return;
                    }
                    downloadFile = filePath + "\\" + downloadFile;
                    DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(downloadFile)));
                    int val;
                    while ((val = dataInputStream.read()) != -1) {
                        outputStream.write(val);
                    }
                    printWriter.close();
                    outputStream.close();
                    bufferedReader.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }).start();
        }
    }

    private static boolean isValidFileName(String downloadFile, String filePath) {
        File file = new File(filePath + "\\" + downloadFile);
        if (!file.exists()) {
            return false;
        }
        return true;

    }

}
