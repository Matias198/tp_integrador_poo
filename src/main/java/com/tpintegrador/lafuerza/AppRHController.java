/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpintegrador.lafuerza;

import com.tpintegrador.lafuerza.modelo.Cliente;
import com.tpintegrador.lafuerza.modelo.Rutina;
import com.tpintegrador.lafuerza.modelo.Tutor;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author matia
 */
public class AppRHController implements Initializable {

    @FXML
    private TableView tablaRutina;
    @FXML
    private ComboBox valoracion;
    @FXML
    private Button btnValoracion;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (!App.getIsCliente()) {
            btnValoracion.setDisable(true);
            btnValoracion.setVisible(false);
            valoracion.setDisable(true);
            valoracion.setVisible(false);
        }
        ObservableList<Integer> items = FXCollections.observableArrayList();
        items.addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        valoracion.setItems(items);

        //TABLA
        //COLUMNA ID
        TableColumn<Rutina, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        //COLUMNA DIA
        TableColumn<Rutina, String> diaColumn = new TableColumn<>("DIA");
        diaColumn.setCellValueFactory(new PropertyValueFactory<>("nombreDia"));
        //COLUMNA FECHA
        TableColumn<Rutina, LocalDate> fechaColumn = new TableColumn<>("FECHA INICIO");
        fechaColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        //COLUMNA DURACION
        TableColumn<Rutina, Integer> duracionColumn = new TableColumn<>("DURACION");
        duracionColumn.setCellValueFactory(new PropertyValueFactory<>("duracion"));
        //COLUMNA VALORACION
        TableColumn<Rutina, Integer> valoracionColumn = new TableColumn<>("VALOR TUTOR");
        valoracionColumn.setCellValueFactory(new PropertyValueFactory<>("valoracionTutor"));
        //COLUMNA EXPIRADO
        TableColumn<Rutina, Boolean> expiradoColumn = new TableColumn<>("EXPIRADO");
        expiradoColumn.setCellValueFactory(new PropertyValueFactory<>("expirado"));
        //COLUMNA TUTOR
        TableColumn<Rutina, Tutor> tutorColumn = new TableColumn<>("TUTOR");
        tutorColumn.setCellValueFactory(new PropertyValueFactory<>("tutor"));
        //COLUMNA CLIENTE
        TableColumn<Rutina, Cliente> clienteColumn = new TableColumn<>("CLIENTE");
        clienteColumn.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        tablaRutina.getColumns().add(idColumn);
        tablaRutina.getColumns().add(diaColumn);
        tablaRutina.getColumns().add(fechaColumn);
        tablaRutina.getColumns().add(duracionColumn);
        tablaRutina.getColumns().add(valoracionColumn);
        tablaRutina.getColumns().add(tutorColumn);
        tablaRutina.getColumns().add(clienteColumn);
        tablaRutina.getColumns().add(expiradoColumn);

        RefreshTableView();
    }

    public void RefreshTableView() {
        tablaRutina.getItems().clear();
        tablaRutina.refresh();
        if (App.getIsCliente()){
            tablaRutina.getItems().addAll(Rutina.getRutinasHistorial(App.getIdCliente(), true));
        }else{
            tablaRutina.getItems().addAll(Rutina.getRutinasHistorial(App.getIdTutor(), false));
        }
    }
    

    @FXML
    private void clickValorar() throws IOException {
        try {
            if (tablaRutina.getSelectionModel().getSelectedIndex() != -1) {
                Rutina rut = (Rutina) tablaRutina.getSelectionModel().getSelectedItem();
                Integer valor = Integer.parseInt(valoracion.getSelectionModel().getSelectedItem().toString());
                rut.setValoracionTutor(valor);
                Rutina.modificarRutina(rut);
                App.MsgBox(Alert.AlertType.INFORMATION, "EXITO", "Se valoró al tutor: " + rut.getTutor().getNombres(), "ID del tutor: " + rut.getTutor().getDni());
                RefreshTableView();
            }
        } catch (NumberFormatException e) {
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
