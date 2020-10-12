package multicast;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MulticastFX extends Application {
    private Button btnExit = new Button("退出");
    private Button btnSend = new Button("发送");
    private Button btnOpen = new Button("加载");
    private Button btnSave = new Button("保存");
    private Button btnConnect = new Button("连接");
    //待发送信息的文本框
    private TextField inputField = new TextField("我的UDP服务器已经准备好，端口号是&8081&");
    //显示信息的文本区域
    private TextArea receiveArea = new TextArea();
    //接受ip地址的单行文本框172.16.229.253
    private TextField ipAddressField = new TextField("127.0.0.1");
    //接受port的单行文本框
    private TextField ipPortField = new TextField("8081");
    private Multicast multicast = new Multicast();


    public MulticastFX() throws IOException {
    }

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
        Scene scene = new Scene(mainPane, 700, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void btnSendAction() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                btnSend.setOnMouseClicked(mouseEvent -> {
                    String message = inputField.getText();
                    try {
                        multicast.send(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Thread thread = new Thread(() -> {
                        String line = null;
                        try {
                            System.out.println("1");
                            while ((line = multicast.receive()) != null) {
                                String finalLine = line;
                                Platform.runLater(() -> {
                                    receiveArea.appendText(finalLine + "\n");
                                });
                            }
                            receiveArea.appendText("连接结束！");
                            Thread.sleep(1000);
                            System.exit(0);
                        } catch (InterruptedException | IOException e) {
                            e.printStackTrace();
                        }
                    });
                    thread.start();

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
                        System.exit(0);
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
