package view;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import modelo.Empresa;

public class MainViewEmpresa {
	JPanel panelCont = new JPanel();
	JPanel panelFirst = new JPanel();
	JButton btnVoltar = new JButton("Voltar");
	CardLayout cl = new CardLayout();

	public MainViewEmpresa(JFrame loginFrame, Empresa e) {
		JFrame frame = new JFrame("Sistema");
		initComponents(frame, loginFrame, e);
		frame.setBounds(100, 100, 670, 485);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}

	public void initComponents(JFrame frame, JFrame loginFrame, Empresa e) {
		final EmpresaViewTeste empresaView = new EmpresaViewTeste(e);

		final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		panelFirst.setLayout(null);
		panelFirst.setBackground(Color.WHITE);
		panelCont.setLayout(cl);
		panelCont.add(panelFirst, "1");
		
		tabbedPane.setBounds(219, -88, 573, 656);
		panelFirst.add(tabbedPane);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(SystemColor.activeCaption);
		tabbedPane.addTab("New tab", null, panel_1, null);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.RED);
		tabbedPane.addTab("New tab", null, empresaView, null);
		tabbedPane.addTab("New tab", null, new EmpresaViewTeste(e), null);
		EmpresaViewTeste clienteView_1 = new EmpresaViewTeste(e);
		clienteView_1.setBackground(SystemColor.inactiveCaptionBorder);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(SystemColor.inactiveCaption);
		panel_2.setBounds(0, 0, 218, 568);
		panelFirst.add(panel_2);
		panel_2.setLayout(null);
		
		
		JButton btnGerenciarProfessor = new JButton("CRUD1");
		btnGerenciarProfessor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(2);
			}
		});
		btnGerenciarProfessor.setForeground(Color.WHITE);
		btnGerenciarProfessor.setBackground(Color.DARK_GRAY);
		btnGerenciarProfessor.setBounds(23, 109, 171, 27);
		panel_2.add(btnGerenciarProfessor);
		
		JButton btnGerenciarCurso = new JButton("CRUD2");
		btnGerenciarCurso.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(3);
			}
		});
		btnGerenciarCurso.setForeground(Color.WHITE);
		btnGerenciarCurso.setBackground(Color.DARK_GRAY);
		btnGerenciarCurso.setBounds(23, 148, 171, 27);
		panel_2.add(btnGerenciarCurso);
		
		JButton btnGerenciarDisciplina = new JButton("CRUD3");
		btnGerenciarDisciplina.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(4);
			}
		});
		btnGerenciarDisciplina.setForeground(Color.WHITE);
		btnGerenciarDisciplina.setBackground(Color.DARK_GRAY);
		btnGerenciarDisciplina.setBounds(23, 187, 171, 27);
		panel_2.add(btnGerenciarDisciplina);
		
		JLabel lblSistemaFaculdade = new JLabel("Candidato");
		lblSistemaFaculdade.setFont(new Font("Dialog", Font.BOLD, 16));
		lblSistemaFaculdade.setForeground(Color.WHITE);
		lblSistemaFaculdade.setBounds(33, 12, 161, 46);
		panel_2.add(lblSistemaFaculdade);
		
		JButton btnLogout = new JButton("Logout");
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loginFrame.setVisible(true);
				frame.dispose();
			}
		});
		btnLogout.setForeground(Color.WHITE);
		btnLogout.setBackground(Color.DARK_GRAY);
		btnLogout.setBounds(23, 319, 171, 27);
		panel_2.add(btnLogout);
		
		frame.getContentPane().add(panelCont);
		
	}

}