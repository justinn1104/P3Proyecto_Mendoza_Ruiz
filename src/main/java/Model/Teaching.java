package Model;

import View.Login;
import java.io.IOException;
import javax.swing.JOptionPane;
import org.bson.Document;

public class Teaching extends Person implements Interface{
    private mongoDB mongo;

    public Teaching(String nombre, String apellido, String cedula, String materia, String tipo) {
        super(nombre, apellido, cedula, materia, tipo);
        this.mongo = mongoDB.getInstance();
    }
    
    @Override
    public void getDataPerson(String cedula, String nombre, String apellido, String correo, String contra,  String usuario, String tipo) {
        Document doc = new Document("Cedula",cedula)
            .append("Nombre-Apellido", nombre+" "+apellido)
            .append("Tipo", tipo)
            .append("E-Mail", correo)
            .append("Usuario", contra)
            .append("Password", usuario);
        mongo.setCollName("Credenciales");
        mongo.createDocument(doc);
        System.out.println("Credenciales generadas con exito.");
        JOptionPane.showMessageDialog(null, "Credenciales generadas con exito!!"+
                "\nCedula: "+getCedula()+
                "\nNombre-Apellido: "+getNombre()+" "+getApellido()+
                "\nCorreo: "+correo+
                "\nUsuario: "+usuario+
                "\nContraseña: "+contra);
    }
    
}
