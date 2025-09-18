package com.laspalmas.api.mapper;


import com.laspalmas.api.model.MensajeDTO;
import com.laspalmas.api.model.Mensaje;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MensajeMapper {

    @Mapping(source = "remitente.id", target = "remitenteId")
    @Mapping(source = "remitente.nombre", target = "remitenteNombre")
    @Mapping(source = "destinatario.id", target = "destinatarioId")
    @Mapping(source = "destinatario.nombre", target = "destinatarioNombre")
    @Mapping(source = "archivo.id", target = "archivoId")
    @Mapping(source = "pedido.id", target = "pedidoId")
    @Mapping(source = "estado", target = "estado")
    MensajeDTO toDTO(Mensaje mensaje);

    @InheritInverseConfiguration
    @Mapping(target = "remitente", ignore = true)
    @Mapping(target = "destinatario", ignore = true)
    @Mapping(target = "archivo", ignore = true)
    @Mapping(target = "pedido", ignore = true)
    Mensaje toEntity(MensajeDTO dto);
}
