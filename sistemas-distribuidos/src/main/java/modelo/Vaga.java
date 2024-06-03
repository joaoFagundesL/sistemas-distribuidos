package modelo;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
public class Vaga implements Entidade {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  
  @Cascade(org.hibernate.annotations.CascadeType.DELETE)
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "vaga")
  private List<VagaCompetencia> competencias;
  
  @Cascade(org.hibernate.annotations.CascadeType.DELETE)
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "vaga")
  private List<VagaCandidato> vagaCandidato;


public List<VagaCandidato> getVagaCandidato() {
	return vagaCandidato;
}

public void setVagaCandidato(List<VagaCandidato> vagaCandidato) {
	this.vagaCandidato = vagaCandidato;
}

public List<VagaCompetencia> getCompetencias() {
	return competencias;
}

public void setCompetencias(List<VagaCompetencia> competencias) {
	this.competencias = competencias;
}


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }
}
