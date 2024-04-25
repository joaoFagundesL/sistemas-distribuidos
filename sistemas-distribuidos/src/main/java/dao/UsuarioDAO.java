package dao;

import java.math.BigInteger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

import modelo.Usuario;

public class UsuarioDAO extends GenericoDAO<Usuario>{
	
	public Usuario consultarPeloEmail(String email) {
		EntityManager em = getEM();
		Usuario usuario;
		try {
			Query query = em.createNamedQuery("Usuario.consultarEmail");
			query.setParameter("email", email);
			usuario = (Usuario) query.getSingleResult();
			
			return usuario;
		} catch(NoResultException e) {
			return null;
		}
	}
	
	public void update(Usuario a, String nome, String email, String senha) {
		EntityManager em = getEM();
		
		em.getTransaction().begin();
		
		a = consultarPorId(Usuario.class, a.getId());
		
		if (nome != null && !nome.isEmpty()) 
	        a.setNome(nome);
        
        if (email != null && !email.isEmpty()) 
            a.setEmail(email);
        
        if (senha != null && !senha.isEmpty()) 
            a.setSenha(senha);
	
		em.merge(a);
		em.getTransaction().commit();
	}
	
	@Transactional
	public void insertWithQuery(Usuario usuario) throws Exception {
		EntityManager em = getEM();
		
		Query query = em.createNativeQuery("INSERT INTO Usuario (name, email, senha)"
										+ " VALUES (?, ?, ?)");
		em.getTransaction().begin();
		query.setParameter(1, usuario.getNome());
		query.setParameter(2, usuario.getEmail());
		query.setParameter(3, usuario.getSenha());
		
		query.executeUpdate();
		
		BigInteger generatedId = (BigInteger) em.createNativeQuery("SELECT LAST_INSERT_ID()").getSingleResult();
	    
	  int truncatedId = generatedId.intValue();
	  usuario.setId(truncatedId);	
	    
		em.getTransaction().commit();
	}
}
