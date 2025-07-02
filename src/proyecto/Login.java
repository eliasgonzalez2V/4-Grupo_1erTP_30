package proyecto;

import java.util.ArrayList;
import java.util.Scanner;

public class Login {

//    public Login(ArrayList<Socio> socios, ArrayList<Empleado> empleados, ArrayList<Administrador> administradores) {
//        this.socios = socios;
//        this.empleados = empleados;
//        this.administradores = administradores;
//    }
    public void iniciarSesion() {
        Scanner input = new Scanner(System.in);
        int op,cont;
        do {

            System.out.println("1-Iniciar sesion como socio");
            System.out.println("2-Iniciar sesion como empleado");
            System.out.println("3-Iniciar sesion como administrador");
            System.out.println("4-Salir");
            System.out.print("-");
            op = input.nextInt();
            input.nextLine();// para el espacio del input.nextInt()
            cont=0;
            switch (op) {
                case 1 -> {
                    System.out.print("Ingrese DNI: ");
                    String dni = input.nextLine();
                    System.out.print("Ingrese contraseña: ");
                    String contrasenia = input.nextLine();
                    for (Socio s : BaseDeDatos.socios) {
                        if (s.getDNI().equals(dni) && s.getContrasenia().equals(contrasenia)) {
                            BaseDeDatos.socioActual = s;
                            System.out.println("Bienvenido, " + s.getNombre());
                            Menu menu = new MenuSocio();
                            menu.mostrar();
                            cont++;
                        } 
                    }
                    if (cont==0) {
                            System.out.println("Usuario no encontrado");
                    }
                    break;
                }
                case 2 -> {
                    System.out.print("Ingrese DNI: ");
                    String dni = input.nextLine();
                    System.out.print("Ingrese contraseña: ");
                    String contrasenia = input.nextLine();
                    for (Empleado e : BaseDeDatos.empleados) {
                        if (e.getDNI().equals(dni) && e.getContrasenia().equals(contrasenia)) {
                            BaseDeDatos.empleadoActual = e;
                            System.out.println("Bienvenido, " + e.getNombre());
                            Menu menu = new MenuEmpleado();
                            menu.mostrar();
                            cont++;
                        } 
                    }
                    if (cont==0) {
                            System.out.println("Usuario no encontrado");
                    }
                    break;
                }
                case 3 -> {
                    System.out.print("Ingrese DNI: ");
                    String dni = input.nextLine();
                    System.out.print("Ingrese contraseña: ");
                    String contrasenia = input.nextLine();
                    for (Administrador a : BaseDeDatos.admin) {
                        if (a.getDNI().equals(dni) && a.getContrasenia().equals(contrasenia)) {
                            BaseDeDatos.administradorActual = a;
                            System.out.println("Bienvenido, " + a.getNombre());
                            Menu menu = new MenuAdministrador();
                            menu.mostrar();
                            cont++;
                        } 
                    }
                    if (cont==0) {
                            System.out.println("Usuario no encontrado");
                    }
                    break;
                }
                case 4 -> {
                    System.out.println("Ha salido correctamente!");
                    break;
                }
                default ->
                    System.out.println("Error! opcion invalida, por favor ingrese nuevamente la opcion...");
            }
        } while (op != 4);
    }
}

//    private ArrayList<Persona> personas;
//   
//    public Login(){
//        personas = new ArrayList();
//    }
//    public Persona autenticar(String nombre, String contraseña){
//        for(Persona persona : personas){
//            if(persona.getNombre().equals(nombre) && persona.getContrasenia().equals(contraseña)){
//                return persona;
//            }
//        }
//        return null;
//    }

