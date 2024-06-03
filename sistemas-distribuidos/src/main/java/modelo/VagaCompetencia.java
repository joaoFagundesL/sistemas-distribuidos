package modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class VagaCompetencia implements Entidade{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  
  @ManyToOne
  @JoinColumn(name = "vaga_id")
  private Vaga vaga;

  @ManyToOne
  @JoinColumn(name = "competencia_id")
  private Competencia competencia;
  
  private String experience;

	public String getExperience() {
	return experience;
}

public void setExperience(String experience) {
	this.experience = experience;
}

	@Override
	public Integer getId() {
		return id;
	}

	public Vaga getVaga() {
		return vaga;
	}

	public void setVaga(Vaga vaga) {
		this.vaga = vaga;
	}

	public Competencia getCompetencia() {
		return competencia;
	}

	public void setCompetencia(Competencia competencia) {
		this.competencia = competencia;
	}
	  
  
}
