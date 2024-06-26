package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import modelo.CandidatoCompetencia;
import modelo.CandidatoEmpresa;
import modelo.Competencia;

public class CandidatoEmpresaDAO extends GenericoDAO<CandidatoEmpresa> {
  public void inserirEmpresaCandidato(Integer empresaId, Integer candidatoId) {
    EntityManager em = getEM();

    Query query = em.createNativeQuery("INSERT INTO CandidatoEmpresa (candidato_id, empresa_id) VALUES (?, ?)");
    em.getTransaction().begin();
    query.setParameter(1, candidatoId);
    query.setParameter(2, empresaId);
    query.executeUpdate();
    em.getTransaction().commit();
  }
  
  public List<CandidatoEmpresa> consultarEmpresas(Integer userId) {
	  EntityManager em = getEM();
	    List<CandidatoEmpresa> empresas;

	    Query query = em.createNamedQuery("CandidatoEmpresa.consultarEmpresas");
	    query.setParameter("candidatoId", userId);

	    empresas = query.getResultList();
	    return empresas;
  }
}
