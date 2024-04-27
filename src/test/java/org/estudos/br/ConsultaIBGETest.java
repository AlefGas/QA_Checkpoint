package org.estudos.br;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ConsultaIBGETest {

    @Mock
    private HttpURLConnection connectionMock;


    // JSON de resposta simulada
    private static final String JSON_RESPONSE = "{\"id\":35,\"sigla\":\"SP\",\"nome\":\"São Paulo\",\"regiao\":{\"id\":3,\"sigla\":\"SE\",\"nome\":\"Sudeste\"}}";   // Método executado antes de cada teste
    private static final String DISTRITOS_API_URL = "https://servicodados.ibge.gov.br/api/v1/localidades/distritos/";
    @BeforeEach
    public void setup() throws IOException {
        // Inicializa os mocks
        MockitoAnnotations.openMocks(this);

        // Configura o comportamento do mock
        InputStream inputStream = new ByteArrayInputStream(JSON_RESPONSE.getBytes());
        when(connectionMock.getInputStream()).thenReturn(inputStream);
    }

    // Teste para verificar se o método consultarEstado retorna o JSON esperado para o estado de São Paulo
    @Test
    @DisplayName("Consulta usando o Mock")
    public void testConsultarEstadoComMock() throws IOException {
        // Sigla do estado a ser consultado
        String estadoUf = "SP";

        // Act (Execução do método a ser testado)
        String response = ConsultaIBGE.consultarEstado(estadoUf);

        // Verificamos se o JSON retornado é o mesmo que o JSON de resposta simulada
        assertEquals(JSON_RESPONSE, response, "O JSON retornado não corresponde ao esperado.");
    }

    @Test
    @DisplayName("Teste de erro do mock")
    public void testConsultarErroEstadoNaoEncontrado() throws IOException {
        // Sigla do estado a ser consultado, que não existe na API
        String estadoUf = "kl";

        // Act (Execução do método a ser testado)
        String response = ConsultaIBGE.consultarEstado(estadoUf);

        // Verificamos se o JSON retornado é difernet que o JSON de resposta simulada
        assertNotEquals(JSON_RESPONSE, response, "O JSON retornado não corresponde ao esperado.");

    }

    @Mock
    private HttpURLConnection mockConnection;
    private static final String JSON_RESPONSE2 = "{\"id\":35,\"sigla\":\"SP\",\"nome\":\"São Paulo\",\"regiao\":{\"id\":3,\"sigla\":\"SE\",\"nome\":\"Sudeste\"}}";
    @BeforeEach
    public void setUp() throws Exception {
        // Inicializa os mocks
        MockitoAnnotations.openMocks(this);

        // Configura o comportamento do mock
        InputStream inputStream = new ByteArrayInputStream(JSON_RESPONSE.getBytes());
        when(connectionMock.getInputStream()).thenReturn(inputStream);
    }


    @ParameterizedTest
    @ValueSource(ints = {5682, 2408369, 53415, 2933307, 320662})
    @DisplayName("Teste consultando distritros não existentes")
    public void testConsultarMultiplosDistritos(int id_Distrito) throws IOException {
        // Consulta informações do distrito com os identificadores fornecidos
        String resposta = ConsultaIBGE.consultarDistrito(id_Distrito);

        // Verifica se a resposta não está vazia
        assert !resposta.isEmpty() : "A resposta não deve estar vazia para o id do distrito " + id_Distrito;

        // Verifica se o status code é 200 (OK)
        HttpURLConnection connection = (HttpURLConnection) new URL(DISTRITOS_API_URL + id_Distrito).openConnection();
        int statusCode = connection.getResponseCode();
        assertNotEquals(400, statusCode, "O status code deve ser 400 para o id do distrito " + id_Distrito);
    }


}