package Model;

import View.Recuperar;

public class ModelRecuperar {
    private String dato;

    public ModelRecuperar(String dato) {
        this.dato = dato;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    // Método para validar correo
    public boolean validarCorreo(String correo, Recuperar view) {
        boolean band = true;
        String regexCorreo = "^[\\w._%+-]+@(espe\\.edu\\.ec|gmail\\.com|hotmail\\.com|yahoo\\.es)$";
        if (!correo.matches(regexCorreo)) {
            view.errDato.setText("*Campo Válido:(@espe.edu, @gmail.com, @hotmail.com, @yahoo.es).");
            view.errDato.setVisible(true);
            band = false;
        }else{
            view.errDato.setVisible(false);
            band = true;
        }
        return band;
    }

    // Método para validar cédula
    public boolean validarCedula(String cedula, Recuperar view) {
        boolean band = true;
        if (!cedula.matches("\\d{10}")) {
            view.errDato.setText("Campo Válido: 10 dígitos númericos.");
            view.errDato.setVisible(true);
            band = false;
        }else{
            view.errDato.setVisible(false);
            band = true;
        }
        return band;
    }

    // Método para validar nombre de usuario
    public boolean validarNombreUsuario(String nombreUsuario, Recuperar view) {
        boolean band = true;
        if (nombreUsuario.length() < 8) {
            view.errDato.setText("*Campo Válido: [8=>] caracteres.");
            view.errDato.setVisible(true);
            band = false;
        }else{
            view.errDato.setVisible(false);
            band = true;
        }
        return band;
    }
}
