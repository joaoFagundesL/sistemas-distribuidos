package modelo;


import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@NamedQueries({
	@NamedQuery(name = "Usuario.consultarUser",
            query = "SELECT u FROM Usuario u WHERE u.user = :user"),
	
	@NamedQuery(name = "Usuario.consultarEmail",
    	query = "SELECT u FROM Usuario u WHERE u.email = :email"),
})

@Entity
public class Usuario extends Pessoa implements Entidade {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String user;
    private String senha;
    private String email;

    public Usuario() {
    }

    public Usuario(Integer id, String user, String senha) {
        this.id = id;
        this.user = user;
        this.senha = senha;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
    
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

}