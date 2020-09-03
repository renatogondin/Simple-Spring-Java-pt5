import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import corejava.Console;
import excecao.ClienteNaoEncontradoException;
import excecao.MotoristaNaoEncontradoException;
import excecao.UsuarioNaoEncontradoException;
import modelo.Carro;
import modelo.Cliente;
import modelo.Motorista;
import modelo.SingletonPerfis;
import modelo.Usuario;
import service.ClienteAppService;
import service.MotoristaAppService;
import service.UsuarioAppService;

public class PrincipalMotorista {
	public static void main(String[] args)   {
		String nome;
		String sobrenome;
		String email;

		Cliente umCliente;
		Motorista umMotorista;

		@SuppressWarnings("resource")
		ApplicationContext fabrica = new ClassPathXmlApplicationContext("beans-jpa.xml");
		ClienteAppService clienteAppService = (ClienteAppService)fabrica.getBean ("clienteAppService");
		MotoristaAppService motoristaAppService = (MotoristaAppService)fabrica.getBean ("motoristaAppService");
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
			/* ==> */ System.out.println('\n' + "1. Cadastrar um motorista de um cliente");
			System.out.println("\n2. Alterar um motorista");
			/* ==> */ System.out.println("3. Remover um motorista");
			System.out.println("4. Listar um motorista e todos os seus carros");
			System.out.println("5. Listar Todos os motoristas e seus carros");
			System.out.println("6. Sair");

			int opcao = Console.readInt('\n' + "Digite um n�mero entre 1 e 6:");

			switch (opcao) {
			case 1: {
				long idCliente = Console.readInt('\n' + "Informe o n�mero do cliente: ");

				try {
					umCliente = clienteAppService.recuperaUmCliente(idCliente);
				} catch (ClienteNaoEncontradoException e) {
					System.out.println('\n' + e.getMessage());
					break;
				}

				nome = Console.readLine('\n' + "Informe o nome do Motorista: ");
				sobrenome = Console.readLine("Informe o sobrenome do Motorista: ");
				email = Console.readLine("Informe o email do Motorista: ");

				umMotorista = new Motorista(nome, sobrenome, email, umCliente);

				try
				/* ==> */ {
					motoristaAppService.inclui(umMotorista);

					System.out.println('\n' + "Motorista adicionado com sucesso");
				} catch (ClienteNaoEncontradoException e) {
					System.out.println(e.getMessage());
				}

				break;
			}
			case 2: {
				int resposta = Console.readInt('\n' + "Digite o n�mero do motorista que voc� deseja alterar: ");

				try {
					umMotorista = motoristaAppService.recuperaUmMotorista(resposta);
					
				} catch (MotoristaNaoEncontradoException e) {
					System.out.println('\n' + e.getMessage());
					break;
				}

				System.out
						.println('\n' + "N�mero = " + umMotorista.getId() + "    nome = " + umMotorista.getNome()+"  sobrenome = "+ umMotorista.getSobrenome()+"   email= "+umMotorista.getEmail());

				System.out.println('\n' + "O que voc� deseja alterar?");
				System.out.println('\n' + "1. Nome");
				System.out.println("\n2. Email");

				int opcaoAlteracao = Console.readInt('\n' + "Digite um n�mero de 1 a 2:");

				switch (opcaoAlteracao) {
				case 1:
					String nomeM = Console.readLine("Digite o novo Nome: ");
					umMotorista.setNome(nomeM);

					try {
						motoristaAppService.altera(umMotorista);

						System.out.println('\n' + "Altera��o de motorista efetuada com sucesso!");
					} catch (MotoristaNaoEncontradoException e) {
						System.out.println('\n' + e.getMessage());
					}

					break;

				case 2:
					String em = Console.readLine("Digite o novo email: ");
					umMotorista.setEmail(em);

					try {
						motoristaAppService.altera(umMotorista);

						System.out.println('\n' + "Altera��o de email efetuada " + "com sucesso!");
					} catch (MotoristaNaoEncontradoException e) {
						System.out.println('\n' + e.getMessage());
					}

					break;

				default:
					System.out.println('\n' + "Op��o inv�lida!");
				}

				break;
			}


			case 3: {
				int resposta = Console.readInt('\n' + "Digite o n�mero do motorista que voc� deseja remover: ");

				try {
					umMotorista = motoristaAppService.recuperaUmMotorista(resposta);
				} catch (MotoristaNaoEncontradoException e) {
					System.out.println('\n' + e.getMessage());
					break;
				}

				System.out.println('\n' + "N�mero = " + umMotorista.getId() + "    Nome = " + umMotorista.getNome()
						+ "    Sobrenome = " + umMotorista.getSobrenome() + "    email=" + umMotorista.getEmail());

				String resp = Console.readLine('\n' + "Confirma a remo��o do motorista? S/N");

				if (resp.equals("s")) {
					try
					/* ==> */ {
						motoristaAppService.exclui(umMotorista.getId());
						System.out.println('\n' + "Motorista removido com sucesso!");
					} catch (MotoristaNaoEncontradoException e) {
						System.out.println(e.getMessage());
					}
				} else {
					System.out.println('\n' + "Motorista n�o removido.");
				}

				break;
			}

			case 4: {
				try {
					long resposta = Console.readInt('\n' + "Informe o n�mero do Motorista: ");
					umMotorista = motoristaAppService.recuperaUmMotoristaECarros(resposta);

				} catch (MotoristaNaoEncontradoException e) {
					System.out.println('\n' + e.getMessage());
					break;
				}

				System.out.println('\n' + "N�mero = " + umMotorista.getId() + "    Nome = " + umMotorista.getNome()
						+ "    Sobrenome = " + umMotorista.getSobrenome() + "    Email=" + umMotorista.getEmail());

				List<Carro> carros = umMotorista.getCarros();

				for (Carro carro : carros) {
					System.out.println('\n' + "      Carro n�mero = " + carro.getId() + "  marca = "
							+ carro.getMarca() + "  modelo  = " + carro.getModelo() + "  placa = "
							+ carro.getPlaca());
				}

				break;
			}

			case 5: {
				List<Motorista> motoristas = motoristaAppService.recuperaMotoristasECarros();

				if (motoristas.size() != 0) {
					System.out.println("");

					for (Motorista motorista : motoristas) {

						System.out.println('\n' + "N�mero = " + motorista.getId() + "    Nome = "
								+ motorista.getNome() + "    sobrenome=" + motorista.getSobrenome() + "     email=" + motorista.getEmail());

						List<Carro> carros = motorista.getCarros();

						for (Carro carro : carros) {
							System.out.println('\n' + "      Carro n�mero = " + carro.getId() + "  marca = "
									+ carro.getMarca() + "  modelo  = " + carro.getModelo() + "  placa = "
									+ carro.getPlaca());
						}
					}
				} else {
					System.out.println('\n' + "N�o h� motoristas cadastrados.");
				}

				break;
			}

			case 6: {
				continua = false;
				break;
			}

			default:
				System.out.println('\n' + "Op��o inv�lida!");
			}
		}
	}
}
