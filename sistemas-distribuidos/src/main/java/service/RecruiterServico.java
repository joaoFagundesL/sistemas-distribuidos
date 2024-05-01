package service;

import org.json.JSONObject;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import controller.EmpresaController;
import controller.UsuarioController;
import dao.UsuarioDAO;
import modelo.Empresa;
import modelo.Usuario;
import utitlity.EmailValidator;
import utitlity.JwtUtility;

public class RecruiterServico {
  public void logoutRecruiter(JSONObject jsonMessage, JSONObject jsonResponse) {
    JwtUtility jwt = new JwtUtility();
    JSONObject data = jsonMessage.getJSONObject("data");
    String token = data.getString("token");

    try {
      jwt.verifyToken(token);
      buildLogoutJsonRecruiter(jsonResponse, "SUCCESS", token);
    } catch(JWTVerificationException e) {
      buildLogoutJsonRecruiter(jsonResponse, "INVALID_TOKEN", token);
    }
  }

 public void lookup_recruiter(JSONObject jsonMessage, JSONObject jsonResponse) {
    JwtUtility jwt = new JwtUtility();
    JSONObject data = jsonMessage.getJSONObject("data");
    String token = data.getString("token");

    try {
      DecodedJWT decodedJWT = jwt.verifyToken(token);
      Claim idClaim = decodedJWT.getClaim("id");
      String userIdAsString = idClaim.asString();
      Integer id = Integer.parseInt(userIdAsString);
      EmpresaController cController = new EmpresaController();
      Empresa c = cController.consultarPorId(id);

      String nome = c.getUsuario().getNome();
      String email = c.getUsuario().getEmail();
      String senha = c.getUsuario().getSenha();
      String industry = c.getIndustry();
      String descricao = c.getDescricao();

      buildLookupRecruiter(jsonResponse, "SUCCESS", token, nome, email, senha, industry, descricao);    
    } catch(JWTVerificationException e) {
      buildLookupRecruiter(jsonResponse, "SUCCESS", token, "", "", "", "", "");    
    }
  }

  public JSONObject buildLookupRecruiter(JSONObject jsonResponse, String status, String token,
    String nome, String email, String senha, String industry, String descricao) {
    jsonResponse.put("operation", "LOOKUP_ACCOUNT_RECRUITER");
    JSONObject data = new JSONObject();
    data.put("token", token);
    data.put("status", status);
    data.put("email", email);
    data.put("password", senha);
    data.put("name", nome);
    data.put("industry", industry);
    data.put("description", descricao);
    jsonResponse.put("data", data);
    return jsonResponse;
  }

  public void loginRecruiter(JSONObject jsonMessage, JSONObject jsonResponse) {
    EmpresaController eController = new EmpresaController();
    JSONObject data = jsonMessage.getJSONObject("data");

    String email = data.getString("email");
    String senha = data.getString("password");

    if (email.isEmpty() || senha.isEmpty()) {
      buildJsonLoginRecruiter(jsonResponse, "INVALID_FIELD", "", email, senha);
      return;
    }

    // decidiram mudar para apenas invalid_login, caso mudem de novo a logica ja 
    // esta pronta, é so descomentar a linha
    if (!eController.isUserValid(email)) {
      // buildJsonLoginRecruiter(jsonResponse, "USER_NOT_FOUND", "", email, senha);
      buildJsonLoginRecruiter(jsonResponse, "INVALID_LOGIN", "", email, senha);
      return;
    }

    if (!eController.isPasswordValid(email, senha)) {
      // buildJsonLoginRecruiter(jsonResponse, "INVALID_PASSWORD", "", email, senha);
      buildJsonLoginRecruiter(jsonResponse, "INVALID_LOGIN", "", email, senha);
      return;
    } 

    Integer id = eController.consultarId(email);
    String idString = String.valueOf(id);
    String token = JwtUtility.generateToken(idString, "recruiter");
    buildJsonLoginRecruiter(jsonResponse, "SUCCESS", token, email, senha);
  }

  public void signupRecruiter(JSONObject jsonMessage, JSONObject jsonResponse) {
    EmailValidator emailValidator = new EmailValidator();
    UsuarioDAO dao = new UsuarioDAO();
    JSONObject data = jsonMessage.getJSONObject("data");

    String email = data.getString("email");
    String senha = data.getString("password");
    String nome = data.getString("name");
    String description = data.getString("industry");
    String descricao = data.getString("description");

    if (email.isEmpty() || senha.isEmpty() || nome.isEmpty() || description.isEmpty() || descricao.isEmpty()) {
      buildJsonSignupRecruiter(jsonResponse, "INVALID_FIELD", email, senha, nome, description, descricao);
      return; 
    }

    if (!emailValidator.isValidEmail(email)) {
      buildJsonSignupRecruiter(jsonResponse, "INVALID_EMAIL", email, senha, nome, description, descricao);
      return; } 

    if (dao.consultarPeloEmail(email) != null) {
      buildJsonSignupRecruiter(jsonResponse, "USER_EXISTS", email, senha, nome, description, descricao);
      return;
    }

    UsuarioController ucontroller = new UsuarioController();
    Usuario u = ucontroller.insert(nome, email, senha);
    EmpresaController econtroller = new EmpresaController();
    Empresa e = econtroller.insert(u, descricao, description);
    buildJsonSignupRecruiter(jsonResponse, "SUCCESS",  email, senha, nome, description, descricao);
  }

  public JSONObject buildJsonLoginRecruiter(JSONObject res, String status, String token, String email, String senha) {
    res.put("operation", "LOGIN_RECRUITER");
    res.put("status", status);
    JSONObject data = new JSONObject();
    data.put("token", token);
    data.put("email", email);
    data.put("senha", senha);
    res.put("data", data);
    return res;
  }

  public JSONObject buildJsonSignupRecruiter(JSONObject res, String status, String email, String senha, String nome,
    String description, String descricao) {
    res.put("operation", "SIGNUP_RECRUITER");
    res.put("status", status);
    JSONObject data = new JSONObject();
    data.put("", "");
    res.put("data", data);
    return res;
  }

  public JSONObject buildLogoutJsonRecruiter(JSONObject json, String status, String token) {
    json.put("operation", "LOGOUT_RECRUITER");
    json.put("status", status);
    JSONObject data = new JSONObject();
    data.put("token", token);
    json.put("data", data);
    return json;
  }
}
