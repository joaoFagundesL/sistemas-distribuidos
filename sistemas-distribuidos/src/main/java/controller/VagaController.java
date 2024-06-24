package controller;

import java.util.List;

import dao.VagaDAO;
import modelo.Competencia;
import modelo.Empresa;
import modelo.Vaga;

public class VagaController {
  VagaDAO dao = new VagaDAO();

  public Vaga insert(Competencia competencia, Integer experience, Empresa empresa, String available,
		  			String searchable) {
    Vaga v = new Vaga();
    v.setSkill(competencia);
    v.setSearchable(searchable);
    v.setAvailable(available);
    v.setExperience(experience);
    v.setEmpresa(empresa);

    try {
      dao.insertWithQuery(v);
      return v;
    } catch (Exception e1) {
      e1.printStackTrace();
      return null;
    }
  }

  public List<Vaga> consultarTodos() {
    return dao.consultarTodos();
  }
  
  public void setAvailable(Vaga vaga, String available) {
	  dao.updateAvailable(vaga, available);
  }
  
  public void setSearchable(Vaga vaga, String searchable) {
	  dao.updateSearchable(vaga, searchable);
  }

  public List<Vaga> getVagasBySkills(List<String> skills) {
    return dao.findBySkills(skills);
  }

  public List<Vaga> getBySkillAndExperience(List<String> skills, Integer experience, String filter) {
    return dao.getBySkillsAndExperience(skills, experience, filter);
  }

  public List<Vaga> getByExperience (Integer experience) {
    return dao.getByExperience(experience);
  }


  public Vaga consultarPorId(Integer id) {
    return dao.consultarPorId(Vaga.class, id);
  }

  public void update(Vaga vaga, Competencia skill, Integer experience) {
    dao.update(vaga, skill, experience);
  }

  public void remover(Class<Vaga> clazz, Integer id) {
    dao.remover(Vaga.class, id);
  }
  //  public boolean inserirVagaCandidato(Vaga vaga, Candidato candidato) {
  //    try {
  //      dao.inserirVagaCandidato(vaga.getId(), candidato.getId());
  //      return true;
  //    } catch (Exception e) {
  //      e.printStackTrace();
  //      return false;
  //    }
  //  }

}
