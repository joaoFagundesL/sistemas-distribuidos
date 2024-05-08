package controller;
import dao.CandidatoDAO;
import dao.UsuarioDAO;
import modelo.Candidato;
import modelo.Usuario;


/* mudar para GenericoController e implementar com usuario */
public class CandidatoController {
  CandidatoDAO dao = new CandidatoDAO();

  public boolean isUserValid(String email) {
    Candidato candidato = dao.consultarPeloUsuarioIdEmail(email);
    System.out.println(candidato == null);
    return candidato != null; 
  }

  public Candidato consultarPorId(Integer id) {
    return dao.consultarPorId(Candidato.class, id);
  }

  public Boolean isPasswordValid(String email, String senha) {
    Candidato c = dao.consultarPeloUsuarioIdEmail(email);

    String passHash = c.getUsuario().getSenha();
    if(!senha.equals(passHash)) {
      return false;
    }
    return true;
  }

  public Integer consultarId(String email) {
    Candidato c = dao.consultarPeloEmail(email);
    return c.getId();
  }

  public Candidato getCandidadoLogin(String email) {
    Candidato c = dao.consultarPeloEmail(email);
    return c;
  }

  public Candidato insert(Usuario u) {
    CandidatoDAO cdao = new CandidatoDAO();
    Candidato c = new Candidato();
    c.setUsuario(u);

    try {
      cdao.insertWithQuery(c);
      return c;
    } catch (Exception e1) {
      e1.printStackTrace();
      return null;
    }
  }

  public void remover(Class<Candidato> clazz, Integer id) {
    CandidatoDAO dao = new CandidatoDAO();
    dao.remover(Candidato.class, id);
  }

}
