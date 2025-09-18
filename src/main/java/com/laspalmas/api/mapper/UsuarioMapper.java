package com.laspalmas.api.mapper;

import com.laspalmas.api.model.Usuario;
import com.laspalmas.api.model.UsuarioDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioDTO toDTO(Usuario usuario);
}
