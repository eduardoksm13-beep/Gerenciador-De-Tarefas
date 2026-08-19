package main.dao;
import main.conexao.ConexaoFactory;
import main.model.Tarefa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

//Essa classe possui todos os métodos que manipulam a tabela tarefas.
public class TarefaDAO {
    public Tarefa salvar(Tarefa tarefa) throws SQLException {
        String sql = "INSERT INTO tarefas (titulo, descricao, usuarios_id) VALUES (?, ?, ?)"; //<- Script SQL. Os "?" são onde será inseridos os parametros.

        try (Connection conn = ConexaoFactory.criarConexao(); //<-Cria uma conexão com o banco de dados
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            //prepareStatement() -> Essa função  preparar uma instrução SQL para execução.
            //Statement.RETURN_GENERATED_KEYS -> após execultar o "INSERT", o JDBC retorna a chave(id) gerada automaticamente pelo banco

            stmt.setString(1, tarefa.getTitulo()); // <- 1º Parâmetro
            stmt.setString(2, tarefa.getDescricao()); // <- 2º Parâmetro
            stmt.setLong(3, tarefa.getUsuarioId());// <- 3º Parâmetro. OBS: setLong recebe um ID

            stmt.executeUpdate(); //<-Executa comandos que atualiza o banco (INSERT, UPDATE, DELETE)

            try (ResultSet rs = stmt.getGeneratedKeys()) { // getGeneratedKeys() -> Retorna o ID gerado automaticamente pelo banco.
                if (rs.next()) {
                    tarefa.setId(rs.getLong(1)); //<- Atribui a tarefa o ID gerado pelo banco de dados
                }
            }
        }

        return tarefa;
    }

    // Lista as tarefas de um usuário específico
    public List<Tarefa> listarPorUsuario(Long usuarioId) throws SQLException {
        String sql = "SELECT id, titulo, descricao, concluida, data_criacao, usuarios_id " +
                "FROM tarefas WHERE usuario_id = ? ORDER BY id";

        List<Tarefa> tarefas = new ArrayList<>();

        try (Connection conn = ConexaoFactory.criarConexao(); //<-Cria uma conexão com o banco de dados
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, usuarioId); //<- Define os parâmentros

            try (ResultSet rs = stmt.executeQuery()) { // excuteQuery() <- Executa um SELECT e retorna um ResultSet.
                while (rs.next()) {
                    tarefas.add(mapearTarefa(rs));
                }
            }
        }

        return tarefas;
    }

    // Lista todas as tarefas do sistema, já trazendo o nome do usuário via JOIN
    public List<Tarefa> listarTodasComUsuario() throws SQLException {
        String sql = "SELECT t.id, t.titulo, t.descricao, t.concluida, t.data_criacao, " +
                "t.usuarios_id, u.name AS nome_usuario " +
                "FROM tarefas t " +
                "JOIN usuarios u ON u.id = t.usuarios_id " +
                "ORDER BY t.id";

        List<Tarefa> tarefas = new ArrayList<>();

        try (Connection conn = ConexaoFactory.criarConexao();  //<-Cria uma conexão com o banco de dados

             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) { // excuteQuery() <- Executa um SELECT e retorna um ResultSet.

            while (rs.next()) {
                Tarefa tarefa = mapearTarefa(rs);
                tarefa.setNomeUsuario(rs.getString("nome_usuario"));
                tarefas.add(tarefa);
            }
        }

        return tarefas;
    }

    public void marcarComoConcluida(Long id) throws SQLException {
        String sql = "UPDATE tarefas SET concluida = TRUE WHERE id = ?";

        try (Connection conn = ConexaoFactory.criarConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    public void deletar(Long id) throws SQLException {
        String sql = "DELETE FROM tarefas WHERE id = ?";

        try (Connection conn = ConexaoFactory.criarConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate(); //<- Executa comandos que alteram o banco (INSERT, UPDATE, DELETE)
        }
    }

    private Tarefa mapearTarefa(ResultSet rs) throws SQLException {
        Timestamp ts = rs.getTimestamp("data_criacao");

        return new Tarefa(
                rs.getLong("id"),
                rs.getString("titulo"),
                rs.getString("descricao"),
                rs.getBoolean("concluida"),
                ts != null ? ts.toLocalDateTime() : null,
                rs.getLong("usuarios_id")
        );
    }



}
