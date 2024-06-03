package controller;

import dao.VagaCandidatoDAO;
import modelo.Candidato;
import modelo.Vaga;
import modelo.VagaCandidato;
import modelo.VagaCompetencia;

public class VagaCandidatoController {
	VagaCandidatoDAO cdao = new VagaCandidatoDAO();
	
	public VagaCandidato insert(Vaga vaga, Candidato candidato) {
	    VagaCandidato vc = new VagaCandidato();

	    vc.setCandidato(candidato);
	    vc.setVaga(vaga);

	    try {
	      cdao.inserirVagaCandidato(candidato.getId(), vaga.getId());
	      return vc;
	    } catch (Exception e1) {
	      e1.printStackTrace();
	      return null;
	    }
	  }
}
