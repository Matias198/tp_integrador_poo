/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpintegrador.lafuerza;

import com.tpintegrador.lafuerza.modelo.Cliente;
import com.tpintegrador.lafuerza.modelo.Rutina;
import com.tpintegrador.lafuerza.modelo.Tutor;
import com.tpintegrador.lafuerza.servicio.RutinaService;
import com.tpintegrador.lafuerza.servicio.TutorService;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author matia
 */
public class AppTutorControllerB implements Initializable {

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
    private ListView listaEsp;
    @FXML
    private TableView tablaRutinasActivas;
    @FXML
    private TableView tablaHistorial;
    @FXML
    private Label labelPromedio;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnBorrarLista;
    @FXML
    private Button btnLimpiar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Tutor tut = Tutor.buscarTutor(App.getIdTutor());
        ObservableList<String> items0 = FXCollections.observableArrayList();
        ObservableList<String> items1 = FXCollections.observableArrayList();
        items0.addAll("Masculino", "Femenino");
        items1.addAll("Hipertrofia", "Fuerza", "Potencia", "Resistencia");
        formSexo.setItems(items0);
        formEsp.setItems(items1);
        formFechaNac.getEditor().setDisable(true);
        formFechaNac.getEditor().setOpacity(1);

        formDni.setText(tut.getDni().toString());
        formDni.setDisable(true);
        formNombres.setText(tut.getNombres());
        formNombres.setDisable(true);
        formApellidos.setText(tut.getApellidos());
        formApellidos.setDisable(true);
        formFechaNac.setValue(tut.getFechaNacimiento());
        formFechaNac.setDisable(true);
        formFechaNac.getEditor().setOpacity(1);
        formSexo.setValue(tut.getSexo());
        formSexo.setDisable(true);
        
        String[] parts;
        parts = tut.getEspecialidad().split(" ");
        List<String> lista = new ArrayList<>();
        lista.addAll(Arrays.asList(parts));
        listaEsp.getItems().addAll(lista);
        btnAgregar.setDisable(true);
        btnBorrarLista.setDisable(true);
        btnLimpiar.setDisable(true);
        formEsp.setDisable(true);

        //TABLA ACTIVAS
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
        //COLUMNA CLIENTE
        TableColumn<Rutina, Cliente> clienteColumn = new TableColumn<>("CLIENTE");
        clienteColumn.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        tablaRutinasActivas.getColumns().add(idColumn);
        tablaRutinasActivas.getColumns().add(diaColumn);
        tablaRutinasActivas.getColumns().add(fechaColumn);
        tablaRutinasActivas.getColumns().add(duracionColumn);
        tablaRutinasActivas.getColumns().add(clienteColumn);
        tablaRutinasActivas.getItems().addAll(Rutina.getRutinasActivas(App.getIdTutor(), false));

        //TABLA
        //COLUMNA ID
        TableColumn<Rutina, Long> idHColumn = new TableColumn<>("ID");
        idHColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        //COLUMNA DIA
        TableColumn<Rutina, String> diaHColumn = new TableColumn<>("DIA");
        diaHColumn.setCellValueFactory(new PropertyValueFactory<>("nombreDia"));
        //COLUMNA FECHA
        TableColumn<Rutina, LocalDate> fechaHColumn = new TableColumn<>("FECHA INICIO");
        fechaHColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        //COLUMNA DURACION
        TableColumn<Rutina, Integer> duracionHColumn = new TableColumn<>("DURACION");
        duracionHColumn.setCellValueFactory(new PropertyValueFactory<>("duracion"));
        //COLUMNA VALORACION
        TableColumn<Rutina, Integer> valoracionColumn = new TableColumn<>("VALOR TUTOR");
        valoracionColumn.setCellValueFactory(new PropertyValueFactory<>("valoracionTutor"));
        //COLUMNA EXPIRADO
        TableColumn<Rutina, Boolean> expiradoColumn = new TableColumn<>("EXPIRADO");
        expiradoColumn.setCellValueFactory(new PropertyValueFactory<>("expirado"));
        //COLUMNA CLIENTE
        TableColumn<Rutina, Cliente> clienteHColumn = new TableColumn<>("CLIENTE");
        clienteHColumn.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        tablaHistorial.getColumns().add(idHColumn);
        tablaHistorial.getColumns().add(diaHColumn);
        tablaHistorial.getColumns().add(fechaHColumn);
        tablaHistorial.getColumns().add(duracionHColumn);
        tablaHistorial.getColumns().add(valoracionColumn);
        tablaHistorial.getColumns().add(clienteHColumn);
        tablaHistorial.getColumns().add(expiradoColumn);
        tablaHistorial.getItems().addAll(Rutina.getRutinasHistorial(App.getIdTutor(), false));

