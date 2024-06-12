package service;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import controller.CandidatoCompetenciaController;
import controller.CandidatoController;
import controller.CompetenciaController;
import modelo.Candidato;
import modelo.CandidatoCompetencia;
import modelo.Competencia;
import utitlity.JwtUtility;

public class CompetenciaServico {

  CandidatoController candidatoController = new CandidatoController();
  CandidatoCompetenciaController ccController = new CandidatoCompetenciaController();
  CompetenciaController competenciaController = new CompetenciaController();

  public void includeSkill(JSONObject jsonMessage, JSONObject jsonResponse) {
    JSONObject data = jsonMessage.getJSONObject("data");

    String skill = data.getString("skill");
    String experienceString = data.getString("experience");

    Integer experience = Integer.parseInt(experienceString);


    if (skill.isEmpty()) {
      buildJson(jsonResponse, "INVALID_FIELD", "INCLUDE_SKILL");
      return; 
    }

    JwtUtility jwt = new JwtUtility();
    String token = jsonMessage.getString("token");

    try {
      DecodedJWT decodedJWT = jwt.verifyToken(token);
      Claim idClaim = decodedJWT.getClaim("id");
      String userIdAsString = idClaim.asString();
      Integer id = Integer.parseInt(userIdAsString);
      Candidato c = candidatoController.consultarPorId(id);

      if (competenciaExiste(jsonResponse, id, skill)) {
        return;
      }

      Competencia competencia = competenciaController.listarCompetenciaNome(skill);
      
      if (competencia == null) {
    	  buildJson(jsonResponse, "SKILL_NOT_FOUND", "INCLUDE_SKILL");
    	  return;
      }

      ccController.inserirCandidatoCompetencia(competencia ,c, experience);
      buildJson(jsonResponse, "SUCCESS", "INCLUDE_SKILL");
    } catch(JWTVerificationException e) {
      buildJson(jsonResponse, "INVALID_TOKEN", "INCLUDE_SKILL");
    }
  }

