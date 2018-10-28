//package br.com.alura.owasp.dao;
//
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//
//import org.springframework.stereotype.Repository;
//
//import br.com.alura.owasp.model.Usuario;
//import br.com.alura.owasp.util.ConnectionFactory;
//
//@Repository
//public class UsuarioDaoImpl_JDBC implements UsuarioDao {
//
//	Connection connection = new ConnectionFactory().getConnection();
//
//	public void salva(Usuario usuario) {
//		String queryUsuario = "insert into Usuario (email,senha,nome,nomeImagem) values ('"
//				+ usuario.getEmail()
//				+ "','"
//				+ usuario.getSenha()
//				+ "','"
//				+ usuario.getNome() + "','" + usuario.getNomeImagem() + "');";
//		String queryUsuario_Role = "insert into Usuario_Role (Usuario_email, roles_name) values ('"
//				+ usuario.getEmail()
//				+ "','"
//				+ usuario.getRoles().get(0).getName() + "');";
//		try {
//			Statement statement = connection.createStatement();
//			statement.executeUpdate(queryUsuario);
//			statement.executeUpdate(queryUsuario_Role);
//		} catch (SQLException e) {
//			throw new RuntimeException(e);
//		}
//	}
//
//	public Usuario procuraUsuario(Usuario usuario) {
//		String query = "SELECT * FROM Usuario WHERE email=" + "'"
//				+ usuario.getEmail() + "'" + " and senha=" + "'"
//				+ usuario.getSenha() + "';";
//		try {
//			Statement statement = connection.createStatement();
//			ResultSet results = statement.executeQuery(query);
//			Usuario usuarioRetorno = new Usuario();
//			while (results.next()) {
//				usuarioRetorno.setEmail(results.getString("email"));
//				usuarioRetorno.setSenha(results.getString("senha"));
//				usuarioRetorno.setNomeImagem(results.getString("nomeImagem"));
//				usuarioRetorno.setNome(results.getString("nome"));
//				if(usuarioRetorno.getEmail().equals(usuario.getEmail())){
//					break;
//				}
//			}
//			if (usuarioRetorno.getEmail() == null
//					&& usuarioRetorno.getSenha() == null) {
//				return null;
//			} else {
//				return usuarioRetorno;
//			}
//		} catch (SQLException e) {
//			throw new RuntimeException(e);
//		}
//	}
//}
