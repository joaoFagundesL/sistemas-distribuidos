package dao;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import modelo.VagaCandidato;

public class VagaCandidatoDAO extends GenericoDAO<VagaCandidato> {
	public void inserirVagaCandidato(Integer candidatoId, Integer vagaId) {
	    EntityManager em = getEM();

	    Query query = em.createNativeQuery("INSERT INTO VagaCandidato (vaga_id, candidato_id) VALUES (?, ?)");
	    em.getTransaction().begin();
	    query.setParameter(1, vagaId);
	    query.setParameter(2, candidatoId);
	    query.executeUpdate();
	    em.getTransaction().commit();
	  }
}
