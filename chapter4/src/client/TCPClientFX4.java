package client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.AssertionUtils;

import java.io.*;
import java.net.Socket;
import java.util.Optional;

/**
 * From 服务器：同学你好，欢迎使用《互联网程序设计》登录查分服务器From 服务器：发送 学号&姓名&密码 查询分数，发送 bye 结束服务连接From 服务器：查询成功，成绩如下：
 * 学号                   分数       作业标题
 * 20181003114           5.0        第2讲作业From 服务器：成功完成登录查分！
 */
public class TCPClientFX4 extends Application {
    private static final String dialogIp = "172.16.229.253";
    private static final String dialogPort = "2021";
    private static final String fileDownloadIp = "172.16.229.253";
    private static final String fileDownloadPort = "2020";
    private final Button btnExit = new Button("退出");
    private final Button btnSend = new Button("发送");
    private final Button btnOpen = new Button("加载");
    private final Button btnSave = new Button("保存");
    private final Button btnDownload = new Button("下载");
    private final Button btnShowFiles = new Button("显示");
    private Button btnConnect = new Button("连接");
    //待发送信息的文本框
    private final TextField inputField = new TextField("20181003114&李源&123456");
    //显示信息的文本区域
    private final TextArea receiveArea = new TextArea();
    //接受ip地址的单行文本框
    private final TextField ipAddressField = new TextField(dialogIp);
    //接受port的单行文本框
    private final TextField ipPortField = new TextField(dialogPort);
    //    private static TCPClient tcpClient;
    private Socket socket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("登录查看成绩");
        receiveArea.setWrapText(true);
        BorderPane mainPane = new BorderPane();
        //输入区域
        VBox vBox = setVBoxArea();
        mainPane.setCenter(vBox);
        //底部按钮区域
        HBox hBox = setHBoxArea();
        mainPane.setBottom(hBox);
        //顶部ip相关区域
        HBox hBox1 = setTopIpArea();
        mainPane.setTop(hBox1);
        btnExitAction();
        btnSendAction();
        btnOpenAction(primaryStage);
        btnSaveAction(primaryStage);
        btnConnectAction();
        btnDownloadAction();
        btnShowFilesAction();
        highlightTextAreaAction();
        Scene scene = new Scene(mainPane, 700, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void btnShowFilesAction() {
        btnShowFiles.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                send("fileList");
            }
        });
    }

    private void btnDownloadAction() {
        btnDownload.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (inputField.getText().equals("")) {
                    inputField.appendText("[请输入正确的文件夹选项]");
                    return;
                }
                String fileName = inputField.getText().trim();
                FileChooser fileChooser = new FileChooser();
                fileChooser.setInitialFileName(fileName);
                File savedFile = fileChooser.showSaveDialog(null);
                if (savedFile == null) {
                    return;//用户放弃操作
                }
                try {
                    FileDataClient fileDataClient = new FileDataClient(fileDownloadIp, Integer.parseInt(fileDownloadPort));
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText(savedFile.getName() + " 下载中...");
                        alert.showAndWait();
//                        receiveArea.appendText("文件下载中！"+"\n");
                        try {
                            fileDataClient.getFile(savedFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
//                        receiveArea.appendText("文件下载完毕！"+"\n");
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("下载完毕！");
                        alert.showAndWait();
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }


                //TODO

            }
        });
    }

    private void highlightTextAreaAction() {
        receiveArea.selectionProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                if (!receiveArea.getSelectedText().equals("")) {
                    inputField.setText(receiveArea.getSelectedText());
                }
            }
        });

    }

    private void btnConnectAction() {
        btnConnect.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
//                    tcpClient = new TCPClient("127.0.0.1", 8008);
                    String ip = ipAddressField.getText();
                    int port = Integer.parseInt(ipPortField.getText());
                    socket = new Socket(ip, port);
                    bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                    receiveArea.setText("[连接建立成功！]" + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
//        socketClient = new SocketClient("172.16.229.253", 8008);
    }

    private void btnSaveAction(Stage primaryStage) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                btnSave.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        DirectoryChooser directoryChooser = new DirectoryChooser();
                        File file = directoryChooser.showDialog(primaryStage);
                        if (file == null || !file.isDirectory()) {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "请选择文件夹！");
                            Optional<ButtonType> buttonType = alert.showAndWait();
                            return;
                        }
                        String exportFilePath = file.getAbsolutePath();
                        String message = receiveArea.getText();
                        try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(exportFilePath + "/temp.txt")))) {
                            bufferedWriter.write(message);
                            bufferedWriter.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }

    private void btnOpenAction(Stage primaryStage) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                btnOpen.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        FileChooser fileChooser = new FileChooser();
                        File file = fileChooser.showOpenDialog(primaryStage);
                        if (file != null && file.canRead()) {
                            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
                                StringBuilder stringBuilder = new StringBuilder();
                                String line;
                                while ((line = bufferedReader.readLine()) != null) {
                                    stringBuilder.append(line);
                                }
                                receiveArea.setText(stringBuilder.toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "请选择文件！");
                            alert.showAndWait();
                            return;
                        }
                    }
                });
            }
        }).start();
    }

    private void btnSendAction() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                btnSend.setOnMouseClicked(mouseEvent -> {
                    String content = inputField.getText();
                    send(content);
                });
            }
        }).start();
    }

    private void send(String content) {
        try {
            AssertionUtils.assertNonNull(socket);
        } catch (Exception e) {
            receiveArea.setText("[ERROR 请先建立连接！]");
            return;
        }
        printWriter.println(content);
        Thread thread = new Thread(() -> {
            String line = null;
            try {
                System.out.println("1");
                while ((line = bufferedReader.readLine()) != null) {
//                                System.out.println(line);
                    //对主界面的修改必须在主线程中进行
                    String finalLine = line;
                    Platform.runLater(()->{
                        receiveArea.appendText(finalLine + "\n");
                    });
                }
                receiveArea.appendText("连接结束！");
                Thread.sleep(1000);
                System.exit(0);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    private void btnExitAction() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                btnExit.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        printWriter.println("bye");
                        System.out.println(Thread.activeCount());
//                            socket.close();
                        //                        System.exit(0);
                    }
                });
            }
        }).start();
    }

    private HBox setTopIpArea() {
        HBox hBox1 = new HBox();
        Label ipAddressLabel = new Label("ip地址");
        ipAddressLabel.setLabelFor(ipAddressField);
        Label ipPortLabel = new Label("端口");
        ipPortLabel.setLabelFor(ipPortField);
        ipPortLabel.setAlignment(Pos.BASELINE_CENTER);
        btnConnect = new Button("连接");
        hBox1.getChildren().addAll(ipAddressLabel, ipAddressField, ipPortLabel, ipPortField, btnConnect);
        hBox1.setAlignment(Pos.BOTTOM_CENTER);
        hBox1.setSpacing(15);
        hBox1.setPadding(new Insets(20, 20, 0, 20));
        return hBox1;
    }

    private VBox setVBoxArea() {
        //内容显示区域
        VBox vBox = new VBox();
        vBox.setSpacing(10);//各控件之间的间隔
        //VBox面板中的内容距离四周的留空区域
        vBox.setPadding(new Insets(10, 20, 10, 20));
        vBox.getChildren().addAll(new Label("信息显示区："),
                receiveArea, new Label("信息输入区："), inputField);
        //设置显示信息区的文本区域可以纵向自动扩充范围
        receiveArea.setEditable(true);
        VBox.setVgrow(receiveArea, Priority.ALWAYS);
        return vBox;
    }

    private HBox setHBoxArea() {
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(10, 20, 10, 20));
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.getChildren().addAll(btnShowFiles, btnDownload, btnSend, btnSave, btnOpen, btnExit);
        return hBox;
    }

}
