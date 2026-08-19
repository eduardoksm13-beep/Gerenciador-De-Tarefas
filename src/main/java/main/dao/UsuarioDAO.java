package main.dao;



import main.conexao.ConexaoFactory;
import main.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public Usuario salvar(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (name, email) VALUES (?, ?)";

        try (Connection conn = ConexaoFactory.criarConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());

            stmt.executeUpdate();


            try (ResultSet rs = stmt.getGeneratedKeys()) {  // <- Retorna o ID gerado automaticamente pelo banco (SERIAL)
                if (rs.next()) {
                    usuario.setId(rs.getLong(1)); //<- Atribui ao usuario um ID gerado no banco de dados
                }
            }
        }

        return usuario;
    }

    public List<Usuario> listarTodos() throws SQLException {
        String sql = "SELECT id, name, email FROM usuarios ORDER BY id";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conn = ConexaoFactory.criarConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
        }

        return usuarios;
    }

    public Usuario buscarPorId(Long id) throws SQLException {
        String sql = "SELECT id, name, email FROM usuarios WHERE id = ?";

        try (Connection conn = ConexaoFactory.criarConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        }

        return null;
    }
    public Usuario buscaPorEmail(String email) throws SQLException{
        String sql = "SELECT id, name, email FROM usuarios WHERE email = ?";

        try (Connection conn = ConexaoFactory.criarConexao();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
        try (ResultSet rs = stmt.executeQuery()){  // excuteQuery() <- Executa um SELECT e retorna um ResultSet.
            if(rs.next()){
                return mapearUsuario(rs);
                }

            }
        }
        return null;
    }
    //Metodo para saber se um email já foi cadastrado:
    public boolean emailJaCadastrado(String email)throws  SQLException{
        return buscaPorEmail(email) != null;
    }

    public void atualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET name = ?, email = ? WHERE id = ?";

        try (Connection conn = ConexaoFactory.criarConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setLong(3, usuario.getId());

            stmt.executeUpdate();
        }
    }

    public void deletar(Long id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (Connection conn = ConexaoFactory.criarConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    // Metodo auxiliar para não repetir a conversão ResultSet -> Usuario em todo lugar
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        return new Usuario(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email")
        );
    }
}

