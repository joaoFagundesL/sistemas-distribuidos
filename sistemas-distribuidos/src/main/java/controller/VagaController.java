package controller;

import java.util.List;

import dao.VagaDAO;
import modelo.Competencia;
import modelo.Vaga;

public class VagaController {
  VagaDAO dao = new VagaDAO();

  public Vaga insert(Competencia competencia, Integer experience) {
    Vaga v = new Vaga();
    v.setSkill(competencia);
    v.setExperience(experience);

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

  public Vaga consultarPorId(Integer id) {
    return dao.consultarPorId(Vaga.class, id);
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
