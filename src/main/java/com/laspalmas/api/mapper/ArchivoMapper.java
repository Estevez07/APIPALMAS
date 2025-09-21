package com.laspalmas.api.mapper;

import com.laspalmas.api.dto.ArchivoDTO;
import com.laspalmas.api.model.Archivo;

import org.mapstruct.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Mapper(componentModel = "spring")
public interface ArchivoMapper {

    @Mapping(target = "base64", expression = "java(encodeBase64(archivo.getContenido()))")
    ArchivoDTO toDTO(Archivo archivo);

    // Método auxiliar para codificar Base64
    default String encodeBase64(byte[] contenido) {
        return contenido != null ? Base64.getEncoder().encodeToString(contenido) : null;
    }

//<<<<<<<<<<<<<<<<<<<<<<-------------------------------------------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pedido", ignore = true) // lo seteamos en el servicio
    @Mapping(target = "contenido", expression = "java(fileToBytes(file))")
    @Mapping(target = "nombre", expression = "java(file.getOriginalFilename())")
    @Mapping(target = "tipo", expression = "java(file.getContentType())")
    Archivo toEntity(MultipartFile file);

    // helper para manejar IOException
    default byte[] fileToBytes(MultipartFile file) {
        try {
            return file != null ? file.getBytes() : null;
        } catch (IOException e) {
            throw new RuntimeException("Error al leer archivo", e);
        }
    }





}
