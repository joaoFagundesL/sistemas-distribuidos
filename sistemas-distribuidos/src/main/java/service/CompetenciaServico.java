package service;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import controller.CandidatoController;
import controller.CompetenciaController;
import dao.CompetenciaDAO;
import modelo.Candidato;
import modelo.Competencia;
import utitlity.JwtUtility;

public class CompetenciaServico {
  public void includeSkill(JSONObject jsonMessage, JSONObject jsonResponse) {
    JSONObject data = jsonMessage.getJSONObject("data");

    String skill = data.getString("skill");
    Integer experience = data.getInt("experience");

    if (skill.isEmpty()) {
      buildJson(jsonResponse, "INVALID_FIELD", "INCLUDE_SKILL");
      return; 
    }

    JwtUtility jwt = new JwtUtility();
    String token = jsonMessage.getString("token");

    CandidatoController candidatoController = new CandidatoController();
    CompetenciaController competenciaController = new CompetenciaController();

    try {
      DecodedJWT decodedJWT = jwt.verifyToken(token);
      Claim idClaim = decodedJWT.getClaim("id");
      String userIdAsString = idClaim.asString();
      Integer id = Integer.parseInt(userIdAsString);
      Candidato c = candidatoController.consultarPorId(id);

      Competencia comp = competenciaController.insert(c, skill, experience);
      buildJson(jsonResponse, "SUCCESS", "INCLUDE_SKILL");
    } catch(JWTVerificationException e) {
      buildJson(jsonResponse, "INVALID_TOKEN", "INCLUDE_SKILL");
    }
  }

  public void deleteSkill(JSONObject jsonMessage, JSONObject jsonResponse) {
    JwtUtility jwt = new JwtUtility();
    JSONObject data = jsonMessage.getJSONObject("data");
    String token = jsonMessage.getString("token");

    try {
      DecodedJWT decodedJWT = jwt.verifyToken(token);
      Claim idClaim = decodedJWT.getClaim("id");
      String userIdAsString = idClaim.asString();
      Integer id = Integer.parseInt(userIdAsString);
      CandidatoController cController = new CandidatoController();
      Candidato c = cController.consultarPorId(id);

      Integer idComp = data.getInt("id");

      CompetenciaController cc = new CompetenciaController();
      Competencia competencia = cc.listarCompetenciaEspecifica(id, idComp);

      if (competencia == null) {
        buildJson(jsonResponse, "SKILL_NOT_FOUND", "DELETE_SKILL");
      } else {
        cc.remover(Competencia.class, competencia.getId()); 
        buildJson(jsonResponse, "SUCCESS", "DELETE_SKILL");
      }

    } catch(JWTVerificationException e) {
      buildJson(jsonResponse, "INVALID_TOKEN", "DELETE_SKILL");
    }
  }

  public void lookupSkillset(JSONObject jsonMessage, JSONObject jsonResponse) {
    JwtUtility jwt = new JwtUtility();
    String token = jsonMessage.getString("token");

    CandidatoController candidatoController = new CandidatoController();
    CompetenciaController competenciaController = new CompetenciaController();

    try {
      DecodedJWT decodedJWT = jwt.verifyToken(token);
      Claim idClaim = decodedJWT.getClaim("id");
      String userIdAsString = idClaim.asString();
      Integer id = Integer.parseInt(userIdAsString);
      Candidato c = candidatoController.consultarPorId(id);

      List<Competencia> competencias = competenciaController.listarCompetenciaUsuario(c.getId());
      buildSkillset(jsonResponse, competencias);

    } catch(JWTVerificationException e) {
      buildJson(jsonResponse, "INVALID_TOKEN", "LOOKUP_SKILLSET");
    }
  }

  public void lookupSkill(JSONObject jsonMessage, JSONObject jsonResponse) {
    JwtUtility jwt = new JwtUtility();
    String token = jsonMessage.getString("token");

    JSONObject data = jsonMessage.getJSONObject("data");

    CandidatoController candidatoController = new CandidatoController();
    CompetenciaController competenciaController = new CompetenciaController();

    try {
      DecodedJWT decodedJWT = jwt.verifyToken(token);
      Claim idClaim = decodedJWT.getClaim("id");
      String userIdAsString = idClaim.asString();
      Integer id = Integer.parseInt(userIdAsString);
      Candidato c = candidatoController.consultarPorId(id);

      Integer idCompetencia = data.getInt("id");

      Competencia competencia = competenciaController.listarCompetenciaEspecifica(id, idCompetencia);

      if (competencia == null) {
        buildJson(jsonResponse, "SKILL_NOT_FOUND", "LOOKUP_SKILL");
      } else {
        buildSkill(jsonResponse, competencia);
      }
    } catch(JWTVerificationException e) {
      buildJson(jsonResponse, "INVALID_TOKEN", "LOOKUP_SKILLSET");
    }
  }

  public  JSONObject buildSkillset(JSONObject res, List<Competencia> competencias) {
    res.put("operation", "LOOKUP_SKILLSET");
    res.put("status", "SUCCESS");

    JSONArray skillset = new JSONArray();
    for (Competencia competencia : competencias) {
      JSONObject skill = new JSONObject();
      skill.put("skill", competencia.getSkill());
      skill.put("experience", competencia.getExperience());
      skill.put("id", competencia.getId()); 
      skillset.put(skill);
    }

    JSONObject data = new JSONObject();
    data.put("skillset_size", competencias.size());
    data.put("skillset", skillset);

    res.put("data", data);
    return res;
  }

  public  JSONObject buildSkill(JSONObject res, Competencia competencia) {
    res.put("operation", "LOOKUP_SKILLSET");
    res.put("status", "SUCCESS");

    JSONObject data = new JSONObject();

    data.put("skill", competencia.getSkill());
    data.put("experience", competencia.getExperience());
    data.put("id", competencia.getId()); 

    res.put("data", data);
    return res;
  }

  public JSONObject buildJson(JSONObject res, String status, String operation) {
    res.put("operation", operation);
    res.put("status", status);
    JSONObject data = new JSONObject();
    res.put("data", data);
    return res;
  }
}
