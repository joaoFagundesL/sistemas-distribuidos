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

    Query query = em.createNativeQuery("INSERT INTO Competencia (skill, experience, candidato_id)"
      + " VALUES (?, ?, ?)");

    em.getTransaction().begin();
    query.setParameter(1, competencia.getSkill());
    query.setParameter(2, competencia.getExperience());
    query.setParameter(3, c.getId());

    query.executeUpdate();
    em.getTransaction().commit();
  }

  public void update(Competencia c, String skill, Integer experience) {
    EntityManager em = getEM();

    em.getTransaction().begin();

    c = consultarPorId(Competencia.class, c.getId());

    if (skill != null && !skill.isEmpty()) {
      c.setSkill(skill);
    }

    if (experience != null) { 
      c.setExperience(experience);
    }
    em.merge(c);
    em.getTransaction().commit();
  }

  @SuppressWarnings("unchecked")
  public List<Competencia> listarCompetenciaUsuario(Integer id) {
    EntityManager em = getEM();
    List<Competencia> competencias;

    Query query = em.createNamedQuery("Competencia.listarCompetenciaUsuario");
    query.setParameter("id", id);

    competencias = query.getResultList();
    return competencias;
  }

  public Competencia listarCompetenciaEspecifica(Integer candidatoId, Integer competenciaId) {
    EntityManager em = getEM();
    Competencia competencia = null;

    try {
      Query query = em.createNamedQuery("Competencia.listarCompetenciaEspecifica");
      query.setParameter("candidatoId", candidatoId);
      query.setParameter("competenciaId", competenciaId);

      competencia = (Competencia) query.getSingleResult();
    } catch (NoResultException e) {
      competencia = null;
    }

    return competencia;
  }

}
