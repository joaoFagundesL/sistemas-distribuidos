package dao;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import modelo.Vaga;

public class VagaDAO extends GenericoDAO<Vaga> {
 @Transactional
  public void insertWithQuery(Vaga vaga) throws Exception {
    EntityManager em = getEM();

    em.getTransaction().begin();

    em.persist(vaga);

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
