package com.tpintegrador.lafuerza;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import static javafx.application.Platform.exit;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Long idCliente;
    private static Long idTutor;
    private static Long idRutina;
    private static Boolean isCliente;
    
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("VistaLaFuerza"));
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void close() {
        exit();
    }

    public static void setIdCliente(Long id) {
        idCliente = id;
    }

    public static Long getIdCliente() {
        return idCliente;
    }

    public static Long getIdRutina() {
        return idRutina;
    }

    public static void setIdRutina(Long idRutina) {
        App.idRutina = idRutina;
    }

    public static Long getIdTutor() {
        return idTutor;
    }

    public static void setIdTutor(Long idTutor) {
        App.idTutor = idTutor;
    }

    public static Boolean getIsCliente() {
        return isCliente;
    }

    public static void setIsCliente(Boolean isCliente) {
        App.isCliente = isCliente;
    }
    
    public static void MsgBox(Alert.AlertType tipo, String titulo, String header, String content) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                System.out.println("OK.");
            }
        });
    }

}
