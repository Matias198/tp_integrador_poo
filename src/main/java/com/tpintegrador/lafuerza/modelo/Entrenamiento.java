package com.tpintegrador.lafuerza.modelo;

import com.tpintegrador.lafuerza.servicio.EntrenamientoService;
import com.tpintegrador.lafuerza.servicio.RutinaService;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "entrenamiento")
public class Entrenamiento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private List<String> detalle;
    private Double volumen;
    private Integer series;
    private List<Integer> repeticiones;
    private List<Double> peso;
    private Integer segundosDescanso;

//    @OneToMany(mappedBy = "entrenamiento", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<Ejercicio> ejercicios;

    @ManyToMany()
    @JoinColumn(name = "ejercicio_id", referencedColumnName = "id")
    private Set<Ejercicio> ejercicios;
    
    @ManyToOne()
    @JoinColumn(name = "rutina_id", referencedColumnName = "id")
    private Rutina rutina;
    
//    @ManyToOne()
//    @JoinColumn(name = "tutor_id", referencedColumnName = "dni")
//    private Tutor tutor;

    //CONSTRUCTOR
    public Entrenamiento() {
    }

    public Entrenamiento(List<String> detalle, Double volumen, Integer series, List<Integer> repeticiones, List<Double> peso, Integer segundosDescanso, Set<Ejercicio> ejercicios, Rutina rutina) {
        this.detalle = detalle;
        this.volumen = volumen;
        this.series = series;
        this.repeticiones = repeticiones;
        this.peso = peso;
        this.segundosDescanso = segundosDescanso;
        this.ejercicios = ejercicios;
        this.rutina = rutina;
    }


    //GETTER & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<String> detalle) {
        this.detalle = detalle;
    }

    public Double getVolumen() {
        return volumen;
    }

    public void setVolumen(Double volumen) {
        this.volumen = volumen;
    }

    public Integer getSeries() {
        return series;
    }

    public void setSeries(Integer series) {
        this.series = series;
    }

    public List<Integer> getRepeticiones() {
        return repeticiones;
    }

    public void setRepeticiones(List<Integer> repeticiones) {
        this.repeticiones = repeticiones;
    }

    public List<Double> getPeso() {
        return peso;
    }

    public void setPeso(List<Double> peso) {
        this.peso = peso;
    }

    public Integer getSegundosDescanso() {
        return segundosDescanso;
    }

    public void setSegundosDescanso(Integer segundosDescanso) {
        this.segundosDescanso = segundosDescanso;
    }

    public Set<Ejercicio> getEjercicios() {
        return ejercicios;
    }

    public void setEjercicios(Set<Ejercicio> ejercicios) {
        this.ejercicios = ejercicios;
    }

    public Rutina getRutina() {
        return rutina;
    }

    public void setRutina(Rutina rutina) {
        this.rutina = rutina;
    }
    
    @Override
    public String toString(){
        String aux;
        aux = this.getId()+ " " +
              this.getDetalle()+ " " +
              this.getVolumen()+ " " +
              this.getRepeticiones()+ " " +
              this.getPeso()+ " " +
              this.getSegundosDescanso();
        return aux;
    }
    
    public static void agregarEntrenamiento(Entrenamiento entrenamiento){
        EntrenamientoService.crearEntrenamiento(entrenamiento);
    }
    
    public static void agregarEntrenamiento(List<String> detalle, Double volumen, Integer series, List<Integer> repeticiones, List<Double> peso, Integer segundosDescanso, Set<Ejercicio> ejercicios, Rutina rutina){
        Entrenamiento temporal = new Entrenamiento();
        temporal.setDetalle(detalle);
        temporal.setVolumen(volumen);
        temporal.setSeries(series);
        temporal.setRepeticiones(repeticiones);
        temporal.setPeso(peso);
        temporal.setSegundosDescanso(segundosDescanso);
        temporal.setEjercicios(ejercicios);
        temporal.setRutina(rutina);
        EntrenamientoService.crearEntrenamiento(temporal);
    }
    
    public static void eliminarEntrenamiento(Entrenamiento entrenamiento) {
        EntrenamientoService.eliminarEntrenamientoById(entrenamiento.getId());
    }

    public static Entrenamiento buscarEntrenamiento(Long id) {
        return EntrenamientoService.findEntrenamientoById(id);
    }

    public static void modificarEntrenamiento(Long id, List<String> detalle, Double volumen, Integer series, List<Integer> repeticiones, List<Double> peso, Integer segundosDescanso, Set<Ejercicio> ejercicios, Rutina rutina){
        Entrenamiento temporal = new Entrenamiento();
        temporal.setId(id);
        temporal.setDetalle(detalle);
        temporal.setVolumen(volumen);
        temporal.setSeries(series);
        temporal.setRepeticiones(repeticiones);
        temporal.setPeso(peso);
        temporal.setSegundosDescanso(segundosDescanso);
        temporal.setEjercicios(ejercicios);
        temporal.setRutina(rutina);
        EntrenamientoService.modificarEntrenamiento(temporal);
    }
    
    public static ObservableList<Entrenamiento> getEntrenamientosDeRutinas(Long id) {
        ObservableList<Entrenamiento> ens = FXCollections.observableArrayList();
        RutinaService.findRutinaById(id).getEntrenamientos().forEach(en -> {
            ens.add(en);
        });
        return ens;
    }
    
    public static ObservableList<Entrenamiento> getEntrenamientos() {
        ObservableList<Entrenamiento> ens = FXCollections.observableArrayList();
        EntrenamientoService.findEntrenamientos().forEach(en -> {
            ens.add(en);
        });
        return ens;
    }
}
