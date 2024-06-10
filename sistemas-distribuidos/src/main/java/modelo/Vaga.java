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

@NamedQueries({
  @NamedQuery(name = "Vaga.consultarTodos",
    query = "SELECT v FROM Vaga v"),
})


@Entity
public class Vaga implements Entidade {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "competencia_id")
  private Competencia competencia;

  private Integer experience;

  @Cascade(org.hibernate.annotations.CascadeType.DELETE)
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "vaga")
  private List<VagaCandidato> vagaCandidato;

  public List<VagaCandidato> getVagaCandidato() {
    return vagaCandidato;
  }

  public Competencia getSkill() {
    return competencia;
  }

  public Integer getExperience() {
    return experience;
  }

  public void setExperience(Integer experience) {
    this.experience = experience;
  }

  public void setSkill(Competencia competencia) {
    this.competencia = competencia;
  }

  public void setVagaCandidato(List<VagaCandidato> vagaCandidato) {
    this.vagaCandidato = vagaCandidato;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }
}
