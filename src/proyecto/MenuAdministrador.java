package proyecto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuAdministrador implements Menu {

    @Override
    public void mostrar() {
        Administrador admin = BaseDeDatos.administradorActual;
        ArrayList<Socio> socios = BaseDeDatos.socios;
        ArrayList<Garage> garages = BaseDeDatos.garages;
        ArrayList<Empleado> empleados = BaseDeDatos.empleados;

        ArrayList<Zona> zonas = BaseDeDatos.zonas;

        Scanner input = new Scanner(System.in);
        int opcion;
        do {
            System.out.println("\n--- Menú Administrador ---");
            System.out.println("1. Asignar garage a vehículo");
            System.out.println("2. Ver fecha de asignación de un garage");
            System.out.println("3. Mostrar garages asignados");
            System.out.println("4. Mostrar vehículos asignados");
            System.out.println("5. Mostrar fecha de compra de garages");
            System.out.println("6. Marcar un garage como comprado");
            System.out.println("7. Asignar empleado a zona");
            System.out.println("8. Mostrar empleados asignados a cada zona");
            System.out.println("9. Trbajos de los empleados");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            opcion = input.nextInt();
            input.nextLine();

            switch (opcion) {
                case 1 ->
                    asignarGarage(garages);
                case 2 ->
                    fechaDeAsignacion(garages);
                case 3 ->
                    garagesAsignados(garages);
                case 4 ->
                    vehiculosAsignados(garages);
                case 5 ->
                    fechaDeCompra(garages);
                case 6 ->
                    garageComprado(garages, socios);
                case 7 ->
                    asignarEmpleadoAZona();
                case 8 ->
                    mostrarEmpleadosPorZona();
                case 9 ->
                    EmpleadosTrabajo(empleados);
                case 0 ->
                    System.out.println("Saliendo del menú administrador.");
                default ->
                    System.out.println("Opción inválida.");
            }

        } while (opcion != 0);
    }

    public void asignarGarage(ArrayList<Garage> garages) {
        Scanner input = new Scanner(System.in);
        System.out.println("Asignar Garage a Vehículo");
        System.out.print("Ingrese matrícula del vehículo: ");
        String matricula = input.nextLine();

        for (Garage g : garages) {
            if (g.getVehiculo() != null && g.getVehiculo().getMatricula().equalsIgnoreCase(matricula)) {
                System.out.println("Error: Ya hay un vehículo con esa matrícula asignado.");
                return;
            }
        }

        System.out.print("Ingrese nombre del vehículo: ");
        String nombre = input.nextLine();
        System.out.print("Ingrese tipo de vehículo: ");
        String tipo = input.nextLine();
        System.out.print("Ingrese dimensiones: ");
        String dimensiones = input.nextLine();

        Vehiculo nuevoVehiculo = new Vehiculo(matricula, nombre, tipo, dimensiones);

        for (Garage g : garages) {
            if (g.getVehiculo() == null) {
                if (g.getTipoDeRenta() == 0) {
                    g.asignarVehiculo(nuevoVehiculo, LocalDate.now());
                    System.out.println("Vehículo asignado correctamente al garage " + g.getNroDeGarage());
                    return;
                }
            }
        }

        System.out.println("No hay garages disponibles.");
    }

    public void fechaDeAsignacion(ArrayList<Garage> garages) {
        Scanner input = new Scanner(System.in);
        System.out.print("Ingrese número de garage: ");
        int nro = input.nextInt();
        for (Garage g : garages) {
            if (g.getNroDeGarage() == nro) {
                if (g.getFechaAsignacion() != null) {
                    System.out.println("Fecha de asignación: " + g.getFechaAsignacion());
                } else {
                    System.out.println("Garage no asignado aún.");
                }
                return;
            }
        }
        System.out.println("Garage no encontrado.");
    }

    public void garagesAsignados(ArrayList<Garage> garages) {
        for (Garage g : garages) {
            if (g.getVehiculo() != null) {
                System.out.println("Garage " + g.getNroDeGarage() + " asignado a vehículo " + g.getVehiculo().getMatricula());
            }
        }
    }

    public void vehiculosAsignados(ArrayList<Garage> garages) {
        for (Garage g : garages) {
            if (g.getVehiculo() != null) {
                System.out.println("Vehículo: " + g.getVehiculo());
            }
        }
    }

    public void fechaDeCompra(ArrayList<Garage> garages) {
        boolean hayComprados = false;

        for (Garage g : garages) {
            if (g.getTipoDeRenta() == 2 ) {
                if (!hayComprados) {
                    System.out.println("\n--- Garages comprados ---");
                    hayComprados = true;
                }
                System.out.println("Garage N° " + g.getNroDeGarage()
                        + " | Fecha de compra: " + g.getFechaCompra()
                        + " | Socio: " + g.getDueno().getDNI());
            }
        }

        if (!hayComprados) {
            System.out.println("\nNo hay garages comprados.");
        }
    }

    public void garageComprado(ArrayList<Garage> garages, ArrayList<Socio> socios) {
        Scanner input = new Scanner(System.in);
        System.out.print("Ingrese numero de garage: ");
        int nro = input.nextInt();
        input.nextLine();

        for (Garage g : garages) {
            if (g.getNroDeGarage() == nro) {
                if (g.getTipoDeRenta() == 2 || g.getDueno() != null || g.getFechaCompra() != null) {
                    System.out.println("Este garage ya fue comprado.");
                    return;
                }

                System.out.print("Ingrese DNI del socio: ");
                String dni = input.nextLine();

                for (Socio socio : socios) {
                    if (socio.getDNI().equalsIgnoreCase(dni)) {
                        g.comprarGarage(socio, LocalDate.now());
                        g.setFechaCompra(LocalDate.now());

                        System.out.println("\nEl garage N° " + g.getNroDeGarage()
                                + " fue comprado correctamente por el socio " + socio.getNombre()
                                + " (DNI: " + socio.getDNI() + ") en fecha: " + g.getFechaCompra());
                        return;
                    }
                }

                System.out.println("Socio con ese DNI no encontrado.");
                return;
            }
        }

        System.out.println("Garage no encontrado.");
    }

    public void asignarEmpleadoAZona() {
        Scanner input = new Scanner(System.in);

        Zona zonaSeleccionada = null;
        while (true) {
            System.out.print("\nIngrese letra de zona: ");
            char idZona = input.next().toUpperCase().charAt(0);
            input.nextLine();

            for (Zona z : BaseDeDatos.zonas) {
                if (z.getId() == idZona) {
                    zonaSeleccionada = z;
                    break;
                }
            }

            if (zonaSeleccionada != null) {
                break;
            }

            System.out.println("\nZona no encontrada.");
            int opcion;
            do {
                System.out.println("\n¿Desea volver a intentarlo?");
                System.out.println("1. Si");
                System.out.println("2. Volver al menu");
                System.out.print("Opcion: ");
                opcion = input.nextInt();
                input.nextLine();
            } while (opcion != 1 && opcion != 2);

            if (opcion == 2) {
                return;
            }
        }

        Empleado empleadoSeleccionado = null;
        while (true) {
            System.out.print("\nIngrese DNI del empleado: ");
            String dni = input.nextLine();

            for (Empleado e : BaseDeDatos.empleados) {
                if (e.getDNI().equalsIgnoreCase(dni)) {
                    empleadoSeleccionado = e;
                    break;
                }
            }

            if (empleadoSeleccionado != null) {
                break;
            }

            System.out.println("\nEmpleado no encontrado.");
            int opcion;
            do {
                System.out.println("\n¿Desea volver a intentarlo?");
                System.out.println("1. Si");
                System.out.println("2. Volver al menu");
                System.out.print("Opcion: ");
                opcion = input.nextInt();
                input.nextLine();
            } while (opcion != 1 && opcion != 2);

            if (opcion == 2) {
                return;
            }
        }

        if (!empleadoSeleccionado.getZonasAsignadas().contains(zonaSeleccionada)) {
            zonaSeleccionada.asignarEmpleado(empleadoSeleccionado);
            empleadoSeleccionado.asignarZona(zonaSeleccionada);
        }

        String tipoZona = zonaSeleccionada.getTipoVehiculos();
        ArrayList<Vehiculo> disponibles = new ArrayList<>();
        for (Garage g : BaseDeDatos.garages) {
            Vehiculo v = g.getVehiculo();
            if (v != null && v.getTipo().equalsIgnoreCase(tipoZona)) {
                disponibles.add(v);
            }
        }

        if (disponibles.isEmpty()) {
            System.out.println("\nNo hay vehiculos disponibles a asignar en la zona. ");
            return;
        }

        boolean seguir = true;
        while (seguir && !disponibles.isEmpty()) {
            System.out.print("\nIngrese matricula del vehiculo a asignar: ");
            String matricula = input.nextLine();
            Vehiculo vehiculoSeleccionado = null;

            for (Vehiculo v : disponibles) {
                if (v.getMatricula().equalsIgnoreCase(matricula)) {
                    vehiculoSeleccionado = v;
                    break;
                }
            }

            if (vehiculoSeleccionado == null) {
                System.out.println("\nMatricula no valida o vehiculo no disponible.");
                int opcion;
                do {
                    System.out.println("\n¿Desea volver a intentarlo?");
                    System.out.println("1. Si");
                    System.out.println("2. Volver al menu");
                    System.out.print("Opcion: ");
                    opcion = input.nextInt();
                    input.nextLine();
                } while (opcion != 1 && opcion != 2);
                if (opcion == 2) {
                    return;
                } else {
                    continue;
                }
            }

            boolean yaAsignadoAGeneral = false;
            for (Empleado otroEmpleado : BaseDeDatos.empleados) {
                for (Vehiculo v : otroEmpleado.getVehiculosAsignados()) {
                    if (v.getMatricula().equalsIgnoreCase(vehiculoSeleccionado.getMatricula())) {
                        yaAsignadoAGeneral = true;
                        break;
                    }
                }
                if (yaAsignadoAGeneral) {
                    break;
                }
            }

            if (yaAsignadoAGeneral) {
                System.out.println("\nEste vehiculo ya esta asignado. No se puede volver a asignar.");
            } else {
                empleadoSeleccionado.getVehiculosAsignados().add(vehiculoSeleccionado);
                disponibles.remove(vehiculoSeleccionado);
                System.out.println("\nVehículo " + vehiculoSeleccionado.getMatricula() + " asignado correctamente.");
            }

            if (disponibles.isEmpty()) {
                System.out.println("\nNo hay mas vehiculos disponibles del tipo " + tipoZona + ".");
                System.out.println("Ya no se puede asignar otro vehiculo");
                seguir = false;
            } else {
                String respuesta;
                do {
                    System.out.print("\n¿Desea asignar otro vehículo? (s/n): ");
                    respuesta = input.nextLine().trim().toLowerCase();
                } while (!respuesta.equals("s") && !respuesta.equals("n"));

                seguir = respuesta.equals("s");
            }
        }
    }

    public void mostrarEmpleadosPorZona() {
        System.out.println("\n---Empleados asignados por Zona---");

        for (Zona zona : BaseDeDatos.zonas) {
            System.out.println("\nZona " + zona.getId() + " - Tipo de vehiculos: " + zona.getTipoVehiculos());

            ArrayList<Empleado> empleados = zona.getEmpleados();

            if (empleados.isEmpty()) {
                System.out.println(" No hay empleados asignados a esta zona.");
                continue;
            }

            for (Empleado e : empleados) {
                System.out.println(" Empleado: " + e.getNombre() + " | DNI: " + e.getDNI());

                ArrayList<Vehiculo> vehiculosEnZona = new ArrayList<>();
                for (Vehiculo v : e.getVehiculosAsignados()) {
                    if (v.getTipo().equalsIgnoreCase(zona.getTipoVehiculos())) {
                        vehiculosEnZona.add(v);
                    }
                }

                System.out.println("   Vehiculos asignados en zona " + zona.getId() + ": " + vehiculosEnZona.size());

                if (vehiculosEnZona.isEmpty()) {
                    System.out.println("   (Sin vehiculos en esta zona)");
                } else {
                    System.out.println("   Matriculas:");
                    for (Vehiculo v : vehiculosEnZona) {
                        System.out.println("     - " + v.getMatricula());
                    }
                }
            }
        }
    }

    public void EmpleadosTrabajo(ArrayList<Empleado> empleados) {
        for (Empleado empleado : empleados) {
            System.out.println("Nombre: " + empleado.getNombre());
            System.out.println("DNI: " + empleado.getDNI());
            System.out.println("Especialidad: " + empleado.getEspecialidad());
            empleado.cargarVehiculo();
            empleado.lavarVehiculo();
            empleado.mantenerVehiculo();
            System.out.println("");
        }

    }

}

