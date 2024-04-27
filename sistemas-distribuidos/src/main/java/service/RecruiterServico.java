package service;

import org.json.JSONObject;

import com.auth0.jwt.exceptions.JWTVerificationException;

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
    String token = jsonMessage.getString("token");

    try {
      jwt.verifyToken(token);
      buildLogoutJsonRecruiter(jsonResponse, "SUCCESS", token);
    } catch(JWTVerificationException e) {
      buildLogoutJsonRecruiter(jsonResponse, "INVALID_TOKEN", token);
    }
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

    if (!eController.isUserValid(email)) {
      buildJsonLoginRecruiter(jsonResponse, "USER_NOT_FOUND", "", email, senha);
      return;
    }

    if (!eController.isPasswordValid(email, senha)) {
      buildJsonLoginRecruiter(jsonResponse, "INVALID_PASSWORD", "", email, senha);
      return;
    } 

    Integer id = eController.consultarId(email);
    String idString = String.valueOf(id);
    String token = JwtUtility.generateToken(idString, "recruiter");
    buildJsonLoginRecruiter(jsonResponse, "SUCCESS", token, email, senha);

    UsuarioController uController = new UsuarioController();
    uController.inserirToken(id, token);
  }

  public void signupRecruiter(JSONObject jsonMessage, JSONObject jsonResponse) {
    EmailValidator emailValidator = new EmailValidator();
    UsuarioDAO dao = new UsuarioDAO();
    JSONObject data = jsonMessage.getJSONObject("data");

    String email = data.getString("email");
    String senha = data.getString("password");
    String nome = data.getString("name");
    String branch = data.getString("branch");
    String descricao = data.getString("description");

    if (email.isEmpty() || senha.isEmpty() || nome.isEmpty() || branch.isEmpty() || descricao.isEmpty()) {
      buildJsonSignupRecruiter(jsonResponse, "INVALID_FIELD", email, senha, nome, branch, descricao);
      return; 
    }

    if (!emailValidator.isValidEmail(email)) {
      buildJsonSignupRecruiter(jsonResponse, "INVALID_EMAIL", email, senha, nome, branch, descricao);
      return; } 

    if (dao.consultarPeloEmail(email) != null) {
      buildJsonSignupRecruiter(jsonResponse, "USER_EXISTS", email, senha, nome, branch, descricao);
      return;
    }

    UsuarioController ucontroller = new UsuarioController();
    Usuario u = ucontroller.insert(nome, email, senha);
    EmpresaController econtroller = new EmpresaController();
    Empresa e = econtroller.insert(u, descricao, branch);
    buildJsonSignupRecruiter(jsonResponse, "SUCCESS",  email, senha, nome, branch, descricao);
  }

  public JSONObject buildJsonLoginRecruiter(JSONObject res, String status, String token, String email, String senha) {
    res.put("operation", "LOGIN_RECRUITER");
    res.put("status", status);
    res.put("token", token);
    JSONObject data = new JSONObject();
    data.put("email", email);
    data.put("senha", senha);
    res.put("data", data);
    return res;
  }

  public JSONObject buildJsonSignupRecruiter(JSONObject res, String status, String email, String senha, String nome,
    String branch, String descricao) {
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
    json.put("token", token);
    json.put("data", data);
    return json;
  }
}
