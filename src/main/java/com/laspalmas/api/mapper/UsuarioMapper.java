package com.laspalmas.api.mapper;

import com.laspalmas.api.dto.UsuarioDTO;
import com.laspalmas.api.model.Usuario;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioDTO toDTO(Usuario usuario);
}
