package vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import controlador.Conexion;
import modelo.Consultas;
import javax.swing.ImageIcon;

public class RegistroTabla extends JFrame implements ActionListener {

    private JPanel contentPane;
    private JTextField textFieldCodigo;
    private JTextField textFieldNombre;
    private JTextField textFieldPrecio;
    private JTextField textFieldCantidad;
    private Connection connection;

    JButton btnVolver, btnEnviar;

    private String baseDeDatos;
    private String username;
    private String password;
    private JLabel lblNewLabel_1;

    public RegistroTabla(String username, String password, String baseDeDatos) {
        this.username = username;
        this.password = password;
        this.baseDeDatos = baseDeDatos;
        // Crea la conexión a la base de datos, de la clase conexion
        try {
            Conexion co = new Conexion(username, password, baseDeDatos);
            connection = co.obtenerConexion();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: No se pudo establecer la conexión con la base de datos.");
            e.printStackTrace();
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        

		setTitle("tabla registro");
		setResizable(false);
		setLocationRelativeTo(null);

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("CÓDIGO:");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblNewLabel.setBounds(22, 69, 56, 14);
        contentPane.add(lblNewLabel);

        JLabel lblNombre = new JLabel("NOMBRE:");
        lblNombre.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblNombre.setBounds(22, 100, 56, 14);
        contentPane.add(lblNombre);

        JLabel lblPrecio = new JLabel("PRECIO:");
        lblPrecio.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblPrecio.setBounds(22, 131, 56, 14);
        contentPane.add(lblPrecio);

        JLabel lblCantidad = new JLabel("CANTIDAD:");
        lblCantidad.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblCantidad.setBounds(24, 171, 74, 14);
        contentPane.add(lblCantidad);

        textFieldCodigo = new JTextField();
        textFieldCodigo.setBounds(88, 66, 208, 20);
        contentPane.add(textFieldCodigo);
        textFieldCodigo.setColumns(10);

        textFieldNombre = new JTextField();
        textFieldNombre.setColumns(10);
        textFieldNombre.setBounds(88, 97, 208, 20);
        contentPane.add(textFieldNombre);

        textFieldPrecio = new JTextField();
        textFieldPrecio.setColumns(10);
        textFieldPrecio.setBounds(88, 128, 208, 20);
        contentPane.add(textFieldPrecio);

        textFieldCantidad = new JTextField();
        textFieldCantidad.setColumns(10);
        textFieldCantidad.setBounds(88, 168, 208, 20);
        contentPane.add(textFieldCantidad);

        btnVolver = new JButton("VOLVER A LA TABLA");
        btnVolver.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnVolver.setBounds(10, 11, 149, 23);
        btnVolver.addActionListener(this);
        contentPane.add(btnVolver);

        btnEnviar = new JButton("REGISTRAR");
        btnEnviar.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnEnviar.setBounds(181, 227, 109, 23);
        btnEnviar.addActionListener(this);
        btnEnviar.setBackground(Color.green);
        contentPane.add(btnEnviar);
        
        lblNewLabel_1 = new JLabel("");
        lblNewLabel_1.setIcon(new ImageIcon(RegistroTabla.class.getResource("/imagenes/fondoBlanco.jpg")));
        lblNewLabel_1.setBounds(0, 0, 434, 261);
        contentPane.add(lblNewLabel_1);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnEnviar) {
            //capturo los datos ingresados en la caja de texto y los guardo en diferentes variables
            String codigo = textFieldCodigo.getText();
            String nombre = textFieldNombre.getText();
            double precio = Double.parseDouble(textFieldPrecio.getText());
            int cantidad = Integer.parseInt(textFieldCantidad.getText());

            // Creamos una instancia de Consultas para utilizar sus métodos
            Consultas consultas = new Consultas();
            
            // Verifica si el código ya existe en la base de datos
            boolean codigoExistente = consultas.verificarExistenciaRegistro(connection, codigo);

            if (!codigoExistente) {
                // El código no existe, entonces se puede agregar el registro
                boolean exito = consultas.agregarRegistro(connection, codigo, nombre, precio, cantidad);
                if (exito) {
                    // Limpiar las cajas de texto
                    textFieldCodigo.setText("");
                    textFieldNombre.setText("");
                    textFieldPrecio.setText("");
                    textFieldCantidad.setText("");
                    // Mostrar un JOptionPane con un mensaje de éxito
                    JOptionPane.showMessageDialog(null, "Nuevo registro agregado exitosamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "Ocurrió un error al intentar agregar el registro. Por favor, inténtalo nuevamente.");
                }
            } else {
                // El código ya existe en la base de datos
                JOptionPane.showMessageDialog(null, "El código ingresado ya existe en la base de datos. Por favor, ingresa un código diferente.");
            }
        }
        if (e.getSource() == btnVolver) {
            //se instancia tabla
            Tabla tabla = new Tabla(username,password,baseDeDatos);
            tabla.setVisible(true);
            dispose();
        }
    }
}