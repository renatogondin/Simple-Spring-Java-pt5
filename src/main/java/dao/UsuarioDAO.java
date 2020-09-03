package dao;


import excecao.ObjetoNaoEncontradoException;
import modelo.Usuario;

public interface UsuarioDAO {


    Usuario recuperaUmUsuarioEPerfis(String username) throws ObjetoNaoEncontradoException;
}