package controller;

import java.util.List;

import dao.CandidatoCompetenciaDAO;
import modelo.Candidato;
import modelo.CandidatoCompetencia;
import modelo.Competencia;

public class CandidatoCompetenciaController {

  CandidatoCompetenciaDAO cdao = new CandidatoCompetenciaDAO();

  public List<CandidatoCompetencia> listarCompetenciaUsuario(Integer id) {
    return cdao.listarCompetenciaUsuario(id);
  }


   public void update(CandidatoCompetencia c, Integer experience, Competencia competencia) {
    cdao.update(c, experience, competencia);
  }

  public CandidatoCompetencia listarCompetenciaEspecifica(Integer idCandidato, Integer idCompetencia) {
    return cdao.listarCompetenciaEspecifica(idCandidato, idCompetencia);
  }

  public void remover(Class<CandidatoCompetencia> clazz, Integer id) {
    cdao.remover(CandidatoCompetencia.class, id);
  }

  public boolean inserirCandidatoCompetencia(Competencia competencia, Candidato candidato, Integer experience) {
    try {
      cdao.inserirCompetenciaCandidato(competencia.getId(), candidato.getId(), experience);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
}
