package controller;

import dao.EmpresaDAO;
import dao.UsuarioDAO;
import modelo.Empresa;
import modelo.Usuario;

public class EmpresaController {
  EmpresaDAO dao = new EmpresaDAO();

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

  public Empresa consultarPorId(Integer id) {
    return dao.consultarPorId(Empresa.class, id);
  }

  public Boolean isUserValid(String email) {
    Empresa e = dao.consultarPeloUsuarioId(email);

    if(e == null) {
      return false;
    }
    return true;
  }


  public Boolean isPasswordValid(String email, String senha) {
    Empresa e = dao.consultarPeloUsuarioId(email);

    String passHash = e.getUsuario().getSenha();
    if(!senha.equals(passHash)) {
      return false;
    }
    return true;
  }

  public Integer consultarId(String email) {
    Empresa e = dao.consultarPeloUsuarioId(email);
    return e.getId();
  }

  public Empresa getCandidadoLogin(String email) {
    Empresa e = dao.consultarPeloUsuarioId(email);
    return e;
  }


}
