package com.tpintegrador.lafuerza.modelo;

import com.tpintegrador.lafuerza.servicio.TutorService;
import java.time.LocalDate;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
@DiscriminatorValue(value = "tutor")
public class Tutor extends Persona {

    private String especialidad;
    private Double valoracion = 0d;

    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Rutina> rutinas;

    //CONSTRUCTOR

    public Tutor() {
    }

    public Tutor(String especialidad, Set<Rutina> rutinas) {
        this.especialidad = especialidad;
        this.rutinas = rutinas;
    }

    public Tutor(String especialidad, Set<Rutina> rutinas, Long dni, String nombres, String apellidos, LocalDate fechaNacimiento, String sexo) {
        super(dni, nombres, apellidos, fechaNacimiento, sexo);
        this.especialidad = especialidad;
        this.rutinas = rutinas;
    }
   

    //GETTERS & SETTERS
    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public Set<Rutina> getRutinas() {
        return rutinas;
    }

    public void setRutinas(Set<Rutina> rutinas) {
        this.rutinas = rutinas;
    }

    public Double getValoracion() {
        return valoracion;
    }

    public void setValoracion(Double valoracion) {
        this.valoracion = valoracion;
    }
    
    @Override
    public String toString(){
        String aux;
        aux = this.getDni() + " " +
              this.getNombres() + " " +
              this.getApellidos();
        return aux;
    }
    
    public static void agregarTutor(String especialidad, Set<Rutina> rutinas, Long dni, String nombres, String apellidos, LocalDate fechaNacimiento, String sexo) {
        Tutor temporal = new Tutor();
        temporal.setDni(dni);
        temporal.setEspecialidad(especialidad);
        temporal.setRutinas(rutinas);
        temporal.setNombres(nombres);
        temporal.setApellidos(apellidos);
        temporal.setFechaNacimiento(fechaNacimiento);
        temporal.setSexo(sexo);
        TutorService.crearTutor(temporal);
    }
    
    public static void eliminarTutor(Tutor tutor) {
        TutorService.eliminarTutorById(tutor.getDni());
    }

    public static Tutor buscarTutor(Long id) {
        return TutorService.findTutorById(id);
    }
    
    public static void modificarTutor(Tutor tutor){
        TutorService.modificarTutor(tutor);
    }
    
    public static void modificarTutor(String especialidad, Set<Rutina> rutinas, Long dni, String nombres, String apellidos, LocalDate fechaNacimiento, String sexo, Double valoracion) {
        Tutor temporal = new Tutor();
        temporal.setDni(dni);
        temporal.setEspecialidad(especialidad);
        temporal.setRutinas(rutinas);
        temporal.setNombres(nombres);
        temporal.setApellidos(apellidos);
        temporal.setFechaNacimiento(fechaNacimiento);
        temporal.setSexo(sexo);
        temporal.setValoracion(valoracion);
        TutorService.modificarTutor(temporal);
    }

    public static ObservableList<String> getTutoresTipo(String tipo) {
        ObservableList<String> tutores = FXCollections.observableArrayList();
        TutorService.findTutores().forEach(tutor -> {
            if (tutor.getEspecialidad().toUpperCase().equals(tipo) || tutor.getEspecialidad().toUpperCase().contains(tipo)) {
                tutores.add(tutor.getNombres() + " " + tutor.getApellidos() + " / Sexo: " + tutor.getSexo());
            }
        });
        return tutores;
    }
    
    public static ObservableList<Tutor> getTutores() {
        ObservableList<Tutor> tuts = FXCollections.observableArrayList();
        TutorService.findTutores().forEach(t -> {
            tuts.add(t);
        });
        return tuts;
    }
}
