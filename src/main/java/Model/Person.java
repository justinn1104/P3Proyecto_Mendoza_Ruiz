package Model;

public abstract class Person{
    protected String nombre, apellido, cedula, materia, tipo;
    protected String errNombre, errApellido, errCedula;

    public Person(String nombre, String apellido, String cedula, String materia, String tipo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
        this.materia = materia;
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    
    
    public String generateContrasena(String nombre, String apellido, String fechaNacimiento){
        // Obtener las partes de la fecha de nacimiento
        String[] fechaPartes = fechaNacimiento.split("-");
        String anioNacimiento = fechaPartes[0];
        
        // Generar contraseña
        String nombreParte = nombre.substring(0, 3); // "Jus"
        String apellidoParte = apellido.substring(0, 5); // "Mendo"
        String contrasena = nombreParte.substring(0, 1).toUpperCase() + 
                            nombreParte.substring(1).toLowerCase() + 
                            apellidoParte.substring(0, 1).toUpperCase() + 
                            apellidoParte.substring(1).toLowerCase() + 
                            anioNacimiento + "@";
        
        return contrasena;
    }
    
    public String generateCorreo(String nombre, String apellido, String fechaNacimiento){
        // Obtener las partes de la fecha de nacimiento
        String[] fechaPartes = fechaNacimiento.split("-");
        String anioNacimiento = fechaPartes[0];
        
        // Generar correo
        String correo = nombre.substring(0, 3).toLowerCase() + apellido.toLowerCase() + anioNacimiento + "@espe.edu.ec";
        
        return correo;
    }
    
    public String generateUsuario(String nombre, String apellido, String fechaNacimiento){
        // Obtener las partes de la fecha de nacimiento
        String[] fechaPartes = fechaNacimiento.split("-");
        String anioNacimiento = fechaPartes[0];
        // Generar usuario
        String correo = nombre.substring(0, 3).toLowerCase() + apellido.toLowerCase() + anioNacimiento;
        return correo;
    }
    
    // Métodos abstractos correctamente declarados
    public abstract void getDataPerson(String cedula, String nombre, String apellido, String correo, String contra,  String usuario, String tipo);
}
