import javafx.application.Application;
import javafx.application.Platform;
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

import java.io.*;
import java.net.Socket;
import java.util.Optional;

public class TCPClientFX extends Application {
    private Button btnExit = new Button("退出");
    private Button btnSend = new Button("发送");
    private Button btnOpen = new Button("加载");
    private Button btnSave = new Button("保存");
    private Button btnConnect = new Button("连接");
    //待发送信息的文本框
    private TextField inputField = new TextField("这是邮件内容");

    private TextField from = new TextField("2812329425@qq.com");
    private TextField to = new TextField("1987151116@qq.com");

    private TextField subject = new TextField("邮件标题");
    //显示信息的文本区域
    private TextArea receiveArea = new TextArea();
    //接受ip地址的单行文本框
    private TextField ipAddressField = new TextField("smtp.qq.com");
    //接受port的单行文本框
    private TextField ipPortField = new TextField("25");
    //    private static TCPClient tcpClient;
    private Socket socket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private TCPMailClient tcpMailClient;

    public void start(Stage primaryStage) throws IOException {
//        primaryStage.setTitle("登录查看成绩");
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
        Scene scene = new Scene(mainPane, 700, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void btnConnectAction() throws IOException {
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
                            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));) {
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
                    String host = ipAddressField.getText().trim();
                    Integer port = Integer.parseInt(ipPortField.getText());
                    try {
                        tcpMailClient = new TCPMailClient(host, port);
                        tcpMailClient.send("Helo liyuan");
                        tcpMailClient.send("auth login");
                        String username = "MjgxMjMyOTQyNUBxcS5jb20=";
                        tcpMailClient.send(username);
                        String code = "aHV3dGR6Y3FlZW9qZGRnag==";
                        tcpMailClient.send(code);
                        String fromMail = "mail from: <" + from.getText().trim() + ">";
                        tcpMailClient.send(fromMail);
                        String toMail = "rcpt to: <" + to.getText().trim() + ">";
                        tcpMailClient.send(toMail);
                        String data = "data";
                        tcpMailClient.send(data);
                        tcpMailClient.send("from:"+from.getText().trim());
                        String content = "subject:"+subject.getText().trim();
                        tcpMailClient.send(content);
                        tcpMailClient.send(inputField.getText());
                        tcpMailClient.send(".");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }).start();
    }

    private void btnExitAction() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                btnExit.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if (printWriter != null) {
                            printWriter.println("bye");
                        } else {
                            System.exit(-1);
                        }
                        //                            socket.close();
                    }
                });
            }
        }).start();
    }

    private HBox setTopIpArea() {
        HBox hBox1 = new HBox();
        Label ipAddressLabel = new Label("邮件服务器地址:");
        ipAddressLabel.setLabelFor(ipAddressField);
        Label ipPortLabel = new Label("邮件服务器端口:");
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
        vBox.getChildren().addAll(new Label("发送方:"), from, new Label("接收方:"), to, new Label("邮件标题"), subject, new Label("信息显示区："),
                receiveArea, new Label("信息输入区："), inputField);
        //设置显示信息区的文本区域可以纵向自动扩充范围
        receiveArea.setEditable(false);
        VBox.setVgrow(receiveArea, Priority.ALWAYS);
        return vBox;
    }

    private HBox setHBoxArea() {
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(10, 20, 10, 20));
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.getChildren().addAll(btnSend, btnSave, btnOpen, btnExit);
        return hBox;
    }


}
