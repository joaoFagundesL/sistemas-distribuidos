package modelo;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;

@Entity
@NamedQueries({
  @NamedQuery(
    name = "Competencia.listarCompetenciaNome",
    query = "SELECT c FROM Competencia c WHERE c.skill = :skill"
  ),
})

public class Competencia implements Entidade {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String skill;

  @Cascade(org.hibernate.annotations.CascadeType.DELETE)
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "competencia")
  private List<CandidatoCompetencia> candidatos;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "competencia")
  private List<Vaga> vagas;

  public List<Vaga> getVagas() {
    return vagas;
  }

  public void setVagas(List<Vaga> vagas) {
    this.vagas = vagas;
  }

  public Competencia() {
  }

  public String getSkill() {
    return skill;
  }
  public List<CandidatoCompetencia> getCandidatos() {
    return candidatos;
  }

  public void setCandidatos(List<CandidatoCompetencia> candidatos) {
    this.candidatos = candidatos;
  }

  public void setSkill(String skill) {
    this.skill = skill;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }
}
