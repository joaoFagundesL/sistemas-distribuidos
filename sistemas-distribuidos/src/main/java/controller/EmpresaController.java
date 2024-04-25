package controller;

import dao.EmpresaDAO;
import modelo.Empresa;
import modelo.Usuario;

public class EmpresaController {
	public Empresa insert(Usuario u, String descricao, String branch) {
		EmpresaDAO dao = new EmpresaDAO();
		Empresa a = new Empresa();
		a.setUsuario(u);
    a.setBranch(branch);
    a.setDescricao(descricao);
		
		try {
			dao.insertWithQuery(a);
			return a;
		} catch (Exception e1) {
			e1.printStackTrace();
			return null;
		}
	}
	
}
