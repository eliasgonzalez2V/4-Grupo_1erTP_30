package proyecto;

import java.time.LocalDate;
import java.util.ArrayList;

public class BaseDeDatos {

    public static FactoriaDeEmpleados fe;
    public static ArrayList<Empleado> empleados = new ArrayList<>();
    public static ArrayList<Administrador> admin = new ArrayList<>();
    public static ArrayList<Socio> socios = new ArrayList<>();

    public static ArrayList<Zona> zonas = new ArrayList<>();
    public static ArrayList<Garage> garages = new ArrayList<>();
    public static ArrayList<Vehiculo> vehiculo = new ArrayList<>();

    public static Socio socioActual = null;
    public static Empleado empleadoActual = null;
    public static Administrador administradorActual = null;

    public static void BaseDeDatos() {
        ArrayList<Vehiculo> vehiMantenimiento = new ArrayList<>();//Lista de vehiculos para mantenimiento

        vehiMantenimiento.add(new Vehiculo("fgs 122", true, true, false));
        vehiMantenimiento.add(new Vehiculo("egd 834", true, true, false));
        vehiMantenimiento.add(new Vehiculo("ppp 122", true, false, true));
        vehiMantenimiento.add(new Vehiculo("nic 962", true, false, true));
        vehiMantenimiento.add(new Vehiculo("rep 526", false, true, false));
        vehiMantenimiento.add(new Vehiculo("mes 862", false, true, false));


    }
    /*
    public void insertarEmpleado(String DNI, String nom, String dir, double tel, String pass, int cod, String esp) {
        Empleado empleado = fe.crearEmpleado(DNI, nom, dir, tel, pass, cod, esp);
        System.out.println(empleado);
    }
     */

}
