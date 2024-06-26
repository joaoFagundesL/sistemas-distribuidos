package modelo;

import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;


@Entity
@NamedQueries({

  @NamedQuery(name = "Candidato.consultarTodos",
    query = "SELECT c FROM Candidato c"),

  @NamedQuery(name = "Candidato.consultarEmail",
    query = "SELECT c FROM Candidato c WHERE c.usuario.email = :email"),

  @NamedQuery(name = "Candidato.consultarTodosNome",
    query = "SELECT c FROM Candidato c WHERE c.usuario.name = :name"),

  @NamedQuery(name = "Candidato.consultarPeloUsuarioIdEmail",
    query = "SELECT c FROM Candidato c WHERE c.usuario.id = (SELECT u.id FROM Usuario u WHERE u.email = :email)"),

  @NamedQuery(
    name = "Candidato.findByIdWithUser",
    query = "SELECT c FROM Candidato c JOIN FETCH c.usuario WHERE c.id = :id"
  )
})
public class Candidato implements Entidade {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @OneToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "usuario_id", referencedColumnName = "id")
  private Usuario usuario;

  @Cascade(org.hibernate.annotations.CascadeType.DELETE)
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "candidato")
  private List<CandidatoCompetencia> competencias;
  
  @Cascade(org.hibernate.annotations.CascadeType.DELETE)
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "candidato")
  private List<CandidatoEmpresa> empresas;
  
  @Cascade(org.hibernate.annotations.CascadeType.DELETE)
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "candidato")
  private List<VagaCandidato> vagas;


  public List<VagaCandidato> getVagas() {
	return vagas;
}

public void setVagas(List<VagaCandidato> vagas) {
	this.vagas = vagas;
}

/* Construtores */
  public Candidato() {
  }

  /* Getters e Setters */
  @Override
  public Integer getId() {
    return id;
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public List<CandidatoCompetencia> getCompetencias() {
    return competencias;
  }

  public void setCompetencias(List<CandidatoCompetencia> competencias) {
    this.competencias = competencias;
  }

  /* Equals e HashCode */
  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

}

