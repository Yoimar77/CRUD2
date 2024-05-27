// Clase Tabla en el paquete vista
package vista;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import controlador.Conexion;
import modelo.Consultas;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;

public class Tabla extends JFrame implements ActionListener {

    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel model;
    private JButton btnMostrarProductos, btnBuscar,btnEliminar, btnAtras, btnAgregarRegistro;
    private JTextField textFieldProducto;
    private Connection connection;
    private String baseDeDatos;
    private String username;
    private String password;
    private JTextField textFieldCodigo;
    private JLabel lblNewLabel_3;

    public Tabla(String username, String password, String baseDeDatos) {
        this.username = username;
        this.password = password;
        this.baseDeDatos = baseDeDatos;
        // Crea la conexión a la base de datos
        try {
            Conexion co = new Conexion(username, password, baseDeDatos);
            connection = co.obtenerConexion();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: No se pudo establecer la conexión con la base de datos.");
            e.printStackTrace();
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 583, 416);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        

		setTitle("base de datos");
		setResizable(false);
		setLocationRelativeTo(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(42, 63, 450, 256);
        contentPane.add(scrollPane);

        table = new JTable();
        table.setFont(new Font("Tahoma", Font.BOLD, 11));
        model = new DefaultTableModel();
        table.setModel(model);

        model.addColumn("código");
        model.addColumn("nombre");
        model.addColumn("precio");
        model.addColumn("cantidad");

        scrollPane.setViewportView(table);

        btnMostrarProductos = new JButton("MOSTRAR Y ACTUALIZAR PRODUCTOS");
        btnMostrarProductos.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnMostrarProductos.setBounds(141, 36, 283, 23);
        // para que el boton pueda ser escuchado
        btnMostrarProductos.addActionListener(this);
        contentPane.add(btnMostrarProductos);

        textFieldProducto = new JTextField();
        textFieldProducto.setBounds(236, 5, 157, 20);
        contentPane.add(textFieldProducto);
        textFieldProducto.setColumns(10);

        JLabel lblNewLabel = new JLabel("PRODUCTO:");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblNewLabel.setBounds(147, 8, 79, 14);
        contentPane.add(lblNewLabel);

        btnBuscar = new JButton("BUSCAR");
        btnBuscar.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnBuscar.setBounds(403, 4, 89, 23);
        // para que el boton pueda ser escuchado
        btnBuscar.addActionListener(this);
        contentPane.add(btnBuscar);
        
        textFieldCodigo = new JTextField();
        textFieldCodigo.setBounds(98, 330, 150, 20);
        contentPane.add(textFieldCodigo);
        textFieldCodigo.setColumns(10);
        
        JLabel lblNewLabel_1 = new JLabel("CÓDIGO");
        lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblNewLabel_1.setBounds(42, 330, 46, 14);
        contentPane.add(lblNewLabel_1);
        
        btnEliminar = new JButton("ELIMINAR DE LA TABLA");
        btnEliminar.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnEliminar.setBounds(258, 330, 207, 23);
        btnEliminar.addActionListener(this);
        contentPane.add(btnEliminar);
        
        btnAtras = new JButton("ATRAS");
        btnAtras.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnAtras.setBounds(-1, 0, 89, 23);
        btnAtras.addActionListener(this);
        btnAtras.setBackground(Color.red);
        contentPane.add(btnAtras);
        
        JLabel lblNewLabel_2 = new JLabel("PARA AGREGAR UN REGISTRO A LA TABLA, DA CLICK EN ESTE BOOTON --->");
        lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblNewLabel_2.setBounds(67, 361, 428, 14);
        contentPane.add(lblNewLabel_2);
        
        btnAgregarRegistro = new JButton("");
        btnAgregarRegistro.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnAgregarRegistro.setBounds(486, 352, 50, 23);
        btnAgregarRegistro.addActionListener(this);
        btnAgregarRegistro.setBackground(Color.green);
        contentPane.add(btnAgregarRegistro);
        
        lblNewLabel_3 = new JLabel("New label");
        lblNewLabel_3.setIcon(new ImageIcon(Tabla.class.getResource("/imagenes/fondoBlanco.jpg")));
        lblNewLabel_3.setBounds(-1, 0, 568, 382);
        contentPane.add(lblNewLabel_3);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Consultas consultas = new Consultas();
        if (btnMostrarProductos == e.getSource()) {
            consultas.cargarProductos(connection, model);
        }

        if (btnBuscar == e.getSource()) {
            String nombreProducto = textFieldProducto.getText();
            consultas.buscarProducto(connection, nombreProducto, model);
        }
        if (btnEliminar == e.getSource()) {
            String codigo = textFieldCodigo.getText();
            boolean existeRegistro = consultas.verificarExistenciaRegistro(connection, codigo);
            if (existeRegistro) {
                int confirmacion = JOptionPane.showConfirmDialog(null, "¿Seguro que quieres eliminar este registro de tu base de datos?", "Confirmación de eliminación", JOptionPane.YES_NO_OPTION);
                if (confirmacion == JOptionPane.YES_OPTION) {
                    boolean eliminado = consultas.eliminarRegistro(connection, codigo,model);
                    if (eliminado) {
                        JOptionPane.showMessageDialog(null, "Registro eliminado exitosamente.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al eliminar el registro.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "El registro con el código indicado no existe en la base de datos.");
            }
        }
        if (btnAtras == e.getSource()) {
        	DatosAdministrador ventana = new DatosAdministrador();
        	ventana.setVisible(true);
        	dispose();
        }
        if(btnAgregarRegistro == e.getSource()) {
        	RegistroTabla ventana = new RegistroTabla(username, password, baseDeDatos);
        	ventana.setVisible(true);
        	dispose();
        }
        
    }
}