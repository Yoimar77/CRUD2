// Clase Conexion en el paquete controlador
package controlador;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private String username;
    private String password;
    private String baseDeDatos;

    // Constructor que acepta los parámetros necesarios
    public Conexion(String username, String password, String baseDeDatos) {
        this.username = username;
        this.password = password;
        this.baseDeDatos = baseDeDatos;
    }

    // Método para obtener la conexión a la base de datos
    public Connection obtenerConexion() throws SQLException {
        Connection connection = null;
        try {
            // Cargar el controlador JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Construir la URL completa concatenando la URL base y el nombre de la base de datos
            String url ="jdbc:mysql://localhost:3306";
            String urlCompleta = url + "/" + baseDeDatos;
            // Establecer la conexión utilizando DriverManager
            connection = DriverManager.getConnection(urlCompleta, username, password);
        } catch (ClassNotFoundException e) {
            // Manejar la excepción en caso de que no se encuentre el controlador JDBC
            JOptionPane.showMessageDialog(null, "Error: No se encontró el controlador JDBC.");
            throw new SQLException("Controlador JDBC no encontrado", e);
        } catch (SQLException e) {
            // Manejar la excepción en caso de que no se pueda establecer la conexión
            JOptionPane.showMessageDialog(null, "Error: No se pudo establecer la conexión con la base de datos.");
            throw e;
        }
        // Retornar la conexión obtenida
        return connection;
    }
}