package teste;

//import static org.junit.Assert.fail;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import excecao.AcimaDoPrecoViagemException;
import excecao.CarroNaoEncontradoException;
import excecao.ClienteNaoEncontradoException;
import excecao.MotoristaNaoEncontradoException;
import modelo.Carro;
import modelo.Cliente;
import modelo.Motorista;
import modelo.Viagem;
import service.ClienteAppService;
import service.MotoristaAppService;
import service.ViagemAppService;




@ContextConfiguration(locations = "/applicationContext.xml")
public class TesteJunit extends AbstractTransactionalJUnit4SpringContextTests {
	private long idCliente;

	ApplicationContext fabrica = new ClassPathXmlApplicationContext("/applicationContext.xml");

	@Autowired
	private MotoristaAppService motoristaAppService = (MotoristaAppService)fabrica.getBean ("motoristaAppService");
	@Autowired
	private ViagemAppService viagemAppService = (ViagemAppService)fabrica.getBean ("viagemAppService");

	private Cliente umCliente;
	
	private Carro umCarro;

	@Before
	public void init() {
		System.out.println("Começa o init()>>>>>>>>>>>>>>>>");
		jdbcTemplate.update("INSERT INTO cliente(nome, sobrenome,email) " + "VALUES (?, ?, ?)", "TESTE 1",
				"Tester", "testando@bla.com");
		
		idCliente = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
		
		
		umCliente = jdbcTemplate.queryForObject("SELECT * FROM cliente WHERE ID = ?", new ClienteRowMapper(),
				idCliente);
		jdbcTemplate.update("INSERT INTO motorista (nome, sobrenome,email, cliente_id) " + "VALUES (?, ?, ?,?)", "m1",
				"M", "motorista@bla.com",idCliente);
		jdbcTemplate.update("INSERT INTO motorista (nome, sobrenome,email, cliente_id) " + "VALUES (?, ?, ?,?)", "m2",
				"M", "motorista@bla.com",idCliente);
		jdbcTemplate.update("INSERT INTO motorista (nome, sobrenome,email, cliente_id) " + "VALUES (?, ?, ?,?)", "m3",
				"M", "motorista@bla.com",idCliente);
		jdbcTemplate.update("INSERT INTO motorista(nome, sobrenome,email, cliente_id) " + "VALUES (?, ?, ?,?)", "m4",
				"M", "motorista@bla.com",idCliente);
		System.out.println("Termina o init>>>>>>>>>>>>");
	}
	
	
	//se preço da viagem é acima de 500 ele lança a exceção
	//exceção de modelo de negócios
	@Test(expected=AcimaDoPrecoViagemException.class)
	public void deveImpedirPrecoAlto() throws CarroNaoEncontradoException {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		System.out.println("Começou a execução do teste 1");
		Motorista ze = new Motorista("Ze", "Silva", "ze@bla.com", umCliente);
		umCarro = new Carro("Honda", "Civic","LLL-1212",ze);
		Viagem viagem1 = new Viagem(501, "Floresta", "Leblon", umCarro);
		
			viagemAppService.inclui(viagem1);
			
		
		
	}
	//teste para quebra de constraint de BD de nome de motorista muito grande
	
	@Test(expected = DataIntegrityViolationException.class)
	public void motoristaNaoPodeTerNomeMuitoGrande() throws ClienteNaoEncontradoException{
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		System.out.println("Começou a execução do teste de violação de constraint:");
		System.out.println("Do motorista que tem um nome muito grande");
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		Motorista motorista5= new Motorista("mimimimimimimimimimimimimimimimi", "Silva", "motorista@bla.com", umCliente);
	
			
			motoristaAppService.inclui(motorista5);	
		
	}
	
		
	
	
	
	
	private class ClienteRowMapper implements RowMapper<Cliente> {
		public Cliente mapRow(ResultSet rs, int rowNum) throws SQLException {
			Cliente cliente = new Cliente();

			cliente.setId(rs.getLong("ID"));
			cliente.setNome(rs.getString("NOME"));
			cliente.setSobrenome(rs.getString("SOBRENOME"));
			cliente.setEmail(rs.getString("EMAIL"));

			return cliente;
		}
	}
	

	
}
