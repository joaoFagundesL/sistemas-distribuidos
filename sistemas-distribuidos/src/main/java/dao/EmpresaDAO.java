package dao;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import modelo.Empresa;
import modelo.Pessoa;
import modelo.Usuario;

public class EmpresaDAO extends GenericoDAO<Empresa> {
	
	public void update(Empresa e, String nome, String email, String token, String senha, String branch, String descricao) {
		EntityManager em = getEM();
		
		em.getTransaction().begin();
		
		e = consultarPorId(Empresa.class, e.getId());
		
		e.setBranch(branch);
		e.setDescricao(descricao);
		e.getUsuario().setNome(nome);
    e.getUsuario().setToken(token);
		e.getUsuario().setEmail(email);
		e.getUsuario().setSenha(senha);
		
	    if (e.getUsuario() instanceof Pessoa) {
	        Pessoa pessoa = (Pessoa) e.getUsuario(); 
	        if (pessoa instanceof Usuario) {
	            Usuario user = (Usuario) pessoa;
	            user.setSenha(senha);
	        } else {
	            throw new IllegalArgumentException("object nao é User");
	        }
	    } else {
	        throw new IllegalArgumentException("object nao é Pessoa");
	    }
		
		em.merge(e);
		em.getTransaction().commit();
	}
	
	
	@Transactional
	public void insertWithQuery(Empresa e) throws Exception {
		EntityManager em = getEM();
		
		Query query = em.createNativeQuery("INSERT INTO Candidato (branch, descricao, usuario_id)"
										+ " VALUES (?, ?, ?)");
		em.getTransaction().begin();
		query.setParameter(1, e.getBranch());
		query.setParameter(2, e.getDescricao());
		query.setParameter(3, e.getUsuario().getId());
		
		query.executeUpdate();
		em.getTransaction().commit();
	}
}
