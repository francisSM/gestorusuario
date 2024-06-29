package com.example.gestorusuario.controller;
import com.example.gestorusuario.model.Usuario;
import com.example.gestorusuario.repositories.UsuarioRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UsuarioController {
    @Autowired
    UsuarioRepository usuarioRepository;
    public UsuarioController() {
    }

    @GetMapping({"/registrarse"})
    public String getRegPage(@ModelAttribute("usuario") Usuario usuario) {
        return "registrarse";
    }

    @PostMapping({"/registrarse"})
    public String saveUser(@ModelAttribute("usuario") Usuario usuario, Model model) {
        this.usuarioRepository.save(usuario);
        model.addAttribute("message", "Usuario Registrado");
        return "registrarse";
    }

    @GetMapping({"/usuarios"})
    public String usersPage(Model model) {
        List<Usuario> listaUsuarios = this.usuarioRepository.findAll();
        model.addAttribute("usuario", listaUsuarios);
        return "usuarios";
    }
}