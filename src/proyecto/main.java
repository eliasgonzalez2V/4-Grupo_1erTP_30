package proyecto;

import java.util.ArrayList;
import java.time.LocalDate;
import java.util.Scanner;
import static proyecto.BaseDeDatos.fe;

public class main {

    public static void main(String[] args) {
        {
            /*
        ArrayList<Vehiculo> vehiMantenimiento = new ArrayList<>();//Lista de vehiculos
        
        vehiMantenimiento.add(new Vehiculo("fgs 122",true,true,false));
        vehiMantenimiento.add(new Vehiculo("egd 834",true,true,false));
        vehiMantenimiento.add(new Vehiculo("ppp 122",true,false,true));
        vehiMantenimiento.add(new Vehiculo("nic 962",true,false,true));
        vehiMantenimiento.add(new Vehiculo("rep 526",false,true,false));
        vehiMantenimiento.add(new Vehiculo("mes 862",false,true,false));
        
        BaseDeDatos emp = new BaseDeDatos();
        ArrayList<Vehiculo> vAsig = new ArrayList<>();
        vAsig.add(new Vehiculo("fgs 122", "Sandero", "4x4", "12x8"));
        vAsig.add(new Vehiculo("egd 834", "Duster", "3x5", "11x9"));
        emp.insertarEmpleado("32589632", "Jose", "queseyo 111", 11.36, "dificil", 01, "Lavador", vAsig);

        ArrayList<Vehiculo> vAsig2 = new ArrayList<>();
        vAsig2.add(new Vehiculo("ppp 122", "Falcon", "2x2", "7x7"));
        vAsig2.add(new Vehiculo("nic 962", "Suran", "6x5", "9x9"));
        emp.insertarEmpleado("40632582", "Luis", "ajaja 221", 1196523698, "media", 02, "Tecnico", vAsig2);

        ArrayList<Vehiculo> vAsig3 = new ArrayList<>();
        vAsig3.add(new Vehiculo("rep 526", "Clio", "mini", "5x5"));
        vAsig3.add(new Vehiculo("mes 862", "Corsa", "medio", "6x5"));
        emp.insertarEmpleado("41589632", "Pepe", "Libia 798", 1144789632, "facil", 03, "Playero", vAsig3);
             
            
            
        Scanner sc = new Scanner(System.in);
        System.out.print("Ingrese su DNI: ");
        String dni = sc.nextLine();

        for (Socio s : BaseDeDatos.socios) {
            if (s.getDNI().equals(dni)) {
                BaseDeDatos.socioActual = s;
                System.out.println("Bienvenido, " + s.getNombre());
                Menu menu = new MenuSocio();
                menu.mostrar();
                return;
            }
        }
            
            
            
            
             */
        }

        cargarDatosIniciales();

        Login inicio = new Login();
        inicio.iniciarSesion();

    }

