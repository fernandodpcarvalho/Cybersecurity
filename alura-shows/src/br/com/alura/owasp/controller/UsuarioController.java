package br.com.alura.owasp.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.alura.owasp.dao.UsuarioDao;
import br.com.alura.owasp.model.Role;
import br.com.alura.owasp.model.Usuario;
import br.com.alura.owasp.model.UsuarioDTO;
import br.com.alura.owasp.retrofit.GoogleWebClient;
import br.com.alura.owasp.validator.ImagemValidator;

@Controller
@Transactional
public class UsuarioController {

	@Autowired
	private UsuarioDao dao;
	
	@Autowired
	GoogleWebClient cliente;
	
	@Autowired
	private ImagemValidator ImagemValidator;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setAllowedFields("nome", "email", "senha", "nomeImagem");
	}

	@RequestMapping("/usuario")
	public String usuario(Model model) {
		Usuario usuario = new Usuario();
		model.addAttribute("usuario", usuario);
		return "usuario";
	}

	@RequestMapping("/usuarioLogado")
	public String usuarioLogado() {
		return "usuarioLogado";
	}

	@RequestMapping(value = "/registrar", method = RequestMethod.POST)
	public String registrar(MultipartFile imagem,
			@ModelAttribute("usuarioRegistro") UsuarioDTO usuarioDTO,
			RedirectAttributes redirect, HttpServletRequest request,
			Model model, HttpSession session) throws IllegalStateException, IOException {
		//Se usar o initBinder, pode-se retirar o DTO
		Usuario usuarioRegistro = usuarioDTO.montaUsuario();

		if(ImagemValidator.tratarImagem(imagem, usuarioRegistro, request)) {
			usuarioRegistro.getRoles().add(new Role("ROLE_USER"));			
			dao.salva(usuarioRegistro);
			session.setAttribute("usuario", usuarioRegistro);
			model.addAttribute("usuario", usuarioRegistro);
			return "usuarioLogado";
		}
		
		redirect.addFlashAttribute("mensagem", "A imagem passada não é válida!");
		return "redirect:/usuario";
	}

	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String login(@ModelAttribute("usuario") Usuario usuario,
			RedirectAttributes redirect, Model model, HttpSession session, HttpServletRequest request) throws IOException {

		boolean verificaRecaptcha = cliente.verifica(request.getParameter("g-recaptcha-response"));
		if(verificaRecaptcha) {
			return procurarUsuario(usuario, redirect, model, session);
		}
		
		redirect.addFlashAttribute("mensagem", "Comprove que você é humano!");
		return "redirect:/usuario";

	}

	private String procurarUsuario(Usuario usuario, RedirectAttributes redirect, Model model, HttpSession session) {
		Usuario usuarioRetornado = dao.procuraUsuario(usuario);
		model.addAttribute("usuario", usuarioRetornado);
		if (usuarioRetornado == null) {
			redirect.addFlashAttribute("mensagem", "Usuário não encontrado");
			return "redirect:/usuario";
		}
		session.setAttribute("usuario", usuarioRetornado);
		return "usuarioLogado";
	
	}

	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("usuario");
		return "usuario";
	}
}
