package service;

import org.json.JSONObject;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import controller.EmpresaController;
import controller.UsuarioController;
import dao.EmpresaDAO;
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
      buildInvalidToken(jsonResponse, "LOGOUT_RECRUITER");
    }
  }

 public void lookup_recruiter(JSONObject jsonMessage, JSONObject jsonResponse) {
    JwtUtility jwt = new JwtUtility();
    String token = jsonMessage.getString("token");

    try {
      DecodedJWT decodedJWT = jwt.verifyToken(token);
      Claim idClaim = decodedJWT.getClaim("id");
      String userIdAsString = idClaim.asString();
      Integer id = Integer.parseInt(userIdAsString);
      EmpresaController cController = new EmpresaController();
      Empresa c = cController.consultarPorId(id);

      if (c == null) {
        buildLookupRecruiter(jsonResponse, "USER_NOT_FOUND", token, "", "", "", "", "");
        return;
      }

      String nome = c.getUsuario().getNome();
      String email = c.getUsuario().getEmail();
      String senha = c.getUsuario().getSenha();
      String industry = c.getIndustry();
      String descricao = c.getDescricao();

      buildLookupRecruiter(jsonResponse, "SUCCESS", token, nome, email, senha, industry, descricao);    
    } catch(JWTVerificationException e) {
      buildInvalidToken(jsonResponse, "LOOKUP_ACCOUNT_RECRUITER");
    }
  }

 public void deleteAccount(JSONObject jsonMessage, JSONObject jsonResponse) {
    JwtUtility jwt = new JwtUtility();
    String token = jsonMessage.getString("token");

    try {
      DecodedJWT decodedJWT = jwt.verifyToken(token);
      Claim idClaim = decodedJWT.getClaim("id");
      String userIdAsString = idClaim.asString();
      Integer id = Integer.parseInt(userIdAsString);
      EmpresaController eController = new EmpresaController();
      Empresa c = eController.consultarPorId(id);

      if (c == null) {
        buildJsonDeleteRecruiter(jsonResponse, "USER_NOT_FOUND");
        return;
      }

      UsuarioController ucontroller = new UsuarioController();

      eController.remover(Empresa.class, c.getId());
      ucontroller.remover(Usuario.class, c.getUsuario().getId());

      buildJsonDeleteRecruiter(jsonResponse, "SUCCESS");


    } catch(JWTVerificationException e) {
      buildInvalidToken(jsonResponse, "DELETE_ACCOUNT_RECRUITER");
    }
  }

 public JSONObject buildInvalidToken(JSONObject res, String operation) {
    res.put("operation", operation);
    res.put("status", "INVALID_TOKEN");
    JSONObject data = new JSONObject();
    res.put("data", data);
    return res;
  }

  public JSONObject buildJsonDeleteRecruiter(JSONObject res, String status) {
    res.put("operation", "DELETE_ACCOUNT_RECRUITER");
    res.put("status", status);
    JSONObject data = new JSONObject();
    res.put("data", data);
    return res;
  }

  public JSONObject buildLookupRecruiter(JSONObject jsonResponse, String status, String token,
    String nome, String email, String senha, String industry, String descricao) {

    jsonResponse.put("operation", "LOOKUP_ACCOUNT_RECRUITER");
    jsonResponse.put("status", status);
    JSONObject data = new JSONObject();
    data.put("token", token);
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
      buildJsonLoginRecruiter(jsonResponse, "INVALID_FIELD", "");
      return;
    }

    // decidiram mudar para apenas invalid_login, caso mudem de novo a logica ja 
    // esta pronta, é so descomentar a linha
    if (!eController.isUserValid(email)) {
      buildJsonLoginRecruiter(jsonResponse, "INVALID_LOGIN", "");
      return;
    }

    if (!eController.isPasswordValid(email, senha)) {
      buildJsonLoginRecruiter(jsonResponse, "INVALID_LOGIN", "");
      return;
    } 

    Integer id = eController.consultarId(email);
    String idString = String.valueOf(id);
    String token = JwtUtility.generateToken(idString, "recruiter");
    buildJsonLoginRecruiter(jsonResponse, "SUCCESS", token);
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

  public JSONObject buildJsonLoginRecruiter(JSONObject res, String status, String token) {
    res.put("operation", "LOGIN_RECRUITER");
    res.put("status", status);
    JSONObject data = new JSONObject();
    data.put("token", token);
    res.put("data", data);
    return res;
  }

  public JSONObject buildJsonSignupRecruiter(JSONObject res, String status, String email, String senha, String nome,
    String description, String descricao) {
    res.put("operation", "SIGNUP_RECRUITER");
    res.put("status", status);
    JSONObject data = new JSONObject();
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

  public JSONObject buildUpdateJsonRecruiter(JSONObject res, String status) {
    res.put("operation", "UPDATE_ACCOUNT_RECRUITER");
    res.put("status", status);
    JSONObject data = new JSONObject();
    res.put("data", data);
    return res;
  }

  public void updateRecruiter(JSONObject jsonMessage, JSONObject jsonResponse) {
    JwtUtility jwt = new JwtUtility();

    JSONObject data = jsonMessage.getJSONObject("data");
    String token = jsonMessage.getString("token");


    if ((data.has("email") && data.getString("email").isEmpty()) ||
  (data.has("name") && data.getString("name").isEmpty()) ||
  (data.has("password") && data.getString("password").isEmpty()) || 
  (data.has("industry") && data.getString("industry").isEmpty()) ||
  (data.has("description") && data.getString("description").isEmpty())) {
      buildUpdateJsonRecruiter(jsonResponse, "INVALID_FIELD");
      return;
    }   

    if (!data.has("email") && !data.has("password") && !data.has("name")
    && !data.has("description") && !data.has("industry")) {
      buildUpdateJsonRecruiter(jsonResponse, "INVALID_FIELD");
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
      EmpresaController cController = new EmpresaController();
      Empresa c = cController.consultarPorId(id);

      if (c == null) {
        buildUpdateJsonRecruiter(jsonResponse, "USER_NOT_FOUND");
        return;
      }

      String email = "";  
      String senha = "";
      String nome = "";       
      String description = "";
      String industry = "";

      if (data.has("email")) {
        email = data.getString("email");

        Usuario existingCandidatoWithEmail = dao.consultarPeloEmail(email);

        if (existingCandidatoWithEmail != null && !existingCandidatoWithEmail.getId().equals(c.getUsuario().getId())) {
          buildUpdateJsonRecruiter(jsonResponse, "INVALID_EMAIL");
          return;
        }
      }

      if (data.has("password")) {
        senha = data.getString("password");
      }

      if (data.has("name")) {
        nome = data.getString("name");
      }

      if (data.has("description")) {
        description = data.getString("description");
      }

      if (data.has("industry")) {
        industry = data.getString("industry");
      }

      EmpresaDAO cdao = new EmpresaDAO();
      cdao.update(c, nome, email, senha, industry, description);
      ucontroller.updateEmpresa(c, nome, email, senha);

      buildUpdateJsonRecruiter(jsonResponse, "SUCCESS");
    } catch (JWTVerificationException e) {
      buildInvalidToken(jsonResponse, "UPDATE_ACCOUNT_CANDIDATE");
    }
  }}
