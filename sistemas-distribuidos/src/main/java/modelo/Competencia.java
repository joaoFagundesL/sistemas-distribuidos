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
    name = "Competencia.listarCompetenciaUsuario",
    query = "SELECT c FROM Competencia c JOIN c.candidato ca WHERE ca.id = :id"
  ),

  @NamedQuery(
    name = "Competencia.listarCompetenciaEspecifica",
    query = "SELECT c FROM Competencia c JOIN c.candidato ca WHERE ca.id = :candidatoId AND c.id = :competenciaId"
  )
})

public class Competencia implements Entidade {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String skill;
  private Integer experience;

  @ManyToOne
  @JoinColumn(name = "candidato_id")
  private Candidato candidato;

  public Competencia() {
  }

  public String getSkill() {
    return skill;
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
  public Integer getExperience() {
    return experience;
  }
  public void setExperience(Integer experience) {
    this.experience = experience;
  }
}
