package controller;
import dao.CandidatoDAO;
import modelo.Candidato;
import modelo.Usuario;

public class CandidatoController {
	CandidatoDAO dao = new CandidatoDAO();

  public Boolean isUserValid(String usuario) {
		Candidato c = dao.consultarPeloUser(usuario);

    if(c == null) {
      return false;
    }
    return true;
  }

  public Boolean isPasswordValid(String usuario, String senha) {
		Candidato c = dao.consultarPeloUser(usuario);

    String passHash = c.getUsuario().getSenha();
    if(!senha.equals(passHash)) {
      return false;
    }
    return true;
  }

  public Candidato getCandidadoLogin(String usuario) {
		Candidato c = dao.consultarPeloUser(usuario);
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
