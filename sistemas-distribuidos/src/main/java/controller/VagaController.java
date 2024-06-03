package controller;

import dao.VagaDAO;
import modelo.Candidato;
import modelo.Vaga;

public class VagaController {
  VagaDAO dao = new VagaDAO();

  public Vaga insert(Vaga vaga, String skill, String experience) {
    Vaga v = new Vaga();

    try {
      dao.insertWithQuery(vaga);
      return v;
    } catch (Exception e1) {
      e1.printStackTrace();
      return null;
    }
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
