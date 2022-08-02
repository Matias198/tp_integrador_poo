/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpintegrador.lafuerza;

import com.tpintegrador.lafuerza.modelo.Cliente;
import com.tpintegrador.lafuerza.modelo.Entrenamiento;
import com.tpintegrador.lafuerza.modelo.Rutina;
import com.tpintegrador.lafuerza.modelo.Tutor;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author matia
 */
public class AppRAController implements Initializable {

    private double volumen;

    @FXML
    private TableView tablaRutina;
    @FXML
    private TableView tablaEntrenamiento;
    @FXML
    private TextField volumenTotal;
    @FXML
    private Button btnBorrarRutina;

    public AppRAController() {
        this.volumen = 0;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (!App.getIsCliente()) {
            btnBorrarRutina.setDisable(true);
            btnBorrarRutina.setVisible(false);
        }
        //TABLA
        //COLUMNA DIA
        TableColumn<Rutina, String> diaColumn = new TableColumn<>("DIA");
        diaColumn.setCellValueFactory(new PropertyValueFactory<>("nombreDia"));
        //COLUMNA FECHA
        TableColumn<Rutina, LocalDate> fechaColumn = new TableColumn<>("FECHA INICIO");
        fechaColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        //COLUMNA DURACION
        TableColumn<Rutina, Integer> duracionColumn = new TableColumn<>("DURACION");
        duracionColumn.setCellValueFactory(new PropertyValueFactory<>("duracion"));
        //COLUMNA TUTOR
        TableColumn<Rutina, Tutor> tutorColumn = new TableColumn<>("TUTOR");
        tutorColumn.setCellValueFactory(new PropertyValueFactory<>("tutor"));
        //COLUMNA CLIENTE
        TableColumn<Rutina, Cliente> clienteColumn = new TableColumn<>("CLIENTE");
        clienteColumn.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        tablaRutina.getColumns().add(diaColumn);
        tablaRutina.getColumns().add(fechaColumn);
        tablaRutina.getColumns().add(duracionColumn);
        tablaRutina.getColumns().add(tutorColumn);
        tablaRutina.getColumns().add(clienteColumn);
        tablaRutina.getItems().add(Rutina.buscarRutina(App.getIdRutina()));

        //TABLA
        //COLUMNA DETALLE
        TableColumn<Entrenamiento, List<String>> detalleColumn = new TableColumn<>("DETALLE");
        detalleColumn.setCellValueFactory(new PropertyValueFactory<>("detalle"));
        //COLUMNA REPS
        TableColumn<Entrenamiento, List<Integer>> repsColumn = new TableColumn<>("REPS");
        repsColumn.setCellValueFactory(new PropertyValueFactory<>("repeticiones"));
        //COLUMNA PESO
        TableColumn<Entrenamiento, List<Double>> pesoColumn = new TableColumn<>("PESO");
        pesoColumn.setCellValueFactory(new PropertyValueFactory<>("peso"));
        //COLUMNA VOLUMEN
        TableColumn<Entrenamiento, Double> volumenColumn = new TableColumn<>("VOLUMEN");
        volumenColumn.setCellValueFactory(new PropertyValueFactory<>("volumen"));
        //COLUMNA SERIES
        TableColumn<Entrenamiento, Integer> serieColumn = new TableColumn<>("SERIES");
        serieColumn.setCellValueFactory(new PropertyValueFactory<>("series"));
        //COLUMNA DESCANSO
        TableColumn<Entrenamiento, Integer> segDescColumn = new TableColumn<>("SEG DESCANSO");
        segDescColumn.setCellValueFactory(new PropertyValueFactory<>("segundosDescanso"));
        tablaEntrenamiento.getColumns().add(detalleColumn);
        tablaEntrenamiento.getColumns().add(repsColumn);
        tablaEntrenamiento.getColumns().add(pesoColumn);
        tablaEntrenamiento.getColumns().add(volumenColumn);
        tablaEntrenamiento.getColumns().add(serieColumn);
        tablaEntrenamiento.getColumns().add(segDescColumn);
        tablaEntrenamiento.getItems().addAll(Entrenamiento.getEntrenamientosDeRutinas(App.getIdRutina()));
        Entrenamiento.getEntrenamientosDeRutinas(App.getIdRutina()).forEach(entrenamiento -> {
            volumen += entrenamiento.getVolumen();
        });
        volumenTotal.setText(String.valueOf(volumen));
    }

    @FXML
    private void clickBorrar() throws IOException {
        try {
            Rutina.buscarRutina(App.getIdRutina()).getEntrenamientos().forEach(ent -> {
                Entrenamiento.eliminarEntrenamiento(ent);
            });
            if (Rutina.buscarRutina(App.getIdRutina()).getEntrenamientos().isEmpty()) {
                Rutina.eliminarRutina(Rutina.buscarRutina(App.getIdRutina()));
                App.setRoot("VistaClienteB");
            }
        } catch (IOException e) {
            App.MsgBox(Alert.AlertType.ERROR, "ERROR", "Error inesperado", "Causa:" + e.getMessage());
        }

    }

    @FXML
    private void inicio() throws IOException {
        App.setRoot("VistaLaFuerza");
    }

    @FXML
    private void tutores() throws IOException {
        App.setRoot("VistaTutorA");
    }

    @FXML
    private void clientes() throws IOException {
        App.setRoot("VistaClienteA");
    }

    @FXML
    private void ejercicios() throws IOException {
        App.setRoot("VistaEjercicio");
    }

    @FXML
    private void musculos() throws IOException {
        App.setRoot("VistaGMuscular");
    }

    @FXML
    private void clickSalir() throws IOException {
        App.close();
    }

}
