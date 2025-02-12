package Main;

import Controller.ControllerLogin;
import Model.ModelLogin;
import View.Login;

public class main {
    public static void main(String[] args) {
        Login view = new Login();
        ModelLogin model = new ModelLogin("","");
        ControllerLogin controller = new ControllerLogin(model, view);
        controller.viewIniciar();
    }
}
