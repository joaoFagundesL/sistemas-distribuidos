package modelo;

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
  @NamedQuery(name = "Empresa.consultarTodos",
    query = "SELECT e FROM Empresa e"),

  @NamedQuery(name = "Empresa.consultarPeloUsuarioIdEmail",
    query = "SELECT e FROM Empresa e WHERE e.usuario.id = (SELECT u.id FROM Usuario u WHERE u.email = :email)")
})

public class Empresa implements Entidade{

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String industry;
  private String descricao;

  @OneToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "usuario_id", referencedColumnName = "id")
  private Usuario usuario;

  public Empresa() {
  }

  public String getIndustry() {
    return industry;
  }

  public void setIndustry(String industry) {
    this.industry = industry;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
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

  @Override
  public Integer getId() {
    return id;
  }
}
