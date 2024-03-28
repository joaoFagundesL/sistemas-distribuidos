package dao;

import javax.persistence.*;
import modelo.*;

public class GenericoDAO<T extends Entidade> {
	
	public EntityManager getEM() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-project");
		return emf.createEntityManager();
	}
	
	public T salvar(T t) throws Exception {
		EntityManager em = getEM();
		
		try {
			em.getTransaction().begin();
			
			if(t.getId() == null) {em.persist(t);}
			else {t = em.merge(t);}
			
			em.getTransaction().commit();
		
		} finally {em.close(); }
	
		return t;
	}
	
	public void remover(Class<T> clazz, Integer id) {
		EntityManager em = getEM();
		try {
			em.getTransaction().begin();
			
			T t = em.find(clazz, id);
			
			em.remove(t);
			em.getTransaction().commit();
		} finally {
			em.close();
		}
	}
	
	public T consultarPorId(Class<T> clazz, Integer id) {
		EntityManager em = getEM();
		T t = null;
		
		try {t = em.find(clazz, id);}
		
		finally {em.close();}
		
		return t;
	}
}