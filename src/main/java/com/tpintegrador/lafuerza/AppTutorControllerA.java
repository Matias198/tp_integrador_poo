/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpintegrador.lafuerza;

import com.tpintegrador.lafuerza.modelo.Persona;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author matia
 */
public class AppTutorControllerA implements Initializable {

    //Formulario
    @FXML
    private TextField formDni;
    @FXML
    private TextField formNombres;
    @FXML
    private TextField formApellidos;
    @FXML
    private DatePicker formFechaNac;
    @FXML
    private ComboBox formSexo;
    @FXML
    private ComboBox formEsp;
    @FXML
    private TableView tablaTutores;
    @FXML
    private TextField searchBoxDni;
    @FXML
    private ListView listaEsp;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> items0 = FXCollections.observableArrayList();
        ObservableList<String> items1 = FXCollections.observableArrayList();
        items0.addAll("Masculino", "Femenino");
        items1.addAll("Hipertrofia", "Fuerza", "Potencia", "Resistencia");
        formSexo.setItems(items0);
        formEsp.setItems(items1);
        formFechaNac.getEditor().setDisable(true);
        formFechaNac.getEditor().setOpacity(1);

        //TABLA
        //COLUMNA DNI
        TableColumn<Tutor, Long> dniColumn = new TableColumn<>("DNI");
        dniColumn.setMinWidth(120);
        dniColumn.setResizable(false);
        dniColumn.setCellValueFactory(new PropertyValueFactory<>("dni"));
        //COLUMNA NOMBRES
        TableColumn<Tutor, String> nombresColumn = new TableColumn<>("NOMBRES");
        nombresColumn.setMinWidth(120);
        nombresColumn.setResizable(false);
        nombresColumn.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        //COLUMNA APELLIDOS
        TableColumn<Tutor, String> apellidosColumn = new TableColumn<>("APELLIDOS");
        apellidosColumn.setMinWidth(120);
        apellidosColumn.setResizable(false);
        apellidosColumn.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        //COLUMNA FECHA NACIMIENTO
        TableColumn<Tutor, LocalDate> fecNacColumn = new TableColumn<>("NACIMIENTO");
        fecNacColumn.setMinWidth(120);
        fecNacColumn.setResizable(false);
        fecNacColumn.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));
        //COLUMNA SEXO
        TableColumn<Tutor, String> sexoColumn = new TableColumn<>("SEXO");
        sexoColumn.setMinWidth(120);
        sexoColumn.setResizable(false);
        sexoColumn.setCellValueFactory(new PropertyValueFactory<>("sexo"));

        tablaTutores.getColumns().add(dniColumn);
        tablaTutores.getColumns().add(nombresColumn);
        tablaTutores.getColumns().add(apellidosColumn);
        tablaTutores.getColumns().add(fecNacColumn);
        tablaTutores.getColumns().add(sexoColumn);
        RefreshTableView();
    }

    public void RefreshTableView() {
        tablaTutores.getItems().clear();
        tablaTutores.refresh();
        tablaTutores.getItems().addAll(Tutor.getTutores());
    }

    @FXML
    private void clickIns() throws IOException {
        try {
            if (!"".equals(formDni.getText())) {
                if (!"".equals(formNombres.getText())) {
                    if (!"".equals(formApellidos.getText())) {
                        if (formFechaNac.getValue() != null) {
                            if (formSexo.getValue() != null) {
                                if (!"".equals(listaEsp.getItems().toString())) {
                                    Long dni = Long.valueOf(formDni.getText());
                                    boolean valor = (Persona.buscarPersona(dni) == null);
                                    if (valor) {
                                        String nombres = formNombres.getText();
                                        String apellidos = formApellidos.getText();
                                        LocalDate fechaNacimiento = formFechaNac.getValue();
                                        String sexo = formSexo.getSelectionModel().getSelectedItem().toString();
                                        String especialidades = "";
                                        for (int i = 0; i < listaEsp.getItems().size(); i++) {
                                            especialidades += listaEsp.getItems().get(i).toString() + " ";
                                        }

                                        Tutor.agregarTutor(especialidades, null, dni, nombres, apellidos, fechaNacimiento, sexo);
//                                        tutorService.crearTutor(tutorService.agregarDatosTutor(especialidades, Long.valueOf(dni), nombres, apellidos, fechaNacimiento, sexo));
                                        RefreshTableView();
                                        App.MsgBox(Alert.AlertType.INFORMATION, "EXITO", "Se completo exitosamente la inscripcion del nuevo tutor al sistema :D", "Nuevo tutor inscripto con el ID: " + formDni.getText());
                                        formDni.clear();
                                        formNombres.clear();
                                        formApellidos.clear();
                                        formFechaNac.setValue(null);
                                        formSexo.setValue(null);
                                        listaEsp.getItems().clear();
                                    } else {
                                        App.MsgBox(Alert.AlertType.WARNING, "WARNING", "El DNI ingresado no es valido", "El valor: '" + formDni.getText() + "' ya se encuentra registrado en el sistema");
                                    }
                                } else {
                                    App.MsgBox(Alert.AlertType.WARNING, "WARNING", "Ingrese un valor para el campo ESPECIALIDADES", "El valor: '" + listaEsp.getItems().toString() + "' no es un valor valido para el campo ESPECIALIDADES");
                                }
                            } else {
                                App.MsgBox(Alert.AlertType.WARNING, "WARNING", "Ingrese un valor para el campo SEXO", "El valor: '" + formSexo.getValue() + "' no es un valor valido para el campo SEXO");
                            }
                        } else {
                            App.MsgBox(Alert.AlertType.WARNING, "WARNING", "Ingrese un valor para el campo FECHA NACIMIENTO", "El valor: '" + formFechaNac.getValue() + "' no es un valor valido para el campo FECHA NACIMIENTO");
                        }
                    } else {
                        App.MsgBox(Alert.AlertType.WARNING, "WARNING", "Ingrese un valor para el campo APELLIDOS", "El valor: '" + formApellidos.getText() + "' no es un valor valido para el campo APELLIDOS");
                    }
                } else {
                    App.MsgBox(Alert.AlertType.WARNING, "WARNING", "Ingrese un valor para el campo NOMBRES", "El valor: '" + formNombres.getText() + "' no es un valor valido para el campo NOMBRES");
                }
            } else {
                App.MsgBox(Alert.AlertType.WARNING, "WARNING", "Ingrese un valor para el campo DNI", "El valor: '" + formDni.getText() + "' no es un valor valido para el campo DNI");
            }
        } catch (NumberFormatException e) {
            App.MsgBox(Alert.AlertType.ERROR, "ERROR", "Error en los datos de inscripción", "Causa:" + e.getMessage());
        }
    }

    @FXML
    private void clickEliminar() throws IOException {
        try {
            if (tablaTutores.getSelectionModel().getSelectedIndex() != -1) {
                Tutor tuto = (Tutor) tablaTutores.getSelectionModel().getSelectedItem();
                Tutor.eliminarTutor(tuto);
                App.MsgBox(Alert.AlertType.INFORMATION, "EXITO", "Se completo exitosamente la baja del tutor", "Se dio de baja al tutor con el ID: " + tuto.getDni());
                RefreshTableView();
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @FXML
    private void clickBuscar() throws IOException {
        try {
            Long dni = Long.valueOf(searchBoxDni.getText());
            int id = -1;
            Tutor tuto;
            for (int i = 0; i < tablaTutores.getItems().size(); i++) {
                tuto = (Tutor) tablaTutores.getItems().get(i);
                if (tuto.getDni().equals(dni)) {
                    id = i;
                    System.out.println("EXITO, ID: " + id);
                    tablaTutores.getSelectionModel().select(tuto);
                    tablaTutores.requestFocus();
                }
            }
            if (id == -1) {
                App.MsgBox(Alert.AlertType.WARNING, "WARNING", "No se encontró el tutor con el DNI especificado", "El DNI " + dni + " no se encuentra registrado.");
            }
        } catch (NumberFormatException e) {
            App.MsgBox(Alert.AlertType.ERROR, "ERROR", "El valor introducido para la busqueda no es valido", "El valor " + searchBoxDni.getText() + " no es un numero de DNI. " + e.getMessage());
        }
    }

    @FXML
    private void clickAgregar() throws IOException {
        int indice = formEsp.getSelectionModel().getSelectedIndex();
        try {
            if (indice != -1) {
                if (!(listaEsp.getItems().contains(formEsp.getItems().get(indice)))) {
                    listaEsp.getItems().add(formEsp.getItems().get(indice));
                }
            }
        } catch (Exception e) {
            App.MsgBox(Alert.AlertType.ERROR, "ERROR", "Error inesperado", "Causa:" + e.getMessage());
        }
    }

    @FXML
    private void clickBorrarLista() throws IOException {
        int indice = listaEsp.getSelectionModel().getSelectedIndex();
        System.out.println(indice);
        try {
            if (indice != -1) {
                listaEsp.getItems().remove(indice);
            } else if (listaEsp.getItems().size() > 0) {
                listaEsp.getItems().remove(0);
            }
        } catch (Exception e) {
            App.MsgBox(Alert.AlertType.ERROR, "ERROR", "Error inesperado", "Causa:" + e.getMessage());
        }

    }

    @FXML
    private void clickAdministrar() throws IOException {
        if (tablaTutores.getSelectionModel().getSelectedIndex() != -1) {
            Tutor tut = (Tutor) tablaTutores.getSelectionModel().getSelectedItem();
            System.out.println(tut.getDni());
            App.setIdTutor(tut.getDni());
            System.out.println(App.getIdTutor());
            App.setIsCliente(false);
            App.setRoot("VistaTutorB");
        }
    }

    @FXML
    private void clickLimpiar() throws IOException {
        listaEsp.getItems().clear();
    }

    @FXML
    private void inicio() throws IOException {
        App.setRoot("VistaLaFuerza");
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
