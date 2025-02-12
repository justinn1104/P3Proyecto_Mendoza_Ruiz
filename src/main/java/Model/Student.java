package Model;

import javax.swing.JOptionPane;
import org.bson.Document;

public class Student extends Person implements Interface {
    protected String mateEstado;
    private final mongoDB mongo;

    public Student(String mateEstado, String nombre, String apellido, String cedula, String tipo, String materia) {
        super(nombre, apellido, cedula, materia, tipo);
        this.mateEstado = mateEstado;
        this.mongo = mongoDB.getInstance();
    }

    public String getMateEstado() {
        return mateEstado;
    }

    public void setMateEstado(String mateEstado) {
        this.mateEstado = mateEstado;
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
