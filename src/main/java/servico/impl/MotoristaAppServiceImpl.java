package servico.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import anotacoes.Perfil;
import dao.ClienteDAO;
import dao.MotoristaDAO;
import excecao.ClienteNaoEncontradoException;
import excecao.ObjetoNaoEncontradoException;
import excecao.MotoristaNaoEncontradoException;
import excecao.MotoristaTemNomeGrandeException;
import modelo.Cliente;
import modelo.Motorista;
import service.MotoristaAppService;

public class MotoristaAppServiceImpl implements MotoristaAppService {
	@Autowired
	private MotoristaDAO motoristaDAO;
	@Autowired
	private ClienteDAO clienteDAO ;

	
	@Transactional
	@Perfil(nomes={"admin", "user"})
	public long inclui(Motorista umMotorista) throws ClienteNaoEncontradoException {
		
	
		try {
			// NENHUMA VALIDA��O EST� SENDO REALIZADA AQUI!!!

			Cliente umCliente = umMotorista.getCliente();

			try {
				clienteDAO.recuperaUmClienteComLock(umCliente.getId());
			} catch (ObjetoNaoEncontradoException e) {
				throw new ClienteNaoEncontradoException("Cliente n�o encontado");
			}
			
			if(umMotorista.getNome().length() > 30) {
				throw new MotoristaTemNomeGrandeException();
			}
			long numero = motoristaDAO.inclui(umMotorista);

			return numero;
		} catch (ClienteNaoEncontradoException e) {

			throw e;
		}
	}

	@Transactional
	public void altera(Motorista umMotorista) throws MotoristaNaoEncontradoException {
		try {

			motoristaDAO.altera(umMotorista);

		} catch (ObjetoNaoEncontradoException e) {

			throw new MotoristaNaoEncontradoException("Motorista n�o encontrado");
		}
	}

	@Transactional
	public void exclui(long numero) throws MotoristaNaoEncontradoException {
		try {

			motoristaDAO.exclui(numero);

		} catch (ObjetoNaoEncontradoException e) {
			throw new MotoristaNaoEncontradoException("Motorista n�o encontrado");
		}
	}

	public Motorista recuperaUmMotorista(long numero) throws MotoristaNaoEncontradoException {
		try {
			Motorista umMotorista = motoristaDAO.recuperaUmMotorista(numero);

			return umMotorista;
		} catch (ObjetoNaoEncontradoException e) {
			throw new MotoristaNaoEncontradoException("Motorista n�o encontrado");
		} 
	}

	public Motorista recuperaUmMotoristaECarros(long numero) throws MotoristaNaoEncontradoException {
		try {
			return motoristaDAO.recuperaUmMotoristaECarros(numero);
		} catch (ObjetoNaoEncontradoException e) {
			throw new MotoristaNaoEncontradoException("Motorista n�o encontrado");
		} 
	}

	public List<Motorista> recuperaMotoristasECarros() {
		
			return motoristaDAO.recuperaMotoristasECarros();
		 
	}
}