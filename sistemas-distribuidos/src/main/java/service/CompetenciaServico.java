package service;

import org.json.JSONObject;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import controller.CandidatoController;
import controller.CompetenciaController;
import controller.EmpresaController;
import controller.UsuarioController;
import dao.UsuarioDAO;
import modelo.Candidato;
import modelo.Competencia;
import modelo.Usuario;
import utitlity.JwtUtility;

public class CompetenciaServico {
	  public void includeSkill(JSONObject jsonMessage, JSONObject jsonResponse) {
		    UsuarioDAO dao = new UsuarioDAO();
		    JSONObject data = jsonMessage.getJSONObject("data");

		    String skill = data.getString("skill");
		    Integer experience = data.getInt("experience");
		    
		    if (skill.isEmpty()) {
		      buildJsonIncludeSkill(jsonResponse, "INVALID_FIELD", skill, experience);
		      return; 
		    }

		    EmpresaController econtroller = new EmpresaController();
		    
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
	  
	  public JSONObject buildInvalidToken(JSONObject res, String operation) {
		    res.put("operation", operation);
		    res.put("status", "INVALID_TOKEN");
		    JSONObject data = new JSONObject();
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
