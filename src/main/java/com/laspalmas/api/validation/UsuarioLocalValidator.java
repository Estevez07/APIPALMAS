package com.laspalmas.api.validation;

import com.laspalmas.api.model.Usuario;
import com.laspalmas.api.model.enums.Provider;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsuarioLocalValidator implements ConstraintValidator<UsuarioLocal, Usuario> {

    @Override
    public boolean isValid(Usuario usuario, ConstraintValidatorContext context) {
        if (usuario == null) return true; // otro validator se encargará de null

        // Solo validar si el provider es LOCAL
        if (usuario.getProviderInfo().getProvider() == Provider.LOCAL) {
            boolean valid = usuario.getContraseña() != null && !usuario.getContraseña().isBlank()
                         && usuario.getFechaNac() != null;

            if (!valid) {
                // Personalizar mensajes de error para cada campo
                context.disableDefaultConstraintViolation();
                if (usuario.getContraseña() == null || usuario.getContraseña().isBlank()) {
                    context.buildConstraintViolationWithTemplate("La contraseña es obligatoria para usuarios locales")
                           .addPropertyNode("contraseña")
                           .addConstraintViolation();
                }
                if (usuario.getFechaNac() == null) {
                    context.buildConstraintViolationWithTemplate("La fecha de nacimiento es obligatoria para usuarios locales")
                           .addPropertyNode("fechaNac")
                           .addConstraintViolation();
                }
            }

            return valid;
        }

        return true; // Si no es LOCAL, siempre válido
    }
}
