package dao.impl;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import dao.UsuarioDAO;
import excecao.ObjetoNaoEncontradoException;
import modelo.Usuario;

@Repository
public class UsuarioDAOImpl implements UsuarioDAO {
	@PersistenceContext
	protected EntityManager em;
	
	public Usuario recuperaUmUsuarioEPerfis(String username) throws ObjetoNaoEncontradoException{
		List<Usuario> usuarios = em.createQuery("select distinct u from Usuario u left outer join fetch u.perfis").getResultList();
		Usuario umUsuario=null;
		for(int i =0;i<usuarios.size();i++) {
			if(usuarios.get(i).getConta().equals(username)) {
				umUsuario = usuarios.get(i);
			}
		}

		if (umUsuario == null) {
			throw new ObjetoNaoEncontradoException();
		}

		return umUsuario;
	}

}