// Clase datosAdministrador en el paquete vista
package vista;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import controlador.Conexion;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;

public class DatosAdministrador extends JFrame implements ActionListener {

    private JPanel contentPane;
    private JTextField textFieldUsuario;
    private JPasswordField passwordFieldContraseña;
    private JTextField textFieldBaseDeDatos;
    private JButton btnAceptar,btnSalir;

    public DatosAdministrador() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 440, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        

		setTitle("ingresar datos");
		setResizable(false);
		setLocationRelativeTo(null);

        setContentPane(contentPane);
        contentPane.setLayout(null);

        textFieldUsuario = new JTextField();
        textFieldUsuario.setBounds(212, 49, 176, 20);
        contentPane.add(textFieldUsuario);
        textFieldUsuario.setColumns(10);

        passwordFieldContraseña = new JPasswordField();
        passwordFieldContraseña.setBounds(212, 80, 176, 20);
        contentPane.add(passwordFieldContraseña);

        JLabel lblNewLabel = new JLabel("USUARIO:");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblNewLabel.setBounds(132, 52, 83, 14);
        contentPane.add(lblNewLabel);

        JLabel lblContrasea = new JLabel("CONTRASEÑA:");
        lblContrasea.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblContrasea.setBounds(122, 80, 93, 14);
        contentPane.add(lblContrasea);

        JLabel lblBaseDeDatos = new JLabel("BASE DE DATOS: ");
        lblBaseDeDatos.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblBaseDeDatos.setBounds(109, 114, 106, 14);
        contentPane.add(lblBaseDeDatos);

        textFieldBaseDeDatos = new JTextField();
        textFieldBaseDeDatos.setColumns(10);
        textFieldBaseDeDatos.setBounds(212, 111, 176, 20);
        contentPane.add(textFieldBaseDeDatos);

        btnAceptar = new JButton("ACEPTAR");
        btnAceptar.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnAceptar.setBounds(183, 205, 89, 23);
        // para que el boton pueda ser escuchado
        btnAceptar.addActionListener(this);
        btnAceptar.setBackground(Color.green);
        contentPane.add(btnAceptar);
        
        btnSalir = new JButton("SALIR");
        btnSalir.setBounds(0, 0, 89, 23);
        // para que el boton pueda ser escuchado
        btnSalir.addActionListener(this);
        btnSalir.setBackground(Color.red);
        contentPane.add(btnSalir);
        
        JLabel lblNewLabel_1 = new JLabel("New label");
        lblNewLabel_1.setIcon(new ImageIcon(DatosAdministrador.class.getResource("/imagenes/fondoBlanco.jpg")));
        lblNewLabel_1.setBounds(0, 0, 424, 261);
        contentPane.add(lblNewLabel_1);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (btnAceptar == e.getSource()) {
            try {
                // Obtener los valores de usuario, contraseña y nombre de la base de datos
                String usuario = textFieldUsuario.getText();
                String contraseña = new String(passwordFieldContraseña.getPassword());
                String baseDeDatos = textFieldBaseDeDatos.getText();

                // Crear una instancia de la conexión
                Conexion conexion = new Conexion(usuario, contraseña, baseDeDatos);

                // Obtener la conexión de la clase conexion
                Connection connection = conexion.obtenerConexion();

                // Si se obtiene la conexión, abrir la ventana Tabla
                if (connection != null) {
                    Tabla tabla = new Tabla(usuario, contraseña, baseDeDatos);
                    tabla.setVisible(true);

                    // Cerrar esta ventana DatosAdministrador
                    dispose();
                } else {
                    // Si no se puede establecer la conexión, mostrar un mensaje de error
                    JOptionPane.showMessageDialog(null, "Error: No se pudo establecer la conexión con la base de datos.");
                }
            } catch (SQLException ex) {
                // Manejar la excepción en caso de error al establecer la conexión
                JOptionPane.showMessageDialog(null, "Error: No se pudo establecer la conexión con la base de datos.");
            }
        }
        if (btnSalir == e.getSource()) {
        	System.exit(0);
        }
    }
}
