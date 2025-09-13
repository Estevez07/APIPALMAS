package com.laspalmas.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laspalmas.api.model.Archivo;

public interface ArchivoRepository  extends JpaRepository<Archivo, Long> {

}
