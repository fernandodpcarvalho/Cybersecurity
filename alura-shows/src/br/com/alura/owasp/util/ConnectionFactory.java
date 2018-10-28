package br.com.alura.owasp.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {

	public Connection getConnection() {
		try {
			return DriverManager.getConnection("jdbc:mysql://localhost/owasp?createDatabaseIfNotExist=true",
					"fernando", "fernando");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
