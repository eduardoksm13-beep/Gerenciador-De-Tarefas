package main;

//Classe para testar a conexão com o banco de dados
public class TesteConexao {
    public static void main(String[] args) {
        try (var conn = main.conexao.ConexaoFactory.criarConexao()) {
            System.out.println("Conexão bem-sucedida! " + conn.getCatalog());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
