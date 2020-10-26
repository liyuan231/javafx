import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import t.ClientFactory;
import t.service.Client;

import java.io.IOException;

public class HttpClientFX extends Application {
    private Button btnExit = new Button("退出");
    private Button btnSend = new Button("发送");
    private Button btnConnect = new Button("连接");

    private TextArea receiveArea = new TextArea();

    private TextField ipAddressField = new TextField("www.gdufs.edu.cn");
    private TextField ipPortField = new TextField("443");

    private Client client;
    private StringBuilder headers = new StringBuilder("" +
            "GET / HTTP/1.1;\n" +
            "Host: www.gdufs.edu.cn;\n" +
            "Connection: keep-alive;\n" +
            "Cache-Control: max-age=0;\n" +
            "Upgrade-Insecure-Requests: 1;\n" +
            "User-Agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.111 Safari/537.36;\n" +
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9;\n" +
            "Sec-Fetch-Site: none;\n" +
            "Sec-Fetch-Mode: navigate;\n" +
            "Sec-Fetch-User: ?1;\n" +
            "Sec-Fetch-Dest: document;\n" +
            "Accept-Encoding: gzip, deflate, br;\n" +
            "Accept-Language: zh-CN,zh;q=0.9;\n" +
            "Cookie: JSESSIONID=310965DD679FEDA717B606FAD9877D45;\n" +
            "If-None-Match: \"22253-5b2774de86f6c-gzip\";\n" +
            "If-Modified-Since: Sun, 25 Oct 2020 04:32:41 GMT;");
//            .append("Accept:*/*")
//            .append("Accept-Language: zh-CN,zh;q=0.9;")
//            .append("Cache-Control: max-age=0;")
//            .append("Connection:Keep-Alive;")
//            .append("Host: www.gdufs.edu.cn;")
////            .append("GET/HTTP/1.1;")
//            .append("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.111 Safari/537.36;");

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
        btnConnectAction();
        Scene scene = new Scene(mainPane, 700, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void btnConnectAction() {
        btnConnect.setOnAction(event -> {
            try {
                String ip = ipAddressField.getText();
                int port = Integer.parseInt(ipPortField.getText());
                client = ClientFactory.retrieveClient("https", ip, port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void btnSendAction() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                btnSend.setOnMouseClicked(mouseEvent -> {
                    if (client == null || client.isClosed()) {
                        Platform.runLater(() -> {
                            receiveArea.appendText("请先建立连接！\n");
                        });

                        return;
                    }
                    client.send(headers.toString());
                    Thread thread = new Thread(() -> {
                        String line = null;
                        try {
                            System.out.println("1");
                            while ((line = client.receive()) != null) {
                                String finalLine = line;
                                Platform.runLater(() -> {
                                    receiveArea.appendText(finalLine + "\n");
                                });
                            }
                            client.close();
                            Thread.sleep(1000);
                            receiveArea.appendText("连接结束！");
//                            System.exit(0);
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                    thread.start();

                });
            }
        }).start();
    }

    private void btnExitAction() {
        new Thread(() -> btnExit.setOnMouseClicked(mouseEvent -> {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
            System.exit(-1);
        })).start();
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
                receiveArea, new Label("信息输入区："));
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
        hBox.getChildren().addAll(btnSend, btnExit);
        return hBox;
    }


}
