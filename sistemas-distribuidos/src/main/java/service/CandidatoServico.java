package service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import controller.CandidatoController;
import controller.CompetenciaController;
import controller.UsuarioController;
import controller.VagaController;
import dao.CandidatoDAO;
import dao.UsuarioDAO;
import modelo.Candidato;
import modelo.CandidatoCompetencia;
import modelo.Competencia;
import modelo.Usuario;
import modelo.Vaga;
import utitlity.EmailValidator;
import utitlity.JwtUtility;

public class CandidatoServico {
  public void lookup_candidate(JSONObject jsonMessage, JSONObject jsonResponse) {
    JwtUtility jwt = new JwtUtility();
    // JSONObject dataJson = jsonMessage.getJSONObject("data");
    String token = jsonMessage.getString("token");

    try {
      DecodedJWT decodedJWT = jwt.verifyToken(token);
      Claim idClaim = decodedJWT.getClaim("id");
      String userIdAsString = idClaim.asString();
      Integer id = Integer.parseInt(userIdAsString);
      CandidatoController cController = new CandidatoController();
      Candidato c = cController.consultarPorId(id);

      if (c == null) {
        buildLookupCandidate(jsonResponse, "USER_NOT_FOUND", token, "", "", "");    
        return;
      }

      String nome = c.getUsuario().getNome();
      String email = c.getUsuario().getEmail();
      String senha = c.getUsuario().getSenha();
      buildLookupCandidate(jsonResponse, "SUCCESS", token, nome, email, senha);    

    } catch(JWTVerificationException e) {
      buildInvalidToken(jsonResponse, "LOOKUP_ACCOUNT_CANDIDATE");
    }
  }

  public JSONObject buildLookupCandidate(JSONObject jsonResponse, String status, String token, String nome, String email, String senha) { 
    jsonResponse.put("operation", "LOOKUP_ACCOUNT_CANDIDATE"); 
    jsonResponse.put("status", status);
    JSONObject data = new JSONObject();
    data.put("email", email);
    data.put("password", senha);
    data.put("name", nome);
    data.put("token", token);
    jsonResponse.put("data", data);
    return jsonResponse;
  }

  public void updateCandidato(JSONObject jsonMessage, JSONObject jsonResponse) {
    JwtUtility jwt = new JwtUtility();

    JSONObject data = jsonMessage.getJSONObject("data");
    String token = jsonMessage.getString("token");

    // verifica se algum dos campos é vazio
    if (data.has("email") && data.getString("email").isEmpty() ||
    data.has("name") && data.getString("name").isEmpty() ||
    data.has("password") && data.getString("password").isEmpty()) {
      buildUpdateJsonCandidato(jsonResponse, "INVALID_FIELD");
      return;
    }   

    if (!data.has("email") && !data.has("password") && !data.has("name")) {
      buildUpdateJsonCandidato(jsonResponse, "INVALID_FIELD");
      return;
    }

    try {
      jwt.verifyToken(token);

      UsuarioController ucontroller = new UsuarioController();
      UsuarioDAO dao = new UsuarioDAO();

      DecodedJWT decodedJWT = jwt.verifyToken(token);
      Claim idClaim = decodedJWT.getClaim("id");
      String userIdAsString = idClaim.asString();
      Integer id = Integer.parseInt(userIdAsString);
      CandidatoController cController = new CandidatoController();
      Candidato c = cController.consultarPorId(id);

      if (c == null) {
        buildUpdateJsonCandidato(jsonResponse, "USER_NOT_FOUND");
        return;
      }

      String email = "";  
      String senha = "";
      String nome = "";       

      if (data.has("email")) {
        email = data.getString("email");

        Usuario existingCandidatoWithEmail = dao.consultarPeloEmail(email);

        if (existingCandidatoWithEmail != null && !existingCandidatoWithEmail.getId().equals(c.getUsuario().getId())) {
          buildUpdateJsonCandidato(jsonResponse, "INVALID_EMAIL");
          return;
        }
      }

      if (data.has("password")) {
        senha = data.getString("password");;
      }

      if (data.has("name")) {
        nome = data.getString("name");
      }

      CandidatoDAO cdao = new CandidatoDAO();
      cdao.update(c, nome, email, senha);
      ucontroller.update(c, nome, email, senha);

      buildUpdateJsonCandidato(jsonResponse, "SUCCESS");
    } catch(JWTVerificationException e) {
      buildInvalidToken(jsonResponse, "UPDATE_ACCOUNT_CANDIDATE");
    }
  }

