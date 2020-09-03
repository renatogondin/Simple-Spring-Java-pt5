package servico.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import anotacoes.Perfil;
import dao.CarroDAO;
import dao.ViagemDAO;
import excecao.AcimaDoPrecoViagemException;
import excecao.CarroNaoEncontradoException;
import excecao.ViagemNaoEncontradaException;
import excecao.ObjetoNaoEncontradoException;
import modelo.Carro;
import modelo.Viagem;
import service.ViagemAppService;

// @Service
public class ViagemAppServiceImpl implements ViagemAppService {
	@Autowired
	private ViagemDAO viagemDAO;
	@Autowired
	private CarroDAO carroDAO;

	@Transactional
	@Perfil(nomes={"admin"})
	public long inclui(Viagem umaViagem) throws CarroNaoEncontradoException {

		try {
			// NENHUMA VALIDA��O EST� SENDO REALIZADA AQUI!!!
			Carro umCarro = umaViagem.getCarro();

			try {
				carroDAO.recuperaUmCarroComLock(umCarro.getId());
			} catch (ObjetoNaoEncontradoException e) {
				throw new CarroNaoEncontradoException("Carro n�o encontado");
			}
			
			if(umaViagem.getPreco() > 500) {
				throw new AcimaDoPrecoViagemException();
			}
			
			long numero = viagemDAO.inclui(umaViagem);
			return numero;
		} catch (CarroNaoEncontradoException e) {

			throw e;
		}
	}
	@Transactional
	public void exclui(long numero) throws ViagemNaoEncontradaException {
		try {

			viagemDAO.exclui(numero);

		} catch (ObjetoNaoEncontradoException e) {

			throw new ViagemNaoEncontradaException("Viagem n�o encontrada");
		}
	}

	@Transactional
	public void altera(Viagem umaViagem) throws ViagemNaoEncontradaException {
		try {
			viagemDAO.altera(umaViagem);
		} catch (ObjetoNaoEncontradoException e) {
			throw new ViagemNaoEncontradaException("Viagem n�o encontrada");
		}
	}

	public Viagem recuperaUmaViagem(long numero) throws ViagemNaoEncontradaException {
		try {
			Viagem umaViagem = viagemDAO.recuperaUmaViagem(numero);

			return umaViagem;
		} catch (ObjetoNaoEncontradoException e) {
			throw new ViagemNaoEncontradaException("Viagem n�o encontrada!");
		}
	}

	public List<Viagem> recuperaViagens() {

		return viagemDAO.recuperaViagens();

	}
}