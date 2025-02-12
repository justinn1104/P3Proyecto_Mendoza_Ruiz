package Model;

import Model.mongoDB;
import View.Register;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.bson.Document;


public class ModelRegister {
    private String name, lastName, cedula, tipoUser, fechaNacimento, materias, email, 
            nameUser, pasword;
    private final mongoDB mongo;

    public ModelRegister(String name, String lastName, String cedula, String tipoUser, String fechaNacimento, String materias, String email, String nameUser, String pasword) {
        this.name = name;
        this.lastName = lastName;
        this.cedula = cedula;
        this.tipoUser = tipoUser;
        this.fechaNacimento = fechaNacimento;
        this.materias = materias;
        this.email = email;
        this.nameUser = nameUser;
        this.pasword = pasword;
        this.mongo = mongoDB.getInstance();
    }
    
    // Getters y setters omitidos para brevedad...

    public boolean camposValidos(String name, String lastName, String cedula, String tipoUser, String fechaNacimento, ArrayList<String> materiasSeleccionadas, Register view) {
        boolean band = true;
        // Agregar materias seleccionadas al ArrayList
        if (view.radCalculoVec.isSelected()) {
            materiasSeleccionadas.add("Calculo Vectorial");
        }
        if (view.radFisica.isSelected()) {
            materiasSeleccionadas.add("Fisica");
        }
        if (view.radQuimica.isSelected()) {
            materiasSeleccionadas.add("Quimica");
        }
        if (view.radProgramacion.isSelected()) {
            materiasSeleccionadas.add("Programacion");
        }
        // Validar nombre
        if (name == null || name.trim().isEmpty()) {
            view.errName.setText("*Campo Obligatorio: Vacio.");
            view.errName.setVisible(true);
            band = false;
        } else {
            view.errName.setVisible(false);
        }

        // Validar apellido
        if (lastName == null || lastName.trim().isEmpty()) {
            view.errLastName.setText("*Campo Obligatorio: Vacio.");
            view.errLastName.setVisible(true);
            band = false;
        } else {
            view.errLastName.setVisible(false);
        }

        // Validar cédula
        if (cedula == null || cedula.trim().isEmpty()) {
            view.errCedula.setText("*Campo Obligatorio: Vacio.");
            view.errCedula.setVisible(true);
            band = false;
        } else if (cedula.length() != 10) {
            view.errCedula.setText("*Campo Obligatorio: 10 Digitos.");
            view.errCedula.setVisible(true);
            band = false;
        } else if (!cedula.matches("\\d+")) {
            view.errCedula.setText("*Campo Obligatorio: Caracteres Númericos.");
            view.errCedula.setVisible(true);
            band = false;
        } else {
            view.errCedula.setVisible(false);
        }
        if (view.boxTipo.getSelectedItem().toString().equalsIgnoreCase("Seleccionar")) {
            view.errMaterias.setText("*Campo Obligatorio: Seleccionar una opción.");
            view.errMaterias.setVisible(true);
            band = false;
        } else {
            view.errMaterias.setVisible(false);
        }
        // Validar selección de materias según el tipo de usuario
        if ("Docente".equalsIgnoreCase(tipoUser)) {
            if (materiasSeleccionadas.size() != 1) {
                view.errMaterias.setText("*Seleccionar: 1 materia");
                view.errMaterias.setVisible(true);
                band = false;
            } else {
                view.errMaterias.setVisible(false);
            }
        } else if ("Estudiante".equalsIgnoreCase(tipoUser)) {
            if (materiasSeleccionadas.isEmpty()) {
                view.errMaterias.setText("*Seleccionar: 1 o más materia");
                view.errMaterias.setVisible(true);
                band = false;
            } else {
                view.errMaterias.setVisible(false);
            }
        }
        // Validar edad
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            Date birthDate = sdf.parse(fechaNacimento);

            Calendar current = Calendar.getInstance();
            Calendar birthCalendar = Calendar.getInstance();
            birthCalendar.setTime(birthDate);

            int age = current.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR);
            if (current.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }

            if (age < 18 || age > 65) {
                view.errDate.setText("*Campo Invalido: [18-65] años");
                view.errDate.setVisible(true);
                band = false;
            } else {
                view.errDate.setVisible(false);
            }
        } catch (ParseException e) {
            view.errDate.setText("*Campo Invalido: Formato de fecha inválido.");
            view.errDate.setVisible(true);
            band = false;
        }
        // Verificar si la cédula ya existe
        mongo.setCollName("Usuarios");
        if (mongo.validateDocument("Cedula", cedula)) {
            view.errCedula.setText("**Cédula ya existente");
            view.errCedula.setVisible(true);
            band = false;
        } else {
            view.errCedula.setVisible(false);
        }
        return band;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getTipoUser() {
        return tipoUser;
    }

    public void setTipoUser(String tipoUser) {
        this.tipoUser = tipoUser;
    }

    public String getFechaNacimento() {
        return fechaNacimento;
    }

    public void setFechaNacimento(String fechaNacimento) {
        this.fechaNacimento = fechaNacimento;
    }

    public String getMaterias() {
        return materias;
    }

    public void setMaterias(String materias) {
        this.materias = materias;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getPasword() {
        return pasword;
    }

    public void setPasword(String pasword) {
        this.pasword = pasword;
    }
}