  public JSONObject buildUpdateJsonCandidato(JSONObject res, String status) {
    res.put("operation", "UPDATE_ACCOUNT_CANDIDATE");
    res.put("status", status);
    JSONObject data = new JSONObject();
    res.put("data", data);
    return res;
  }

  public JSONObject buildInvalidToken(JSONObject res, String operation) {
    res.put("operation", operation);
    res.put("status", "INVALID_TOKEN");
    JSONObject data = new JSONObject();
    res.put("data", data);
    return res;
  }

  public void logoutCandidato(JSONObject jsonMessage, JSONObject jsonResponse) {
    JwtUtility jwt = new JwtUtility();
    String token = jsonMessage.getString("token");

    try {
      jwt.verifyToken(token);
      buildLogoutJsonCandidato(jsonResponse, "SUCCESS");
    } catch(JWTVerificationException e) {
      buildInvalidToken(jsonResponse, "LOGOUT_CANDIDATE");
    }
  }

  public void signupCandidato(JSONObject jsonMessage, JSONObject jsonResponse) {
    EmailValidator emailValidator = new EmailValidator();
    UsuarioDAO dao = new UsuarioDAO();
    JSONObject data = jsonMessage.getJSONObject("data");

    String email = data.getString("email");
    String senha = data.getString("password");
    String nome = data.getString("name");

    if (email.isEmpty() || senha.isEmpty() || nome.isEmpty()) {
      buildJsonSignupCandidate(jsonResponse, "INVALID_FIELD");
      return; 
    }

    if (!emailValidator.isValidEmail(email)) {
      buildJsonSignupCandidate(jsonResponse, "INVALID_EMAIL");
      return;
    } 

    if (dao.consultarPeloEmail(email) != null) {
      buildJsonSignupCandidate(jsonResponse, "USER_EXISTS");
      return;
    } 

    UsuarioController ucontroller = new UsuarioController();
    Usuario u = ucontroller.insert(nome, email, senha);
    CandidatoController ccontroller = new CandidatoController();
    ccontroller.insert(u);
    buildJsonSignupCandidate(jsonResponse, "SUCCESS");
  }

  public void loginCandidato(JSONObject jsonMessage, JSONObject jsonResponse) {
    CandidatoController fController = new CandidatoController();
    JSONObject data = jsonMessage.getJSONObject("data");

    String email = data.getString("email");
    String senha = data.getString("password");

    if (email.isEmpty() || senha.isEmpty()) {
      buildJsonLoginCandidato(jsonResponse, "INVALID_FIELD", "");
      return;
    }

    if (!fController.isUserValid(email)) {
      // buildJsonLoginCandidato(jsonResponse, "USER_NOT_FOUND", "", email, senha);
      buildJsonLoginCandidato(jsonResponse, "INVALID_LOGIN", "");
      return;
    }

    if (!fController.isPasswordValid(email, senha)) {
      // buildJsonLoginCandidato(jsonResponse, "INVALID_PASSWORD", "", email, senha);
      buildJsonLoginCandidato(jsonResponse, "INVALID_LOGIN", "");
      return;
    } 

    Integer id = fController.consultarId(email);
    String idString = String.valueOf(id);
    String token = JwtUtility.generateToken(idString, "candidate");
    buildJsonLoginCandidato(jsonResponse, "SUCCESS", token);
  }

  public void deleteAccount(JSONObject jsonMessage, JSONObject jsonResponse) {
    JwtUtility jwt = new JwtUtility();
    // JSONObject data = jsonMessage.getJSONObject("data");
    String token = jsonMessage.getString("token");

    try {
      DecodedJWT decodedJWT = jwt.verifyToken(token);
      Claim idClaim = decodedJWT.getClaim("id");
      String userIdAsString = idClaim.asString();
      Integer id = Integer.parseInt(userIdAsString);
      CandidatoController cController = new CandidatoController();
      Candidato c = cController.consultarPorId(id);

      if (c == null) {
        buildJsonDeleteCandidate(jsonResponse, "USER_NOT_FOUND");
        return;
      }

      UsuarioController ucontroller = new UsuarioController();

      cController.remover(Candidato.class, c.getId());
      ucontroller.remover(Usuario.class, c.getUsuario().getId());

      buildJsonDeleteCandidate(jsonResponse, "SUCCESS");


    } catch(JWTVerificationException e) {
      buildInvalidToken(jsonResponse, "DELETE_ACCOUNT_CANDIDATE");
    }

  }

