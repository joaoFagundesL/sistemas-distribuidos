package dao;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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
  
  public List<Vaga> findBySkills(List<Integer> competenciaIds, String filter) {
      if (competenciaIds == null || competenciaIds.isEmpty()) {
          return new ArrayList<>();
      }

      EntityManager em = getEM();
      StringBuilder jpql = new StringBuilder("SELECT v FROM Vaga v JOIN v.competencia c WHERE ");
      for (int i = 0; i < competenciaIds.size(); i++) {
          jpql.append("c.id = :competenciaId").append(i);
          if (i < competenciaIds.size() - 1) {
              jpql.append(" " + filter + " ");
          }
      }

      TypedQuery<Vaga> query = em.createQuery(jpql.toString(), Vaga.class);
      for (int i = 0; i < competenciaIds.size(); i++) {
          query.setParameter("competenciaId" + i, competenciaIds.get(i));
      }

      return query.getResultList();
  }
  
  public List<Vaga> getBySkillsAndExperience(List<Integer> competenciaIds, List<Integer> experienciaIds, String filter) {
	    if (competenciaIds == null || competenciaIds.isEmpty() || experienciaIds == null || experienciaIds.isEmpty()) {
	        return new ArrayList<>();
	    }

	    EntityManager em = getEM();
	    StringBuilder jpql = new StringBuilder("SELECT DISTINCT v FROM Vaga v ");
	    jpql.append("JOIN v.competencia c WHERE ");

	    jpql.append("(");
	    for (int i = 0; i < competenciaIds.size(); i++) {
	        jpql.append("c.id = :competenciaId").append(i);
	        if (i < competenciaIds.size() - 1) {
	            jpql.append(" ").append(filter).append(" ");
	        }
	    }
	    jpql.append(") AND ");

	    jpql.append("(");
	    for (int i = 0; i < experienciaIds.size(); i++) {
	        jpql.append("v.experiencia.id = :experienciaId").append(i);
	        if (i < experienciaIds.size() - 1) {
	            jpql.append(" ").append(filter).append(" ");
	        }
	    }
	    jpql.append(")");

	    TypedQuery<Vaga> query = em.createQuery(jpql.toString(), Vaga.class);

	    for (int i = 0; i < competenciaIds.size(); i++) {
	        query.setParameter("competenciaId" + i, competenciaIds.get(i));
	    }

	    for (int i = 0; i < experienciaIds.size(); i++) {
	        query.setParameter("experienciaId" + i, experienciaIds.get(i));
	    }

	    return query.getResultList();
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
