/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpintegrador.lafuerza;

import com.tpintegrador.lafuerza.modelo.Rutina;
import com.tpintegrador.lafuerza.servicio.RutinaService;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 *
 * @author matia
 */
public class AppLFController implements Initializable {

    private final RutinaService rutinaService;

    public AppLFController() {
        this.rutinaService = new RutinaService();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Actualiza las fechas:
        try {
            for (Rutina rutina : Rutina.getRutinas()){
                boolean valor = false;
                    if (LocalDate.now().isAfter(rutina.getFecha().plusDays(7)) && LocalDate.now().isBefore(rutina.getFecha().plusDays(14)) || (LocalDate.now().isEqual(rutina.getFecha().plusDays(7)))) {
                        rutina.setDuracion(3);
                        valor = true;
                    }
                    if (LocalDate.now().isAfter(rutina.getFecha().plusDays(14)) && LocalDate.now().isBefore(rutina.getFecha().plusDays(21)) || (LocalDate.now().isEqual(rutina.getFecha().plusDays(14)))) {
                        rutina.setDuracion(2);
                        valor = true;
                    }
                    if (LocalDate.now().isAfter(rutina.getFecha().plusDays(21)) && LocalDate.now().isBefore(rutina.getFecha().plusDays(28)) || (LocalDate.now().isEqual(rutina.getFecha().plusDays(21)))) {
                        rutina.setDuracion(1);
                        valor = true;
                    }
                    if (LocalDate.now().isAfter(rutina.getFecha().plusDays(28)) || (LocalDate.now().isEqual(rutina.getFecha().plusDays(28)))) {
                        rutina.setDuracion(0);
                        rutina.setExpirado(true);
                        valor = true;
                    }
                    if (valor) {
                        Rutina.modificarRutina(rutina);
                    }
            }
            
        } catch (Exception e) {

        }
    }

    @FXML
    private void musculos() throws IOException {
        App.setRoot("VistaGMuscular");
    }

    @FXML
    private void ejercicios() throws IOException {
        App.setRoot("VistaEjercicio");
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
    private void salir() throws IOException {
        App.close();
    }
}
