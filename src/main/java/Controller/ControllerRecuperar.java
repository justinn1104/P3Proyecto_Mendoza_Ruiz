package Controller;

import Model.ModelLogin;
import Model.ModelRecuperar;
import Model.mongoDB;
import View.Login;
import View.Recuperar;
import com.mongodb.client.MongoCollection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import org.bson.Document;

public class ControllerRecuperar implements ActionListener{
    ModelRecuperar model;
    Recuperar view;
    private final mongoDB mongo;
    private MongoCollection<Document> collection;

    public ControllerRecuperar(ModelRecuperar model, Recuperar view) {
        this.model = model;
        this.view = view;
        this.mongo = mongoDB.getInstance();
        this.view.btnEnviar.addActionListener(this);
        this.view.btnCancelar.addActionListener(this);
        this.view.toggBtnCedula.addActionListener(this);
        this.view.toggBtnEmail.addActionListener(this);
        this.view.toggBtnNameUser.addActionListener(this);
        this.limpiarValidador();
    }

    public ModelRecuperar getModel() {
        return model;
    }

    public void setModel(ModelRecuperar model) {
        this.model = model;
    }

    public Recuperar getView() {
        return view;
    }

    public void setView(Recuperar view) {
        this.view = view;
    }
    public void viewIniciar(){
        view.setVisible(true);
    }
    
    public void btnCancelar(){
        Login view1 = new Login();
        ModelLogin model1 = new ModelLogin("", "");
        ControllerLogin controller = new ControllerLogin(model1, view1);
        controller.viewIniciar();
        view.setVisible(false);
    }
    
    public void limpiarValidador(){
        view.errDato.setVisible(false);
    }
    
    public void recuperarPorCedula(String cedula){
        mongo.setCollName("Credenciales");
        collection = mongo.getCollection();
        Document filtro = new Document("Cedula", cedula);
        System.out.println("Filtro utilizado: " + filtro.toJson());
        // Verificar si el documento fue encontrado
        Document docenteDoc = collection.find(filtro).first();
        if (docenteDoc != null) {
            // Ahora puedes obtener los valores del documento
            String nombreApellido = docenteDoc.getString("Nombre-Apellido");
            String usuario = docenteDoc.getString("Usuario");
            String pasw = docenteDoc.getString("Password");
            System.out.println("Cedula encontrada con exito.");
            JOptionPane.showMessageDialog(view, "INFORMACIÓN DEL USUARIO"+
                    "\nNombre-Apellido: "+nombreApellido+
                    "\nUsuario: "+usuario+
                    "\nPassword: "+pasw);
            limpiarValidador();
        } else {
            System.out.println("Cedula no encontrada con exito.");
            view.errDato.setText("*Campo Obligatorio: No existe");
            JOptionPane.showMessageDialog(view, "Usuario no existente.", "Búsqueda", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    public void recuperarPorEmail(String email){
        mongo.setCollName("Credenciales");
        collection = mongo.getCollection();
        Document filtro = new Document("E-Mail", email);
        System.out.println("Filtro utilizado: " + filtro.toJson());
        // Verificar si el documento fue encontrado
        Document docenteDoc = collection.find(filtro).first();
        if (docenteDoc != null) {
            // Ahora puedes obtener los valores del documento
            String nombreApellido = docenteDoc.getString("Nombre-Apellido");
            String usuario = docenteDoc.getString("Usuario");
            String pasw = docenteDoc.getString("Password");
            System.out.println("E-Mail encontrada con exito.");
            JOptionPane.showMessageDialog(view, "INFORMACIÓN DEL USUARIO"+
                    "\nNombre-Apellido: "+nombreApellido+
                    "\nUsuario: "+usuario+
                    "\nPassword: "+pasw);
            limpiarValidador();
        } else {
            System.out.println("E-Mail no encontrada con exito.");
            view.errDato.setText("*Campo Obligatorio: No existe");
            JOptionPane.showMessageDialog(view, "Usuario no existente.", "Búsqueda", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    public void recuperarPorUsuario(String usuario){
        mongo.setCollName("Credenciales");
        collection = mongo.getCollection();
        Document filtro = new Document("Usuario", usuario);
        System.out.println("Filtro utilizado: " + filtro.toJson());
        // Verificar si el documento fue encontrado
        Document docenteDoc = collection.find(filtro).first();
        if (docenteDoc != null) {
            // Ahora puedes obtener los valores del documento
            String nombreApellido = docenteDoc.getString("Nombre-Apellido");
            String pasw = docenteDoc.getString("Password");
            System.out.println("Usuario encontrado con exito.");
            JOptionPane.showMessageDialog(view, "INFORMACIÓN DEL USUARIO"+
                    "\nNombre-Apellido: "+nombreApellido+
                    "\nUsuario: "+usuario+
                    "\nPassword: "+pasw);
            limpiarValidador();
        } else {
            System.out.println("Usuario no encontrado con exito.");
            view.errDato.setText("*Campo Obligatorio: No existe");
            JOptionPane.showMessageDialog(view, "Usuario no existente.", "Búsqueda", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    public void btnEnviar(){
        // Validar que se haya seleccionado al menos un toggle button
            if (!view.toggBtnCedula.isSelected() && !view.toggBtnEmail.isSelected() && !view.toggBtnNameUser.isSelected()) {
                // Mostrar un mensaje en el JLabel de error
                view.errDato.setText("*Campo Obligatorio: Tipo de dato (Usuario, Email o Cédula).");
                view.errDato.setVisible(true);
                return;
            }
            // Si se seleccionó uno de los toggles, obtener el valor ingresado en el campo
            model.setDato(view.txtDatoUser.getText().trim());

            // Validar cuál toggle está seleccionado
            if (view.toggBtnCedula.isSelected()) {
                // Lógica para procesar el dato con "Cédula"
                boolean band = model.validarCedula(model.getDato(), view);
                if(band){
                    recuperarPorCedula(model.getDato());
                }
                model.setDato("Cédula: " + model.getDato());
            } else if (view.toggBtnEmail.isSelected()) {
                boolean band = model.validarCorreo(model.getDato(), view);
                if(band){
                    recuperarPorEmail(model.getDato());
                }
                model.setDato("Email: " + model.getDato());
            } else if (view.toggBtnNameUser.isSelected()) {
                boolean band = model.validarNombreUsuario(model.getDato(), view);
                if(band){
                    recuperarPorUsuario(model.getDato());
                }
                model.setDato("Usuario: " + model.getDato());
            }
            // Borrar el mensaje de error si el dato fue procesado correctamente
            System.out.println("Dato enviado correctamente.");
            System.out.println("Dato procesado: " + model.getDato());
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==view.btnEnviar){
            btnEnviar();
        }else if(e.getSource()==view.btnCancelar){
            btnCancelar();
        }else if(e.getSource()==view.toggBtnCedula){
            limpiarValidador();
        }else if(e.getSource()==view.toggBtnEmail){
            limpiarValidador();
        }else if(e.getSource()==view.toggBtnNameUser){
            limpiarValidador();
        }
    }
    
    
    
}
