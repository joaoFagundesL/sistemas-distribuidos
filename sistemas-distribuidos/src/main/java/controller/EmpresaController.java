package controller;

import dao.EmpresaDAO;
import dao.UsuarioDAO;
import modelo.Empresa;
import modelo.Usuario;

public class EmpresaController {
  UsuarioDAO dao = new UsuarioDAO();

	public Empresa insert(Usuario u, String descricao, String branch) {
		EmpresaDAO dao = new EmpresaDAO();
		Empresa a = new Empresa();
		a.setUsuario(u);
    a.setBranch(branch);
    a.setDescricao(descricao);
		
		try {
			dao.insertWithQuery(a);
      return a;
    } catch (Exception e1) {
      e1.printStackTrace();
      return null;
    }
  }

  public Boolean isUserValid(String email) {
    Usuario u = dao.consultarPeloEmail(email);

    if(u == null) {
      return false;
    }
    return true;
  }


  public Boolean isPasswordValid(String email, String senha) {
    Usuario u = dao.consultarPeloEmail(email);

    String passHash = u.getSenha();
    if(!senha.equals(passHash)) {
      return false;
    }
    return true;
  }

  public Integer consultarId(String email) {
    Usuario u = dao.consultarPeloEmail(email);
    return u.getId();
  }

  public Usuario getCandidadoLogin(String email) {
    Usuario u = dao.consultarPeloEmail(email);
    return u;
  }


}
