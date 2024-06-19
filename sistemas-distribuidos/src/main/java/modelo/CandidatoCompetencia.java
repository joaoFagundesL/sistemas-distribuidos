package modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;


@Entity
@NamedQueries({
  @NamedQuery(
    name = "CandidatoCompetencia.listarCompetenciaUsuario",
    query = "SELECT c FROM CandidatoCompetencia c WHERE c.candidato.id = :id"
  ),

  @NamedQuery(
    name = "CandidatoCompetencia.listarCompetenciaEspecifica",
    query = "SELECT c FROM CandidatoCompetencia c WHERE c.candidato.id = :candidatoId AND c.competencia.id = :competenciaId"
  )
  
})

public class CandidatoCompetencia implements Entidade {
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Integer id;

  private Integer experience;
  @ManyToOne
  @JoinColumn(name = "competencia_id")
  private Competencia competencia;

  @ManyToOne
  @JoinColumn(name = "candidato_id")
  private Candidato candidato;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getExperience() {
    return experience;
  }

  public void setExperience(Integer experience) {
    this.experience = experience;
  }

  public Competencia getCompetencia() {
    return competencia;
  }

  public void setCompetencia(Competencia competencia) {
    this.competencia = competencia;
  }

  public Candidato getCandidato() {
    return candidato;
  }

  public void setCandidato(Candidato candidato) {
    this.candidato = candidato;
  }

}
