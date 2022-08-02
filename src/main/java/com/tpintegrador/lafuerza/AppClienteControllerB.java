package com.tpintegrador.lafuerza;

import com.tpintegrador.lafuerza.modelo.Cliente;
import com.tpintegrador.lafuerza.modelo.Rutina;
import com.tpintegrador.lafuerza.servicio.EjercicioService;
import com.tpintegrador.lafuerza.servicio.TutorService;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class AppClienteControllerB implements Initializable {

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
    private TableView tablaRutinasActivas;
    @FXML
    private TableView tablaHistorial;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnNueva;
    @FXML
    private Label labelAlerta;
    @FXML
    private Label labelV1;
    @FXML
    private Label labelV2;
    @FXML
    private ComboBox calculoGM;
    @FXML
    private DatePicker calculoFecha;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Cliente cli = clienteService.findClienteById(App.getIdCliente());
        Cliente cli = Cliente.buscarCliente(App.getIdCliente());
        ObservableList<String> items = FXCollections.observableArrayList();
        items.addAll("Masculino", "Femenino");
        formSexo.setItems(items);
        ObservableList<String> items2 = FXCollections.observableArrayList();
        items2.addAll("Grande", "Medio", "Pequeño");
        calculoGM.setItems(items2);
        calculoFecha.setValue(LocalDate.now());

        formDni.setText(cli.getDni().toString());
        formDni.setDisable(true);
        formNombres.setText(cli.getNombres());
        formNombres.setDisable(true);
        formApellidos.setText(cli.getApellidos());
        formApellidos.setDisable(true);
        formFechaNac.setValue(cli.getFechaNacimiento());
        formFechaNac.setDisable(true);
        formFechaNac.getEditor().setOpacity(1);
        formSexo.setValue(cli.getSexo());
        formSexo.setDisable(true);
        formFechaIng.setValue(cli.getFechaIngreso());
        formFechaIng.setDisable(true);
        formFechaIng.getEditor().setOpacity(1);
        formMedico.setText(cli.getConsideracionMedica());
        formMedico.setDisable(true);

        if (TutorService.findTutores().isEmpty()) {
            if ("".equals(labelAlerta.getText())) {
                labelAlerta.setText("NO HAY TUTORES REGISTRADOS");
            } else {
                labelAlerta.setText(labelAlerta.getText() + ", NO HAY TUTORES REGISTRADOS");
            }
            btnNueva.setDisable(true);
        }

        if (EjercicioService.findEjercicios().isEmpty()) {
            if ("".equals(labelAlerta.getText())) {
                labelAlerta.setText("NO HAY EJERCICIOS REGISTRADOS");
            } else {
                labelAlerta.setText(labelAlerta.getText() + ", NO HAY EJERCICIOS REGISTRADOS");
            }
            btnNueva.setDisable(true);
        }

        if (Rutina.getRutinasActivas(App.getIdCliente(), true).size() >= 7) {
            labelAlerta.setText("MAXIMO 7 RUTINAS ACTIVAS");
            btnNueva.setDisable(true);
        }

        //TABLA ACTIVAS
        //COLUMNA DIA
        TableColumn<Rutina, String> diaColumn = new TableColumn<>("DIA");
        diaColumn.setMinWidth(100);
        diaColumn.setCellValueFactory(new PropertyValueFactory<>("nombreDia"));
        //COLUMNA FECHA INICIO
        TableColumn<Rutina, LocalDate> fechaColumn = new TableColumn<>("DIA INICIO");
        fechaColumn.setMinWidth(100);
        fechaColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        //COLUMNA SEMANAS RESTANTES
        TableColumn<Rutina, Integer> duracionColumn = new TableColumn<>("SEMANAS RESTANTES");
        duracionColumn.setMinWidth(100);
        duracionColumn.setCellValueFactory(new PropertyValueFactory<>("duracion"));

        tablaRutinasActivas.getColumns().add(diaColumn);
        tablaRutinasActivas.getColumns().add(fechaColumn);
        tablaRutinasActivas.getColumns().add(duracionColumn);

        //TABLA ACTIVAS
        //COLUMNA DIA
        TableColumn<Rutina, String> diaColumn2 = new TableColumn<>("DIA");
        diaColumn2.setMinWidth(150);
        diaColumn2.setResizable(false);
        diaColumn2.setCellValueFactory(new PropertyValueFactory<>("nombreDia"));
        //COLUMNA FECHA INICIO
        TableColumn<Rutina, LocalDate> fechaColumn2 = new TableColumn<>("DIA INICIO");
        fechaColumn2.setMinWidth(150);
        fechaColumn2.setResizable(false);
        fechaColumn2.setCellValueFactory(new PropertyValueFactory<>("fecha"));

        tablaHistorial.getColumns().add(diaColumn2);
        tablaHistorial.getColumns().add(fechaColumn2);

        RefreshTableView();
    }