  public void searchJob(JSONObject jsonMessage, JSONObject jsonResponse) {
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

      if (c == null) {
        buildJsonDeleteCandidate(jsonResponse, "USER_NOT_FOUND");
        return;
      }

      List<Vaga> vagas;

      VagaController vagaController = new VagaController();

      String filter = "";

      if (data.has("filter")) {
        filter = data.getString("filter");
      }

      if (data.has("skill")) {
        JSONArray skillArray = (JSONArray) data.get("skill");

        List<String> skillList = new ArrayList<>();
        for (Object skill : skillArray) {
          skillList.add((String) skill);
        }

        CompetenciaController compController = new CompetenciaController();
        List<Integer> skillsId = new ArrayList<>();

        for (int i = 0; i < skillList.size(); i++) {
          Competencia comp = compController.listarCompetenciaNome(skillList.get(i));
          if (comp == null) {
            buildJson(jsonResponse, "SEARCH_JOB", "SKILL_NOT_FOUND");
          }
          Integer skillId = comp.getId();

          skillsId.add(skillId);
        }

        if (data.has("experience")) {
          String experienceString = data.getString("experience");
          Integer experience = Integer.parseInt(experienceString);

          vagas = vagaController.getBySkillAndExperience(skillList, experience, filter);        	  
          buildJsonSearch(jsonResponse, vagas);
        } else {
          vagas = vagaController.getVagasBySkills(skillList);
          buildJsonSearch(jsonResponse, vagas);
        }

      } else {
        String experienceString = data.getString("experience");
        Integer experience = Integer.parseInt(experienceString);

        vagas = vagaController.getByExperience(experience);
        buildJsonSearch(jsonResponse, vagas);

      }

    } catch(JWTVerificationException e) {
      buildInvalidToken(jsonResponse, "SEARCH_JOB");
    }
  }

  public JSONObject buildJsonLoginCandidato(JSONObject res, String status, String token) {
    res.put("operation", "LOGIN_CANDIDATE");
    res.put("status", status);
    JSONObject data = new JSONObject();
    data.put("token", token);
    res.put("data", data);
    return res;
  }

  public  JSONObject buildJsonSearch(JSONObject res, List<Vaga> vagas) {
    res.put("operation", "SEARCH_JOB");
    res.put("status", "SUCCESS");

    JSONArray jobset = new JSONArray();
    Integer size = 0;

    for (Vaga v : vagas) {
      if (v.getSearchable().equals("YES")) {
        size++;
        JSONObject job = new JSONObject();
        job.put("skill", v.getSkill().getSkill());
        job.put("experience", v.getExperience().toString());
        job.put("id", v.getId().toString()); 
        job.put("available", v.getAvailable());
        jobset.put(job);
      }
    }

    JSONObject data = new JSONObject();
    data.put("jobset_size", size.toString());
    data.put("jobset", jobset);

    res.put("data", data);
    return res;
  }

  public JSONObject buildJsonDeleteCandidate(JSONObject res, String status) {
    res.put("operation", "DELETE_ACCOUNT_CANDIDATE");
    res.put("status", status);
    JSONObject data = new JSONObject();
    res.put("data", data);
    return res;
  }

  public JSONObject buildJsonSignupCandidate(JSONObject res, String status) {
    res.put("operation", "SIGNUP_CANDIDATE");
    res.put("status", status);
    JSONObject data = new JSONObject();
    res.put("data", data);
    return res;
  }

  private JSONObject buildLogoutJsonCandidato(JSONObject json, String status) {
    json.put("operation", "LOGOUT_CANDIDATE");
    json.put("status", status);
    JSONObject data = new JSONObject();
    json.put("data", data);
    return json;
  }

  private JSONObject buildJson(JSONObject json, String operation, String status) {
    json.put("operation", operation);
    json.put("status", status);
    JSONObject data = new JSONObject();
    json.put("data", data);
    return json;
  }

}
