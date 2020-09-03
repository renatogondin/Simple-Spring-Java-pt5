package aspects;


//import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import anotacoes.Perfil;
import excecao.NaoAutorizadoException;
import modelo.SingletonPerfis;

@Aspect
public class AspectToLog {
	@Pointcut("execution(* servico..*.*(..))")
	public void verificaPermissao() {
		
	}

	@Around("verificaPermissao()")
	public Object fazVerificacao(ProceedingJoinPoint joinPoint) throws Throwable {
		
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();
		Perfil anotacao = method.getAnnotation(Perfil.class);
		if (method.isAnnotationPresent(Perfil.class)) {
			String[] perfis = anotacao.nomes();
			SingletonPerfis perfil = SingletonPerfis.getSingletonPerfis();
			System.out.println(perfil);
			String[] perfisDoUsuario = perfil.getPerfis();
			boolean temPermissao = false;
			for (int i = 0; i < perfis.length; i++) {
				for (int j = 0; j < perfisDoUsuario.length; j++) {
					if (perfis[i].equals(perfisDoUsuario[j])) {
						temPermissao = true;
					}
				}
			}
			if (temPermissao) {
				try {
					return joinPoint.proceed();
				} catch (Throwable e) {
					throw e;
				}
			} else {
				throw new NaoAutorizadoException("Usuario Não tem permissão");
			}

		} else {
			try {
				return joinPoint.proceed();
			} catch (Throwable e) {
				throw e;
			}
		}

	}
}
