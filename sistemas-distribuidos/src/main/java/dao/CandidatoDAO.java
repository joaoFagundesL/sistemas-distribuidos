package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

import modelo.Candidato;
import modelo.Pessoa;
import modelo.Usuario;

public class CandidatoDAO extends GenericoDAO<Candidato> {
	
	/* Retorna todos os candidatos */
	@SuppressWarnings("unchecked")
	public List<Candidato> consultarTodos() {
		EntityManager em = getEM();
		List<Candidato> candidatos;
		
		Query query = em.createNamedQuery("Candidato.consultarTodos");
		
		candidatos = query.getResultList();
//		System.out.println(candidatos);
		return candidatos;
	}

  public Candidato findByIdWithUser(Integer id) {
    EntityManager em = getEM();
    Candidato candidato;
    try {
      Query query = em.createNamedQuery("Candidato.findByIdWithUser");
      query.setParameter("id", id);
      candidato = (Candidato) query.getSingleResult();
      return candidato;
    } catch(NoResultException e) {
      return null;
    }
  }

  public Candidato consultarPeloEmail(String email) {
    EntityManager em = getEM();
    Candidato candidato;
    try {
      Query query = em.createNamedQuery("Candidato.consultarEmail");
      query.setParameter("email", email);
      candidato = (Candidato) query.getSingleResult();

      return candidato;
    } catch(NoResultException e) {
      return null;
    }
  }

  public Candidato consultarPeloUsuarioIdEmail(String email) {
    EntityManager em = getEM();
    Candidato candidato;
    try {
      Query query = em.createNamedQuery("Candidato.consultarPeloUsuarioIdEmail");
      query.setParameter("email", email);
      candidato = (Candidato) query.getSingleResult();

      return candidato;
    } catch(NoResultException e) {
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  public List<Candidato> consultaPeloNome(String nome) {
    EntityManager em = getEM();
    List<Candidato> candidatos;

    Query query = em.createNamedQuery("Candidato.consultarTodosNome");
    query.setParameter("name", nome);

    candidatos = query.getResultList();
    return candidatos;
  }

  /* Atualiza os registros */
  public void update(Candidato a, String nome, String email, String senha) {
    EntityManager em = getEM();

    em.getTransaction().begin();

    a = consultarPorId(Candidato.class, a.getId());

    //		a.getUsuario().setNome(nome);
    //		a.getUsuario().setEmail(email);
    //		a.getUsuario().setSenha(senha);
    //		a.getUsuario().setUser(usuario);
    a.setUsuario(a.getUsuario());

    if (a.getUsuario() instanceof Pessoa) {
      Pessoa pessoa = (Pessoa) a.getUsuario(); 
      if (pessoa instanceof Usuario) {
        Usuario user = (Usuario) pessoa;
        user.setSenha(senha);
      } else {
        throw new IllegalArgumentException("object nao é User");
      }
    } else {
      throw new IllegalArgumentException("object nao é Pessoa");
    }

    em.merge(a);
    em.getTransaction().commit();
  }

  /* Insere no banco */
  @Transactional
  public void insertWithQuery(Candidato candidato) throws Exception {
    EntityManager em = getEM();

    Query query = em.createNativeQuery("INSERT INTO Candidato (usuario_id)"
      + " VALUES (?)");

    em.getTransaction().begin();
    query.setParameter(1, candidato.getUsuario().getId());

    query.executeUpdate();
    em.getTransaction().commit();
  }
}
