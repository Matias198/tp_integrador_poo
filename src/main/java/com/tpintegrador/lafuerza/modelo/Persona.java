package com.tpintegrador.lafuerza.modelo;

import com.sun.istack.internal.NotNull;
import com.tpintegrador.lafuerza.servicio.PersonaService;
import java.io.Serializable;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name = "persona")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")

public class Persona implements Serializable {

    
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;

    @Id
    private Long dni;
    private String nombres;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String sexo;

    //CONSTRUCTOR 
    public Persona() {
    }

    public Persona(Long dni, String nombres, String apellidos, LocalDate fechaNacimiento, String sexo) {
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
    }

    public Long getDni() {
        return dni;
    }

    public void setDni(Long dni) {
        this.dni = dni;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
    
    public static Persona buscarPersona(Long id){
        return PersonaService.findPersonaById(id);
    }
    
    //Obtener personas
    public static ObservableList<Persona> getPersonas() {
        ObservableList<Persona> personas = FXCollections.observableArrayList();
        PersonaService.findPersonas().forEach(persona -> {
            personas.add(persona);
        });
        return personas;
    }
}
