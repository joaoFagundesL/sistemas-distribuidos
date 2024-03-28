package controller;
import dao.CandidatoDAO;
import modelo.Candidato;
import modelo.Usuario;

public class CandidatoController {
	CandidatoDAO dao = new CandidatoDAO();
	
	public Candidato validarLogin(String usuario, String senha) {
		Candidato c = dao.consultarPeloUser(usuario);
		if(c == null) {
			return null;
		} else {
			String passHash = c.getUsuario().getSenha();
			System.out.println(passHash + " " + senha);
			if(senha.equals(passHash)){
				System.out.println("valid!");
				return c;
			} else {
				System.out.println("not valid!");
				return null;
			}
		}
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