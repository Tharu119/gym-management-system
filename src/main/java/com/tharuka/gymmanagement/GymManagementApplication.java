//package com.tharuka.gymmanagement;
//
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.builder.SpringApplicationBuilder;
//import org.springframework.context.ConfigurableApplicationContext;
//
//@SpringBootApplication
//public class GymManagementApplication extends Application {
//
//    private ConfigurableApplicationContext springContext;
//
//    @Override
//    public void init() {
//        springContext = new SpringApplicationBuilder(GymManagementApplication.class).run();
//    }
//
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
//        fxmlLoader.setControllerFactory(springContext::getBean);
//        Parent root = fxmlLoader.load();
//
//        Scene scene = new Scene(root);
//        primaryStage.setTitle("Gym Management System");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//    @Override
//    public void stop() {
//        springContext.close();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