  public boolean competenciaExiste(JSONObject jsonResponse, Integer id, String skill) {
    List<CandidatoCompetencia> competenciasGeneric = ccController.listarCompetenciaUsuario(id); 

    int size = competenciasGeneric.size();

    for (int i = 0; i < size; i++) {
      if (skill.equals(competenciasGeneric.get(i).getCompetencia().getSkill())) {
        buildJson(jsonResponse, "SKILL_EXISTS", "INCLUDE_SKILL");
        return true;
      }        
    }
    return false;
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
      
      String skill = data.getString("skill");
      
      Competencia comp = competenciaController.listarCompetenciaNome(skill);
      
      // a skill nao existe
      if (comp == null) {
          buildJson(jsonResponse, "SKILL_NOT_FOUND", "DELETE_SKILL");
          return;
      }
      
      // a skill existe, mas agora verificar e o usuario possue ela, pois nao vou deletar
      // uma skill que ele nao possue
      CandidatoCompetencia cc = ccController.listarCompetenciaEspecifica(id, comp.getId());

      if (cc == null) {
        buildJson(jsonResponse, "SKILL_NOT_FOUND", "DELETE_SKILL");
      } else {
        ccController.remover(CandidatoCompetencia.class, cc.getId()); 
        buildJson(jsonResponse, "SUCCESS", "DELETE_SKILL");
      }

    } catch(JWTVerificationException e) {
      buildJson(jsonResponse, "INVALID_TOKEN", "DELETE_SKILL");
    }
  }

  public void lookupSkillset(JSONObject jsonMessage, JSONObject jsonResponse) {
    JwtUtility jwt = new JwtUtility();
    String token = jsonMessage.getString("token");

    try {
      DecodedJWT decodedJWT = jwt.verifyToken(token);
      Claim idClaim = decodedJWT.getClaim("id");
      String userIdAsString = idClaim.asString();
      Integer id = Integer.parseInt(userIdAsString);
      Candidato c = candidatoController.consultarPorId(id);

      List<CandidatoCompetencia> competenciasGeneric = ccController.listarCompetenciaUsuario(c.getId());

      buildSkillset(jsonResponse, competenciasGeneric);

    } catch(JWTVerificationException e) {
      buildJson(jsonResponse, "INVALID_TOKEN", "LOOKUP_SKILLSET");
    }
  }

  public void updateSkill(JSONObject jsonMessage, JSONObject jsonResponse) {
    JwtUtility jwt = new JwtUtility();

    JSONObject data = jsonMessage.getJSONObject("data");
    String token = jsonMessage.getString("token");

    if (!data.has("skill") || data.getString("skill").isEmpty()) {
        buildJson(jsonResponse, "INVALID_FIELD", "UPDATE_SKILL");
        return;
    }
    
    if (!data.has("newSkill") || data.getString("newSkill").isEmpty()) {
        buildJson(jsonResponse, "INVALID_FIELD", "UPDATE_SKILL");
        return;
    }

    if (data.has("experience")) {
        String experienceString = data.getString("experience");
        Integer experience = Integer.parseInt(experienceString);
        if (experience < 0) {
            buildJson(jsonResponse, "INVALID_FIELD", "UPDATE_SKILL");
            return;
        }
    } else {
        buildJson(jsonResponse, "INVALID_FIELD", "UPDATE_SKILL");
        return;
    }

    try {
      jwt.verifyToken(token);

      DecodedJWT decodedJWT = jwt.verifyToken(token);
      Claim idClaim = decodedJWT.getClaim("id");
      String userIdAsString = idClaim.asString();
      Integer id = Integer.parseInt(userIdAsString);

      String skill = "";  
      String newSkill = "";  
      String experienceString = "";
      Integer experience = null;
      
      Competencia originalComp = null;
      Competencia comp = null;

      if (data.has("skill")) {
        skill = data.getString("skill");
        originalComp = competenciaController.listarCompetenciaNome(skill);
      
        if (!competenciaIsValid(skill)) {
        	buildJson(jsonResponse, "SKILL_NOT_FOUND", "UPDATE_SKILL");
            return;
        }
      }
      
      if (data.has("newSkill")) {
          newSkill = data.getString("newSkill");
          comp = competenciaController.listarCompetenciaNome(newSkill);
          if (competenciaExiste(jsonResponse, id, newSkill) && !newSkill.equals(originalComp.getSkill())) {
            buildJson(jsonResponse, "SKILL_EXISTS", "UPDATE_SKILL");
            return;
          }
          
          if (!competenciaIsValid(newSkill)) {
          	buildJson(jsonResponse, "SKILL_NOT_FOUND", "UPDATE_SKILL");
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
      
      if (data.has("newSkill")) {
          newSkill = data.getString("newSkill");
        }

      /* para atualizar a experiencia primeiro eu pego o registro da tabela CandidatoCompetencia, pois
       * o atributo experience só esta presente nessa tabela */
      CandidatoCompetencia competenciaGeneric = ccController.listarCompetenciaEspecifica(id, originalComp.getId());
      
      if (competenciaGeneric == null) {
    	  buildJson(jsonResponse, "SKILL_NOT_FOUND", "UPDATE_SKILL");
          return;
      }

      ccController.update(competenciaGeneric, experience, comp);

      buildJson(jsonResponse, "SUCCESS", "UPDATE_SKILL");
    } catch (JWTVerificationException e) {
      buildJson(jsonResponse, "INVALID_TOKEN", "UPDATE_SKILL");
    }
  }

  public boolean competenciaIsValid(String skill) {
	  Competencia comp = competenciaController.listarCompetenciaNome(skill);
	  if (comp == null) {
		  return false;
	  }
	  
	  return true;
  }
  
  public void lookupSkill(JSONObject jsonMessage, JSONObject jsonResponse) {
    JwtUtility jwt = new JwtUtility();
    String token = jsonMessage.getString("token");

    JSONObject data = jsonMessage.getJSONObject("data");

    try {
      DecodedJWT decodedJWT = jwt.verifyToken(token);
      Claim idClaim = decodedJWT.getClaim("id");
      String userIdAsString = idClaim.asString();
      Integer id = Integer.parseInt(userIdAsString);
      Candidato c = candidatoController.consultarPorId(id);

      String skill = data.getString("skill");
      
      Competencia comp = competenciaController.listarCompetenciaNome(skill);
      
      if (!competenciaIsValid(skill)) {
    	  buildJson(jsonResponse, "SKILL_NOT_FOUND", "LOOKUP_SKILL");
    	  return;
      }

      CandidatoCompetencia competenciaGeneric = ccController.listarCompetenciaEspecifica(id, comp.getId());

      if (competenciaGeneric == null) {
        buildJson(jsonResponse, "SKILL_NOT_FOUND", "LOOKUP_SKILL");
      } else {
        buildSkill(jsonResponse, competenciaGeneric);
      }
    } catch(JWTVerificationException e) {
      buildJson(jsonResponse, "INVALID_TOKEN", "LOOKUP_SKILLSET");
    }
  }

  public  JSONObject buildSkillset(JSONObject res, List<CandidatoCompetencia> competencias) {
    res.put("operation", "LOOKUP_SKILLSET");
    res.put("status", "SUCCESS");

    JSONArray skillset = new JSONArray();
    for (CandidatoCompetencia competenciaGeneric : competencias) {
      JSONObject skill = new JSONObject();
      skill.put("skill", competenciaGeneric.getCompetencia().getSkill());
      skill.put("experience", competenciaGeneric.getExperience().toString());
      // skill.put("id", competenciaGeneric.getCompetencia().getId().toString()); 
      skillset.put(skill);
    }

    Integer size = competencias.size();

    JSONObject data = new JSONObject();
    data.put("skillset_size", size.toString());
    data.put("skillset", skillset);

    res.put("data", data);
    return res;
  }

  public  JSONObject buildSkill(JSONObject res, CandidatoCompetencia competenciaGeneric) {
    res.put("operation", "LOOKUP_SKILLSET");
    res.put("status", "SUCCESS");

    JSONObject data = new JSONObject();

    data.put("skill", competenciaGeneric.getCompetencia().getSkill());
    data.put("experience", competenciaGeneric.getExperience().toString());
    // data.put("id", competenciaGeneric.getCompetencia().getId().toString()); 

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
