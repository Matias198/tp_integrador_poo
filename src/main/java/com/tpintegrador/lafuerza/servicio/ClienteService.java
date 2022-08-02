package com.tpintegrador.lafuerza.servicio;

import com.tpintegrador.lafuerza.modelo.Cliente;
import com.tpintegrador.lafuerza.repositorio.ClienteJpaController;
import com.tpintegrador.lafuerza.repositorio.exceptions.NonexistentEntityException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteService{

    private static final ClienteJpaController ClienteJPA = new ClienteJpaController();

    public static void crearCliente(Cliente cliente){
        try {
            ClienteJPA.create(cliente);
        } catch (Exception ex) {
            Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void crearCli(Cliente cliente) {
        try {
            ClienteJPA.create(cliente);
        } catch (Exception ex) {
            Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void eliminarClienteById(Long idCliente) {
        try {
            ClienteJPA.destroy(idCliente);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void modificarCliente(Cliente cliente) {
        try {
            ClienteJPA.edit(cliente);
        } catch (Exception ex) {
            Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static List<Cliente> findClientes() {
        List<Cliente> listaClientes = ClienteJPA.findClienteEntities();
        return listaClientes;
    }
    
    public static Cliente findClienteById(Long idCliente){
        return ClienteJPA.findCliente(idCliente);
    }
}
