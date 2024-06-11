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
    String experienceString = data.getString("experience");

    Integer experience = Integer.parseInt(experienceString);

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
  public void deleteJob(JSONObject jsonMessage, JSONObject jsonResponse) {
    JwtUtility jwt = new JwtUtility();
    JSONObject data = jsonMessage.getJSONObject("data");
    String token = jsonMessage.getString("token");

    try {
      DecodedJWT decodedJWT = jwt.verifyToken(token);
      Claim idClaim = decodedJWT.getClaim("id");
      String userIdAsString = idClaim.asString();
      Integer id = Integer.parseInt(userIdAsString);
      
      String jobIdString = data.getString("id");

      Integer jobId = Integer.parseInt(jobIdString);
      
      if (jobId == null || jobId < 0) {
          buildJson(jsonResponse, "JOB_NOT_FOUND", "DELETE_JOB");
          return;
      }

      Vaga vaga = vagaController.consultarPorId(jobId);

      if (vaga == null) {
        buildJson(jsonResponse, "JOB_NOT_FOUND", "DELETE_JOB");
      } else {
        vagaController.remover(Vaga.class, vaga.getId()); 
        buildJson(jsonResponse, "SUCCESS", "DELETE_JOB");
      }

    } catch(JWTVerificationException e) {
      buildJson(jsonResponse, "INVALID_TOKEN", "DELETE_JOB");
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

      String vagaIdString = dataJson.getString("id");
      Integer vagaId = Integer.parseInt(vagaIdString);

      Vaga vaga = vagaController.consultarPorId(vagaId);

      if (vaga == null) {
        buildJson(jsonResponse, "JOB_NOT_FOUND", "LOOKUP_JOB");
        return;
      }

      Competencia skill = vaga.getSkill();
      Integer experience = vaga.getExperience();

      buildLookupJob(jsonResponse, "SUCCESS", skill.getSkill(), experience.toString(), vagaId.toString());    

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
      skill.put("experience", vaga.getExperience().toString());
      skill.put("id", vaga.getId().toString()); 
      jobset.put(skill);
    }

    Integer size = vagas.size();

    JSONObject data = new JSONObject();
    data.put("jobset_size", size.toString());
    data.put("jobset", jobset);

    res.put("data", data);
    return res;
  }

  public void updateJob(JSONObject jsonMessage, JSONObject jsonResponse) {
    JwtUtility jwt = new JwtUtility();

    JSONObject data = jsonMessage.getJSONObject("data");
    String token = jsonMessage.getString("token");

    if (!data.has("skill") || data.getString("skill").isEmpty()) {
        buildJson(jsonResponse, "INVALID_FIELD", "UPDATE_JOB");
        return;
    }

    if (data.has("experience")) {
        String experienceString = data.getString("experience");
        Integer experience = Integer.parseInt(experienceString);

        if (experience < 0) {
            buildJson(jsonResponse, "INVALID_FIELD", "UPDATE_JOB");
            return;
        }
    } else {
        buildJson(jsonResponse, "INVALID_FIELD", "UPDATE_JOB");
        return;
    }

    try {
      jwt.verifyToken(token);

      DecodedJWT decodedJWT = jwt.verifyToken(token);
      Claim idClaim = decodedJWT.getClaim("id");
      String userIdAsString = idClaim.asString();
      Integer id = Integer.parseInt(userIdAsString);

      String skill = "";  
      String experienceString = "";
      Integer experience = null;
      
      Competencia originalComp = null;

      if (data.has("skill")) {
        skill = data.getString("skill");
        originalComp = competenciaController.listarCompetenciaNome(skill);
      
        if (!validator.competenciaIsValid(skill)) {
        	buildJson(jsonResponse, "SKILL_NOT_FOUND", "UPDATE_JOB");
          return;
        }
      }

      if (data.has("skill")) {
        skill = data.getString("skill");
      }

      if (data.has("experience")) {
        experienceString = data.getString("experience");
        experience = Integer.parseInt(experienceString);
      }

      String jobIdString = data.getString("id");
      Integer jobId = Integer.parseInt(jobIdString);
      if (jobId < 0) {
        	buildJson(jsonResponse, "JOB_NOT_FOUND", "UPDATE_JOB");
          return;
      }

      Vaga vaga = vagaController.consultarPorId(jobId);
      
      if (vaga == null) {
    	  buildJson(jsonResponse, "JOB_NOT_FOUND", "UPDATE_JOB");
        return;
      }

      vagaController.update(vaga, originalComp, experience);

      buildJson(jsonResponse, "SUCCESS", "UPDATE_JOB");
    } catch (JWTVerificationException e) {
      buildJson(jsonResponse, "INVALID_TOKEN", "UPDATE_JOB");
    }
  }

  public JSONObject buildLookupJob(JSONObject jsonResponse, String status, String skill, String experience, String id) { 
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
