package Controller;

import Model.ModelLogin;
import Model.ModelRegister;
import Model.Student;
import Model.Teaching;
import Model.mongoDB;
import View.Login;
import View.Register;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import org.bson.Document;

public class ControllerRegister implements ActionListener {
    private final ModelRegister model;
    private final Register view;
    private final mongoDB mongo;
    
    public ControllerRegister(ModelRegister model, Register view) {
        this.model = model;
        this.view = view;
        this.view.btnRegister.addActionListener(this);
        this.view.btnCancel.addActionListener(this);
        this.view.txtName.setOpaque(false);
        this.view.txtLastName.setOpaque(false);
        this.view.txtCI.setOpaque(false);
        this.view.boxTipo.setOpaque(false);
        this.view.panelMaterias.setOpaque(false);
        this.view.radCalculoVec.setOpaque(false);
        this.view.radFisica.setOpaque(false);
        this.view.radProgramacion.setOpaque(false);
        this.view.radQuimica.setOpaque(false);
        this.view.calenFechaNaci.setOpaque(false);
        this.mongo = mongoDB.getInstance();
        this.vaciarCampos();
        this.vaciarValidadores();
        
    }

    public void viewIniciar() {
        view.setVisible(true);
    }
    
    public void vaciarCampos(){
        view.txtName.setText("");
        view.txtLastName.setText("");
        view.txtCI.setText("");
        view.radCalculoVec.setSelected(false);
        view.radFisica.setSelected(false);
        view.radQuimica.setSelected(false);
        view.radProgramacion.setSelected(false);
        view.boxTipo.setSelectedItem("Seleccionar");
        view.calenFechaNaci.setDate(new Date());
        view.btnGroup.clearSelection();
    }
    
    public void vaciarValidadores(){
        view.errName.setText("");
        view.errLastName.setText("");
        view.errCedula.setText("");
        view.errMaterias.setText("");
        view.errTipo.setText("");
        view.errDate.setText("");
    }
    
    public void btnGuardarUser() {
        String name = view.txtName.getText();
        String lastName = view.txtLastName.getText();
        String cedula = view.txtCI.getText();
        String tipo = (String) view.boxTipo.getSelectedItem().toString();
        // Crear una lista para almacenar las materias seleccionadas
        ArrayList<String> materiasSeleccionadas = new ArrayList<>();
        // Validar si se seleccionó una fecha
        Date selectedDate = view.calenFechaNaci.getDate();
        if (selectedDate == null) {
            view.errDate.setText("*Seleccione una fecha válida");
            view.errDate.setVisible(true);
            return;
        }
        // Formatear la fecha a String
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(selectedDate);
        model.setName(name);
        model.setLastName(lastName);
        model.setCedula(cedula);
        model.setTipoUser(tipo);
        model.setFechaNacimento(dateString);
        boolean band = model.camposValidos(model.getName(), model.getLastName(), model.getCedula(), model.getTipoUser(), model.getFechaNacimento(), materiasSeleccionadas, view);
        // Convertir la lista de materias a una cadena separada por comas
        String materias = String.join(", ", materiasSeleccionadas);
        model.setMaterias(materias); // Guardar la cadena de materias
        if (band) {
            try {
                if ("Docente".equalsIgnoreCase(tipo)) {
                    Teaching user = new Teaching(model.getName(), model.getLastName(), model.getCedula(), model.getMaterias(), model.getTipoUser());
                    String pasw = user.generateContrasena(model.getName(), model.getLastName(), model.getFechaNacimento());
                    String correo = user.generateCorreo(model.getName(), model.getLastName(), model.getFechaNacimento());
                    String usuario = user.generateUsuario(model.getName(), model.getLastName(), model.getFechaNacimento());
                    model.setPasword(pasw);
                    model.setEmail(correo);
                    model.setNameUser(usuario);
                    // Crear documento común para la colección "Docentes"
                    Document Doc_Docente = new Document("Cedula", model.getCedula())
                        .append("Nombre", model.getName())
                        .append("Apellido", model.getLastName())
                        .append("Materia", model.getMaterias())
                        .append("Tipo", model.getTipoUser())
                        .append("Fecha-Nacimiento", model.getFechaNacimento())
                        .append("E-Mail", model.getEmail())
                        .append("Usuario", model.getNameUser())
                        .append("Password", model.getPasword());
                    mongo.setCollName("Docentes");
                    mongo.createDocument(Doc_Docente);
                    user.getDataPerson(model.getCedula(), model.getName(),model.getLastName(),model.getEmail(),model.getPasword(),model.getNameUser(),model.getTipoUser());
                    JOptionPane.showMessageDialog(null, "Docente Registrado exitosamente.");
                } else if ("Estudiante".equalsIgnoreCase(tipo)) {
                    Student user = new Student("", model.getName(), model.getLastName(), model.getCedula(), model.getMaterias(), model.getTipoUser());
                    String pasw = user.generateContrasena(model.getName(), model.getLastName(), model.getFechaNacimento());
                    String correo = user.generateCorreo(model.getName(), model.getLastName(), model.getFechaNacimento());
                    String usuario = user.generateUsuario(model.getName(), model.getLastName(), model.getFechaNacimento());
                    model.setPasword(pasw);
                    model.setEmail(correo);
                    model.setNameUser(usuario);
                    // Crear documento común para la colección "Docentes"
                    Document Doc_Estudiantes = new Document("Cedula", model.getCedula())
                        .append("Nombre", model.getName())
                        .append("Apellido", model.getLastName())
                        .append("Materias", model.getMaterias())
                        .append("Tipo", model.getTipoUser())
                        .append("Fecha-Nacimiento", model.getFechaNacimento())
                        .append("E-Mail", model.getEmail())
                        .append("Usuario", model.getNameUser())
                        .append("Password", model.getPasword());
                    mongo.setCollName("Estudiantes");
                    mongo.createDocument(Doc_Estudiantes);
                    user.getDataPerson(model.getCedula(), model.getName(),model.getLastName(),model.getEmail(),model.getPasword(),model.getNameUser(),model.getTipoUser());
                }
                // Vaciar campos después de un registro exitoso
                vaciarCampos();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al guardar el registro: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Errores en el formulario.");
        }
    }
    
    public void btnCancelar(){
        Login view1 = new Login();
        ModelLogin model1 = new ModelLogin("", "");
        ControllerLogin controller = new ControllerLogin(model1, view1);
        controller.viewIniciar();
        view.setVisible(false);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.btnCancel) {
            btnCancelar();
        } else if (e.getSource() == view.btnRegister) {
            btnGuardarUser();
        }
    }
}