    public static void cargarDatosIniciales() {
        fe = FactoriaDeEmpleados.getInstance();
        Administrador adminis = new Administrador("87654321", "Milon Sulis", "Calle Falsa 321", 1144332255, "clave321");
        Socio socio1 = new Socio("12345678", "Carlos Pérez", "Calle Falsa 123", 1122334455, "clave123", LocalDate.of(2022, 1, 15));
        Vehiculo v1 = new Vehiculo("AB123CD", "Motorhome Familiar", "Motorhome", "7m x 3m");
        Vehiculo v2 = new Vehiculo("XY789IJ", "Trailer Pequeño", "Trailer", "3m x 2m");
        Vehiculo v3 = new Vehiculo("CD456EF", "Camioneta 4x4", "Trailer", "5m x 2m");
        Vehiculo v4 = new Vehiculo("GH321KL", "Trailer de Carga", "Trailer", "4m x 2.5m");
        Vehiculo v5 = new Vehiculo("MN654OP", "Motorhome Compacto", "Motorhome", "6m x 2.5m");
        Vehiculo v6 = new Vehiculo("QR987ST", "Camión Mediano", "Trailer", "8m x 3m");
        Vehiculo v7 = new Vehiculo("UV246WX", "Casa Rodante Premium", "Motorhome", "9m x 3.2m");
        Vehiculo v8 = new Vehiculo("YZ135AB", "Trailer Ganadero", "Motorhome", "5.5m x 2.8m");
        Vehiculo v9 = new Vehiculo("BC369DE", "Camioneta Pickup", "Motorhome", "5.2m x 2m");
        Vehiculo v10 = new Vehiculo("FG741HI", "Furgón Escolar", "Motorhome", "7.5m x 2.5m");

// Agregar a la base de datos
        BaseDeDatos.vehiculo.add(v1);
        BaseDeDatos.vehiculo.add(v2);
        BaseDeDatos.vehiculo.add(v3);
        BaseDeDatos.vehiculo.add(v4);
        BaseDeDatos.vehiculo.add(v5);
        BaseDeDatos.vehiculo.add(v6);
        BaseDeDatos.vehiculo.add(v7);
        BaseDeDatos.vehiculo.add(v8);
        BaseDeDatos.vehiculo.add(v9);
        BaseDeDatos.vehiculo.add(v10);

        socio1.agregarVehiculo(v1);
        socio1.agregarVehiculo(v2);
        BaseDeDatos.admin.add(adminis);
        BaseDeDatos.socios.add(socio1);
        BaseDeDatos.empleados.add(fe.crearEmpleado("32589632", "Jose", "queseyo 111", 11.36, "dificil", 01, "Lavador"));
        BaseDeDatos.empleados.add(fe.crearEmpleado("40632582", "Luis", "ajaja 221", 1196523698, "media", 02, "Tecnico"));
        BaseDeDatos.empleados.add(fe.crearEmpleado("41589632", "Pepe", "Libia 798", 1144789632, "facil", 03, "Playero"));
        BaseDeDatos.empleados.add(fe.crearEmpleado("11111111", "Carlos Pérez", "Calle 1", 111111111, "clave1", 101, "Tecnico"));
        BaseDeDatos.empleados.add(fe.crearEmpleado("22222222", "María López", "Calle 2", 222222222, "clave2", 102, "Lavador"));
        BaseDeDatos.empleados.add(fe.crearEmpleado("33333333", "Diego Torres", "Calle 3", 333333333, "clave3", 103, "Playero"));

        Zona zonaA = new Zona('A', "Motorhome", 5, 7.0, 3.0);
        Zona zonaB = new Zona('B', "Trailer", 5, 5.0, 2.5);
        BaseDeDatos.zonas.add(zonaA);
        BaseDeDatos.zonas.add(zonaB);

        Garage g1 = new Garage(101, 500.0, true);
        Garage g2 = new Garage(102, 450.0, true);
        Garage g3 = new Garage(103, 450.0, true);
        Garage g4 = new Garage(104, 450.0, true);
        Garage g5 = new Garage(1, 100.0, true);
        Garage g6 = new Garage(2, 120.0, false);
        Garage g7 = new Garage(3, 90.0, true);
        
        
        g1.comprarGarage(socio1, LocalDate.of(2023, 5, 10));
        g2.comprarGarage(socio1, LocalDate.of(2023, 5, 11));
        g3.asignarVehiculo(v10, LocalDate.of(2023, 5, 1));
        g4.asignarVehiculo(v6, LocalDate.MIN);
        BaseDeDatos.garages.add(g1);
        BaseDeDatos.garages.add(g2);
        BaseDeDatos.garages.add(g3);
        BaseDeDatos.garages.add(g4);
        BaseDeDatos.garages.add(g5);
        BaseDeDatos.garages.add(g6);
        BaseDeDatos.garages.add(g7);
        zonaA.agregarGarage(g1);
        zonaB.agregarGarage(g2);
        zonaA.agregarGarage(g3);
        zonaB.agregarGarage(g4);
        zonaA.agregarGarage(g5);
        zonaB.agregarGarage(g6);
        Administrador admin = new Administrador("12345678", "Juan Admin", "Calle Admin 123", 1122334455, "admin123");
        BaseDeDatos.administradorActual = admin;

        Zona zonaC = new Zona('C', "Motorhome", 0, 5.0, 2.5);

        zonaB.agregarGarage(g3);

    }

}
