package com.tpintegrador.lafuerza;

import com.tpintegrador.lafuerza.modelo.Ejercicio;
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

public class AppEjercicioController implements Initializable {

    private int indiceGeneral;

    //Formulario
    @FXML
    private TextField formNombre;
    @FXML
    private TextField formDetalle;
    @FXML
    private ComboBox formGM;
    @FXML
    private ComboBox searchBoxGM;
    @FXML
    private TextField searchBoxEjercicio;
    @FXML
    private TableView tablaEjercicos;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnBuscar;
    @FXML
    private Button btnAdd;
    @FXML
    private Label indice;

    public AppEjercicioController() {
        this.indiceGeneral = 0;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> items = FXCollections.observableArrayList();
        GrupoMuscular.getGrupoMuscular().forEach(gm -> {
            items.add(gm.getMusculo());
        });
//        GrupoMuscularService.findGruposMusculares().forEach(gm -> {
//            items.add(gm.getMusculo());
//        });
        formGM.setItems(items);

        ObservableList<String> items2 = FXCollections.observableArrayList();
        items2.add("TODOS");
        GrupoMuscular.getGrupoMuscular().forEach(gm -> {
            items2.add(gm.getMusculo());
        });
//        GrupoMuscularService.findGruposMusculares().forEach(gm -> {
//            items2.add(gm.getMusculo());
//        });
        searchBoxGM.setItems(items2);

        //TABLA
        //COLUMNA ID
        TableColumn<Ejercicio, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        //COLUMNA NOMBRE
        TableColumn<Ejercicio, String> nombreColumn = new TableColumn<>("NOMBRE");
        nombreColumn.setMinWidth(150);
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        //COLUMNA DETALLE
        TableColumn<Ejercicio, String> detalleColumn = new TableColumn<>("DETALLE");
        detalleColumn.setMinWidth(260);
        detalleColumn.setCellValueFactory(new PropertyValueFactory<>("detalle"));
        //COLUMNA MUSCULO
        TableColumn<GrupoMuscular, String> musculoColumn = new TableColumn<>("MUSCULO");
        musculoColumn.setMinWidth(156);
        musculoColumn.setCellValueFactory(new PropertyValueFactory<>("musculo"));

        tablaEjercicos.getColumns().add(idColumn);
        tablaEjercicos.getColumns().add(nombreColumn);
        tablaEjercicos.getColumns().add(detalleColumn);
        tablaEjercicos.getColumns().add(musculoColumn);
        RefreshTableView();
    }

    //Obtener los gm
//    public ObservableList<GrupoMuscular> getGrupoMuscular() {
//        ObservableList<GrupoMuscular> gms = FXCollections.observableArrayList();
//        gmService.findGruposMusculares().forEach(gm -> {
//            gms.add(gm);
//        });
//        return gms;
//    }
    //Obtener los ej
//    public ObservableList<Ejercicio> getEjercicios() {
//        ObservableList<Ejercicio> ejs = FXCollections.observableArrayList();
//        ejercicioService.findEjercicios().forEach(ej -> {
//            ejs.add(ej);
//        });
//        return ejs;
//    }
    public void RefreshTableView() {
        tablaEjercicos.getItems().clear();
        tablaEjercicos.refresh();
        tablaEjercicos.getItems().addAll(Ejercicio.getEjercicios());
    }

    @FXML
    private void clickAdd() throws IOException {
        try {
            if (!"".equals(formNombre.getText())) {
                if (formGM.getValue() != null) {
                    String nombre = formNombre.getText().toUpperCase();
                    String detalle = formDetalle.getText().toUpperCase();
                    String musculo = formGM.getValue().toString();
                    for (GrupoMuscular gm : GrupoMuscular.getGrupoMuscular()) {
                        if (gm.getMusculo().equals(musculo.toUpperCase())) {
                            if (">>>".equals(btnAdd.getText())) {
                                Ejercicio.agregarEjercicio(nombre, detalle, gm, null);
                            } else {
                                Long id = Long.valueOf(indice.getText());
                                Ejercicio.modificarEjercicio(id, nombre, detalle, gm, Ejercicio.buscarEjercicio(id).getEntrenamientos());
                                App.MsgBox(Alert.AlertType.INFORMATION, "EXITO", "Se modificó el ejercicio con ID: " + indice.getText(), "Ejercicio modificado: " + nombre);
                                btnAdd.setText(">>>");
                                indice.setText("");
                                btnEliminar.disableProperty().set(false);
                                searchBoxGM.disableProperty().set(false);
                                searchBoxGM.getSelectionModel().select(0);
                                btnBuscar.disableProperty().set(false);
                            }
                            RefreshTableView();
                            formNombre.clear();
                            formDetalle.clear();
                            formGM.setValue(null);
                            searchBoxGM.getSelectionModel().selectFirst();
                            break;
                        }
                    }
//                    for (int i = 0; i < GrupoMuscular.getGrupoMuscular().size(); i++) {
//                        if (GrupoMuscular.getGrupoMuscular().get(i).getMusculo().equals(musculo.toUpperCase())) {
//                            if (">>>".equals(btnAdd.getText())) {
//                                EjercicioService.crearEjercicio(EjercicioService.agregarDatosEjercicio(nombre, detalle, getGrupoMuscular().get(i), null));
//                                App.MsgBox(Alert.AlertType.INFORMATION, "EXITO", "Se completo exitosamente la inscripcion del nuevo ejercicio al sistema :D", "Nuevo ejercicio: " + formNombre.getText());
//                            } else {
//                                Ejercicio ej = new Ejercicio();
//                                ej.setNombre(nombre);
//                                ej.setDetalle(detalle);
//                                ej.setMusculo(getGrupoMuscular().get(i));
//                                ej.setEntrenamientos(ejercicioService.findEjercicioById(Long.valueOf(indice.getText())).getEntrenamientos());
//                                ej.setId(Long.valueOf(indice.getText()));
//                                ejercicioService.modificarEjercicio(ej);
//                                MsgBox(Alert.AlertType.INFORMATION, "EXITO", "Se modificó el ejercicio con ID: " + indice.getText(), "Ejercicio modificado: " + nombre);
//                                btnAdd.setText(">>>");
//                                indice.setText("");
//                                btnEliminar.disableProperty().set(false);
//                                searchBoxGM.disableProperty().set(false);
//                                searchBoxGM.getSelectionModel().select(0);
//                                btnBuscar.disableProperty().set(false);
//                            }
//                            RefreshTableView();
//                            formNombre.clear();
//                            formDetalle.clear();
//                            formGM.setValue(null);
//                            searchBoxGM.getSelectionModel().selectFirst();
//                            break;
//                        }
//                    }
                } else {
                    App.MsgBox(Alert.AlertType.WARNING, "WARNING", "Ingrese un valor para el campo MUSCULO", "El valor: '" + formGM.getValue() + "' no es un valor valido para el campo MUSCULO");
                }
            } else {
                App.MsgBox(Alert.AlertType.WARNING, "WARNING", "Ingrese un valor para el campo NOMBRE", "El valor: '" + formNombre.getText() + "' no es un valor valido para el campo NOMBRE");
            }
        } catch (NumberFormatException e) {
            App.MsgBox(Alert.AlertType.ERROR, "ERROR", "Error en los datos de inscripción", "Causa:" + e.getMessage());
        }
    }

