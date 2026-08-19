package main.conexao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexaoFactory {

    private static final Properties props = carregarProperties();

    private static Properties carregarProperties() {
        Properties properties = new Properties();

        try (InputStream input = ConexaoFactory.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) { //<- procura o arquivo dentro da pasta "resourse"

            if (input == null) {
                throw new RuntimeException(
                        "Arquivo config.properties não encontrado em src/main/resources. " +
                                "Copie o config.properties.example, renomeie para config.properties " +
                                "e preencha com seus dados reais do Supabase."
                );
            }

            properties.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar config.properties", e);
        }

        return properties;
    }

    public static Connection criarConexao() throws SQLException {
        String url = props.getProperty("db.url");
        String usuario = props.getProperty("db.usuario");
        String senha = props.getProperty("db.senha");

        return DriverManager.getConnection(url, usuario, senha);
    }
}