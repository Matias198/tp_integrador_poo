package com.tpintegrador.lafuerza.modelo;

import com.tpintegrador.lafuerza.servicio.RutinaService;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "rutina")
public class Rutina implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombreDia;
    private LocalDate fecha;
    private int duracion = 4;
    private boolean expirado = false;
    private int valoracionTutor = -1;

    @OneToMany(mappedBy = "rutina", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Entrenamiento> entrenamientos;

    @ManyToOne()
    @JoinColumn(name = "cliente_id", referencedColumnName = "dni")
    private Cliente cliente;

    @ManyToOne()
    @JoinColumn(name = "tutor_id", referencedColumnName = "dni")
    private Tutor tutor;

    //CONSTRUCTOR
    public Rutina() {
    }

    public Rutina(String nombreDia, LocalDate fecha, Set<Entrenamiento> entrenamientos, Cliente cliente) {
        this.nombreDia = nombreDia;
        this.fecha = fecha;
        this.entrenamientos = entrenamientos;
        this.cliente = cliente;
    }

    //GETTERS & SETTERS
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreDia() {
        return nombreDia;
    }

    public void setNombreDia(String nombreDia) {
        this.nombreDia = nombreDia;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public boolean isExpirado() {
        return expirado;
    }

    public void setExpirado(boolean expirado) {
        this.expirado = expirado;
    }

    public int getValoracionTutor() {
        return valoracionTutor;
    }

    public void setValoracionTutor(int valoracionTutor) {
        this.valoracionTutor = valoracionTutor;
    }

    public Set<Entrenamiento> getEntrenamientos() {
        return entrenamientos;
    }

    public void setEntrenamientos(Set<Entrenamiento> entrenamientos) {
        this.entrenamientos = entrenamientos;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    @Override
    public String toString() {
        String aux;
        aux = this.getId() + " "
                + this.getFecha() + " "
                + this.getDuracion();
        return aux;
    }

    public static ObservableList<Rutina> getRutinasActivas(Long id, Boolean isCliente) {
        ObservableList<Rutina> rutinas = FXCollections.observableArrayList();
        if (isCliente) {
            RutinaService.findRutinas().forEach(rutina -> {
                if ((!(rutina.isExpirado()) && rutina.getCliente().getDni().equals(id))) {
                    rutinas.add(rutina);
                }
            });
        } else {
            RutinaService.findRutinas().forEach(rutina -> {
                if ((!(rutina.isExpirado()) && rutina.getTutor().getDni().equals(id))) {
                    rutinas.add(rutina);
                }
            });
        }

        return rutinas;
    }

    public static ObservableList<Rutina> getRutinasHistorial(Long id, Boolean isCliente) {
        ObservableList<Rutina> rutinas = FXCollections.observableArrayList();
        if (isCliente) {
            RutinaService.findRutinas().forEach(rutina -> {
                if (rutina.isExpirado() && rutina.getCliente().getDni().equals(id)) {
                    rutinas.add(rutina);
                }
            });
        } else {
            RutinaService.findRutinas().forEach(rutina -> {
                if (rutina.isExpirado() && rutina.getTutor().getDni().equals(id)) {
                    rutinas.add(rutina);
                }
            });
        }
        return rutinas;
    }
    
    public static void agregarRutina(Rutina rutina){
        RutinaService.crearRutina(rutina);
    }
    
    public static void agregarRutina(String nombreDia, LocalDate fecha, Set<Entrenamiento> entrenamientos, Cliente cliente){
        Rutina temporal = new Rutina();
        temporal.setNombreDia(nombreDia);
        temporal.setFecha(fecha);
        temporal.setEntrenamientos(entrenamientos);
        temporal.setCliente(cliente);
        RutinaService.crearRutina(temporal);
    }
    
    public static void eliminarRutina(Rutina rutina) {
        RutinaService.eliminarRutinaById(rutina.getId());
    }

    public static Rutina buscarRutina(Long id) {
        return RutinaService.findRutinaById(id);
    }
    
    public static void modificarRutina(Rutina rutina){
        RutinaService.modificarRutina(rutina);
    }
    
    public static void modificarRutina(Long id, String nombreDia, LocalDate fecha, int duracion, boolean valor, Set<Entrenamiento> entrenamientos, Cliente cliente) {
        Rutina temporal = new Rutina();
        temporal.setId(id);
        temporal.setNombreDia(nombreDia);
        temporal.setFecha(fecha);
        temporal.setDuracion(duracion);
        temporal.setExpirado(valor);
        temporal.setEntrenamientos(entrenamientos);
        temporal.setCliente(cliente);
        RutinaService.modificarRutina(temporal);
    }

    public static ObservableList<Rutina> getRutinas() {
        ObservableList<Rutina> rutinas = FXCollections.observableArrayList();
        RutinaService.findRutinas().forEach(rutina -> {
            rutinas.add(rutina);
        });
        return rutinas;
    }
    
    public static ObservableList<Rutina> getRutinasActivas() {
        ObservableList<Rutina> rutinas = FXCollections.observableArrayList();
        RutinaService.findRutinas().forEach(rutina -> {
            if (!(rutina.isExpirado())) {
                rutinas.add(rutina);
            }
        });
        return rutinas;
    }
}
