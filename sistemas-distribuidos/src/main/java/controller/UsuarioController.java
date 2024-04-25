package controller;

import dao.UsuarioDAO;
import modelo.Candidato;
import modelo.Usuario;

public class UsuarioController {
	public void update(Candidato c, String nome, String email, String usuario, String senha) {
		UsuarioDAO udao = new UsuarioDAO();
		udao.update(c.getUsuario(), nome, email, usuario, senha);
	}
	
	public void remover(Class<Usuario> clazz, Integer id) {
		UsuarioDAO udao = new UsuarioDAO();
		udao.remover(Usuario.class, id);
    }
	
	public Usuario insert(String nome, String email, String usuario, String senha) {
		UsuarioDAO dao = new UsuarioDAO();
		Usuario u = new Usuario();
		u.setUser(usuario); 
		u.setSenha(senha);
		u.setEmail(email);
		u.setNome(nome);
		try {
			dao.insertWithQuery(u);
			return u;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
}