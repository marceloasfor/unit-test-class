package br.com.aula;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import br.com.aula.exception.ContaJaExistenteException;
import br.com.aula.exception.ContaNaoExistenteException;
import br.com.aula.exception.ContaSemSaldoException;
import br.com.aula.exception.NaoTransfereValorNegativoException;
import br.com.aula.exception.NumeroContaInvalidoException;

public class BancoTest {

	@Test
	public void deveCadastrarConta() throws ContaJaExistenteException, NumeroContaInvalidoException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta conta = new Conta(cliente, 123, 0, TipoConta.CORRENTE);
		Banco banco = new Banco();

		// Ação
		banco.cadastrarConta(conta);

		// Verificação
		assertEquals(1, banco.obterContas().size());
	}

	@Test(expected = ContaJaExistenteException.class)
	public void naoDeveCadastrarContaNumeroRepetido() throws ContaJaExistenteException, NumeroContaInvalidoException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta conta1 = new Conta(cliente, 123, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta conta2 = new Conta(cliente2, 123, 0, TipoConta.POUPANCA);

		Banco banco = new Banco();

		// Ação
		banco.cadastrarConta(conta1);
		banco.cadastrarConta(conta2);

		Assert.fail();
	}

	@Test(expected = NumeroContaInvalidoException.class)
	public void naoDeveCadastrarContaNumeroInvalido() throws NumeroContaInvalidoException, ContaJaExistenteException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta conta = new Conta(cliente, -12, 0, TipoConta.CORRENTE);
		Banco banco = new Banco();

		// Ação
		banco.cadastrarConta(conta);

		// Verificação
		Assert.fail();
	}
	
	@Test(expected = ContaJaExistenteException.class)
	public void naoDeveCadastrarContaClienteRepetido() throws NumeroContaInvalidoException, ContaJaExistenteException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta conta = new Conta(cliente, 123, 0, TipoConta.CORRENTE);
		Banco banco = new Banco();
		
		Cliente cliente2 = new Cliente("Joao");
		Conta conta2 = new Conta(cliente2, 456, 0, TipoConta.CORRENTE);

		// Ação
		banco.cadastrarConta(conta);
		banco.cadastrarConta(conta2);

		// Verificação
		Assert.fail();
	}

	@Test
	public void deveEfetuarTransferenciaContasCorrentes() throws ContaSemSaldoException, ContaNaoExistenteException, NumeroContaInvalidoException, NaoTransfereValorNegativoException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 100, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 0, TipoConta.CORRENTE);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));

		// Ação
		banco.efetuarTransferencia(123, 456, 100);

		// Verificação
		assertEquals(0, contaOrigem.getSaldo());
		assertEquals(100, contaDestino.getSaldo());
	}
	
	@Test
	public void deveEfetuarTransferenciaContaCorrentePoupanca() throws ContaSemSaldoException, ContaNaoExistenteException, NumeroContaInvalidoException, NaoTransfereValorNegativoException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 100, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 0, TipoConta.POUPANCA);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));

		// Ação
		banco.efetuarTransferencia(123, 456, 100);

		// Verificação
		assertEquals(0, contaOrigem.getSaldo());
		assertEquals(100, contaDestino.getSaldo());
	}
	
	@Test(expected = ContaNaoExistenteException.class)
	public void naoExisteContaDeOrigem() throws ContaSemSaldoException, ContaNaoExistenteException, NumeroContaInvalidoException, NaoTransfereValorNegativoException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 100, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 0, TipoConta.CORRENTE);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));

		// Ação
		banco.efetuarTransferencia(111, 456, 100);

		// Verificação
		Assert.fail();
	}
	
	@Test(expected = ContaSemSaldoException.class)
	public void naoPermitePoupancaNegativa() throws ContaSemSaldoException, ContaNaoExistenteException, NumeroContaInvalidoException, NaoTransfereValorNegativoException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 0, TipoConta.POUPANCA);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 0, TipoConta.CORRENTE);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));

		// Ação
		banco.efetuarTransferencia(123, 456, 100);

		// Verificação
		Assert.fail();
	}
	
	@Test(expected = ContaNaoExistenteException.class)
	public void naoExisteContaDeDestino() throws ContaSemSaldoException, ContaNaoExistenteException, NumeroContaInvalidoException, NaoTransfereValorNegativoException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 100, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 0, TipoConta.CORRENTE);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));

		// Ação
		banco.efetuarTransferencia(123, 444, 100);

		// Verificação
		Assert.fail();
	}
	
	@Test(expected = NaoTransfereValorNegativoException.class)
	public void naoTransfereValorNegativo() throws ContaSemSaldoException, ContaNaoExistenteException, NumeroContaInvalidoException, NaoTransfereValorNegativoException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 100, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 0, TipoConta.CORRENTE);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));

		// Ação
		banco.efetuarTransferencia(123, 456, -100);

		// Verificação
		Assert.fail();
	}
}
