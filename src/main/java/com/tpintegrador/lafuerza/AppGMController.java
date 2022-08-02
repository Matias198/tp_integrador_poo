/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpintegrador.lafuerza;

import com.tpintegrador.lafuerza.modelo.GrupoMuscular;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author matia
 */
public class AppGMController implements Initializable {
    //Servicio de cliente 
    
    private int indiceGeneral;

    @FXML
    private TextField formNombre;
    @FXML
    private ComboBox formGM;
    @FXML
    private TableView tablaMusculos;
    @FXML
    private TextField searchBoxMusculo;
    @FXML
    private ComboBox searchBoxGM;
    @FXML
    private Button btnAdd;
    @FXML
    private Label indice;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnBuscar;

    public AppGMController() {
        this.indiceGeneral = 0;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> items = FXCollections.observableArrayList();
        ObservableList<String> items2 = FXCollections.observableArrayList();
        items.addAll("Grande", "Medio", "Pequeño");
        formGM.setItems(items);
        items2.addAll("Todos", "Grande", "Medio", "Pequeño");
        searchBoxGM.setItems(items2);

        //TABLA
        //COLUMNA ID
        TableColumn<GrupoMuscular, Long> idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(200);
        idColumn.setResizable(false);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        //COLUMNA MUSCULOS
        TableColumn<GrupoMuscular, String> nombreColumn = new TableColumn<>("MUSCULO");
        nombreColumn.setMinWidth(200);
        nombreColumn.setResizable(false);
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("musculo"));
        //COLUMNA GRUPO
        TableColumn<GrupoMuscular, String> grupoColumn = new TableColumn<>("GRUPO");
        grupoColumn.setMinWidth(200);
        grupoColumn.setResizable(false);
        grupoColumn.setCellValueFactory(new PropertyValueFactory<>("tamanioGrupo"));