/*








    public void comprarNuevoGarage() {
    Scanner input = new Scanner(System.in);

    System.out.println("== Compra de nuevo garage ==");

    System.out.print("Ingrese número de garage: ");
    int numero = input.nextInt();

    // Verificar si ya existe un garage con ese número
    for (Garage g : garages) {
        if (g.getNroDeGarage() == numero) {
            System.out.println("Ya existe un garage con ese número.");
            return;
        }
    }

    System.out.print("Ingrese contador de luz inicial: ");
    double contador = input.nextDouble();

    System.out.print("¿Tiene mantenimiento contratado? (true/false): ");
    boolean mantenimiento = input.nextBoolean();

    System.out.print("Ingrese tipo de renta (número entero): ");
    int tipoRenta = input.nextInt();

    Garage nuevo = new Garage(numero, contador, mantenimiento, null, tipoRenta, null);
    nuevo.setFechaCompra(LocalDate.now());

    garages.add(nuevo);
    System.out.println("Garage comprado y agregado correctamente.");
}



















public class MenuAdministrador implements menu{
    
    
    private Administrador administrador;
    private ArrayList<Zona> zonas;
    private ArrayList<Empleado> empleados;

    public MenuAdministrador(Administrador administrador, ArrayList<Zona> zonas, ArrayList<Empleado> empleados) {
        this.administrador = administrador;
        this.zonas = zonas;
        this.empleados = empleados;
    }

    @Override
    public void mostrar() {
        Scanner input = new Scanner(System.in);
        int opcion;
        do {
            System.out.println("\n--- Menú Administrador ---");
            System.out.println("1. Ver datos del administrador");
            System.out.println("2. Ver todas las zonas");
            System.out.println("3. Ver garages de una zona");
            System.out.println("4. Asignar empleado a zona");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            opcion = input.nextInt();
            input.nextLine();

            System.out.println();

            switch (opcion) {
                case 1 -> mostrarDatos();
                case 2 -> mostrarZonas();
                case 3 -> verGaragesPorZona();
                case 4 -> asignarEmpleadoAZona();
                case 0 -> System.out.println("Saliendo del menú administrador.");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    private void mostrarDatos() {
        System.out.println("Nombre: " + administrador.getNombre());
        System.out.println("DNI: " + administrador.getDNI());
    }

    private void mostrarZonas() {
        for (Zona z : zonas) {
            System.out.println("Zona: " + z.getId() + " | Tipo: " + z.getTipoVehiculos());
        }
    }

    private void verGaragesPorZona() {
        Scanner input = new Scanner(System.in);
        System.out.print("Ingrese letra de zona: ");
        char idZona = input.next().toUpperCase().charAt(0);

        for (Zona z : zonas) {
            if (z.getId() == idZona) {
                for (Garage g : z.getGarages()) {
                    System.out.println("Garage N°: " + g.getNroDeGarage() + " | Contador: " + g.getContadorLuz());
                }
                return;
            }
        }
        System.out.println("Zona no encontrada.");
    }

    private void asignarEmpleadoAZona() {
        Scanner input = new Scanner(System.in);
        System.out.print("Ingrese nombre del empleado: ");
        String nombre = input.nextLine();
        Empleado elegido = null;

        for (Empleado e : empleados) {
            if (e.getNombre().equalsIgnoreCase(nombre)) {
                elegido = e;
                break;
            }
        }

        if (elegido == null) {
            System.out.println("Empleado no encontrado.");
            return;
        }

        System.out.print("Ingrese letra de zona a asignar: ");
        char zonaId = input.next().toUpperCase().charAt(0);
        for (Zona z : zonas) {
            if (z.getId() == zonaId) {
                z.asignarEmpleado(elegido);
                System.out.println("Empleado asignado a zona " + zonaId);
                return;
            }
        }

        System.out.println("Zona no encontrada.");
    }
    
    
    
    public void asignarGarage(){
        
    }
    public void fechaDeAsignacion(){
        
    }
    public void garageAsignado(){
        
    }
    public void vehiculoAsignado(){
        
    }
    public void fechaDeCompra(){
        
    }
    public void garageComprado(){
        
    }
    public void asignarEmpleado(){
        
    }
    public void comprarGarage(){
        
    }

    public void mostrarZonaAsignada() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void mostrarVehiculosAsignados() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
}*/
