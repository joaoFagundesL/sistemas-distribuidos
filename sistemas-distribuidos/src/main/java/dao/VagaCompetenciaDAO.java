package dao;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import modelo.VagaCompetencia;

public class VagaCompetenciaDAO  extends GenericoDAO<VagaCompetencia>{
	public void inserirVagaCompetencia(Integer competenciaId, Integer vagaId, Integer experience) {
	    EntityManager em = getEM();

	    Query query = em.createNativeQuery("INSERT INTO VagaCompetencia (vaga_id, competencia_id, experience) VALUES (?, ?, ?)");
	    em.getTransaction().begin();
	    query.setParameter(1, vagaId);
	    query.setParameter(2, competenciaId);
	    query.setParameter(3, experience);
	    query.executeUpdate();
	    em.getTransaction().commit();
	  }
}
