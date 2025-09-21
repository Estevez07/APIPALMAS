package com.laspalmas.api.mapper;

import com.laspalmas.api.dto.PedidoDTO;
import com.laspalmas.api.model.Pedido;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UsuarioMapper.class, ArchivoMapper.class})
public interface PedidoMapper {
    PedidoDTO toDTO(Pedido pedido);
}
