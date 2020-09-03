package service;

import excecao.UsuarioNaoEncontradoException;
import modelo.Usuario;

public interface UsuarioAppService {
    
    Usuario recuperaUmUsuarioEPerfis(String username) throws UsuarioNaoEncontradoException;
}