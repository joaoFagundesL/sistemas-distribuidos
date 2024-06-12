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

    Query query = em.createNativeQuery("INSERT INTO Vaga(experience, competencia_id, empresa_id)"
      + " VALUES (?, ?, ?)");

    em.getTransaction().begin();
    query.setParameter(2, vaga.getSkill().getId());
    query.setParameter(3, vaga.getEmpresa().getId());
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
  
  public List<Vaga> findBySkills(List<String> competencias, String filter) {
      if (competencias == null || competencias.isEmpty()) {
          return new ArrayList<>();
      }
      
      if (filter.equals("E")) {
    	  filter = "AND";
    	  
      } else {
		filter = "OR";
	  }

      EntityManager em = getEM();
      StringBuilder jpql = new StringBuilder("SELECT v FROM Vaga v JOIN v.competencia c WHERE ");
      for (int i = 0; i < competencias.size(); i++) {
          jpql.append("c.skill = :competencia").append(i);
          if (i < competencias.size() - 1) {
              jpql.append(" " + filter + " ");
          }
      }

      TypedQuery<Vaga> query = em.createQuery(jpql.toString(), Vaga.class);
      for (int i = 0; i < competencias.size(); i++) {
          query.setParameter("competencia" + i, competencias.get(i));
      }
      
      System.out.println("query = " + jpql.toString());
      return query.getResultList();
  }
  
  public List<Vaga> getBySkillsAndExperience(List<String> competencias, Integer experiencia, String filter) {
	    if (competencias == null || competencias.isEmpty() || experiencia == null) {
	        return new ArrayList<>();
	    }
	    
	    if (filter.equals("E")) {
	        filter = "AND";
	    } else {
	        filter = "OR";
	    }

	    EntityManager em = getEM();
	    StringBuilder jpql = new StringBuilder("SELECT DISTINCT v FROM Vaga v ");
	    jpql.append("JOIN v.competencia c WHERE ");

	    jpql.append("(");
	    for (int i = 0; i < competencias.size(); i++) {
	        jpql.append("c.skill = :competenciaId").append(i);
	        if (i < competencias.size() - 1) {
	            jpql.append(" ").append(filter).append(" ");
	        }
	    }
	    jpql.append(") " + filter + " ");

	    jpql.append("v.experience <= :experiencia");

	    TypedQuery<Vaga> query = em.createQuery(jpql.toString(), Vaga.class);

	    for (int i = 0; i < competencias.size(); i++) {
	        query.setParameter("competenciaId" + i, competencias.get(i));
	    }

	    query.setParameter("experiencia", experiencia);

	    return query.getResultList();
	}



  public List<Vaga> getByExperience(Integer experiencia, String filter) {
	  		  
	    EntityManager em = getEM();
	    
	    if (filter.equals("E")) {
	    	filter = "AND";
	    	
	    } else {
	    	filter = "OR";
	    }
	    System.out.println("experience = " + experiencia);
	    StringBuilder jpql = new StringBuilder("SELECT DISTINCT v FROM Vaga v WHERE v.experience <= :experiencia");

	    TypedQuery<Vaga> query = em.createQuery(jpql.toString(), Vaga.class);

	    query.setParameter("experiencia", experiencia);

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
