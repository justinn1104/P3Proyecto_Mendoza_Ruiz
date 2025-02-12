package Controller;

import Model.ModelGestionDocente;
import Model.ModelGestionEstudiante;
import Model.ModelLogin;
import Model.ModelRegister;
import Model.ModelRecuperar;
import Model.mongoDB;
import View.GestionDocente;
import View.GestionEstudiante;
import View.Login;
import View.Register;
import View.Recuperar;
import com.mongodb.client.MongoCollection;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import org.bson.Document;

public class ControllerLogin implements ActionListener{
    ModelLogin model;
    Login view;
    private final mongoDB mongo;
    private MongoCollection<Document> collection;
    
    public ControllerLogin(ModelLogin model, Login view) {
        this.model = model;
        this.view = view;
        this.mongo = mongoDB.getInstance();
        view.txtPassword.setOpaque(false);
        view.txtUserName.setOpaque(false);
        view.btnSignUp.setOpaque(false);
        view.panelPas.setOpaque(false);
        view.panelUserName.setOpaque(false);
        view.btnRecuperar.setContentAreaFilled(false);
        view.btnRecuperar.setBorderPainted(false);
        view.btnRecuperar.setFocusPainted(false);
        view.btnRecuperar.setForeground(Color.BLUE);
        Color color = new Color(204,204,204);
        // Crear un borde con un grosor personalizado
        Border customBorder = BorderFactory.createLineBorder(color, 2); // Grosor: 3 píxeles
        view.panelPas.setBorder((Border) customBorder);
        view.panelUserName.setBorder((Border) customBorder);
        view.icoPasw.setBorder((Border) customBorder);
        view.icoUser.setBorder((Border) customBorder);
        view.icoUser.setBackground(color); 
        view.icoUser.setOpaque(true); 
        view.icoPasw.setBackground(color); 
        view.icoPasw.setOpaque(true); 
        view.btnRecuperar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        view.btnRecuperar.setHorizontalAlignment(SwingConstants.CENTER);
        view.btnSession.addActionListener(this);
        view.btnSignUp.addActionListener(this);
        view.btnRecuperar.addActionListener(this);
        this.vaciarCampos();
        this.vaciarValidadores();
    }
    public void viewIniciar(){
        view.setVisible(true);
    }
    
