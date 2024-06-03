package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import modelo.CandidatoCompetencia;
import modelo.Competencia;

public class CandidatoCompetenciaDAO extends GenericoDAO<CandidatoCompetencia> {
  public void inserirCompetenciaCandidato(Integer competenciaId, Integer candidatoId, Integer experience) {
    EntityManager em = getEM();

    Query query = em.createNativeQuery("INSERT INTO CandidatoCompetencia (candidato_id, competencia_id, experience) VALUES (?, ?, ?)");
    em.getTransaction().begin();
    query.setParameter(1, candidatoId);
    query.setParameter(2, competenciaId);
    query.setParameter(3, experience);
    query.executeUpdate();
    em.getTransaction().commit();
  }

  @SuppressWarnings("unchecked")
  public List<CandidatoCompetencia> listarCompetenciaUsuario(Integer id) {
    EntityManager em = getEM();
    List<CandidatoCompetencia> competencias;

    Query query = em.createNamedQuery("CandidatoCompetencia.listarCompetenciaUsuario");
    query.setParameter("id", id);

    competencias = query.getResultList();
    return competencias;
  }

  public void update(CandidatoCompetencia c, Integer experience, Competencia competencia) {
    EntityManager em = getEM();

    em.getTransaction().begin();

    c = consultarPorId(CandidatoCompetencia.class, c.getId());

    if (experience != null) {
      c.setExperience(experience);
    }
    
    if (competencia != null) {    	
    	c.setCompetencia(competencia);
    }
    

    em.merge(c);
    em.getTransaction().commit();
  }

  public CandidatoCompetencia listarCompetenciaEspecifica(Integer candidatoId, Integer competenciaId) {
    EntityManager em = getEM();
    CandidatoCompetencia competencia = null;

    try {
      Query query = em.createNamedQuery("CandidatoCompetencia.listarCompetenciaEspecifica");
      query.setParameter("candidatoId", candidatoId);
      query.setParameter("competenciaId", competenciaId);

      competencia = (CandidatoCompetencia) query.getSingleResult();
    } catch (NoResultException e) {
      competencia = null;
    }

    return competencia;
  }


}
