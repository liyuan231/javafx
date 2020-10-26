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
import t.service.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class URLClientFX extends Application {
    private Button btnExit = new Button("退出");
    private Button btnSend = new Button("发送");
//    private Button btnConnect = new Button("连接");

    private TextArea receiveArea = new TextArea();
    private TextField inputField = new TextField("https://www.gdufs.edu.cn");

    private TextField ipAddressField = new TextField("www.gdufs.edu.cn");


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
//        btnConnectAction();
        Scene scene = new Scene(mainPane, 700, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

//    private void btnConnectAction() {
//        btnConnect.setOnAction(event -> {
//            try {
//                String ip = ipAddressField.getText();
////                int port = Integer.parseInt(ipPortField.getText());
//                client = ClientFactory.retrieveClient("https", ip, port);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//    }

    private void btnSendAction() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                btnSend.setOnMouseClicked(mouseEvent -> {
//                    client.send(headers.toString());
                    Thread thread = new Thread(() -> {
                        String line = null;
                        try {
                            System.out.println("1");
                            String address = inputField.getText().trim();
                            URL url = new URL(address);
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
                            System.out.println("连接成功！");
                            while ((line = bufferedReader.readLine()) != null) {
                                String finalLine = line;
                                Platform.runLater(() -> {
                                    receiveArea.appendText(finalLine + "\n");
                                });
                            }
//                            client.close();
                            Thread.sleep(1000);
                            Platform.runLater(()->{
                                receiveArea.appendText("连接结束！");
                            });
//                            System.exit(0);
                        } catch (IOException | InterruptedException e) {
                            Platform.runLater(() -> {
                                receiveArea.setText(e.getMessage());
                            });
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
//                client.close();
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
//        ipPortLabel.setLabelFor(ipPortField);
        ipPortLabel.setAlignment(Pos.BASELINE_CENTER);
//        btnConnect = new Button("连接");
        hBox1.getChildren().addAll(ipAddressLabel, ipAddressField, ipPortLabel);
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
