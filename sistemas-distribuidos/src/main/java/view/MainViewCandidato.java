package view;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.json.JSONObject;

import cliente.Client;

public class MainViewCandidato {
  private static MainViewCandidato instance = null;

  JPanel panelCont = new JPanel();
  JPanel panelFirst = new JPanel();
  JButton btnVoltar = new JButton("Voltar");
  CardLayout cl = new CardLayout();

  JSONObject response = new JSONObject();

  JFrame frame;

  Boolean isLogout = false;

  public MainViewCandidato() {
  }

  public static MainViewCandidato getInstance() {
    if (instance == null) {
      instance = new MainViewCandidato();
    }
    return instance;
  }

  public Boolean getLogout() {
    return isLogout;
  }

  public void initComponents(LoginView loginClass) {
    final CandidatoView clienteView = new CandidatoView();
    final CompetenciaView competenciaView = new CompetenciaView();
    final FiltrarVagaView filtrarView = new FiltrarVagaView(); 

    if (frame == null || !frame.isVisible()) {
      isLogout = false;
      frame = new JFrame("Sistema");
      frame.setBounds(100, 100, 670, 485);
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      frame.setVisible(true);
    }

    final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    panelFirst.setLayout(null);
    panelFirst.setBackground(Color.WHITE);
    panelCont.setLayout(cl);
    panelCont.add(panelFirst, "1");

    tabbedPane.setBounds(219, -88, 573, 656);
    panelFirst.add(tabbedPane);

    tabbedPane.addTab("Candidato", clienteView);
    tabbedPane.addTab("Competencia", competenciaView);
    tabbedPane.addTab("Filtrar", filtrarView);

    JPanel panel_2 = new JPanel();
    panel_2.setBackground(SystemColor.inactiveCaption);
    panel_2.setBounds(0, 0, 218, 568);
    panelFirst.add(panel_2);
    panel_2.setLayout(null);

    JButton btnGerenciarProfessor = new JButton("Login Infos");
    btnGerenciarProfessor.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        tabbedPane.setSelectedIndex(0); // Vai para CandidatoView
      }
    });
    btnGerenciarProfessor.setForeground(Color.WHITE);
    btnGerenciarProfessor.setBackground(Color.DARK_GRAY);
    btnGerenciarProfessor.setBounds(23, 109, 171, 27);
    panel_2.add(btnGerenciarProfessor);

    JButton btnGerenciarCurso = new JButton("CRUD Skills");
    btnGerenciarCurso.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        tabbedPane.setSelectedIndex(1); // Vai para CompetenciaView
      }
    });
    btnGerenciarCurso.setForeground(Color.WHITE);
    btnGerenciarCurso.setBackground(Color.DARK_GRAY);
    btnGerenciarCurso.setBounds(23, 148, 171, 27);
    panel_2.add(btnGerenciarCurso);

    
    JButton btnGerenciarFiltrar = new JButton("Filtrar Vaga");
    btnGerenciarFiltrar.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        tabbedPane.setSelectedIndex(2); // Vai para CompetenciaView
      }
    });
    btnGerenciarFiltrar.setForeground(Color.WHITE);
    btnGerenciarFiltrar.setBackground(Color.DARK_GRAY);
    btnGerenciarFiltrar.setBounds(23, 187, 171, 27);
    panel_2.add(btnGerenciarFiltrar);

    JLabel lblSistemaFaculdade = new JLabel("Candidato");
    lblSistemaFaculdade.setFont(new Font("Dialog", Font.BOLD, 16));
    lblSistemaFaculdade.setForeground(Color.WHITE);
    lblSistemaFaculdade.setBounds(33, 12, 161, 46);
    panel_2.add(lblSistemaFaculdade);

    JButton btnLogout = new JButton("Logout");
    btnLogout.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        loginClass.frame.setVisible(true);
        JSONObject request = new JSONObject();
        String token = Client.getInstance().getToken();
        buildLogoutJson(request, token);
        clienteView.limparTable();
        competenciaView.limparTable();
        try {
          JSONObject response = Client.getInstance().sendRequest(request);
        }catch(IOException err) {
          err.printStackTrace();
        }

        frame.dispose();
        isLogout = true;
      }
    });
    btnLogout.setForeground(Color.WHITE);
    btnLogout.setBackground(Color.DARK_GRAY);
    btnLogout.setBounds(23, 319, 171, 27);
    panel_2.add(btnLogout);

    frame.getContentPane().add(panelCont);
  }

  private JSONObject buildLogoutJson(JSONObject json, String token) {
    json.put("operation", "LOGOUT_CANDIDATE");
    JSONObject data = new JSONObject();
    json.put("token", token);
    json.put("data", data);
    return json;
  }
}
