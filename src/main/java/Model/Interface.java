package Model;

public interface Interface {
    public void getDataPerson(String cedula, String nombre, String apellido, String correo, String contra,  String usuario, String tipo);
    public String generateContrasena(String nombre, String apellido, String fecha);
    public String generateCorreo(String nombre, String apellido, String fecha);
    public String generateUsuario(String nombre, String apellido, String fechaNacimiento);
    //"-->>Lanzamiento de la interface<<--"
}
