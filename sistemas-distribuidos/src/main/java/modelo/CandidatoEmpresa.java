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
	  @NamedQuery(name = "CandidatoEmpresa.consultarEmpresas",
	    query = "SELECT e FROM CandidatoEmpresa e WHERE e.candidato.id = :candidatoId"),
	})

public class CandidatoEmpresa implements Entidade{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	 @ManyToOne
	 @JoinColumn(name = "empresa_id")
	 private Empresa empresa;
	 
	 @ManyToOne
	 @JoinColumn(name = "candidato_id")
	  private Candidato candidato;

	public Candidato getCandidato() {
		return candidato;
	}

	public void setCandidato(Candidato candidato) {
		this.candidato = candidato;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public Integer getId() {
		return id;
	}
}
