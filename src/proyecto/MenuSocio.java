package proyecto;

import java.util.*;
import java.time.LocalDate;

public class MenuSocio implements Menu {

    @Override
    public void mostrar() {
        Socio socio = BaseDeDatos.socioActual;
        ArrayList<Zona> zonas = BaseDeDatos.zonas;
        ArrayList<Vehiculo> vehiculo = BaseDeDatos.vehiculo;

        Scanner input = new Scanner(System.in);
        int opcion;
        do {
            System.out.println("\n--- Menu del Socio ---");
            System.out.println("1. Ver datos del socio");
            System.out.println("2. Ver garages asociados");
            System.out.println("3. Ver vehiculos del socio");
            System.out.println("4. Ver zonas donde tiene garage");
            System.out.println("5. Asignar vehiculo a un garage disponible");
            System.out.println("6. Agregar vehiculo");
            System.out.println("0. Salir");
            System.out.print("Opcion: ");
            opcion = input.nextInt();
            input.nextLine();
            System.out.println("");

            switch (opcion) {
                case 1 ->
                    mostrarDatos(socio);
                case 2 ->
                    mostrarGarages(socio, zonas);
                case 3 ->
                    mostrarVehiculos(socio, zonas);
                case 4 ->
                    mostrarZonas(socio, zonas);
                case 5 ->
                    asignarVehiculoAGarage(socio, zonas);
                case 6 ->
                    asignarVehiculoNuevo(socio);
                case 0 ->
                    System.out.println("Saliendo del menu socio.");
                default ->
                    System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
    }

    private void mostrarDatos(Socio socio) {
        System.out.println("Nombre: " + socio.getNombre());
        System.out.println("DNI: " + socio.getDNI());
        System.out.println("Direccion: " + socio.getDireccion());
        System.out.println("Telefono: " + socio.getTelefono());
        System.out.println("Fecha de ingreso: " + socio.getFechaDeIngreso());
    }

    private void mostrarGarages(Socio socio, ArrayList<Zona> zonas) {
        boolean tieneGarages = false;
        for (Zona z : zonas) {
            for (Garage g : z.getGarages()) {
                if (g.getDueno() != null && g.getDueno().getDNI().equals(socio.getDNI())) {
                    tieneGarages = true;
                    System.out.println("Garage Num: " + g.getNroDeGarage() + " | Zona: " + z.getId());
                    System.out.println("  Contador de luz: " + g.getContadorLuz());
                    System.out.println("  Vehiculo asignado: " + (g.getVehiculo() != null ? g.getVehiculo() : "Ninguno"));
                    System.out.println();
                }
            }
        }
        if (!tieneGarages) {
            System.out.println("No tiene garages asignados.");
        }
    }

    private void mostrarVehiculos(Socio socio, ArrayList<Zona> zonas) {
        if (socio.getVehiculos().isEmpty()) {
            System.out.println("No hay vehiculos registrados.");
            return;
        }

        for (Vehiculo v : socio.getVehiculos()) {
            System.out.println(v);
            boolean asignado = false;
            for (Zona z : zonas) {
                for (Garage g : z.getGarages()) {
                    if (g.getDueno() != null && g.getDueno().getDNI().equals(socio.getDNI()) && g.getVehiculo() != null && g.getVehiculo().getMatricula().equalsIgnoreCase(v.getMatricula())) {
                        System.out.println("Asignado al Garage Num. " + g.getNroDeGarage() + " en zona " + z.getId());
                        asignado = true;
                    }
                }
            }
            if (!asignado) {
                System.out.println("No asignado a ningún garage");
            }
            System.out.println();
        }
    }

    private void mostrarZonas(Socio socio, ArrayList<Zona> zonas) {
        Set<Character> zonasDelSocio = new HashSet<>();
        for (Zona z : zonas) {
            for (Garage g : z.getGarages()) {
                if (g.getDueno() != null && g.getDueno().getDNI().equals(socio.getDNI())) {
                    zonasDelSocio.add(z.getId());
                }
            }
        }
        if (zonasDelSocio.isEmpty()) {
            System.out.println("No tiene garages en ninguna zona.");
        } else {
            System.out.println("Zonas donde tiene garage: " + zonasDelSocio);
        }
    }

    private void asignarVehiculoAGarage(Socio socio, ArrayList<Zona> zonas) {
        Scanner input = new Scanner(System.in);

        if (socio.getVehiculos().isEmpty()) {
            System.out.println("No tiene vehiculos para asignar.");
            return;
        }

        Vehiculo vehiculo = null;

        boolean continuarMatricula = true;
        while (continuarMatricula && vehiculo == null) {
            System.out.print("Ingrese matricula del vehiculo a asignar: ");
            String mat = input.nextLine();

            for (Vehiculo v : socio.getVehiculos()) {
                if (v.getMatricula().equalsIgnoreCase(mat)) {
                    vehiculo = v;
                    break;
                }
            }

            if (vehiculo == null) {
                System.out.println("\nNo se encontro el vehiculo con esa matricula.");
                System.out.println("¿Desea volver a intentarlo?");
                System.out.println("1. Si");
                System.out.println("2. Volver al menu");
                System.out.print("Opcion: ");
                String respuesta = input.nextLine();

                while (!respuesta.equals("1") && !respuesta.equals("2")) {
                    System.out.println("\nOpcion invalida.");

                    System.out.println("\nNo se encontro el vehiculo con esa matricula.");
                    System.out.println("¿Desea volver a intentarlo?");
                    System.out.println("1. Si");
                    System.out.println("2. Volver al menu");
                    System.out.print("Opción: ");
                    respuesta = input.nextLine();
                }

                if (respuesta.equals("2")) {
                    System.out.println("\nRegresando al menu...");
                    return;
                }
            }
        }

        ArrayList<Garage> garagesDisponibles = new ArrayList<>();
        for (Zona z : zonas) {
            if (z.getTipoVehiculos().equalsIgnoreCase(vehiculo.getTipo())) {
                for (Garage g : z.getGarages()) {
                    if (g.getDueno() != null
                            && g.getDueno().getDNI().equals(socio.getDNI())
                            && g.getVehiculo() == null) {
                        garagesDisponibles.add(g);
                    }
                }
            }
        }

        if (garagesDisponibles.isEmpty()) {
            System.out.println("No hay garages disponibles compatibles para este vehiculo.");
            return;
        }

        System.out.println("\nGarages disponibles para asignar:");
        for (Garage g : garagesDisponibles) {
            System.out.println("  Garage Num." + g.getNroDeGarage());
        }

        boolean asignado = false;
        while (!asignado) {
            System.out.print("Ingrese numero de garage para asignar el vehiculo: ");
            String numIngresado = input.nextLine();

            boolean esNumero = true;
            for (int i = 0; i < numIngresado.length(); i++) {
                if (!Character.isDigit(numIngresado.charAt(i))) {
                    esNumero = false;
                    break;
                }
            }

            if (!esNumero) {
                System.out.println("Debe ingresar un numero valido.");
                continue;
            }

            int nroSel = Integer.parseInt(numIngresado);
            for (Garage g : garagesDisponibles) {
                if (g.getNroDeGarage() == nroSel) {
                    g.asignarVehiculo(vehiculo, LocalDate.now());
                    System.out.println("\nVehiculo asignado exitosamente al garage " + g.getNroDeGarage());
                    asignado = true;
                    break;
                }
            }

            if (!asignado) {
                System.out.println("\nNumero de garage invalido o no disponible.");
                System.out.println("¿Desea volver a intentarlo?");
                System.out.println("1. Si");
                System.out.println("2. Volver al menu");
                System.out.print("Opcion: ");
                String respuesta = input.nextLine();

                while (!respuesta.equals("1") && !respuesta.equals("2")) {
                    System.out.println("\nOpcion invalida.");

                    System.out.println("\nNumero de garage invalido o no disponible.");
                    System.out.println("¿Desea volver a intentarlo?");
                    System.out.println("1. Si");
                    System.out.println("2. Volver al menu");
                    System.out.print("Opcion: ");
                    respuesta = input.nextLine();
                }

                if (respuesta.equals("2")) {
                    System.out.println("\nRegresando al menu...");
                    return;
                }
            }
        }
    }

    private void asignarVehiculoNuevo(Socio socio) {
        boolean encontrado;
        Scanner input = new Scanner(System.in);
        System.out.println("Ingrese los datos del Veiculo");
        System.out.print("Matricula:");
        String matricula;
        matricula = input.nextLine();
        do {
            encontrado = false;

            for (Vehiculo ve : BaseDeDatos.vehiculo) {
                if (ve.getMatricula().equalsIgnoreCase(matricula)) {
                    System.out.println("Matricula existente");
                    encontrado = true;
                }

            }
            if (encontrado) {
                System.out.println("Ingrese nuevamente la matricula");
                matricula = input.nextLine();
            }
        } while (encontrado);
        System.out.print("Nombre:");
        String nombre = input.nextLine();
        System.out.print("Tipo:");
        String tipo = input.nextLine();
        System.out.print("Dimensiones:");
        String dimen = input.nextLine();
        Vehiculo v = new Vehiculo(matricula, nombre, tipo, dimen);
        socio.agregarVehiculo(v);
        BaseDeDatos.vehiculo.add(v);
    }

}
