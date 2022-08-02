package com.tpintegrador.lafuerza;

import com.tpintegrador.lafuerza.modelo.Cliente;
import com.tpintegrador.lafuerza.modelo.Persona;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class AppClienteControllerA implements Initializable {

    
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
    private DatePicker formFechaIng;
    @FXML
    private TextArea formMedico;
    @FXML
    private TableView tablaClientes;
    @FXML
    private TextField searchBoxDni;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ObservableList<String> items = FXCollections.observableArrayList();
        items.addAll("Masculino", "Femenino");
        formSexo.setItems(items);
        formFechaIng.getEditor().setDisable(true);
        formFechaIng.getEditor().setOpacity(1);
        formFechaNac.getEditor().setDisable(true);
        formFechaNac.getEditor().setOpacity(1);
        formFechaIng.setValue(LocalDate.now());

        //TABLA
        //COLUMNA DNI
        TableColumn<Cliente, Long> dniColumn = new TableColumn<>("DNI");
        dniColumn.setMinWidth(100);
        dniColumn.setResizable(false);
        dniColumn.setCellValueFactory(new PropertyValueFactory<>("dni"));
        //COLUMNA NOMBRES
        TableColumn<Cliente, String> nombresColumn = new TableColumn<>("NOMBRES");
        nombresColumn.setMinWidth(100);
        nombresColumn.setResizable(false);
        nombresColumn.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        //COLUMNA APELLIDOS
        TableColumn<Cliente, String> apellidosColumn = new TableColumn<>("APELLIDOS");
        apellidosColumn.setMinWidth(100);
        apellidosColumn.setResizable(false);
        apellidosColumn.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        //COLUMNA FECHA NACIMIENTO
        TableColumn<Cliente, LocalDate> fecNacColumn = new TableColumn<>("NACIMIENTO");
        fecNacColumn.setMinWidth(100);
        fecNacColumn.setResizable(false);
        fecNacColumn.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));
        //COLUMNA SEXO
        TableColumn<Cliente, String> sexoColumn = new TableColumn<>("SEXO");
        sexoColumn.setMinWidth(100);
        sexoColumn.setResizable(false);
        sexoColumn.setCellValueFactory(new PropertyValueFactory<>("sexo"));
        //COLUMNA FECHA INGRESO
        TableColumn<Cliente, LocalDate> fecIngColumn = new TableColumn<>("INGRESO");
        fecIngColumn.setMinWidth(100);
        fecIngColumn.setResizable(false);
        fecIngColumn.setCellValueFactory(new PropertyValueFactory<>("fechaIngreso"));

        tablaClientes.getColumns().add(dniColumn);
        tablaClientes.getColumns().add(nombresColumn);
        tablaClientes.getColumns().add(apellidosColumn);
        tablaClientes.getColumns().add(fecNacColumn);
        tablaClientes.getColumns().add(sexoColumn);
        tablaClientes.getColumns().add(fecIngColumn);
        RefreshTableView();
    }

