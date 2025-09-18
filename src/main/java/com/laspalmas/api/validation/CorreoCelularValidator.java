package com.laspalmas.api.validation;

import com.laspalmas.api.model.Usuario;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CorreoCelularValidator implements ConstraintValidator<CorreoOCelular, Usuario> {

    @Override
    public boolean isValid(Usuario usuario, ConstraintValidatorContext context) {
        if (usuario == null) return true; 
        return (usuario.getCorreo() != null && !usuario.getCorreo().isBlank())
            || (usuario.getNumeroCelular() != null && !usuario.getNumeroCelular().isBlank());
    }
}