    public void iniciarSesion() {
        String nameUser = view.txtUserName.getText();
        String passUser = view.txtPassword.getText();
        model.setNameUser(nameUser);
        model.setPassUser(passUser);
        boolean band = model.validarCampos(model.getNameUser(), model.getPassUser(), view);
        if (band) {
            try {
                // Obtener la colección "Credenciales" desde mongoDB
                mongo.setCollName("Credenciales");
                collection = mongo.getCollection();
                // Crear el filtro con los datos ingresados
                Document filtro = new Document("Usuario", model.getNameUser())
                        .append("Password", model.getPassUser());
                System.out.println("Filtro utilizado: " + filtro.toJson());
                // Verificar si el documento fue encontrado
                Document docenteDoc = collection.find(filtro).first();
                if (docenteDoc != null) {
                    // Ahora puedes obtener los valores del documento
                    String tipoUsuario = docenteDoc.getString("Tipo");
                    String cedulaUsuario = docenteDoc.getString("Cedula");
                    if ("Estudiante".equalsIgnoreCase(tipoUsuario)) {
                        ModelGestionEstudiante model1 = new ModelGestionEstudiante("","","","","","");
                        GestionEstudiante view1 = new GestionEstudiante();
                        ControllerGestionEstudiante controller1 = new ControllerGestionEstudiante(model1, view1, cedulaUsuario);
                        view.setVisible(false);
                        vaciarCampos();
                        vaciarValidadores();
                        controller1.iniciarView();
                    } else if ("Docente".equalsIgnoreCase(tipoUsuario)) {
                        ModelGestionDocente model2 = new ModelGestionDocente("", "", "", "", "", "");
                        GestionDocente view2 = new GestionDocente();
                        ControllerGestionDocente controller1 = new ControllerGestionDocente(model2, view2, cedulaUsuario);
                        view.setVisible(false);
                        vaciarCampos();
                        vaciarValidadores();
                        controller1.iniciarView();
                    } else {
                        JOptionPane.showMessageDialog(null, "Usuario no reconocido.");
                    }
                } else {
                    // El documento no fue encontrado
                    JOptionPane.showMessageDialog(null, "No se encontró a el usuario en la base de datos.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Campos inválidos");
        }
    }
    
    public void vaciarCampos(){
        view.txtUserName.setText("");
        view.txtPassword.setText("");
    }
    
    public void vaciarValidadores(){
        view.errUserName1.setText("");
        view.errPassword.setText("");
    }
    
    public void mostrarOpciones() {
        // Opciones para el JOptionPane
        String[] opciones = {"Usuario", "Contraseña"};
        
        // Mostrar el JOptionPane con las opciones
        int seleccion = JOptionPane.showOptionDialog(
            null, // Componente padre
            "Selecciona una opción:", // Mensaje
            "Opciones", // Título
            JOptionPane.DEFAULT_OPTION, // Tipo de opciones
            JOptionPane.INFORMATION_MESSAGE, // Tipo de mensaje
            null, // Ícono
            opciones, // Opciones
            opciones[0] // Opción predeterminada
        );

        // Evaluar la selección y llamar a la interfaz correspondiente
        switch (seleccion) {
            case 0: // Usuario
                recuperarUsuario();
                break;
            case 1: // Contraseña
                llamarInterfazRecuperarContrasena();
                break;
            default: // Cerrar o ninguna opción
                System.out.println("No se seleccionó ninguna opción.");
                break;
        }
    }

    public void recuperarUsuario() {
        String cedula = null;
        boolean cedulaValida = false;
        while (!cedulaValida) {
            cedula = JOptionPane.showInputDialog(view, "Ingrese la cédula a buscar:");
            if (cedula == null) {
                return; 
            } else if (cedula.isEmpty() || cedula.length() != 10 || !cedula.matches("\\d+")) {
                JOptionPane.showMessageDialog(view, "La cédula debe tener 10 dígitos y solo números.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                cedulaValida = true;
            }
        }
        mongo.setCollName("Credenciales");
        collection = mongo.getCollection();
        Document filtro = new Document("Cedula", cedula);
        System.out.println("Filtro utilizado: " + filtro.toJson());
        // Verificar si el documento fue encontrado
        Document docenteDoc = collection.find(filtro).first();
        if (docenteDoc != null) {
            // Ahora puedes obtener los valores del documento
            String nombreApellido = docenteDoc.getString("Nombre-Apellido");
            String correo = docenteDoc.getString("E-Mail");
            String usuario = docenteDoc.getString("Usuario");
            System.out.println("Usuario encontrado con exito.");
            JOptionPane.showMessageDialog(view, "INFORMACIÓN DEL USUARIO"+
                    "\nNombre-Apellido: "+nombreApellido+
                    "\nCorreo: "+correo+
                    "\nUsuario: "+usuario);
        } else {
            System.out.println("Usuario no encontrado con exito.");
            JOptionPane.showMessageDialog(view, "Usuario no existente.", "Búsqueda", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void llamarInterfazRecuperarContrasena() {
        Recuperar view4 = new Recuperar();
        ModelRecuperar model4 = new ModelRecuperar("");
        ControllerRecuperar controller1 = new ControllerRecuperar(model4, view4);
        controller1.viewIniciar();
        view.setVisible(false);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==view.btnSession){
            iniciarSesion();
        }else if(e.getSource()==view.btnSignUp){
            Register view3 = new Register();
            ModelRegister model3 = new ModelRegister("","","","","","","","","");
            ControllerRegister controller1 = new ControllerRegister(model3, view3);
            controller1.viewIniciar();
            view.setVisible(false);
        }else if(e.getSource()==view.btnRecuperar){
            mostrarOpciones();
        }
    }
    
}
