package modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@NamedQueries({
  @NamedQuery(name = "Usuario.consultarName",
    query = "SELECT u FROM Usuario u WHERE u.name = :name"),

  @NamedQuery(name = "Usuario.consultarEmail",
    query = "SELECT u FROM Usuario u WHERE u.email = :email"),
})

@Entity
public class Usuario extends Pessoa implements Entidade {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String senha;
  private String email;

  public Usuario() {
  }

  public Usuario(Integer id, String senha) {
    this.id = id;
    this.senha = senha;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
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
