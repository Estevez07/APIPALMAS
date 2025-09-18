package com.laspalmas.api.mapper;


import com.laspalmas.api.model.MensajeDTO;
import com.laspalmas.api.model.Mensaje;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {UsuarioMapper.class, ArchivoMapper.class, PedidoMapper.class})
public interface MensajeMapper {

    @Mapping(target = "archivos", ignore = true)  // se setea solo si viene
    @Mapping(target = "pedido", ignore = true)   // se setea solo si viene
    MensajeDTO toDTO(Mensaje mensaje);

    
    }