package controller;

import dao.EmpresaDAO;
import modelo.Empresa;
import modelo.Usuario;

public class EmpresaController {
  EmpresaDAO dao = new EmpresaDAO();

  public Empresa insert(Usuario u, String descricao, String industry) {
    EmpresaDAO dao = new EmpresaDAO();
    Empresa a = new Empresa();
    a.setUsuario(u);
    a.setIndustry(industry);
    a.setDescricao(descricao);

    try {
      dao.insertWithQuery(a);
      return a;
    } catch (Exception e1) {
      e1.printStackTrace();
      return null;
    }
  }

  public void update(Empresa c, String nome, String email, String senha, String industry, String description) {
    EmpresaDAO udao = new EmpresaDAO();
    udao.update(c, nome, email, senha, industry, description);
  }

  public Empresa consultarPorId(Integer id) {
    return dao.consultarPorId(Empresa.class, id);
  }

  public Boolean isUserValid(String email) {
    Empresa e = dao.consultarPeloUsuarioIdEmail(email);

    if(e == null) {
      return false;
    }
    return true;
  }

  public Boolean isPasswordValid(String email, String senha) {
    Empresa e = dao.consultarPeloUsuarioIdEmail(email);

    String passHash = e.getUsuario().getSenha();
    if(!senha.equals(passHash)) {
      return false;
    }
    return true;
  }

  public Integer consultarId(String email) {
    Empresa e = dao.consultarPeloUsuarioIdEmail(email);
    return e.getId();
  }

  public Empresa getCandidadoLogin(String email) {
    Empresa e = dao.consultarPeloUsuarioIdEmail(email);
    return e;
  }

  public void remover(Class<Empresa> clazz, Integer id) {
    EmpresaDAO dao = new EmpresaDAO();
    dao.remover(Empresa.class, id);
  }
}
