package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Consultas {

    // Método para cargar los productos desde la base de datos y mostrarlos en la tabla
    public void cargarProductos(Connection conexion, DefaultTableModel modelo) {
        try {
            String sql = "SELECT * FROM producto";
            PreparedStatement sentencia = conexion.prepareStatement(sql);
            ResultSet resultado = sentencia.executeQuery();

            // Limpia la tabla antes de cargar nuevos datos
            modelo.setRowCount(0);

            // Llena la tabla con los datos de la base de datos
            while (resultado.next()) {//resultado.next trae una fila completa de la tabla 
                String codigo = resultado.getString("codigo");
                String nombre = resultado.getString("nombre");
                double precio = resultado.getDouble("precio");
                int cantidad = resultado.getInt("cantidad");
                modelo.addRow(new Object[]{codigo, nombre, precio, cantidad});
            }

            // Cierra el statement y el result set
            sentencia.close();
            resultado.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para buscar un producto por su código en la base de datos
    public boolean buscarProducto(Connection conexion, String codigoProducto, DefaultTableModel modelo) {
        try {
            String sql = "SELECT * FROM producto WHERE codigo = ?";
            PreparedStatement sentencia = conexion.prepareStatement(sql);
            sentencia.setString(1, codigoProducto);
            ResultSet resultado = sentencia.executeQuery();

            // Limpia la tabla antes de cargar nuevos datos
            modelo.setRowCount(0);

            // Llena la tabla con los datos del producto encontrado (si existe)
            boolean productoEncontrado = false;
            while (resultado.next()) {
                String codigo = resultado.getString("codigo");
                String nombre = resultado.getString("nombre");
                double precio = resultado.getDouble("precio");
                int cantidad = resultado.getInt("cantidad");
                modelo.addRow(new Object[]{codigo, nombre, precio, cantidad});
                productoEncontrado = true;
            }

            // Cierra el statement y el result set
            sentencia.close();
            resultado.close();

            return productoEncontrado;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para verificar si un registro con el código especificado existe en la base de datos
    public boolean verificarExistenciaRegistro(Connection conexion, String codigo) {
        try {
            String sql = "SELECT * FROM producto WHERE codigo = ?";
            PreparedStatement sentencia = conexion.prepareStatement(sql);
            sentencia.setString(1, codigo);
            ResultSet resultado = sentencia.executeQuery();

            // Verifica si se encontró algún registro con el código especificado
            boolean existeRegistro = resultado.next();

            // Cierra el statement y el result set
            sentencia.close();
            resultado.close();

            return existeRegistro;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para eliminar un registro de la base de datos por su código
    public boolean eliminarRegistro(Connection conexion, String codigo, DefaultTableModel modelo) {
        try {
            String sql = "DELETE FROM producto WHERE codigo = ?";
            PreparedStatement sentencia = conexion.prepareStatement(sql);
            sentencia.setString(1, codigo);

            // Ejecuta la consulta para eliminar el registro
            int filasAfectadas = sentencia.executeUpdate();

            // Cierra el statement
            sentencia.close();

            // Actualiza la tabla si se eliminó el registro correctamente
            if (filasAfectadas > 0) {
                cargarProductos(conexion, modelo);
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para guardar los cambios realizados en la tabla en la base de datos
    public void guardarCambios(Connection conexion, DefaultTableModel modelo) {
        try {
            // Obtener el número de filas en el modelo
            int cantidadFilas = modelo.getRowCount();

            // Verifica si se han realizado cambios en la tabla
            if (cantidadFilas == 0) {
                JOptionPane.showMessageDialog(null, "No hay cambios pendientes.");
                return; // No hay cambios, salir del método
            }

            // Recorre cada fila del modelo para guardar los cambios
            for (int i = 0; i < cantidadFilas; i++) {
                String codigo = (String) modelo.getValueAt(i, 0);
                String nombre = (String) modelo.getValueAt(i, 1);
                double precio = (Double) modelo.getValueAt(i, 2);
                int cantidad = (Integer) modelo.getValueAt(i, 3);

                // Verificar si el código existe en la base de datos antes de actualizar el registro
                boolean existeRegistro = verificarExistenciaRegistro(conexion, codigo);
                if (existeRegistro) {
                    int confirmacion = JOptionPane.showConfirmDialog(null, "El código ingresado ya existe en la base de datos. ¿Desea sobrescribir los datos del registro existente?", "Confirmar actualización", JOptionPane.YES_NO_OPTION);
                    if (confirmacion == JOptionPane.NO_OPTION) {
                        continue; // Saltar esta iteración y pasar al siguiente registro
                    }
                }

                // Actualizar el registro en la base de datos
                String sql = "UPDATE producto SET nombre = ?, precio = ?, cantidad = ? WHERE codigo = ?";
                PreparedStatement sentencia = conexion.prepareStatement(sql);
                sentencia.setString(1, nombre);
                sentencia.setDouble(2, precio);
                sentencia.setInt(3, cantidad);
                sentencia.setString(4, codigo);

                // Ejecutar la consulta de actualización
                sentencia.executeUpdate();

                // Cerrar el statement
                sentencia.close();
            }

            // Actualizar la tabla después de guardar los cambios en la base de datos
            cargarProductos(conexion, modelo);

            // Mostrar mensaje de éxito
            JOptionPane.showMessageDialog(null, "Cambios guardados exitosamente.");

        } catch (SQLException e) { // Capturar cualquier excepción
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al guardar los cambios: " + e.getMessage());
        }
    }

    // Método para agregar un nuevo registro a la base de datos
    public boolean agregarRegistro(Connection conexion, String codigo, String nombre, double precio, int cantidad) {
        try {
            // Verificar si el código ya existe en la base de datos
            boolean existeRegistro = verificarExistenciaRegistro(conexion, codigo);
            if (existeRegistro) {
                JOptionPane.showMessageDialog(null, "El código ingresado ya existe en la base de datos. Por favor, ingresa un código diferente.");
                return false;
            }

            // Insertar el nuevo registro en la base de datos
            String sql = "INSERT INTO producto (codigo, nombre, precio, cantidad) VALUES (?, ?, ?, ?)";
            PreparedStatement sentencia = conexion.prepareStatement(sql);
            sentencia.setString(1, codigo);
            sentencia.setString(2, nombre);
            sentencia.setDouble(3, precio);
            sentencia.setInt(4, cantidad);

            int filasAfectadas = sentencia.executeUpdate();

            // Cerrar el statement
            sentencia.close();

            // Retornar true si se insertó el registro correctamente
            return filasAfectadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al intentar agregar el registro: " + e.getMessage());
            return false;
        }
    }

    // Método para actualizar un dato específico en la base de datos para un registro dado
    public void actualizarDatoEnBD(Connection conexion, String codigo, String nuevoCodigo, int columna, String nuevoValor) {
        try {
            String columnaBD = null;
            switch (columna) {
                case 0: // La columna 0 es el código
                    columnaBD = "codigo";
                    break;
                case 1: // Si la columna es la de nombre
                    columnaBD = "nombre";
                    break;
                case 2: // Si la columna es la de precio
                    columnaBD = "precio";
                    break;
                case 3: // Si la columna es la de cantidad
                    columnaBD = "cantidad";
                    break;
                default:
                    // No hacemos nada si la columna no es válida
                    return;
            }
            
            // Verifica si el nuevo código ya está en uso
            if (!codigo.equals(nuevoCodigo) && verificarExistenciaRegistro(conexion, nuevoCodigo)) {
                JOptionPane.showMessageDialog(null, "El código ingresado ya está en uso. Por favor, ingresa un código diferente.");
                return; // Salir del método sin actualizar el registro
            }

            // Actualizar el registro en la base de datos
            String sql = "UPDATE producto SET " + columnaBD + " = ? WHERE codigo = ?";
            PreparedStatement sentencia = conexion.prepareStatement(sql);
            sentencia.setString(1, nuevoValor);
            sentencia.setString(2, codigo);
            sentencia.executeUpdate();
            sentencia.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}