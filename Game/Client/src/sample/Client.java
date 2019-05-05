package sample;

import javafx.application.Application;
import javafx.stage.Stage;
import sample.Views.ConfigurePage;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class Client extends Application{

    private BufferedReader is;
    private PrintWriter pw;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        pw.println("exit");
        super.stop();
        System.exit(0);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        ConfigurePage configurePage = new ConfigurePage(this);
        configurePage.show();

    }

    public void setIs(BufferedReader is) {
        this.is = is;
    }

    public void setPw(PrintWriter pw) {
        this.pw = pw;
    }
}