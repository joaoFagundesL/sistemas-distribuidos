package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

import modelo.Candidato;
import modelo.Competencia;

public class CompetenciaDAO extends GenericoDAO<Competencia> {
  @Transactional
  public void insertWithQuery(Competencia competencia, Candidato c) throws Exception {
    EntityManager em = getEM();

    Query query = em.createNativeQuery("INSERT INTO Competencia (skill, candidato_id)"
      + " VALUES (?, ?)");

    em.getTransaction().begin();
    query.setParameter(1, competencia.getSkill());
    query.setParameter(2, c.getId());

    query.executeUpdate();
    em.getTransaction().commit();
  }
 
  public void update(Competencia c, String skill) {
    EntityManager em = getEM();

    em.getTransaction().begin();

    c = consultarPorId(Competencia.class, c.getId());

    if (skill != null && !skill.isEmpty()) {
      c.setSkill(skill);
    }
    em.merge(c);
    em.getTransaction().commit();
  }

  public Competencia listarCompetenciaNome(String skill) {
	    EntityManager em = getEM();
	    Competencia competencia = null;

	    try {
	        Query query = em.createNamedQuery("Competencia.listarCompetenciaNome");
	        query.setParameter("skill", skill);
	        
	        competencia = (Competencia) query.getSingleResult();
	    } catch (NoResultException e) {
	        competencia = null;
	    }

	    return competencia;
	}
 }
