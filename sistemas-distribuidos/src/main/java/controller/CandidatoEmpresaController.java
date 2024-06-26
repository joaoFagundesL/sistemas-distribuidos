package controller;

import java.util.List;

import dao.CandidatoEmpresaDAO;
import modelo.CandidatoEmpresa;

public class CandidatoEmpresaController {
	CandidatoEmpresaDAO dao = new CandidatoEmpresaDAO();	
	
	public void inserirEmpresaCandidato(Integer empresaId, Integer userId) {
		dao.inserirEmpresaCandidato(empresaId, userId);
	}
	
	public List<CandidatoEmpresa> consultarEmpresas(Integer id) {
		return dao.consultarEmpresas(id);
	}
}
