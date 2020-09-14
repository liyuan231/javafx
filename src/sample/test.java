package sample;

import java.io.*;

public class test {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("javafx.iml")));
        while (bufferedReader.ready()){
            String s = bufferedReader.readLine();
            System.out.println(s);
        }
//        System.out.println(s);
    }
}
