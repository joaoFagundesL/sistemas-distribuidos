package controller;

import dao.UsuarioDAO;
import modelo.Candidato;
import modelo.Usuario;

public class UsuarioController {
	public void update(Candidato c, String nome, String email, String senha) {
		UsuarioDAO udao = new UsuarioDAO();
		udao.update(c.getUsuario(), nome, email, senha);
	}

	public void remover(Class<Usuario> clazz, Integer id) {
		UsuarioDAO udao = new UsuarioDAO();
		udao.remover(Usuario.class, id);
  }

  public Usuario getUsuario(Integer id) {
    UsuarioDAO dao = new UsuarioDAO();
    return dao.consultarPorId(Usuario.class, id);
  }
	
	public Usuario insert(String nome, String email, String senha) {
		UsuarioDAO dao = new UsuarioDAO();
		Usuario u = new Usuario();
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
