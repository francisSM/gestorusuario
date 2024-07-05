package com.example.gestorusuario.controller;

import com.example.gestorusuario.model.Usuario;
import com.example.gestorusuario.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@SessionAttributes("usuario")
public class UsuarioController {

    @Autowired
    UsuarioRepository usuarioRepository;

    @GetMapping({"/", "/index"})
    public String getIndexPage() {
        return "index";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam("email") String email,
                            @RequestParam("contrasena") String contrasena,
                            Model model) {
        Usuario usuario = usuarioRepository.findByEmailAndContrasena(email, contrasena);
        if (usuario != null) {
            model.addAttribute("usuario", usuario);
            return "redirect:/menu";
        } else {
            model.addAttribute("error", "Credenciales incorrectas. Intente de nuevo.");
            return "index";
        }
    }

    @GetMapping("/usuarios")
    public String getUsersPage(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "usuarios";
    }

    @GetMapping("/menu")
    public String getMenuPage() {
        return "menu";
    }

    @GetMapping("/registrarse")
    public String getRegPage(@ModelAttribute("usuario") Usuario usuario) {
        return "registrarse";
    }

    @PostMapping("/registrarse")
    public String saveUser(@ModelAttribute("usuario") Usuario usuario,
                           @RequestParam("confirmContrasena") String confirmContrasena,
                           Model model) {
        // Verificar si el correo ya está registrado
        Usuario existingUser = usuarioRepository.findByEmail(usuario.getEmail());
        if (existingUser != null) {
            model.addAttribute("message", "El correo ya está registrado");
            return "registrarse";
        }

        // Verificar si las contraseñas coinciden
        if (!usuario.getContrasena().equals(confirmContrasena)) {
            model.addAttribute("message", "Las contraseñas no coinciden");
            return "registrarse";
        }

        // Guardar el usuario si las validaciones pasan
        usuarioRepository.save(usuario);
        model.addAttribute("message", "Usuario Registrado");
        return "registrado";
    }

    @GetMapping("/perfil")
    public String getPerfilPage(@ModelAttribute("usuario") Usuario usuario, Model model) {
        // La anotación @ModelAttribute asegura que el usuario esté disponible en el modelo
        return "perfil";
    }

    @PostMapping("/perfil")
    public String editarPerfil(@ModelAttribute("usuario") Usuario usuario,
                               Model model) {
        // Guardar los cambios en el perfil del usuario
        usuarioRepository.save(usuario);
        model.addAttribute("message", "Perfil actualizado correctamente");
        return "perfil";
    }

    @GetMapping("/editarPerfil")
    public String editarPerfilForm(@ModelAttribute("usuario") Usuario usuario, Model model) {
        // Cargar el usuario actual desde la base de datos usando su ID
        Usuario currentUser = usuarioRepository.findById(usuario.getId()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Agregar el usuario al modelo para que los campos del formulario se llenen automáticamente
        model.addAttribute("usuario", currentUser);

        return "editarPerfil"; // Nombre del archivo HTML o plantilla Thymeleaf
    }

    @PostMapping("/editarPerfil")
    public String actualizarPerfil(@ModelAttribute("usuario") Usuario usuario,
                                   Model model) {
        // Guardar los cambios en el perfil del usuario
        usuarioRepository.save(usuario);
        model.addAttribute("message", "Perfil actualizado correctamente");

        return "perfil"; // Nombre del archivo HTML o plantilla Thymeleaf
    }

    @PostMapping("/eliminarPerfil")
    public String eliminarPerfil(@ModelAttribute("usuario") Usuario usuario,
                                 SessionStatus sessionStatus, Model model) {
        // Eliminar el usuario de la base de datos
        usuarioRepository.delete(usuario);

        // Limpiar los atributos de sesión al cerrar sesión
        sessionStatus.setComplete();

        model.addAttribute("message", "Perfil eliminado correctamente");

        return "redirect:/index";
    }

    @ModelAttribute("usuario")
    public Usuario getDefaultUser() {
        return new Usuario(); // Retorna un usuario vacío como default
    }

    @GetMapping("/logout")
    public String logout(SessionStatus sessionStatus) {
        // Limpiar los atributos de sesión al cerrar sesión
        sessionStatus.setComplete();
        return "redirect:/index";
    }
}
