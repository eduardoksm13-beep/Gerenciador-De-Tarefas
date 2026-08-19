package main;

import main.dao.TarefaDAO;
import main.dao.UsuarioDAO;
import main.model.Tarefa;
import main.model.Usuario;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private static final TarefaDAO tarefaDAO = new TarefaDAO();

    public static void main(String[] args) {
        int opcao;

        do {
            exibirMenu();
            opcao = lerInteiro("Escolha uma opção: ");

            try {
                switch (opcao) {
                    case 1 -> cadastrarUsuario();
                    case 2 -> listarUsuarios();
                    case 3 -> cadastrarTarefa();
                    case 4 -> listarTarefasDeUsuario();
                    case 5 -> listarTodasTarefas();
                    case 6 -> marcarTarefaConcluida();
                    case 7 -> deletarTarefa();
                    case 8 -> deletarUsuario();
                    case 9 -> atualizaUsuario();
                    case 0 -> System.out.println("Saindo... até mais!");
                    default -> System.out.println("Opção inválida.");
                }
            } catch (SQLException e) {
                System.out.println("Erro ao acessar o banco de dados: " + e.getMessage());
            }

            System.out.println();
        } while (opcao != 0);

        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("=== Gerenciador de Tarefas ===");
        System.out.println("1. Cadastrar usuário");
        System.out.println("2. Listar usuários");
        System.out.println("3. Cadastrar tarefa");
        System.out.println("4. Listar tarefas de um usuário");
        System.out.println("5. Listar todas as tarefas (com nome do usuário)");
        System.out.println("6. Marcar tarefa como concluída");
        System.out.println("7. Deletar tarefa");
        System.out.println("8. Deletar usuário");
        System.out.println("9. Atulizar usuário");
        System.out.println("0. Sair");
    }

    private static void cadastrarUsuario() throws SQLException {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();

        if(usuarioDAO.emailJaCadastrado(email)){
            System.out.println("Erro: já existe um usuário com esse email!");
            System.out.println("Teste outro email.");
            return;

        }

        Usuario usuario = new Usuario(nome, email);
        usuarioDAO.salvar(usuario);

        System.out.println("Usuário cadastrado com sucesso! ID: " + usuario.getId());
    }

    private static void listarUsuarios() throws SQLException {
        List<Usuario> usuarios = usuarioDAO.listarTodos();

        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
            return;
        }

        usuarios.forEach(System.out::println);
    }
    private static void deletarUsuario() throws  SQLException{
        Long usuarioId = (long) lerInteiro("Informe o ID do usuário que deseja deletar: ");

        Usuario usuarioDelete = usuarioDAO.buscarPorId(usuarioId);
        if (usuarioDelete == null){
            System.out.println("Id do usuário não encontrado! ");
        }else {
            usuarioDAO.deletar(usuarioId);
            System.out.println("Usuário deletado com sucesso");
        }

    }
    private  static void atualizaUsuario()throws SQLException{
        Long usuarioId = (long) lerInteiro("Informe o ID do usuário que deseja atualizar os dados: ");
        Usuario usuarioUpdate = usuarioDAO.buscarPorId(usuarioId);

        if (usuarioUpdate == null) {
            System.out.println("Id do usuário não encontrado!");
            return;
        }

        System.out.println("Dados atuais:");
        System.out.println("Nome: " + usuarioUpdate.getNome());
        System.out.println("Email: " + usuarioUpdate.getEmail());
        System.out.println("-----------------------------------------");

        System.out.print("Novo nome (Enter para manter o atual): ");
        String novoNome = scanner.nextLine();

        if (novoNome.isBlank()) { //<- Se estiver em branco é reutilizado o nome ja cadastrado no banco de dados
            novoNome = usuarioUpdate.getNome();
        }

        System.out.print("Novo email (Enter para manter o atual): ");
        String novoEmail = scanner.nextLine();
        if (novoEmail.isBlank()) { //<- Se estiver em branco é reutilizado o email ja cadastrado no banco de dados
            novoEmail = usuarioUpdate.getEmail();
        }

        // Só valida duplicidade se o email realmente mudou
        boolean emailMudou = !novoEmail.equals(usuarioUpdate.getEmail());

        if (emailMudou && usuarioDAO.emailJaCadastrado(novoEmail)) {
            System.out.println("Erro: o novo email já pertence a outro usuário!");
            System.out.println("Tente outro.");
            return;
        }

        // Update (agora só existe em um lugar)
        usuarioUpdate.setNome(novoNome);
        usuarioUpdate.setEmail(novoEmail);
        usuarioDAO.atualizar(usuarioUpdate);

        System.out.println("------------------------------------------");
        System.out.println("Dados atualizados com sucesso!");
        System.out.println("Novo nome: " + usuarioUpdate.getNome());
        System.out.println("Novo email: " + usuarioUpdate.getEmail());

    }

    private static void cadastrarTarefa() throws SQLException {
        Long usuarioId = (long) lerInteiro("ID do usuário responsável pela tarefa: ");

        Usuario usuario = usuarioDAO.buscarPorId(usuarioId);
        if (usuario == null) {
            System.out.println("Usuário não encontrado.");
            return;
        }

        System.out.print("Título da tarefa: ");
        String titulo = scanner.nextLine();
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();

        Tarefa tarefa = new Tarefa(titulo, descricao, usuarioId);
        tarefaDAO.salvar(tarefa);

        System.out.println("Tarefa cadastrada com sucesso! ID: " + tarefa.getId());
    }

    private static void listarTarefasDeUsuario() throws SQLException {
        Long usuarioId = (long) lerInteiro("ID do usuário: ");

        List<Tarefa> tarefas = tarefaDAO.listarPorUsuario(usuarioId);

        if (tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa encontrada para esse usuário.");
            return;
        }

        tarefas.forEach(System.out::println);
    }

    private static void listarTodasTarefas() throws SQLException {
        List<Tarefa> tarefas = tarefaDAO.listarTodasComUsuario();

        if (tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa cadastrada.");
            return;
        }

        tarefas.forEach(System.out::println);
    }

    private static void marcarTarefaConcluida() throws SQLException {
        Long tarefaId = (long) lerInteiro("ID da tarefa a marcar como concluída: ");
        tarefaDAO.marcarComoConcluida(tarefaId);
        System.out.println("Tarefa atualizada com sucesso!");
    }

    private static void deletarTarefa() throws SQLException {
        Long tarefaId = (long) lerInteiro("ID da tarefa a deletar: ");
        tarefaDAO.deletar(tarefaId);
        System.out.println("Tarefa deletada com sucesso!");
    }

    private static int lerInteiro(String mensagem) {
        System.out.print(mensagem);
        while (!scanner.hasNextInt()) {
            System.out.print("Digite um número válido: ");
            scanner.next();
        }
        int valor = scanner.nextInt();
        scanner.nextLine(); // limpa o buffer de nova linha
        return valor;
    }
}
