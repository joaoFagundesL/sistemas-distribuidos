package dao;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import modelo.Candidato;
import modelo.Competencia;

public class CompetenciaDAO extends GenericoDAO<Competencia> {
	@Transactional
	  public void insertWithQuery(Competencia competencia, Candidato c) throws Exception {
	    EntityManager em = getEM();

	    Query query = em.createNativeQuery("INSERT INTO Competencia (skill, experience, candidato_id)"
	      + " VALUES (?, ?, ?)");

	    em.getTransaction().begin();
	    query.setParameter(1, competencia.getSkill());
	    query.setParameter(2, competencia.getExperience());
	    query.setParameter(3, c.getId());

	    query.executeUpdate();
	    em.getTransaction().commit();
	  }
}
