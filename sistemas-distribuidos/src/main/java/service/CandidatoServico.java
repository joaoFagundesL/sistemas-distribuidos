package service;

import org.json.JSONObject;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import controller.CandidatoController;
import controller.UsuarioController;
import dao.CandidatoDAO;
import dao.UsuarioDAO;
import modelo.Candidato;
import modelo.Usuario;
import utitlity.EmailValidator;
import utitlity.JwtUtility;

public class CandidatoServico {
  public void lookup_candidate(JSONObject jsonMessage, JSONObject jsonResponse) {
    JwtUtility jwt = new JwtUtility();
    String token = jsonMessage.getString("token");

    try {
      DecodedJWT decodedJWT = jwt.verifyToken(token);
      Claim idClaim = decodedJWT.getClaim("id");
      String userIdAsString = idClaim.asString();
      Integer id = Integer.parseInt(userIdAsString);
      CandidatoController cController = new CandidatoController();
      Candidato c = cController.consultarPorId(id);

      String nome = c.getUsuario().getNome();
      String email = c.getUsuario().getEmail();
      String senha = c.getUsuario().getSenha();

      buildLookupCandidate(jsonResponse, "SUCCESS", token, nome, email, senha);    
    } catch(JWTVerificationException e) {
      buildLookupCandidate(jsonResponse, "SUCCESS", token, "", "", "");    
    }
  }

  public JSONObject buildLookupCandidate(JSONObject jsonResponse, String status, String token,
    String nome, String email, String senha) {
    jsonResponse.put("operation", "LOOKUP_ACCOUNT_CANDIDATE");
    jsonResponse.put("token", token);
    jsonResponse.put("status", status);
    JSONObject data = new JSONObject();
    data.put("email", email);
    data.put("password", senha);
    data.put("name", nome);
    jsonResponse.put("data", data);
    return jsonResponse;
  }

  public void updateCandidato(JSONObject jsonMessage, JSONObject jsonResponse) {
    JwtUtility jwt = new JwtUtility();
    String token = jsonMessage.getString("token");

    JSONObject data = jsonMessage.getJSONObject("data");

    // verifica se algum dos campos é vazio
    if (data.has("email") && data.getString("email").isEmpty() ||
        data.has("name") && data.getString("name").isEmpty() ||
        data.has("password") && data.getString("password").isEmpty() ||
        data.isEmpty()) {
      buildUpdateJsonCandidato(jsonResponse, "INVALID_FIELD", token, data);
      return;
    }   

    try {
      jwt.verifyToken(token);

      UsuarioController ucontroller = new UsuarioController();

      DecodedJWT decodedJWT = jwt.verifyToken(token);
      Claim idClaim = decodedJWT.getClaim("id");
      String userIdAsString = idClaim.asString();
      Integer id = Integer.parseInt(userIdAsString);
      CandidatoController cController = new CandidatoController();
      Candidato c = cController.consultarPorId(id);

      String email = "";  
      String senha = "";
      String nome = "";       

      if (data.has("email")) {
        email = data.getString("email");
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

      buildUpdateJsonCandidato(jsonResponse, "SUCCESS", token, data);
    } catch(JWTVerificationException e) {
      buildLogoutJsonCandidato(jsonResponse, "INVALID_TOKEN", token);
    }
  }

  public JSONObject buildUpdateJsonCandidato(JSONObject res, String status, String token, JSONObject data) {
    res.put("operation", "UPDATE_ACCOUNT_CANDIDATE");
    res.put("status", status);
    res.put("token", token);
    res.put("data", data);
    return res;
  }


  public void logoutCandidato(JSONObject jsonMessage, JSONObject jsonResponse) {
    JwtUtility jwt = new JwtUtility();
    String token = jsonMessage.getString("token");

    try {
      jwt.verifyToken(token);
      buildLogoutJsonCandidato(jsonResponse, "SUCCESS", token);
    } catch(JWTVerificationException e) {
      buildLogoutJsonCandidato(jsonResponse, "INVALID_TOKEN", token);
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
      buildJsonSignupCandidate(jsonResponse, "INVALID_FIELD", email, senha, nome);
      return; 
    }

    if (!emailValidator.isValidEmail(email)) {
      buildJsonSignupCandidate(jsonResponse, "INVALID_EMAIL", email, senha, nome);
      return;
    } 

    if (dao.consultarPeloEmail(email) != null) {
      buildJsonSignupCandidate(jsonResponse, "USER_EXISTS", email, senha, nome);
      return;
    } 

    UsuarioController ucontroller = new UsuarioController();
    Usuario u = ucontroller.insert(nome, email, senha);
    CandidatoController ccontroller = new CandidatoController();
    ccontroller.insert(u);
    buildJsonSignupCandidate(jsonResponse, "SUCCESS", email, senha, nome);
  }

  public void loginCandidato(JSONObject jsonMessage, JSONObject jsonResponse) {
    CandidatoController fController = new CandidatoController();
    JSONObject data = jsonMessage.getJSONObject("data");

    String email = data.getString("email");
    String senha = data.getString("password");

    if (email.isEmpty() || senha.isEmpty()) {
      buildJsonLoginCandidato(jsonResponse, "INVALID_FIELD", "", email, senha);
      return;
    }

    if (!fController.isUserValid(email)) {
      buildJsonLoginCandidato(jsonResponse, "USER_NOT_FOUND", "", email, senha);
      return;
    }

    if (!fController.isPasswordValid(email, senha)) {
      buildJsonLoginCandidato(jsonResponse, "INVALID_PASSWORD", "", email, senha);
      return;
    } 

    Integer id = fController.consultarId(email);
    String idString = String.valueOf(id);
    String token = JwtUtility.generateToken(idString, "candidate");
    buildJsonLoginCandidato(jsonResponse, "SUCCESS", token, email, senha);

    UsuarioController uController = new UsuarioController();
    uController.inserirToken(id, token);
  }

  public JSONObject buildJsonLoginCandidato(JSONObject res, String status, String token, String email, String senha) {
    res.put("operation", "LOGIN_CANDIDATE");
    res.put("status", status);
    res.put("token", token);
    JSONObject data = new JSONObject();
    data.put("email", email);
    data.put("senha", senha);
    res.put("data", data);
    return res;
  }

  public JSONObject buildJsonSignupCandidate(JSONObject res, String status, String email, String senha, String nome) {
    res.put("operation", "SIGNUP_CANDIDATE");
    res.put("status", status);
    JSONObject data = new JSONObject();
    data.put("", "");
    res.put("data", data);
    return res;
  }

  private JSONObject buildLogoutJsonCandidato(JSONObject json, String status, String token) {
    json.put("operation", "LOGOUT_CANDIDATE");
    json.put("status", status);
    JSONObject data = new JSONObject();
    json.put("token", token);
    json.put("data", data);
    return json;
  }

}