    @FXML
    private void clickEliminar() throws IOException {
        try {
            if (tablaEjercicos.getSelectionModel().getSelectedIndex() != -1) {
                Ejercicio ej = (Ejercicio) tablaEjercicos.getSelectionModel().getSelectedItem();
                Ejercicio.eliminarEjercicio(ej);
                App.MsgBox(Alert.AlertType.INFORMATION, "EXITO", "Se eliminó con exito el ejercicio", "Se dio de baja al ejercicio " + ej.getNombre() + ". ID: " + ej.getId());
                RefreshTableView();
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @FXML
    private void clickBuscar() throws IOException {

        try {
            String ejercico = searchBoxEjercicio.getText().toUpperCase();
            int id = -1;
            Ejercicio ej;
            for (int i = indiceGeneral; i < tablaEjercicos.getItems().size(); i++) {
                ej = (Ejercicio) tablaEjercicos.getItems().get(i);
                if (ej.getNombre().equals(ejercico) || ej.getNombre().contains(ejercico)) {
                    id = i;
                    System.out.println("EXITO, ID: " + id);
                    tablaEjercicos.getSelectionModel().select(ej);
                    tablaEjercicos.requestFocus();
                    indiceGeneral = i + 1;
                    break;
                }
            }
            if (id == -1) {
                indiceGeneral = 0;
                App.MsgBox(Alert.AlertType.WARNING, "WARNING", "No se encontró el ejercicio especificado", "No se encuentra el registro o se llegó al final de la lista.");
            }
        } catch (NumberFormatException e) {
            App.MsgBox(Alert.AlertType.ERROR, "ERROR", "El valor introducido para la busqueda no es valido", "Mensaje de error: " + e.getMessage());
        }
    }

    public void RefreshTableViewFiltered(String filtro) {
        
        if (filtro.equals("TODOS")) {
            RefreshTableView();
        } else {
            ObservableList<Ejercicio> ejsf = FXCollections.observableArrayList();
            Ejercicio.getEjercicios().forEach(ej -> {
                if (ej.getMusculo().getMusculo().toUpperCase().equals(filtro)) {
                    ejsf.add(ej);
                }
            });
            tablaEjercicos.getItems().clear();
            tablaEjercicos.getItems().addAll(ejsf);
        }
        
//        if (filtro.equals("TODOS")) {
//            RefreshTableView();
//        } else {
//            ObservableList<Ejercicio> ejsf = FXCollections.observableArrayList();
//            ejercicioService.findEjercicios().forEach(ej -> {
//                if (ej.getMusculo().getMusculo().toUpperCase().equals(filtro)) {
//                    ejsf.add(ej);
//                }
//            });
//            tablaEjercicos.getItems().clear();
//            tablaEjercicos.getItems().addAll(ejsf);
//        }
    }

    @FXML
    private void selectedItemChanged() throws IOException {
        RefreshTableViewFiltered(searchBoxGM.getSelectionModel().getSelectedItem().toString().toUpperCase());
        System.out.println(searchBoxGM.getSelectionModel().getSelectedItem().toString().toUpperCase());
    }

    @FXML
    private void clickEditar() throws IOException {
        btnEliminar.disableProperty().set(true);
        searchBoxGM.disableProperty().set(true);
        btnBuscar.disableProperty().set(true);
        btnAdd.setText("Guardar");
        Ejercicio obj = (Ejercicio) tablaEjercicos.getItems().get(tablaEjercicos.getSelectionModel().getSelectedIndex());
        formNombre.setText(obj.getNombre());
        formDetalle.setText(obj.getDetalle());
        indice.setText(obj.getId().toString());
        formGM.getSelectionModel().select((obj.getMusculo().getMusculo()));
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
    private void tutores() throws IOException {
        App.setRoot("VistaTutorA");
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
