package dao;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import modelo.Competencia;
import modelo.Vaga;

public class VagaDAO extends GenericoDAO<Vaga> {

  /* Insere no banco */
  @Transactional
  public void insertWithQuery(Vaga vaga) throws Exception {
    EntityManager em = getEM();

    Query query = em.createNativeQuery("INSERT INTO Vaga(experience, competencia_id)"
      + " VALUES (?, ?)");

    em.getTransaction().begin();
    query.setParameter(2, vaga.getSkill().getId());
    query.setParameter(1, vaga.getExperience());

    query.executeUpdate();
    em.getTransaction().commit();
  }

	@SuppressWarnings("unchecked")
	public List<Vaga> consultarTodos() {
		EntityManager em = getEM();
		List<Vaga> vagas;
		
		Query query = em.createNamedQuery("Vaga.consultarTodos");
		
		vagas = query.getResultList();
		return vagas;
	}

  public void update(Vaga vaga, Competencia skill, Integer experience) {
    EntityManager em = getEM();

    em.getTransaction().begin();

    if (skill != null && experience != null) {
      vaga.setSkill(skill);
      vaga.setExperience(experience);
    }
    em.merge(vaga);
    em.getTransaction().commit();
  }
 
//  public void inserirVagaCandidato(Integer vagaId, Integer candidatoId) throws Exception {
//    EntityManager em = getEM();
//
//    Query query = em.createNativeQuery("INSERT INTO Candidato_Vaga (vaga_id, candidatoId) VALUES (?, ?)");
//    em.getTransaction().begin();
//    query.setParameter(1, vagaId);
//    query.setParameter(2, candidatoId);
//    query.executeUpdate();
//    em.getTransaction().commit();
//  }
}
