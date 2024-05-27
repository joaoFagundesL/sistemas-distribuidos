package dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;
import modelo.Empresa;
import modelo.Pessoa;
import modelo.Usuario;

public class EmpresaDAO extends GenericoDAO<Empresa> {
	
	public void update(Empresa e, String nome, String email, String senha, String industry, String description) {
		EntityManager em = getEM();
		
		em.getTransaction().begin();
		
		e = consultarPorId(Empresa.class, e.getId());
		
    e.setUsuario(e.getUsuario());

    if (e.getUsuario() instanceof Pessoa) {
      Pessoa pessoa = (Pessoa) e.getUsuario(); 
      if (pessoa instanceof Usuario) {
        Usuario user = (Usuario) pessoa;
        user.setSenha(senha);
        if (industry != null && !industry.isEmpty()) {
          e.setIndustry(industry);
        }
        if (description != null && !description.isEmpty()) {
          e.setDescricao(description);
        }

      } else {
        throw new IllegalArgumentException("object nao é User");
      }
    } else {
      throw new IllegalArgumentException("object nao é Pessoa");
    }

    em.merge(e);
    em.getTransaction().commit();
  }

  public Empresa consultarPeloUsuarioIdEmail(String email) {
    EntityManager em = getEM();
    Empresa empresa;
    try {
      Query query = em.createNamedQuery("Empresa.consultarPeloUsuarioIdEmail");
      query.setParameter("email", email);
      empresa = (Empresa) query.getSingleResult();

      return empresa;
    } catch(NoResultException e) {
      return null;
    }
  }

  @Transactional
  public void insertWithQuery(Empresa e) throws Exception {
    EntityManager em = getEM();

    Query query = em.createNativeQuery("INSERT INTO Empresa (industry, descricao, usuario_id)"
      + " VALUES (?, ?, ?)");

    em.getTransaction().begin();
    query.setParameter(1, e.getIndustry());
    query.setParameter(2, e.getDescricao());
    query.setParameter(3, e.getUsuario().getId());

    query.executeUpdate();
    em.getTransaction().commit();
  }
}
