import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import corejava.Console;
import excecao.CarroNaoEncontradoException;
import excecao.MotoristaNaoEncontradoException;
import excecao.UsuarioNaoEncontradoException;
import modelo.Carro;
import modelo.Viagem;
import modelo.Motorista;
import modelo.SingletonPerfis;
import modelo.Usuario;
import service.CarroAppService;
import service.MotoristaAppService;
import service.UsuarioAppService;

public class PrincipalCarro {
	public static void main(String[] args) {
		String marca;
		String modelo;
		String placa;

		Carro umCarro;
		Motorista umMotorista;
		
		@SuppressWarnings("resource")
		ApplicationContext fabrica = new ClassPathXmlApplicationContext("beans-jpa.xml");
		CarroAppService carroAppService = (CarroAppService)fabrica.getBean ("carroAppService");
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
			/* ==> */ System.out.println('\n' + "1. Cadastrar um carro de um motorista");
			System.out.println("2. Alterar um carro");
			/* ==> */ System.out.println("3. Remover um carro");
			
			System.out.println("4. Listar um carro e todas as suas viagens");
			System.out.println("5. Listar todos os carros e suas viagens");
			System.out.println("6. Sair");

			int opcao = Console.readInt('\n' + "Digite um n�mero entre 1 e 6:");

			switch (opcao) {
			case 1: {
				long idMotorista = Console.readInt('\n' + "Informe o n�mero do Motorista: ");

				try {
					umMotorista = motoristaAppService.recuperaUmMotorista(idMotorista);
				} catch (MotoristaNaoEncontradoException e) {
					System.out.println('\n' + e.getMessage());
					break;
				}

				marca = Console.readLine('\n' + "Informe a marca do carro: ");
				modelo = Console.readLine("Informe o modelo do carro: ");
				placa = Console.readLine("Informe a placa do carro: ");

				umCarro = new Carro(marca, modelo, placa, umMotorista);

				try
				/* ==> */ {
					carroAppService.inclui(umCarro);

					System.out.println('\n' + "Carro adicionado com sucesso");
				} catch (MotoristaNaoEncontradoException e) {
					System.out.println(e.getMessage());
				}

				break;
			}
			case 2: {
				int resposta = Console.readInt('\n' + "Digite o n�mero do carro que voc� deseja alterar: ");

				try {
					umCarro = carroAppService.recuperaUmCarro(resposta);
					
				} catch (CarroNaoEncontradoException e) {
					System.out.println('\n' + e.getMessage());
					break;
				}

				System.out
						.println('\n' + "N�mero = " + umCarro.getId() + "    Marca = " + umCarro.getMarca()+"  Placa = "+ umCarro.getPlaca()+"   Modelo = "+umCarro.getModelo());

				System.out.println('\n' + "O que voc� deseja alterar?");
				System.out.println('\n' + "1. Marca");
				System.out.println("\n2. Modelo");
				System.out.println("\n3. Placa");

				int opcaoAlteracao = Console.readInt('\n' + "Digite um n�mero de 1 a 3:");

				switch (opcaoAlteracao) {
				case 1:
					String marcaC = Console.readLine("Digite a nova marca: ");
					umCarro.setMarca(marcaC);

					try {
						carroAppService.altera(umCarro);

						System.out.println('\n' + "Altera��o de carro efetuada com sucesso!");
					} catch (CarroNaoEncontradoException e) {
						System.out.println('\n' + e.getMessage());
					}

					break;

				case 2:
					String tipoC = Console.readLine("Digite o novo modelo: ");
					umCarro.setModelo(tipoC);

					try {
						carroAppService.altera(umCarro);

						System.out.println('\n' + "Altera��o de tipo efetuada " + "com sucesso!");
					} catch (CarroNaoEncontradoException e) {
						System.out.println('\n' + e.getMessage());
					}

					break;
				case 3:
					String carroP = Console.readLine("Digite a nova placa: ");
					umCarro.setPlaca(carroP);

					try {
						carroAppService.altera(umCarro);

						System.out.println('\n' + "Altera��o de placa efetuada " + "com sucesso!");
					} catch (CarroNaoEncontradoException e) {
						System.out.println('\n' + e.getMessage());
					}

					break;

				default:
					System.out.println('\n' + "Op��o inv�lida!");
				}

				break;
			}


			case 3: {
				int resposta = Console.readInt('\n' + "Digite o n�mero do carro que voc� deseja remover: ");

				try {
					umCarro = carroAppService.recuperaUmCarro(resposta);
				} catch (CarroNaoEncontradoException e) {
					System.out.println('\n' + e.getMessage());
					break;
				}

				System.out.println('\n' + "N�mero = " + umCarro.getId() + "    marca = " + umCarro.getMarca()
						+ "    modelo= " + umCarro.getModelo() + "  placa= " + umCarro.getPlaca());

				String resp = Console.readLine('\n' + "Confirma a remo��o do Carro? s/n");

				if (resp.equals("s")) {
					try
					/* ==> */ {
						carroAppService.exclui(umCarro.getId());
						System.out.println('\n' + "carro removido com sucesso!");
					} catch (CarroNaoEncontradoException e) {
						System.out.println(e.getMessage());
					}
				} else {
					System.out.println('\n' + "Carro n�o removido.");
				}

				break;
			}

			case 4: {
				try {
					long resposta = Console.readInt('\n' + "Informe o n�mero da Carro: ");
					umCarro = carroAppService.recuperaUmCarroEViagens(resposta);

				} catch (CarroNaoEncontradoException e) {
					System.out.println('\n' + e.getMessage());
					break;
				}

				System.out.println('\n' + "N�mero = " + umCarro.getId() + "    marca = " + umCarro.getMarca()
						+ "    modelo = " + umCarro.getModelo() + "  placa = " + umCarro.getPlaca());

				List<Viagem> viagens = umCarro.getViagens();

				for (Viagem viagem : viagens) {
					System.out.println('\n' + "      Viagem n�mero = " + viagem.getId() + "  sa�da = "
							+ viagem.getSaida() + "  destino  = " + viagem.getDestino() + "  pre�o da gasolina = "
							+ viagem.getPreco());
				}

				break;
			}

			case 5: {
				List<Carro> carros = carroAppService.recuperaCarrosEViagens();

				if (carros.size() != 0) {

					for (Carro carro : carros) {

						System.out.println('\n' + "N�mero = " + carro.getId() + "    marca = "
								+ carro.getMarca() + "    modelo = " + carro.getModelo() + "  placa = "
								+ carro.getPlaca());

						List<Viagem> viagens = carro.getViagens();

						for (Viagem viagem : viagens) {
							System.out.println('\n' + "      Viagem n�mero = " + viagem.getId() + "  sa�da = "
									+ viagem.getSaida() + "  destino  = " + viagem.getDestino() + "  pre�o da gasolina = "
									+ viagem.getPreco());
						}
					}
				} else {
					System.out.println('\n' + "N�o h� carros cadastrados.");
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
