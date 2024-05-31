package service;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import controller.CandidatoController;
import controller.CompetenciaController;
import modelo.Candidato;
import modelo.Competencia;
import utitlity.JwtUtility;

public class CompetenciaServico {
  public void includeSkill(JSONObject jsonMessage, JSONObject jsonResponse) {
    JSONObject data = jsonMessage.getJSONObject("data");

    String skill = data.getString("skill");
    Integer experience = data.getInt("experience");

    if (skill.isEmpty()) {
      buildJsonIncludeSkill(jsonResponse, "INVALID_FIELD", skill, experience);
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
      buildJsonIncludeSkill(jsonResponse, "SUCCESS",  skill, experience);
    } catch(JWTVerificationException e) {
      buildInvalidToken(jsonResponse, "INCLUDE_SKILL");
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
      buildInvalidToken(jsonResponse, "LOOKUP_SKILLSET");
    }

  }

  public JSONObject buildInvalidToken(JSONObject res, String operation) {
    res.put("operation", operation);
    res.put("status", "INVALID_TOKEN");
    JSONObject data = new JSONObject();
    res.put("data", data);
    return res;
  }

  public  JSONObject buildSkillset(JSONObject res, List<Competencia> competencias) {
    res.put("operation", "LOOKUP_SKILLSET");
    res.put("status", "SUCCESS");

    JSONArray skillset = new JSONArray();
    for (Competencia competencia : competencias) {
      JSONObject skill = new JSONObject();
      skill.put("skill", competencia.getSkill());
      skill.put("experience", competencia.getExperience());
      skill.put("id", competencia.getId());  // Assumindo que o objeto Competencia tem um método getId()
      skillset.put(skill);
    }

    JSONObject data = new JSONObject();
    data.put("skillset_size", competencias.size());
    data.put("skillset", skillset);

    res.put("data", data);
    return res;
  }

  public JSONObject buildJsonIncludeSkill(JSONObject res, String status, String skill, Integer experience) {
    res.put("operation", "INCLUDE_SKILL");
    res.put("status", status);
    JSONObject data = new JSONObject();
    res.put("data", data);
    return res;
  }


}
