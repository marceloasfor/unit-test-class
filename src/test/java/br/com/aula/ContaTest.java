package br.com.aula;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Test;

import br.com.aula.exception.NumeroContaInvalidoException;

public class ContaTest {

	@Test
	public void deveCreditar() throws NumeroContaInvalidoException {

		// Cenario
		Cliente cliente = new Cliente("João");
		Conta c = new Conta(cliente, 123, 10, TipoConta.CORRENTE);

		// Ação
		c.creditar(5);

		// Verificação
		assertEquals(15, c.getSaldo());
		assertThat(c.getSaldo(), is(15));
	}
	
	@Test(expected = NumeroContaInvalidoException.class)
	public void naoDeveCriarContaNumeroInvalido() throws NumeroContaInvalidoException {

		// Cenario
		Cliente cliente = new Cliente("João");
		Conta c = new Conta(cliente, -1, 10, TipoConta.CORRENTE);

		// Verificação
		Assert.fail();
	}

}
