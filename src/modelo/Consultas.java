package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import controlador.Conexion;

public class Consultas {

    // Método para cargar todos los productos desde la base de datos y mostrarlos en la tabla
    public static void cargarProductos(Connection connection, DefaultTableModel model) {
        try {
            // Consulta SQL para seleccionar todos los productos
            String consulta = "SELECT codigo, nombre, precio, cantidad FROM producto";
            // Prepara la consulta
            PreparedStatement ps = connection.prepareStatement(consulta);
            // Ejecuta la consulta y obtener los resultados
            ResultSet rs = ps.executeQuery();
            // Carga los resultados en la tabla
            cargarTabla(rs, model);
        } catch (SQLException e) {
            // Capturar y manejar cualquier excepción SQL que ocurra
            System.out.println("Error al cargar productos: " + e);
        }
    }

    // Método para buscar un producto por nombre y mostrarlo en la tabla
    public static void buscarProducto(Connection connection, String nombre, DefaultTableModel model) {
        if (!nombre.isEmpty()) {
            try {
                // Consulta SQL para buscar un producto por su nombre
                String consulta = "SELECT codigo, nombre, precio, cantidad FROM producto WHERE nombre=?";
                // Preparar la consulta con un parámetro (nombre del producto)
                PreparedStatement ps = connection.prepareStatement(consulta);
                ps.setString(1, nombre); // Establecer el valor del parámetro
                // Ejecutar la consulta y obtener los resultados
                ResultSet rs = ps.executeQuery();
                // Cargar los resultados en la tabla
                cargarTabla(rs, model);
            } catch (SQLException e) {
                // Capturar y manejar cualquier excepción SQL que ocurra
                System.out.println("Error al buscar producto: " + e);
            }
        }
    }

    // Método privado para cargar los resultados de la consulta en la tabla
    private static void cargarTabla(ResultSet rs, DefaultTableModel model) throws SQLException {
        // Limpia el modelo de la tabla antes de agregar nuevos datos
        model.setRowCount(0);
        // Obtiene metadatos de los resultados para obtener el número de columnas
        ResultSetMetaData rsMd = rs.getMetaData();
        int cantidadColumnas = rsMd.getColumnCount();
        // Recorre los resultados y agregar cada fila a la tabla
        while (rs.next()) {
            Object[] fila = new Object[cantidadColumnas];
            // Llenar la fila con los datos de cada columna
            for (int i = 0; i < cantidadColumnas; i++) {
                fila[i] = rs.getObject(i + 1);
            }
            // Agregar la fila al modelo de la tabla
            model.addRow(fila);
        }
    }

    // Método para verificar si el registro existe en la base de datos, con el código que le llega por parámetro
    public static boolean verificarExistenciaRegistro(Connection connection, String codigo) {
        try {
            // Consulta SQL para verificar la existencia del registro con el código indicado
            String consulta = "SELECT COUNT(*) FROM producto WHERE codigo=?";
            // Prepara la consulta con un parámetro (código del producto)
            PreparedStatement ps = connection.prepareStatement(consulta);
            ps.setString(1, codigo); // Establecer el valor del parámetro
            // Ejecuta la consulta y obtiene los resultados
            ResultSet rs = ps.executeQuery();
            // Obtiene el resultado del conteo
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; // Devolver true si el conteo es mayor que cero (el registro existe)
            }
        } catch (SQLException e) {
            // Capturar y manejar cualquier excepción SQL que ocurra
            System.out.println("Error al verificar existencia del registro: " + e);
        }
        return false; // Devolver false si ocurre algún error o si el conteo es cero (el registro no existe)
    }

    // Método para eliminar un registro de la base de datos y limpiar la tabla
    public static boolean eliminarRegistro(Connection connection, String codigo, DefaultTableModel model) {
        try {
            // Consulta SQL para eliminar el registro con el código proporcionado
            String consulta = "DELETE FROM producto WHERE codigo=?";
            // Preparar la consulta con un parámetro (código del producto)
            PreparedStatement ps = connection.prepareStatement(consulta);
            ps.setString(1, codigo); // Establecer el valor del parámetro
            // Ejecutar la consulta
            int filasAfectadas = ps.executeUpdate();
            // Comprobar si se eliminó correctamente (si se afectó una fila)
            if (filasAfectadas > 0) {
                // Limpiar la tabla
                model.setRowCount(0);
                return true; // Se eliminó exitosamente
            }
        } catch (SQLException e) {
            // Capturar y manejar cualquier excepción SQL que ocurra
            System.out.println("Error al eliminar registro: " + e);
        }
        return false; // No se pudo eliminar el registro
    }

    // Método para agregar un registro nuevo a la tabla
    public static boolean agregarRegistro(Connection connection, String codigo, String nombre, double precio,
            int cantidad) {
        try {
            // Consulta SQL para insertar un nuevo registro
            String consulta = "INSERT INTO producto (codigo, nombre, precio, cantidad) VALUES (?, ?, ?, ?)";
            // Preparar la consulta con los parámetros (código, nombre, precio, cantidad)
            PreparedStatement ps = connection.prepareStatement(consulta);
            ps.setString(1, codigo);
            ps.setString(2, nombre);
            ps.setDouble(3, precio);
            ps.setInt(4, cantidad);
            // Ejecutar la consulta
            int filasAfectadas = ps.executeUpdate();
            // Comprobar si se agregó correctamente (si se afectó una fila)
            if (filasAfectadas > 0) {
                return true; // Se agregó exitosamente
            }
        } catch (SQLException e) {
            // Capturar y manejar cualquier excepción SQL que ocurra
            System.out.println("Error al agregar registro: " + e);
        }
        return false; // No se pudo agregar el registro
    }
}