//ACA
//    public ObservableList<Cliente> getClientes() {
//        ObservableList<Cliente> clientes = FXCollections.observableArrayList();
//        clienteService.findClientes().forEach(cliente -> {
//            clientes.add(cliente);
//        });
//        return clientes;
//    }

    public void RefreshTableView() {
        tablaClientes.getItems().clear();
        tablaClientes.refresh();
//        tablaClientes.getItems().addAll(getClientes());
        tablaClientes.getItems().addAll(Cliente.getClientes());
    }

    @FXML
    private void clickInscribir() throws IOException {
        try {
            if (!"".equals(formDni.getText())) {
                if (!"".equals(formNombres.getText())) {
                    if (!"".equals(formApellidos.getText())) {
                        if (formFechaNac.getValue() != null) {
                            if (formSexo.getValue() != null) {
                                if (formFechaIng.getValue() != null) {
                                    if (!"".equals(formMedico.getText())) {
//                                        int j = 0;
//                                        while (valor){
//                                            
//                                            j++;
//                                        }

//                                        for (int i = 0; i < PersonaService.findPersonas().size(); i++) {
//                                            if (PersonaService.findPersonas().get(i).getDni().equals(Long.valueOf(formDni.getText()))) {
//                                                valor = false;
//                                            }
//                                        }
                                        Long dni = Long.valueOf(formDni.getText());
                                        boolean valor = (Persona.buscarPersona(dni) == null);
                                        if (valor) {
                                            
                                            String nombres = formNombres.getText();
                                            String apellidos = formApellidos.getText();
                                            LocalDate fechaNacimiento = formFechaNac.getValue();
                                            String sexo = formSexo.getSelectionModel().getSelectedItem().toString();
                                            LocalDate fechaIngreso = formFechaIng.getValue();
                                            String consideracionMedica = formMedico.getText();
                                            
//                                            clienteService.crearCliente(clienteService.agregarDatosCliente(fechaIngreso, consideracionMedica, Long.valueOf(dni), nombres, apellidos, fechaNacimiento, sexo));
                                            Cliente.agregarCliente(fechaIngreso, consideracionMedica, Long.valueOf(dni), nombres, apellidos, fechaNacimiento, sexo);
                                            
                                            RefreshTableView();
                                            App.MsgBox(AlertType.INFORMATION, "EXITO", "Se completo exitosamente la inscripcion del nuevo cliente al sistema :D", "Nuevo cliente inscripto con el ID: " + formDni.getText());
                                            formDni.clear();
                                            formNombres.clear();
                                            formApellidos.clear();
                                            formFechaNac.setValue(null);
                                            formSexo.setValue(null);
                                            formFechaIng.setValue(LocalDate.now());
                                            formMedico.clear();
                                        } else {
                                            App.MsgBox(Alert.AlertType.WARNING, "WARNING", "El DNI ingresado no es valido", "El valor: '" + formDni.getText() + "' ya se encuentra registrado en el sistema");
                                        }
                                    } else {
                                        App.MsgBox(AlertType.WARNING, "WARNING", "Ingrese un valor para el campo CONSIDERACIONES MEDICAS", "El valor: '" + formMedico.getText() + "' no es un valor valido para el campo CONSIDERACIONES MEDICAS");
                                    }
                                } else {
                                    App.MsgBox(AlertType.WARNING, "WARNING", "Ingrese un valor para el campo FECHA INGRESO", "El valor: '" + formFechaIng.getValue() + "' no es un valor valido para el campo FECHA INGRESO");
                                }
                            } else {
                                App.MsgBox(AlertType.WARNING, "WARNING", "Ingrese un valor para el campo SEXO", "El valor: '" + formSexo.getValue() + "' no es un valor valido para el campo SEXO");
                            }
                        } else {
                            App.MsgBox(AlertType.WARNING, "WARNING", "Ingrese un valor para el campo FECHA NACIMIENTO", "El valor: '" + formFechaNac.getValue() + "' no es un valor valido para el campo FECHA NACIMIENTO");
                        }
                    } else {
                        App.MsgBox(AlertType.WARNING, "WARNING", "Ingrese un valor para el campo APELLIDOS", "El valor: '" + formApellidos.getText() + "' no es un valor valido para el campo APELLIDOS");
                    }
                } else {
                    App.MsgBox(AlertType.WARNING, "WARNING", "Ingrese un valor para el campo NOMBRES", "El valor: '" + formNombres.getText() + "' no es un valor valido para el campo NOMBRES");
                }
            } else {
                App.MsgBox(AlertType.WARNING, "WARNING", "Ingrese un valor para el campo DNI", "El valor: '" + formDni.getText() + "' no es un valor valido para el campo DNI");
            }
        } catch (NumberFormatException e) {
            App.MsgBox(AlertType.ERROR, "ERROR", "Error en los datos de inscripción", "Causa:" + e.getMessage());
        }
    }

    @FXML
    private void clickEliminar() throws IOException {
        try {
            if (tablaClientes.getSelectionModel().getSelectedIndex() != -1) {
                Cliente cli = (Cliente) tablaClientes.getSelectionModel().getSelectedItem();
                Cliente.eliminarCliente(cli);
                
//                clienteService.eliminarClienteById(cli.getDni());

                App.MsgBox(AlertType.INFORMATION, "EXITO", "Se completo exitosamente la baja del cliente", "Se dio de baja al cliente con el ID: " + cli.getDni());
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
            Cliente cli;
            for (int i = 0; i < tablaClientes.getItems().size(); i++) {
                cli = (Cliente) tablaClientes.getItems().get(i);
                if (cli.getDni().equals(dni)) {
                    id = i;
                    System.out.println("EXITO, ID: " + id);
                    tablaClientes.getSelectionModel().select(cli);
                    tablaClientes.requestFocus();
                }
            }
            if (id == -1) {
                App.MsgBox(AlertType.WARNING, "WARNING", "No se encontró el cliente con el DNI especificado", "El DNI " + dni + " no se encuentra registrado.");
            }
        } catch (NumberFormatException e) {
            App.MsgBox(AlertType.ERROR, "ERROR", "El valor introducido para la busqueda no es valido", "El valor " + searchBoxDni.getText() + " no es un numero de DNI. " + e.getMessage());
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

    @FXML
    private void clickAdministrar() throws IOException {
        if (tablaClientes.getSelectionModel().getSelectedIndex() != -1) {
            Cliente cli = (Cliente) tablaClientes.getSelectionModel().getSelectedItem();
            System.out.println(cli.getDni());
            App.setIdCliente(cli.getDni());
            App.setIsCliente(true);
            App.setRoot("VistaClienteB");
        }
    }
}
