package com.tpintegrador.lafuerza;

import com.tpintegrador.lafuerza.modelo.Cliente;
import com.tpintegrador.lafuerza.modelo.Ejercicio;
import com.tpintegrador.lafuerza.modelo.Entrenamiento;
import com.tpintegrador.lafuerza.modelo.Rutina;
import com.tpintegrador.lafuerza.modelo.Tutor;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class AppREController implements Initializable {

    private final Set<Entrenamiento> setEntrenamientos;
    private double volumenTotal = 0;
    private final Rutina r;
    private int indiceGeneral;

    @FXML
    private DatePicker formFechaInicio;
    @FXML
    private TextField formDia;
    @FXML
    private ComboBox formEjercicio;
    @FXML
    private Spinner formRepeticiones;
    @FXML
    private Spinner formPeso;
    @FXML
    private ListView tablaEjercicios;
    @FXML
    private Spinner formSeries;
    @FXML
    private Spinner formSegDesc;
    @FXML
    private TableView tablaEntrenamientos;
    @FXML
    private TableView tablaRutina;
    @FXML
    private TextField volumenReal;
    @FXML
    private ComboBox formTipoRutina;
    @FXML
    private ComboBox formEncargado;
    @FXML
    private TextField searchBoxEjercicio;

    public AppREController() {
        this.r = new Rutina();
        this.setEntrenamientos = new HashSet<>();
        this.indiceGeneral = 0;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        formEncargado.setDisable(true);
        ObservableList<String> items1 = FXCollections.observableArrayList();
        items1.addAll("Hipertrofia", "Fuerza", "Potencia", "Resistencia");
        formTipoRutina.setItems(items1);
        formEjercicio.setItems(Ejercicio.getEjercicioMode());
        formFechaInicio.getEditor().setDisable(true);
        formFechaInicio.getEditor().setOpacity(1);

        //VALUANDO SPINNERS
        SpinnerValueFactory<Integer> valueRepeticiones = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 1);
        formRepeticiones.setValueFactory(valueRepeticiones);
        SpinnerValueFactory<Double> valuePeso = new SpinnerValueFactory.DoubleSpinnerValueFactory(1d, 10000d, 1);
        formPeso.setValueFactory(valuePeso);
        SpinnerValueFactory<Integer> valueSeries = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        formSeries.setValueFactory(valueSeries);
        SpinnerValueFactory<Integer> valueDescanso = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 60, 1);
        formSegDesc.setValueFactory(valueDescanso);

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

        tablaEntrenamientos.getColumns().add(detalleColumn);
        tablaEntrenamientos.getColumns().add(repsColumn);
        tablaEntrenamientos.getColumns().add(pesoColumn);
        tablaEntrenamientos.getColumns().add(volumenColumn);
        tablaEntrenamientos.getColumns().add(serieColumn);
        tablaEntrenamientos.getColumns().add(segDescColumn);

        RefreshTableView();

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

    }

    public void RefreshTableView() {
        tablaEntrenamientos.getItems().clear();
        tablaEntrenamientos.refresh();
        tablaEntrenamientos.getItems().addAll(setEntrenamientos);
    }

    @FXML
    public void cambiarTutores() throws IOException {
        
        if ((formTipoRutina.getSelectionModel().getSelectedIndex() != -1) && !Tutor.getTutoresTipo(formTipoRutina.getSelectionModel().getSelectedItem().toString().toUpperCase()).isEmpty()) {
            formEncargado.setDisable(false);
            formEncargado.getItems().clear();
            formEncargado.getItems().addAll(Tutor.getTutoresTipo(formTipoRutina.getSelectionModel().getSelectedItem().toString().toUpperCase()));
        } else {
            formEncargado.getSelectionModel().select(-1);
            formEncargado.setDisable(true);
        }
    }

    @FXML
    public void cambiarDia() throws IOException {
        int value = formFechaInicio.getValue().getDayOfWeek().getValue();
        switch (value) {
            case 1:
                formDia.setText("Lunes");
                break;
            case 2:
                formDia.setText("Martes");
                break;
            case 3:
                formDia.setText("Miercoles");
                break;
            case 4:
                formDia.setText("Jueves");
                break;
            case 5:
                formDia.setText("Viernes");
                break;
            case 6:
                formDia.setText("Sabado");
                break;
            case 7:
                formDia.setText("Domingo");
                break;
            default:
                formDia.setText("Día");
                break;
        }
    }

    @FXML
    public void clickAgregar() throws IOException {
        int indice = formEjercicio.getSelectionModel().getSelectedIndex();
        String aux;
        boolean valor = false;
        try {
            if (indice != -1) {
                if (!"".equals(formRepeticiones.getEditor().getText()) && !"".equals(formPeso.getEditor().getText()) && Integer.valueOf(formRepeticiones.getEditor().getText()) >= 0 && Double.valueOf(formPeso.getEditor().getText()) >= 0) {
                    aux = formEjercicio.getItems().get(indice).toString() + ", " + formRepeticiones.getEditor().getText() + ", " + formPeso.getEditor().getText();
                    if (tablaEjercicios.getItems().isEmpty()) {
                        tablaEjercicios.getItems().add(aux);
                    } else {
                        for (int i = 0; i < tablaEjercicios.getItems().size(); i++) {
                            if (tablaEjercicios.getItems().get(i).toString().equals(aux)) {
                                valor = true;
                                break;
                            }
                        }
                        if (!valor) {
                            tablaEjercicios.getItems().add(aux);
                        }
                    }
                }
            }
        } catch (NumberFormatException e) {
            App.MsgBox(Alert.AlertType.ERROR, "ERROR", "Error inesperado", "Causa:" + e.getMessage());
        }
    }

    @FXML
    public void clickBorrarLista() throws IOException {
        int indice = tablaEjercicios.getSelectionModel().getSelectedIndex();
        try {
            if (indice != -1) {
                tablaEjercicios.getItems().remove(indice);
            } else if (tablaEjercicios.getItems().size() > 0) {
                tablaEjercicios.getItems().remove(0);
            }
        } catch (Exception e) {
            App.MsgBox(Alert.AlertType.ERROR, "ERROR", "Error inesperado", "Causa:" + e.getMessage());
        }
    }

    @FXML
    public void clickLimpiar() throws IOException {
        tablaEjercicios.getItems().clear();
    }

    @FXML
    public void clickAdd() throws IOException {
        try {
            if (!"".equals(formRepeticiones.getEditor().getText())) {
                if (!tablaEjercicios.getItems().isEmpty()) {
                    if (!"".equals(formPeso.getEditor().getText())) {
                        if (!"".equals(formSeries.getEditor().getText())) {
                            if (!"".equals(formSegDesc.getEditor().getText())) {
                                Entrenamiento ent = new Entrenamiento();
                                double volumen = 0;
                                Integer series = Integer.parseInt(formSeries.getEditor().getText());
                                Integer segundosDescanso = Integer.parseInt(formSegDesc.getEditor().getText());
                                List<String> detalle = new ArrayList<>();
                                List<Integer> repeticiones = new ArrayList<>();
                                List<Double> peso = new ArrayList<>();
                                Set<Ejercicio> ejercicios = new HashSet<>();
                                String[] aux;
                                if (!tablaEjercicios.getItems().isEmpty()) {
                                    for (int i = 0; i < tablaEjercicios.getItems().size(); i++) {
                                        aux = tablaEjercicios.getItems().get(i).toString().split(", ");
                                        detalle.add(aux[0] + " (" + aux[1] + ")");
                                        repeticiones.add(Integer.parseInt(aux[2]));
                                        peso.add(Double.parseDouble(aux[3]));
                                        volumen += (Integer.parseInt(aux[2]) * Double.parseDouble(aux[3]) * series);
                                        if (!Ejercicio.getEjercicios().isEmpty()) {
                                            for (int j = 0; j < Ejercicio.getEjercicios().size(); j++) {
                                                if (Ejercicio.getEjercicios().get(j).getNombre().equals(aux[0]) && Ejercicio.getEjercicios().get(j).getDetalle().equals(aux[1])) {
                                                    ejercicios.add(Ejercicio.getEjercicios().get(j));
                                                }
                                            }
                                        }
                                    }
                                }
                                volumenTotal += volumen;
                                volumenReal.setText(String.valueOf(volumenTotal));
                                ent.setDetalle(detalle);
                                ent.setVolumen(volumen);
                                ent.setSeries(series);
                                ent.setRepeticiones(repeticiones);
                                ent.setPeso(peso);
                                ent.setSegundosDescanso(segundosDescanso);
                                ent.setEjercicios(ejercicios);
                                ent.setRutina(null);
                                setEntrenamientos.add(ent);
                                tablaEjercicios.getItems().clear();
                                RefreshTableView();
                            } else {
                                App.MsgBox(Alert.AlertType.WARNING, "WARNING", "Ingrese un valor para el campo SEGUNDOS DE DESCANSO", "El valor: '" + formSegDesc.getEditor().getText() + "' no es un valor valido para el campo SEGUNDOS DE DESCANSO");
                            }
                        } else {
                            App.MsgBox(Alert.AlertType.WARNING, "WARNING", "Ingrese un valor para el campo SERIES", "El valor: '" + formSeries.getEditor().getText() + "' no es un valor valido para el campo SERIES");
                        }
                    } else {
                        App.MsgBox(Alert.AlertType.WARNING, "WARNING", "Ingrese un valor para el campo SERIES", "El valor: '" + formSeries.getEditor().getText() + "' no es un valor valido para el campo SERIES");
                    }
                } else {
                    App.MsgBox(Alert.AlertType.WARNING, "WARNING", "Ingrese los ejercicios en la lista", "La lista de ejercicios a realizar no puede permanecer vacía, agrege los ejercicios antes de crear un entrenamiento");
                }
            } else {
                App.MsgBox(Alert.AlertType.WARNING, "WARNING", "Ingrese un valor para el campo REPETICIONES", "El valor: '" + formRepeticiones.getEditor().getText() + "' no es un valor valido para el campo REPETICIONES");
            }
        } catch (NumberFormatException e) {
            App.MsgBox(Alert.AlertType.ERROR, "ERROR", "Error inesperado", "Causa:" + e.getMessage());
        }
    }

    @FXML
    public void clickQuitarEntrenamiento() throws IOException {
        try {
            if (tablaEntrenamientos.getSelectionModel().getSelectedIndex() != -1) {
                Entrenamiento tuto = (Entrenamiento) tablaEntrenamientos.getSelectionModel().getSelectedItem();
                volumenTotal -= tuto.getVolumen();
                volumenReal.setText(String.valueOf(volumenTotal));
                setEntrenamientos.remove(tuto);
                RefreshTableView();
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @FXML
    public void clickGenerar() throws IOException {
        try {
            if (!tablaEntrenamientos.getItems().isEmpty()) {
                if (formFechaInicio.getValue() != null) {
                    if (formTipoRutina.getValue() != null) {
                        if (formEncargado.getValue() != null) {
                            r.setNombreDia(formDia.getText());
                            LocalDate fecha = formFechaInicio.getValue();
                            r.setFecha(fecha);
                            String[] parts;
                            parts = formEncargado.getSelectionModel().getSelectedItem().toString().split(" ");
                            Tutor.getTutores().forEach(t -> {
                                if (parts[0].contains(t.getNombres()) && parts[1].contains(t.getApellidos())) {
                                    r.setTutor(t);
                                }
                            });
                            r.setCliente(Cliente.buscarCliente(App.getIdCliente()));
                            ObservableList<Rutina> rut = FXCollections.observableArrayList();
                            rut.add(r);
                            tablaRutina.getItems().clear();
                            tablaRutina.refresh();
                            tablaRutina.getItems().addAll(rut);
                        } else {
                            App.MsgBox(Alert.AlertType.WARNING, "WARNING", "Ingrese un valor para el campo ENCARGADO", "El valor: '" + formEncargado.getValue() + "' no es un valor valido para el campo ENCARGADO");
                        }
                    } else {
                        App.MsgBox(Alert.AlertType.WARNING, "WARNING", "Ingrese un valor para el campo TIPO RUTINA", "El valor: '" + formTipoRutina.getValue() + "' no es un valor valido para el campo TIPO RUTINA");
                    }
                } else {
                    App.MsgBox(Alert.AlertType.WARNING, "WARNING", "Ingrese un valor para el campo FECHA DE INICIO", "El valor: '" + formFechaInicio.getValue() + "' no es un valor valido para el campo FECHA DE INICIO");
                }
            } else {
                App.MsgBox(Alert.AlertType.WARNING, "WARNING", "La tabla de entrenamientos se encuentra vacía", "Debe cargar los ejercicios en la tabla de entrenamientos mediante el botón \">>>");
            }
        } catch (NumberFormatException e) {
            App.MsgBox(Alert.AlertType.ERROR, "ERROR", "Error inesperado", "Causa:" + e.getMessage());
        }
    }

    @FXML
    public void clickQuitarRutina() throws IOException {
        tablaRutina.getItems().clear();
    }

    //SE CREAN LOS ENTRENAMIENTOS Y LA RUTINA ASOCIADA
    //SE ASOCIA EL TUTOR Y EL CLIENTE
    @FXML
    public void confirmarRutina() throws IOException {
        if (!tablaRutina.getItems().isEmpty()) {
            boolean valor = false;
            for (Rutina findRutina : Rutina.getRutinas()) {
                if ((formDia.getText().equals(findRutina.getNombreDia()) && !findRutina.isExpirado()) && findRutina.getCliente().getDni().equals(App.getIdCliente())) {
                    valor = true;
                    tablaRutina.getItems().clear();
                    break;
                }
            }
            if (!valor) {
                if (Rutina.getRutinasActivas().size() < 7) {
                    Rutina nuevaRutina = new Rutina();
                    Rutina.agregarRutina(nuevaRutina);
                    // BD ---> nuevaRutina <--- Asigna un id automaticamente
                    nuevaRutina.setCliente(r.getCliente());
                    nuevaRutina.setTutor(r.getTutor());
                    nuevaRutina.setNombreDia(r.getNombreDia());
                    nuevaRutina.setFecha(r.getFecha());
                    Set<Entrenamiento> setFinal = new HashSet<>();
                    setEntrenamientos.forEach(e -> {
                        e.setRutina(nuevaRutina);
                        Entrenamiento.agregarEntrenamiento(e);
                        setFinal.add(e);
                    });
                    nuevaRutina.setEntrenamientos(setFinal);
                    Rutina.modificarRutina(nuevaRutina);
                    tablaRutina.getItems().clear();
                    tablaEjercicios.getItems().clear();
                    tablaEntrenamientos.getItems().clear();
                    setEntrenamientos.clear();
                    volumenReal.clear();
                    App.MsgBox(Alert.AlertType.INFORMATION, "EXITO", "Se completo exitosamente la creacion de la rutina al sistema :D", "Nueva rutina para el cliente con el ID: " + App.getIdCliente());
                } else {
                    App.MsgBox(Alert.AlertType.WARNING, "WARNING", "La cantidad de rutinas activas maximas por semana es de 7", "Se alcanzó el maximo de rutinas activas simultaneamente para un mismo cliente");
                }

            } else {
                App.MsgBox(Alert.AlertType.WARNING, "WARNING", "El dia " + formDia.getText() + " ya se encuentra con una rutina activa", "El dia: '" + formDia.getText() + "' no se encuentra disponible");
            }

        } else {
            App.MsgBox(Alert.AlertType.WARNING, "WARNING", "Debe generar la vista previa de la rutina antes de confirmar", "Para poder generar la rutina se debe listar los entrenamientos y luego presionar el botón \"GENERAR RUTINA");
        }
    }

    @FXML
    private void clickBuscar() throws IOException {
        try {
            if (!"".equals(searchBoxEjercicio.getText())) {
                String ejercicio = searchBoxEjercicio.getText().toUpperCase();
                int id = -1;
                String aux;
                String[] parts;
                for (int i = indiceGeneral; i < formEjercicio.getItems().size(); i++) {
                    aux = formEjercicio.getItems().get(i).toString().toUpperCase();
                    parts = aux.split(", ");
                    if (parts[0].contains(ejercicio)) {
                        id = i;
                        System.out.println("EXITO, ID: " + id);
                        formEjercicio.getSelectionModel().select(formEjercicio.getItems().get(i));
                        formEjercicio.requestFocus();
                        indiceGeneral = i + 1;
                        break;
                    }
                }
                if (id == -1) {
                    indiceGeneral = 0;
                    App.MsgBox(Alert.AlertType.WARNING, "WARNING", "No se encontró el ejercicio especificado", "No se encuentra el registro o se llegó al final de la lista.");
                }
            }
        } catch (NumberFormatException e) {
            App.MsgBox(Alert.AlertType.ERROR, "ERROR", "El valor introducido para la busqueda no es valido", "Mensaje de error: " + e.getMessage());
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
