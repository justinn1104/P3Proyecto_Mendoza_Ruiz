package Model;

import View.Login;

public class ModelLogin {
    private String nameUser, passUser, errNameUser, errPassUser;

    public ModelLogin(String nameUser, String passUser) {
        this.nameUser = nameUser;
        this.passUser = passUser;
    }
    
    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getPassUser() {
        return passUser;
    }

    public void setPassUser(String passUser) {
        this.passUser = passUser;
    }

    public String getErrNameUser() {
        return errNameUser;
    }

    public void setErrNameUser(String errNameUser) {
        this.errNameUser = errNameUser;
    }

    public String getErrPassUser() {
        return errPassUser;
    }

    public void setErrPassUser(String errPassUser) {
        this.errPassUser = errPassUser;
    }
    
    public boolean validarCampos(String name, String pass, Login view){
        boolean band = true;
        System.out.println("\n--->>>LANZAMINETO DE LA CLASE HIJA<<<---"
                + "'extends Person implements Interface' \n");
        if(name.isEmpty()){
            view.errUserName1.setText("*Campo Obligatorio");
            view.errUserName1.setVisible(true);
            band = false;
        }else{
            view.errUserName1.setVisible(false);
        }
        if(pass.isEmpty()){
            view.errPassword.setText("*Campo Obligatorio");
            view.errPassword.setVisible(true);
            band = false;
        }else if(pass.length()<8 || pass.length()>30){
            view.errPassword.setText("*Caracteres 8-30");
            view.errPassword.setVisible(true);
            band = false;
        }else{
            view.errPassword.setVisible(false);
        }
        
        return band;
    }
    
}
