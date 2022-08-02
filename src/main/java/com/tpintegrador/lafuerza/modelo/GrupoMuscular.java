package com.tpintegrador.lafuerza.modelo;

import com.tpintegrador.lafuerza.servicio.GrupoMuscularService;
import java.io.Serializable;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "grupo_muscular")
public class GrupoMuscular implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String musculo;
    private String tamanioGrupo;

    @OneToMany(mappedBy = "musculo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Ejercicio> ejercicios;

    //CONSTRUCTOR
    public GrupoMuscular() {
    }

    public GrupoMuscular(String musculo, String tamanioGrupo, Set<Ejercicio> ejercicios) {
        this.musculo = musculo;
        this.tamanioGrupo = tamanioGrupo;
        this.ejercicios = ejercicios;
    }

    //GETTERS & SETTERS
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMusculo() {
        return musculo;
    }

    public void setMusculo(String musculo) {
        this.musculo = musculo;
    }

    public String getTamanioGrupo() {
        return tamanioGrupo;
    }

    public void setTamanioGrupo(String tamanioGrupo) {
        this.tamanioGrupo = tamanioGrupo;
    }

    public Set<Ejercicio> getEjercicios() {
        return ejercicios;
    }

    public void setEjercicios(Set<Ejercicio> ejercicios) {
        this.ejercicios = ejercicios;
    }

    @Override
    public String toString(){
        String aux;
        aux = this.getMusculo();
        return aux;
    }
    
    public static void agregarGrupoMuscular(GrupoMuscular grupomuscular){
        GrupoMuscularService.crearGrupoMuscular(grupomuscular);
    }
    
    public static void agregarGrupoMuscular(String musculo, String tamanioGrupo, Set<Ejercicio> ejercicios){
        GrupoMuscular temporal = new GrupoMuscular();
        temporal.setMusculo(musculo);
        temporal.setTamanioGrupo(tamanioGrupo);
        temporal.setEjercicios(ejercicios);
        GrupoMuscularService.crearGrupoMuscular(temporal);
    }
    
    public static void eliminarGrupoMuscular(GrupoMuscular gm) {
        GrupoMuscularService.eliminarGrupoMuscular(gm.getId());
    }

    public static GrupoMuscular buscarGrupoMuscular(Long id) {
        return GrupoMuscularService.findGrupoMuscularById(id);
    }

    public static void modificarEjercicio(Long id, String musculo, String tamanioGrupo, Set<Ejercicio> ejercicios) {
        GrupoMuscular temporal = new GrupoMuscular();
        temporal.setId(id);
        temporal.setMusculo(musculo);
        temporal.setTamanioGrupo(tamanioGrupo);
        temporal.setEjercicios(ejercicios);
        GrupoMuscularService.modificarGrupoMuscular(temporal);
    }
    
    public static ObservableList<GrupoMuscular> getGrupoMuscular() {
        ObservableList<GrupoMuscular> gms = FXCollections.observableArrayList();
        GrupoMuscularService.findGruposMusculares().forEach(gm -> {
            gms.add(gm);
        });
        return gms;
    }
}
