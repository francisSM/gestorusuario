package com.example.gestorusuario.repositories;

import com.example.gestorusuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByEmailAndContrasena(String email, String contrasena);
    Usuario findByEmail(String email);  // Ajuste para devolver un único usuario
}