//    public ObservableList<Rutina> getRutinasActivas() {
//        ObservableList<Rutina> rutinas = FXCollections.observableArrayList();
//        RutinaService.findRutinas().forEach(rutina -> {
//            if ((!(rutina.isExpirado()) && rutina.getCliente().getDni().equals(App.getIdCliente()))) {
//                rutinas.add(rutina);
//            }
//        });
//        return rutinas;
//    }

//    public ObservableList<Rutina> getRutinasHistorial() {
//        ObservableList<Rutina> rutinas = FXCollections.observableArrayList();
//        RutinaService.findRutinas().forEach(rutina -> {
//            if (rutina.isExpirado() && rutina.getCliente().getDni().equals(App.getIdCliente())) {
//                rutinas.add(rutina);
//            }
//        });
//        return rutinas;
//    }

    public void RefreshTableView() {
        tablaRutinasActivas.getItems().clear();
        tablaHistorial.getItems().clear();
        tablaRutinasActivas.refresh();
        tablaHistorial.refresh();
        tablaRutinasActivas.getItems().addAll(Rutina.getRutinasActivas(App.getIdCliente(), true));
        tablaHistorial.getItems().addAll(Rutina.getRutinasHistorial(App.getIdCliente(), true));
    }

    @FXML
    private void clickNueva() throws IOException {
        App.setRoot("VistaRutinaEntrenamiento");
    }

    @FXML
    private void clickEditar() throws IOException {

        if ("Editar".equals(btnEditar.getText())) {
            formNombres.setDisable(false);
            formApellidos.setDisable(false);
            formFechaNac.setDisable(false);
            formSexo.setDisable(false);
            formFechaIng.setDisable(false);
            formMedico.setDisable(false);
            btnEditar.setText("Guardar");
        } else {
            try {
                if (!"".equals(formDni.getText())) {
                    if (!"".equals(formNombres.getText())) {
                        if (!"".equals(formApellidos.getText())) {
                            if (formFechaNac.getValue() != null) {
                                if (formSexo.getValue() != null) {
                                    if (formFechaIng.getValue() != null) {
                                        if (!"".equals(formMedico.getText())) {
//                                            Cliente cliente = new Cliente();
                                            Long dni = App.getIdCliente();
                                            String nombres = formNombres.getText();
                                            String apellidos = formApellidos.getText();
                                            LocalDate fechaNacimiento = formFechaNac.getValue();
                                            String sexo = formSexo.getSelectionModel().getSelectedItem().toString();
                                            LocalDate fechaIngreso = formFechaIng.getValue();
                                            String consideracionMedica = formMedico.getText();
                                           
                                            Cliente.modificarCliente(dni, nombres, apellidos, fechaNacimiento, sexo, fechaIngreso, consideracionMedica);
//                                            cliente.setDni(App.getIdCliente());
//                                            cliente.setNombres(nombres);
//                                            cliente.setApellidos(apellidos);
//                                            cliente.setFechaNacimiento(fechaNacimiento);
//                                            cliente.setSexo(sexo);
//                                            cliente.setFechaIngreso(fechaIngreso);
//                                            cliente.setConsideracionMedica(consideracionMedica);
//                                            cliente.setRutinas(clienteService.findClienteById(App.getIdCliente()).getRutinas());
//                                            clienteService.modificarCliente(cliente);
                                            
                                            btnEditar.setText("Editar");
                                            formNombres.setDisable(true);
                                            formApellidos.setDisable(true);
                                            formFechaNac.setDisable(true);
                                            formSexo.setDisable(true);
                                            formFechaIng.setDisable(true);
                                            formMedico.setDisable(true);
                                        } else {
                                            App.MsgBox(Alert.AlertType.WARNING, "WARNING", "Ingrese un valor para el campo CONSIDERACIONES MEDICAS", "El valor: '" + formMedico.getText() + "' no es un valor valido para el campo CONSIDERACIONES MEDICAS");
                                        }
                                    } else {
                                        App.MsgBox(Alert.AlertType.WARNING, "WARNING", "Ingrese un valor para el campo FECHA INGRESO", "El valor: '" + formFechaIng.getValue() + "' no es un valor valido para el campo FECHA INGRESO");
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
    }

    @FXML
    private void calcularGm() throws IOException {
        try {
            if (calculoGM.getSelectionModel().getSelectedIndex() != -1) {
                String aux;
                switch (calculoGM.getSelectionModel().getSelectedIndex()) {
                    case 0:
                        aux = "GRANDE";
                        break;
                    case 1:
                        aux = "MEDIO";
                        break;
                    case 2:
                        aux = "PEQUEÑO";
                        break;
                    default:
                        aux = "";
                        break;
                }
                
//                List<Rutina> rutinasCliente = new ArrayList<>();
//                RutinaService.findRutinas().forEach(rutina -> {
//                    if (rutina.getCliente().getDni().equals(App.getIdCliente()) && !rutina.isExpirado()) {
//                        rutinasCliente.add(rutina);
//                    }
//                });
//
//                //Aca empieza el arbol de busqueda
//                Set<Entrenamiento> setEntrenamientos;
//                Set<Ejercicio> setEjercicios;
//                int indice; //Es el indice de las listas SERIES, REPETICIONES, PESOS
//                int auxiliar; //Determina la posicion del musculo referido dentro de los elementos en las listas
//                Integer repeticiones, series;
//                Double peso;
//                List<Double> volumenes = new ArrayList<>();
//                for (Rutina unaRutina : rutinasCliente) {
//                    setEntrenamientos = unaRutina.getEntrenamientos();
//                    for (Entrenamiento unEntrenamiento : setEntrenamientos) {
//                        indice = -1;
//                        auxiliar = -1;
//                        setEjercicios = unEntrenamiento.getEjercicios();
//                        for (Ejercicio unEjercicio : setEjercicios) {
//                            auxiliar++; //Aux es el indice de ejercicios para referenciar en entrenamiento
//                            if (unEjercicio.getMusculo().getTamanioGrupo().toUpperCase().equals(aux.toUpperCase())) {
//                                indice = auxiliar;
//                            }
//                            if (indice > -1) {
//                                peso = unEntrenamiento.getPeso().get(indice);
//                                repeticiones = unEntrenamiento.getRepeticiones().get(indice);
//                                series = unEntrenamiento.getSeries();
//                                volumenes.add(peso * repeticiones * series);
//                                indice = -1;
//                            }
//                        }
//                    }
//                }
//                System.out.println(volumenes);
//                for (Double volumen : volumenes) {
//                    volumenGM += volumen;
//                }
                labelV1.setText(Cliente.calcularVolumenGM(App.getIdCliente(), aux).toString() + " Kg");
            }
        } catch (NumberFormatException e) {
            App.MsgBox(Alert.AlertType.ERROR, "ERROR", "Error inesperado", "Causa:" + e.getMessage());
        }
    }

    @FXML
    private void calcularSm() throws IOException {
        try {
//            Double volumenGM = 0d;
            LocalDate fechaInicio = calculoFecha.getValue().minusDays(7);
            LocalDate fechaFinal = calculoFecha.getValue();
            
//            List<Rutina> rutinasCliente = new ArrayList<>();
//            RutinaService.findRutinas().forEach(rutina -> {
//                if (rutina.getCliente().getDni().equals(App.getIdCliente())) {
//                    if (!rutina.getFecha().isBefore(fechaInicio) && !rutina.getFecha().isAfter(fechaFinal)) {
//                        rutinasCliente.add(rutina);
//                    }
//                }
//            });
//            //Aca empieza el arbol de busqueda
//            Set<Entrenamiento> setEntrenamientos;
//            List<Double> volumenes = new ArrayList<>();
//            for (Rutina unaRutina : rutinasCliente) {
//                setEntrenamientos = unaRutina.getEntrenamientos();
//                for (Entrenamiento unEntrenamiento : setEntrenamientos) {
//                    volumenes.add(unEntrenamiento.getVolumen());
//                }
//            }
//            System.out.println(volumenes);
//            for (Double volumen : volumenes) {
//                volumenGM += volumen;
//            }
            labelV2.setText(Cliente.calcularVolumenF(App.getIdCliente(), fechaInicio, fechaFinal).toString() + " Kg");
        } catch (NumberFormatException e) {
            App.MsgBox(Alert.AlertType.ERROR, "ERROR", "Error inesperado", "Causa:" + e.getMessage());
        }
    }
    
    @FXML
    private void clickAdminRutina() throws IOException{
        if (tablaRutinasActivas.getSelectionModel().getSelectedIndex() != -1){
            Rutina rut = (Rutina) tablaRutinasActivas.getSelectionModel().getSelectedItem();
            System.out.println(rut.getId());
            App.setIdRutina(rut.getId());
            App.setRoot("VistaRutinaActiva");
        }
    }

    @FXML
    private void clickHistorial() throws IOException{
        App.setRoot("VistaRutinaHistorial");
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