        tablaMusculos.getColumns().add(idColumn);
        tablaMusculos.getColumns().add(nombreColumn);
        tablaMusculos.getColumns().add(grupoColumn);
        RefreshTableView();
    }

    public void RefreshTableView() {
        tablaMusculos.getItems().clear();
        tablaMusculos.refresh();
        tablaMusculos.getItems().addAll(GrupoMuscular.getGrupoMuscular());
    }

    @FXML
    private void clickAdd() throws IOException {
        try {
            if (!"".equals(formNombre.getText())) {
                if (formGM.getValue() != null) {
                    String nombre = formNombre.getText().toUpperCase();
                    String gm = formGM.getSelectionModel().getSelectedItem().toString().toUpperCase();
                    if (">>>".equals(btnAdd.getText())) {
                        GrupoMuscular grupomuscular = new GrupoMuscular();
                        grupomuscular.setMusculo(nombre);
                        grupomuscular.setTamanioGrupo(gm);
                        grupomuscular.setEjercicios(null);
                        GrupoMuscular.agregarGrupoMuscular(grupomuscular);
                        App.MsgBox(Alert.AlertType.INFORMATION, "EXITO", "Se agregó exitosamente el nuevo grupo muscular", "Nuevo musculo agregado: " + formNombre.getText());
                    } else {
                        Long id = Long.valueOf(indice.getText());
                        System.out.println(indice.getText());
                        GrupoMuscular.modificarEjercicio(id, nombre, gm, GrupoMuscular.buscarGrupoMuscular(id).getEjercicios());
                        App.MsgBox(Alert.AlertType.INFORMATION, "EXITO", "Se modificó el grupo muscular con ID: " + indice.getText(), "Musculo modificado: " + nombre);
                        btnAdd.setText(">>>");
                        indice.setText("");
                        btnEliminar.disableProperty().set(false);
                        searchBoxGM.disableProperty().set(false);
                        searchBoxGM.getSelectionModel().select(0);
                        btnBuscar.disableProperty().set(false);
                    }
                    RefreshTableView();
                    formNombre.clear();
                    formGM.setValue(null);
                } else {
                    App.MsgBox(Alert.AlertType.WARNING, "WARNING", "Ingrese un valor para el campo grupo muscular", "El valor: '" + formGM.getValue() + "' no es un valor valido para el campo grupo muscular");
                }
            } else {
                App.MsgBox(Alert.AlertType.WARNING, "WARNING", "Ingrese un valor para el campo nombre", "El valor: '" + formNombre.getText() + "' no es un valor valido para el campo nombre");
            }

        } catch (NumberFormatException e) {
            App.MsgBox(Alert.AlertType.ERROR, "ERROR", "Error en los datos", "Causa:" + e.getMessage());
        }
    }

    @FXML
    private void clickEliminar() throws IOException {
        try {
            if (tablaMusculos.getSelectionModel().getSelectedIndex() != -1) {
                GrupoMuscular gm = (GrupoMuscular) tablaMusculos.getSelectionModel().getSelectedItem();
                GrupoMuscular.eliminarGrupoMuscular(gm);
                App.MsgBox(Alert.AlertType.INFORMATION, "EXITO", "Se eliminó con exito el musculo del grupo muscular", "Se dio de baja al musculo " + gm.getMusculo() + ". ID: " + gm.getId());
                RefreshTableView();
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @FXML
    private void Buscar() throws IOException {
        try {
            String m = searchBoxMusculo.getText().toUpperCase();
            int id = -1;
            GrupoMuscular gm;
            for (int i = indiceGeneral; i < tablaMusculos.getItems().size(); i++) {
                gm = (GrupoMuscular) tablaMusculos.getItems().get(i);
                if (gm.getMusculo().equals(m) || gm.getMusculo().contains(m)) {
                    id = i;
                    System.out.println("EXITO, ID: " + id);
                    tablaMusculos.getSelectionModel().select(gm);
                    tablaMusculos.requestFocus();
                    indiceGeneral = i + 1;
                    break;
                }
            }
            if (id == -1) {
                indiceGeneral = 0;
                App.MsgBox(Alert.AlertType.WARNING, "WARNING", "No se encontró el musculo especificado", "No se encuentra el registro o se llegó al final de la lista.");
            }
        } catch (NumberFormatException e) {
            App.MsgBox(Alert.AlertType.ERROR, "ERROR", "El valor introducido para la busqueda no es valido", "Mensaje de error: " + e.getMessage());
        }
    }

    @FXML
    private void clickEditar() throws IOException {
        try {
            if (tablaMusculos.getSelectionModel().getSelectedIndex() != -1) {
                btnEliminar.disableProperty().set(true);
                searchBoxGM.disableProperty().set(true);
                btnBuscar.disableProperty().set(true);
                btnAdd.setText("Guardar");
                GrupoMuscular gm = (GrupoMuscular) tablaMusculos.getSelectionModel().getSelectedItem();
                indice.setText(String.valueOf(gm.getId()));
                formNombre.setText(gm.getMusculo());
                formGM.getSelectionModel().select(gm.getTamanioGrupo());
            }
        } catch (Exception e) {
            App.MsgBox(Alert.AlertType.ERROR, "ERROR", "Error inesperado", "Mensaje de error: " + e.getMessage());
        }
    }

    public void RefreshTableViewFiltered(String filtro) {
        try {
            if (filtro.equals("TODOS")) {
                RefreshTableView();
            } else {
                ObservableList<GrupoMuscular> gmsf = FXCollections.observableArrayList();
                GrupoMuscular.getGrupoMuscular().forEach(gm -> {
                    if (gm.getTamanioGrupo().toUpperCase().equals(filtro)) {
                        gmsf.add(gm);
                    }
                });
                tablaMusculos.getItems().clear();
                tablaMusculos.refresh();
                tablaMusculos.getItems().addAll(gmsf);
            }
        } catch (Exception e) {
            App.MsgBox(Alert.AlertType.ERROR, "ERROR", "Error inesperado", "Mensaje de error: " + e.getMessage());
        }
    }

    @FXML
    private void selectedItemChanged() throws IOException {
        RefreshTableViewFiltered(searchBoxGM.getSelectionModel().getSelectedItem().toString().toUpperCase());
        System.out.println(searchBoxGM.getSelectionModel().getSelectedItem().toString().toUpperCase());
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
    private void ejercicios() throws IOException {
        App.setRoot("VistaEjercicio");
    }

    @FXML
    private void clientes() throws IOException {
        App.setRoot("VistaClienteA");
    }

    @FXML
    private void clickSalir() throws IOException {
        App.close();
    }

}
