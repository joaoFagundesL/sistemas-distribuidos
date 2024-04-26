package modelo;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

@Entity
@NamedQueries({

    @NamedQuery(name = "Candidato.consultarTodos",
            query = "SELECT c FROM Candidato c"),
 
	@NamedQuery(name = "Candidato.consultarEmail",
	    query = "SELECT c FROM Candidato c WHERE c.usuario.email = :email"),
	
	@NamedQuery(name = "Candidato.consultarTodosNome",
	    query = "SELECT c FROM Candidato c WHERE c.usuario.name = :name"),

  @NamedQuery(name = "Candidato.consultarPeloUsuarioId",
    query = "SELECT c FROM Candidato c WHERE c.usuario.id = (SELECT u.id FROM Usuario u WHERE u.email = :email)")

})
public class Candidato implements Entidade {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;

	/* Construtores */
    public Candidato() {
    }

    public Candidato(Integer id, Integer anoEntrada) {
        super();
        this.id = id;
    }

    /* Getters e Setters */
    @Override
    public Integer getId() {
        return id;
    }
    
    public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

    public void setId(Integer id) {
        this.id = id;
    }

    /* Equals e HashCode */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

