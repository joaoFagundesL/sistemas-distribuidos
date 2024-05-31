package controller;

import java.util.List;

import dao.CompetenciaDAO;
import modelo.Candidato;
import modelo.Competencia;

public class CompetenciaController {
  CompetenciaDAO cdao = new CompetenciaDAO();

  public Competencia insert(Candidato candidato, String skill, Integer experience) {
    Competencia competencia = new Competencia();

    competencia.setExperience(experience);
    competencia.setSkill(skill);

    try {
      cdao.insertWithQuery(competencia, candidato);
      return competencia;
    } catch (Exception e1) {
      e1.printStackTrace();
      return null;
    }
  }

  public Competencia consultarPorId(Integer id) {
    return cdao.consultarPorId(Competencia.class, id);
  }

  public List<Competencia> listarCompetenciaUsuario(Integer id) {
    return cdao.listarCompetenciaUsuario(id);
  }

}
