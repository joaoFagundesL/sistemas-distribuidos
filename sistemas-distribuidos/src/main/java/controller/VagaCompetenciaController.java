package controller;

import dao.VagaCompetenciaDAO;
import modelo.Competencia;
import modelo.Vaga;
import modelo.VagaCompetencia;

public class VagaCompetenciaController {
	VagaCompetenciaDAO cdao = new VagaCompetenciaDAO();
	
	public VagaCompetencia insert(Competencia competencia, Vaga vaga, Integer experience) {
	    VagaCompetencia vc = new VagaCompetencia();

	    vc.setCompetencia(competencia);
	    vc.setExperience(experience);
	    vc.setVaga(vaga);

	    try {
	      cdao.inserirVagaCompetencia(competencia.getId(), vaga.getId(), experience);
	      return vc;
	    } catch (Exception e1) {
	      e1.printStackTrace();
	      return null;
	    }
	  }
}
