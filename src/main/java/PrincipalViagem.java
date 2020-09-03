import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import corejava.Console;
import excecao.CarroNaoEncontradoException;
import excecao.UsuarioNaoEncontradoException;
import excecao.ViagemNaoEncontradaException;
import modelo.Carro;
import modelo.SingletonPerfis;
import modelo.Usuario;
import modelo.Viagem;
import service.CarroAppService;
import service.UsuarioAppService;
import service.ViagemAppService;

public class PrincipalViagem {
	public static void main(String[] args) {
		String saida;
		String destino;
		double preco;
		Carro umCarro;
		Viagem umaViagem;
		
		
		@SuppressWarnings("resource")
		ApplicationContext fabrica = new ClassPathXmlApplicationContext("beans-jpa.xml");
		CarroAppService carroAppService = (CarroAppService)fabrica.getBean ("carroAppService");
		ViagemAppService viagemAppService = (ViagemAppService)fabrica.getBean("viagemAppService");
		UsuarioAppService usuarioAppService = (UsuarioAppService)fabrica.getBean("usuarioAppService");
		// -----
				Usuario umUsuario;
				String senhaUsuario = null;
				
				String outroUser = null;
				do {
					String conta = Console.readLine("Digite como deseja efetuar o login: ");
					try {
						umUsuario = usuarioAppService.recuperaUmUsuarioEPerfis(conta);
						
						outroUser = umUsuario.getConta();
						senhaUsuario = Console.readLine("\nDigite sua senha: ");
					}catch(UsuarioNaoEncontradoException e) {
						System.out.println("Usuario n�o encontrado");
						umUsuario = new Usuario("not found","WRONG");
					}
					if(senhaUsuario.equals(umUsuario.getSenha())) {
						System.out.println("Usu�rio logado: "+ conta);
					}
					else {
						System.out.println("A senha n�o est� correta.");
					}
				}while(!senhaUsuario.equals(umUsuario.getSenha()));
				SingletonPerfis singleton = SingletonPerfis.getSingletonPerfis();
				String[] permissoes = new String[umUsuario.getPerfis().size()];
				for(int i=0; i<permissoes.length;i++) {
					permissoes[i]=umUsuario.getPerfis().get(i).getPerfil();
				}
				
				String[] contas = new String[1];
				contas[0] = outroUser;
				singleton.setPerfis(contas);
				// -----
		

		
		boolean continua = true;
		while (continua) {
			System.out.println('\n' + "O que voc� deseja fazer?");
			/* ==> */ System.out.println('\n' + "1. Cadastrar uma viagem de um carro");
			System.out.println("2. Alterar uma viagem");
			/* ==> */ System.out.println("3. Remover uma viagem");
			System.out.println("4. Listar todas as viagens");
			System.out.println("5. Sair");

			int opcao = Console.readInt('\n' + "Digite um n�mero entre 1 e 5:");

			switch (opcao) {
			case 1: {
				long idCarro = Console.readInt('\n' + "Informe o n�mero do carro: ");

				try {
					umCarro = carroAppService.recuperaUmCarro(idCarro);
				} catch (CarroNaoEncontradoException e) {
					System.out.println('\n' + e.getMessage());
					break;
				}

				preco = Console.readDouble('\n' + "Informe o pre�o da gasolina da viagem: ");
				saida = Console.readLine("Informe o local de sa�da da viagem: ");
				destino = Console.readLine("Informe o destino da viagem: ");

				umaViagem = new Viagem(preco, saida, destino, umCarro);
				

				try
				/* ==> */ {
					viagemAppService.inclui(umaViagem);

					System.out.println('\n' + "Viagem adicionada com sucesso");
				} catch (CarroNaoEncontradoException e) {
					System.out.println(e.getMessage());
				}

				break;
			}
			case 2: {
				int resposta = Console.readInt('\n' + "Digite o n�mero da viagem que voc� deseja alterar: ");

				try {
					umaViagem = viagemAppService.recuperaUmaViagem(resposta);

				} catch (ViagemNaoEncontradaException e) {
					System.out.println('\n' + e.getMessage());
					break;
				}

				System.out.println('\n' + "N�mero = " + umaViagem.getId() + "    saida = " + umaViagem.getSaida()
						+ "  Pre�o = " + umaViagem.getPreco() + "   Destino= " + umaViagem.getDestino());

				System.out.println('\n' + "O que voc� deseja alterar?");
				System.out.println('\n' + "1. Saida");
				System.out.println("\n2. Pre�o da gasolina");
				System.out.println("\n3. Destino");

				int opcaoAlteracao = Console.readInt('\n' + "Digite um n�mero de 1 a 3:");

				switch (opcaoAlteracao) {
				case 1:
					String nomeS = Console.readLine("Digite o novo local de sa�da: ");
					umaViagem.setSaida(nomeS);

					try {
						viagemAppService.altera(umaViagem);

						System.out.println('\n' + "Altera��o de viagem efetuada com sucesso!");
					} catch (ViagemNaoEncontradaException e) {
						System.out.println('\n' + e.getMessage());
					}

					break;

				case 2:
					double novoP = Console.readDouble("Digite o novo Pre�o da gasolina da viagem: ");
					umaViagem.setPreco(novoP);

					try {
						viagemAppService.altera(umaViagem);

						System.out.println('\n' + "Altera��o de viagem efetuada com sucesso!");
					} catch (ViagemNaoEncontradaException e) {
						System.out.println('\n' + e.getMessage());
					}

					break;
				case 3:
					String d = Console.readLine("Digite o novo Destino: ");
					umaViagem.setDestino(d);

					try {
						viagemAppService.altera(umaViagem);

						System.out.println('\n' + "Altera��o de viagem efetuada com sucesso!");
					} catch (ViagemNaoEncontradaException e) {
						System.out.println('\n' + e.getMessage());
					}

					break;
				default:
					System.out.println('\n' + "Op��o inv�lida!");
				}

				break;
			}

			case 3: {
				int resposta = Console.readInt('\n' + "Digite o n�mero da viagem que voc� deseja remover: ");

				try {
					umaViagem = viagemAppService.recuperaUmaViagem(resposta);
				} catch (ViagemNaoEncontradaException e) {
					System.out.println('\n' + e.getMessage());
					break;
				}

				System.out.println(
						'\n' + "N�mero = " + umaViagem.getId() + "    Pre�o = " + umaViagem.getPreco()
								+ "    Sa�da: = " + umaViagem.getSaida() + "  Destino: " + umaViagem.getDestino());

				String resp = Console.readLine('\n' + "Confirma a remo��o da viagem?");

				if (resp.equals("s")) {
					try
					/* ==> */ {
						viagemAppService.exclui(umaViagem.getId());
						System.out.println('\n' + "Viagem removida com sucesso!");
					} catch (ViagemNaoEncontradaException e) {
						System.out.println(e.getMessage());
					}
				} else {
					System.out.println('\n' + "Viagem n�o removida.");
				}

				break;
			}

			case 4: {
				List<Viagem> arrayViagens = viagemAppService.recuperaViagens();

				if (arrayViagens.size() == 0) {
					System.out.println('\n' + "Nao h� viagens cadastradas.");
					break;
				}

				System.out.println("");
				for (Viagem viagem : arrayViagens) {
					System.out.println(
							'\n' + "N�mero = " + viagem.getId() + "    Pre�o = " + viagem.getPreco()
									+ "    Sa�da: = " + viagem.getSaida() + "  Destino: " + viagem.getDestino());
				}

				break;
			}

			case 5: {
				continua = false;
				break;
			}

			default:
				System.out.println('\n' + "Op��o inv�lida!");
			}
		}
	}
}
