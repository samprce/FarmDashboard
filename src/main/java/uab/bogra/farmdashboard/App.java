package uab.bogra.farmdashboard;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static App app;

    public static App getInstance() {
        if (app == null) {
            app = new App();
        }
        return app;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1600, 900);
        stage.setTitle("Farm Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    public void run() {
        launch();
    }
}