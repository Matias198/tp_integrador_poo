package com.tpintegrador.lafuerza.modelo;

import com.tpintegrador.lafuerza.servicio.EjercicioService;
import java.io.Serializable;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ejercicio")
public class Ejercicio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String detalle;
    
    @ManyToOne
    @JoinColumn(name = "musculo", referencedColumnName = "id")
    private GrupoMuscular musculo;
    
    @ManyToMany(mappedBy = "ejercicios", cascade = {CascadeType.ALL}, fetch=FetchType.EAGER)
    private Set<Entrenamiento> entrenamientos;
    
//    @ManyToMany(mappedBy = "ejercicio", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<Entrenamiento> entrenamientos;
    
    //CONSTRUCTOR 
    public Ejercicio() {
    }

    public Ejercicio(String nombre, String detalle, GrupoMuscular musculo, Set<Entrenamiento> entrenamientos) {
        this.nombre = nombre;
        this.detalle = detalle;
        this.musculo = musculo;
        this.entrenamientos = entrenamientos;
    }

    //GETTERS & SETTERS
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public GrupoMuscular getMusculo() {
        return musculo;
    }

    public void setMusculo(GrupoMuscular musculo) {
        this.musculo = musculo;
    }

    public Set<Entrenamiento> getEntrenamientos() {
        return entrenamientos;
    }

    public void setEntrenamientos(Set<Entrenamiento> entrenamientos) {
        this.entrenamientos = entrenamientos;
    }
    
    @Override
    public String toString(){
        String aux;
        aux = this.getId() + " " +
              this.getNombre()+ " " +
              this.getDetalle();
        return aux;
    }
    public static void agregarEjercicio(String nombre, String detalle, GrupoMuscular musculo, Set<Entrenamiento> entrenamientos){
        Ejercicio temporal = new Ejercicio();
        temporal.setNombre(nombre);
        temporal.setDetalle(detalle);
        temporal.setMusculo(musculo);
        temporal.setEntrenamientos(entrenamientos);
        EjercicioService.crearEjercicio(temporal);
    }
    
    public static void eliminarEjercicio(Ejercicio ej) {
        EjercicioService.eliminarEjercicioById(ej.getId());
    }

    public static Ejercicio buscarEjercicio(Long id) {
        return EjercicioService.findEjercicioById(id);
    }

    public static void modificarEjercicio(Long id, String nombre, String detalle, GrupoMuscular musculo, Set<Entrenamiento> entrenamientos) {
        Ejercicio temporal = new Ejercicio();
        temporal.setId(id);
        temporal.setNombre(nombre);
        temporal.setDetalle(detalle);
        temporal.setMusculo(musculo);
        temporal.setEntrenamientos(entrenamientos);
        EjercicioService.modificarEjercicio(temporal);
    }
    
    public static ObservableList<Ejercicio> getEjercicios() {
        ObservableList<Ejercicio> ejs = FXCollections.observableArrayList();
        EjercicioService.findEjercicios().forEach(ej -> {
            ejs.add(ej);
        });
        return ejs;
    }
    
    public static ObservableList<String> getEjercicioMode() {
        ObservableList<String> ejs = FXCollections.observableArrayList();
        EjercicioService.findEjercicios().forEach(ej -> {
            ejs.add(ej.getNombre() + ", " + ej.getDetalle());
        });
        return ejs;
    }
}
