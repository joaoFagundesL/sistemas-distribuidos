package service;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import controller.CompetenciaController;
import controller.EmpresaController;
import controller.VagaCandidatoController;
import controller.VagaController;
import modelo.Competencia;
import modelo.Empresa;
import modelo.Vaga;
import utitlity.JwtUtility;
import utitlity.SkillValidator;

public class VagaServico {
  SkillValidator validator = new SkillValidator();
  VagaController vagaController = new VagaController();
  VagaCandidatoController vagaCandidatoController = new VagaCandidatoController();
  EmpresaController empresaController = new EmpresaController();
  CompetenciaController competenciaController = new CompetenciaController();

  public void includeJob(JSONObject jsonMessage, JSONObject jsonResponse) {
    JSONObject data = jsonMessage.getJSONObject("data");

    String skill = data.getString("skill");
    Integer experience = data.getInt("experience");

    if (skill.isEmpty() || experience < 0 || experience > 10) {
      buildJson(jsonResponse, "INVALID_FIELD", "INCLUDE_JOB");
      return; 
    }

    JwtUtility jwt = new JwtUtility();
    String token = jsonMessage.getString("token");

    try {
      DecodedJWT decodedJWT = jwt.verifyToken(token);
      Claim idClaim = decodedJWT.getClaim("id");
      String userIdAsString = idClaim.asString();
      Integer id = Integer.parseInt(userIdAsString);
      Empresa c = empresaController.consultarPorId(id);

      Competencia competencia = competenciaController.listarCompetenciaNome(skill);

      if (competencia == null) {
        buildJson(jsonResponse, "SKILL_NOT_FOUND", "INCLUDE_JOB");
        return;
      }

      Vaga vaga = vagaController.insert(competencia, experience);

      buildJson(jsonResponse, "SUCCESS", "INCLUDE_JOB");
    } catch(JWTVerificationException e) {
      buildJson(jsonResponse, "INVALID_TOKEN", "INCLUDE_JOB");
    }
  }

  public void lookupJob(JSONObject jsonMessage, JSONObject jsonResponse) {
    JwtUtility jwt = new JwtUtility();
    String token = jsonMessage.getString("token");

    JSONObject dataJson = jsonMessage.getJSONObject("data");

    try {
      DecodedJWT decodedJWT = jwt.verifyToken(token);
      Claim idClaim = decodedJWT.getClaim("id");
      String userIdAsString = idClaim.asString();
      Integer id = Integer.parseInt(userIdAsString);
      Empresa empresa = empresaController.consultarPorId(id);

      if (empresa == null) {
        buildLookupJob(jsonResponse, "USER_NOT_FOUND", "", null, null);    
        return;
      }

      Integer vagaId = dataJson.getInt("id");

      Vaga vaga = vagaController.consultarPorId(vagaId);

      if (vaga == null) {
        buildJson(jsonResponse, "JOB_NOT_FOUND", "LOOKUP_JOB");
        return;
      }

      Competencia skill = vaga.getSkill();
      Integer experience = vaga.getExperience();

      buildLookupJob(jsonResponse, "SUCCESS", skill.getSkill(), experience, vagaId);    

    } catch(JWTVerificationException e) {
      buildJson(jsonResponse, "INVALID_TOKEN", "LOOKUP_JOB");
    }
  }

  public void lookupJobset(JSONObject jsonMessage, JSONObject jsonResponse) {
    JwtUtility jwt = new JwtUtility();
    String token = jsonMessage.getString("token");

    try {
      DecodedJWT decodedJWT = jwt.verifyToken(token);
      Claim idClaim = decodedJWT.getClaim("id");
      String userIdAsString = idClaim.asString();
      Integer id = Integer.parseInt(userIdAsString);

      List<Vaga> vagas = vagaController.consultarTodos();

      buildJobset(jsonResponse, vagas);

    } catch(JWTVerificationException e) {
      buildJson(jsonResponse, "INVALID_TOKEN", "LOOKUP_SKILLSET");
    }
  }

 public  JSONObject buildJobset(JSONObject res, List<Vaga> vagas) {
    res.put("operation", "LOOKUP_SKILLSET");
    res.put("status", "SUCCESS");

    JSONArray jobset = new JSONArray();
    for (Vaga  vaga: vagas) {
      JSONObject skill = new JSONObject();
      skill.put("skill", vaga.getSkill().getSkill());
      skill.put("experience", vaga.getExperience());
      skill.put("id", vaga.getId()); 
      jobset.put(skill);
    }

    JSONObject data = new JSONObject();
    data.put("jobset_size", vagas.size());
    data.put("jobset", jobset);

    res.put("data", data);
    return res;
  }

  public JSONObject buildLookupJob(JSONObject jsonResponse, String status, String skill, Integer experience, Integer id) { 
    jsonResponse.put("operation", "LOOKUP_JOB"); 
    jsonResponse.put("status", status);
    JSONObject data = new JSONObject();
    data.put("skill", skill);
    data.put("experience", experience);
    data.put("id", id);
    jsonResponse.put("data", data);
    return jsonResponse;
  }


  public JSONObject buildJson(JSONObject res, String status, String operation) {
    res.put("operation", operation);
    res.put("status", status);
    JSONObject data = new JSONObject();
    res.put("data", data);
    return res;
  }
}