        List<Integer> sumatoria = new ArrayList<>();
        Double total = 0d;
        Rutina.getRutinasHistorial(App.getIdTutor(), false).forEach(rutina -> {
            if (rutina.getValoracionTutor() != -1) {
                sumatoria.add(rutina.getValoracionTutor());
            }
        });
        for (int i = 0; i < sumatoria.size(); i++) {
            total += sumatoria.get(i);
        }
        if (!sumatoria.isEmpty()) {
            total = total / sumatoria.size();
            String str = String.format("%.02f", total);
            labelPromedio.setText(String.valueOf(str));
        } else {
            labelPromedio.setText("0");
        }

    }

    @FXML
    private void clickAdminRutina() throws IOException {
        if (tablaRutinasActivas.getSelectionModel().getSelectedIndex() != -1) {
            Rutina rut = (Rutina) tablaRutinasActivas.getSelectionModel().getSelectedItem();
            System.out.println(rut.getId());
            App.setIdRutina(rut.getId());
            App.setRoot("VistaRutinaActiva");
        }
    }

    @FXML
    private void clickHistorial() throws IOException {
        App.setRoot("VistaRutinaHistorial");
    }

    @FXML
    private void clickEditar() throws IOException {
        if ("Editar".equals(btnEditar.getText())) {
            formNombres.setDisable(false);
            formApellidos.setDisable(false);
            formFechaNac.setDisable(false);
            formSexo.setDisable(false);
            formEsp.setDisable(false);
            listaEsp.setDisable(false);
            btnAgregar.setDisable(false);
            btnBorrarLista.setDisable(false);
            btnLimpiar.setDisable(false);
            btnEditar.setText("Guardar");
        } else {
            try {

                if (!"".equals(formNombres.getText())) {
                    if (!"".equals(formApellidos.getText())) {
                        if (formFechaNac.getValue() != null) {
                            if (formSexo.getValue() != null) {
                                if (!listaEsp.getItems().isEmpty()) {
                                    String nombres = formNombres.getText();
                                    String apellidos = formApellidos.getText();
                                    LocalDate fechaNacimiento = formFechaNac.getValue();
                                    String sexo = formSexo.getSelectionModel().getSelectedItem().toString();
                                    String especialidades = "";
                                    for (int i = 0; i < listaEsp.getItems().size(); i++) {
                                        especialidades += listaEsp.getItems().get(i).toString() + " ";
                                    }
                                    Tutor tuto = Tutor.buscarTutor(App.getIdTutor());
                                    tuto.setNombres(nombres);
                                    tuto.setApellidos(apellidos);
                                    tuto.setFechaNacimiento(fechaNacimiento);
                                    tuto.setSexo(sexo);
                                    tuto.setEspecialidad(especialidades);
                                    Tutor.modificarTutor(tuto);
                                    formNombres.setDisable(true);
                                    formApellidos.setDisable(true);
                                    formFechaNac.setDisable(true);
                                    formSexo.setDisable(true);
                                    formEsp.setDisable(true);
                                    listaEsp.setDisable(true);
                                    btnAgregar.setDisable(true);
                                    btnBorrarLista.setDisable(true);
                                    btnLimpiar.setDisable(true);
                                    btnEditar.setText("Editar");
                                } else {
                                    App.MsgBox(Alert.AlertType.WARNING, "WARNING", "Ingrese un valor para el campo ESPECIALIDADES", "La lista de especialidades no puede estar vacía");
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
            } catch (NumberFormatException e) {
                App.MsgBox(Alert.AlertType.ERROR, "ERROR", "Error en los datos de inscripción", "Causa:" + e.getMessage());
            }
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
    private void clickLimpiar() throws IOException {
        listaEsp.getItems().clear();
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
