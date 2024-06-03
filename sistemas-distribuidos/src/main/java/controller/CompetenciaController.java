package controller;

import java.util.List;

import dao.CompetenciaDAO;
import modelo.Candidato;
import modelo.Competencia;

public class CompetenciaController {
  CompetenciaDAO cdao = new CompetenciaDAO();

  public Competencia insert(Candidato candidato, String skill, Integer experience) {
    Competencia competencia = new Competencia();

    competencia.setSkill(skill);

    try {
      cdao.insertWithQuery(competencia, candidato);
      return competencia;
    } catch (Exception e1) {
      e1.printStackTrace();
      return null;
    }
  }

   public void update(Competencia c, String skill) {
    cdao.update(c, skill);
  }

  public Competencia listarCompetenciaNome(String skill) {
    return cdao.listarCompetenciaNome(skill);
  }

  public void remover(Class<Competencia> clazz, Integer id) {
    cdao.remover(Competencia.class, id);
  }

  public Competencia consultarPorId(Integer id) {
    return cdao.consultarPorId(Competencia.class, id);
  }
}
