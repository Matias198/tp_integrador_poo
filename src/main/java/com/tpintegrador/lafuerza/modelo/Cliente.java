package com.tpintegrador.lafuerza.modelo;

import com.tpintegrador.lafuerza.servicio.ClienteService;
import com.tpintegrador.lafuerza.servicio.RutinaService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
@DiscriminatorValue(value = "cliente")
public class Cliente extends Persona {

    private LocalDate fechaIngreso;
    private String consideracionMedica;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Rutina> rutinas;

    //CONSTRUCTORES
    public Cliente() {

    }

    public Cliente(LocalDate fechaIngreso, String consideracionMedica) {
        this.fechaIngreso = fechaIngreso;
        this.consideracionMedica = consideracionMedica;

    }

    public Cliente(LocalDate fechaIngreso, String consideracionMedica, Long dni, String nombres, String apellidos, LocalDate fechaNacimiento, String sexo) {
        super(dni, nombres, apellidos, fechaNacimiento, sexo);
        this.fechaIngreso = fechaIngreso;
        this.consideracionMedica = consideracionMedica;

    }

    //GETTERS & SETTERS
    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getConsideracionMedica() {
        return consideracionMedica;
    }

    public void setConsideracionMedica(String consideracionMedica) {
        this.consideracionMedica = consideracionMedica;
    }

    public Set<Rutina> getRutinas() {
        return rutinas;
    }

    public void setRutinas(Set<Rutina> rutinas) {
        this.rutinas = rutinas;
    }

    @Override
    public String toString() {
        String aux;
        aux = this.getDni() + " "
                + this.getNombres() + " "
                + this.getApellidos();
        return aux;
    }

    public static void agregarCliente(LocalDate fechaIngreso, String consideracionMedica, Long dni, String nombres, String apellidos, LocalDate fechaNacimiento, String sexo) {
        Cliente temporal = new Cliente();
        temporal.setNombres(nombres);
        temporal.setApellidos(apellidos);
        temporal.setFechaIngreso(fechaIngreso);
        temporal.setFechaNacimiento(fechaNacimiento);
        temporal.setDni(dni);
        temporal.setConsideracionMedica(consideracionMedica);
        temporal.setSexo(sexo);
        temporal.setRutinas(null);
        ClienteService.crearCliente(temporal);
    }

    public static void eliminarCliente(Cliente cli) {
        ClienteService.eliminarClienteById(cli.getDni());
    }

    public static Cliente buscarCliente(Long id) {
        return ClienteService.findClienteById(id);
    }

    public static void modificarCliente(Long dni, String nombres, String apellidos, LocalDate fechaNacimiento, String sexo, LocalDate fechaIngreso, String consideracionMedica) {
        Cliente temporal = new Cliente();
        temporal.setDni(dni);
        temporal.setNombres(nombres);
        temporal.setApellidos(apellidos);
        temporal.setFechaNacimiento(fechaNacimiento);
        temporal.setSexo(sexo);
        temporal.setFechaIngreso(fechaIngreso);
        temporal.setConsideracionMedica(consideracionMedica);
        temporal.setRutinas(ClienteService.findClienteById(dni).getRutinas());
        ClienteService.modificarCliente(temporal);
    }

    //Obtener los clientes
    public static ObservableList<Cliente> getClientes() {
        ObservableList<Cliente> clientes = FXCollections.observableArrayList();
        ClienteService.findClientes().forEach(cliente -> {
            clientes.add(cliente);
        });
        return clientes;
    }

    public static Double calcularVolumenGM(Long dni, String aux) {
        if (!"".equals(aux)) {
            Double volumenGM = 0d;
            List<Rutina> rutinasCliente = new ArrayList<>();
            RutinaService.findRutinas().forEach(rutina -> {
                if (rutina.getCliente().getDni().equals(dni) && !rutina.isExpirado()) {
                    rutinasCliente.add(rutina);
                }
            });
            //Aca empieza el arbol de busqueda
            Set<Entrenamiento> setEntrenamientos;
            Set<Ejercicio> setEjercicios;
            int indice; //Es el indice de las listas SERIES, REPETICIONES, PESOS
            int auxiliar; //Determina la posicion del musculo referido dentro de los elementos en las listas
            Integer repeticiones, series;
            Double peso;
            List<Double> volumenes = new ArrayList<>();
            for (Rutina unaRutina : rutinasCliente) {
                setEntrenamientos = unaRutina.getEntrenamientos();
                for (Entrenamiento unEntrenamiento : setEntrenamientos) {
                    indice = -1;
                    auxiliar = -1;
                    setEjercicios = unEntrenamiento.getEjercicios();
                    for (Ejercicio unEjercicio : setEjercicios) {
                        auxiliar++; //Aux es el indice de ejercicios para referenciar en entrenamiento
                        if (unEjercicio.getMusculo().getTamanioGrupo().toUpperCase().equals(aux.toUpperCase())) {
                            indice = auxiliar;
                        }
                        if (indice > -1) {
                            peso = unEntrenamiento.getPeso().get(indice);
                            repeticiones = unEntrenamiento.getRepeticiones().get(indice);
                            series = unEntrenamiento.getSeries();
                            volumenes.add(peso * repeticiones * series);
                            indice = -1;
                        }
                    }
                }
            }
            System.out.println(volumenes);
            for (Double volumen : volumenes) {
                volumenGM += volumen;
            }
            return volumenGM;
        } else {
            return 0d;
        }
    }

    public static Double calcularVolumenF(Long id, LocalDate fechaInicio, LocalDate fechaFinal) {
        Double volumenGM = 0d;
        List<Rutina> rutinasCliente = new ArrayList<>();
        RutinaService.findRutinas().forEach(rutina -> {
            if (rutina.getCliente().getDni().equals(id)) {
                if (!rutina.getFecha().isBefore(fechaInicio) && !rutina.getFecha().isAfter(fechaFinal)) {
                    rutinasCliente.add(rutina);
                }
            }
        });
        //Aca empieza la busqueda
        Set<Entrenamiento> setEntrenamientos;
        List<Double> volumenes = new ArrayList<>();
        for (Rutina unaRutina : rutinasCliente) {
            setEntrenamientos = unaRutina.getEntrenamientos();
            for (Entrenamiento unEntrenamiento : setEntrenamientos) {
                volumenes.add(unEntrenamiento.getVolumen());
            }
        }
        System.out.println(volumenes);
        for (Double volumen : volumenes) {
            volumenGM += volumen;
        }
        return volumenGM;
    }
}
