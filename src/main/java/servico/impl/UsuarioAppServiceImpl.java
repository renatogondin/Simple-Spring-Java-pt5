package servico.impl;


import org.springframework.beans.factory.annotation.Autowired;

import dao.UsuarioDAO;
import excecao.ObjetoNaoEncontradoException;
import excecao.UsuarioNaoEncontradoException;
import modelo.Usuario;
import service.UsuarioAppService;

public class UsuarioAppServiceImpl implements UsuarioAppService{
	@Autowired
	private UsuarioDAO usuarioDAO ;

	
	public Usuario recuperaUmUsuarioEPerfis(String username) throws UsuarioNaoEncontradoException {
		try {
			return usuarioDAO.recuperaUmUsuarioEPerfis(username);
		} catch (ObjetoNaoEncontradoException e) {
			throw new UsuarioNaoEncontradoException("Usuario não encontrado");
		} 
	}
}