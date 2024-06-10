package utitlity;

import java.util.List;

import org.json.JSONObject;

import controller.CandidatoCompetenciaController;
import modelo.CandidatoCompetencia;

public class SkillValidator {
	CandidatoCompetenciaController ccController = new CandidatoCompetenciaController();

	public boolean competenciaExiste(JSONObject jsonResponse, Integer id, String skill) {
	    List<CandidatoCompetencia> competenciasGeneric = ccController.listarCompetenciaUsuario(id); 

	    int size = competenciasGeneric.size();

	    for (int i = 0; i < size; i++) {
	      if (skill.equals(competenciasGeneric.get(i).getCompetencia().getSkill())) {
	        return true;
	      }        
	    }
	    return false;
	  }
}
