package proyecto;

import java.util.*;
import java.time.LocalDate;

public class MenuSocio implements Menu {
    private Socio socio;
    private ArrayList<Garage> garages;
    private ArrayList<Zona> zonas;

    public MenuSocio() {
    }
    public MenuSocio(Socio socio, ArrayList<Garage> garages, ArrayList<Zona> zonas) {
        this.socio = socio;
        this.garages = garages;
        this.zonas = zonas;
    }

    @Override
    public void mostrar() {
        Scanner input = new Scanner(System.in);
        int opcion;
        do {
            System.out.println("\n--- Menu del Socio ---");
            System.out.println("1. Ver datos del socio");
            System.out.println("2. Ver garages asociados");
            System.out.println("3. Ver vehiculos del socio");
            System.out.println("4. Ver zonas donde tiene garage");
            System.out.println("5. Asignar vehiculo a un garage disponible");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            opcion = input.nextInt();
            input.nextLine();
            
            System.out.println("");
            
            switch (opcion) {
                case 1 -> mostrarDatos();
                case 2 -> mostrarGarages();
                case 3 -> mostrarVehiculos();
                case 4 -> mostrarZonas();
                case 5 -> asignarVehiculoAGarage();
                case 0 -> System.out.println("Saliendo del menu socio.");
                default -> System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
    }

    private void mostrarDatos() {
        System.out.println("Nombre: " + socio.getNombre());
        System.out.println("DNI: " + socio.getDNI());
        System.out.println("Direccion: " + socio.getDireccion());
        System.out.println("Telefono: " + socio.getTelefono());
        System.out.println("Fecha de ingreso: " + socio.getFechaDeIngreso());
    }

    private void mostrarGarages() {
        boolean tieneGarages = false;
        for (Zona z : zonas) {
            for (Garage g : z.getGarages()) {
                if (g.getDueno() != null && g.getDueno().getDNI().equals(socio.getDNI())) {
                    tieneGarages = true;
                    System.out.println("Garage N°: " + g.getNroDeGarage() + " | Zona: " + z.getId());
                    System.out.println("  Contador de luz: " + g.getContadorLuz());
                    System.out.println("  Vehículo asignado: " + (g.getVehiculo() != null ? g.getVehiculo() : "Ninguno"));
                    System.out.println();
                }
            }
        }
        if (!tieneGarages) {
            System.out.println("No tiene garages asignados.");
        }
    }

    private void mostrarVehiculos() {
        if (socio.getVehiculos().isEmpty()) {
            System.out.println("No hay vehículos registrados.");
            return;
        }

        for (Vehiculo v : socio.getVehiculos()) {
            System.out.println(v);
            boolean asignado = false;
            for (Zona z : zonas) {
                for (Garage g : z.getGarages()) {
                    if (g.getDueno() != null &&
                        g.getDueno().getDNI().equals(socio.getDNI()) &&
                        g.getVehiculo() != null &&
                        g.getVehiculo().getMatricula().equalsIgnoreCase(v.getMatricula())) {

                        System.out.println("Asignado al Garage N°" + g.getNroDeGarage() + " en zona " + z.getId());
                        asignado = true;
                    }
                }
            }
            if (!asignado) {
                System.out.println("No asignado a ningún garage");
            }
            System.out.println("");
        }
    }

    private void mostrarZonas() {
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

    private void asignarVehiculoAGarage() {
        Scanner input = new Scanner(System.in);
        if (socio.getVehiculos().isEmpty()) {
            System.out.println("No tiene vehículos para asignar.");
            return;
        }

        System.out.print("Ingrese matrícula del vehículo a asignar: ");
        String mat = input.nextLine();
        Vehiculo vehiculo = null;
        for (Vehiculo v : socio.getVehiculos()) {
            if (v.getMatricula().equalsIgnoreCase(mat)) {
                vehiculo = v;
                break;
            }
        }

        if (vehiculo == null) {
            System.out.println("No se encontró el vehículo con esa matrícula.");
            return;
        }

        ArrayList<Garage> garagesDisponibles = new ArrayList<>();
        for (Zona z : zonas) {
            if (z.getTipoVehiculos().equalsIgnoreCase(vehiculo.getTipo())) {
                for (Garage g : z.getGarages()) {
                    if (g.getDueno() != null &&
                        g.getDueno().getDNI().equals(socio.getDNI()) &&
                        g.getVehiculo() == null) {

                        garagesDisponibles.add(g);
                    }
                }
            }
        }

        if (garagesDisponibles.isEmpty()) {
            System.out.println("No hay garages disponibles compatibles para este vehículo.");
            return;
        }

        System.out.println("Garages disponibles para asignar:");
        for (Garage g : garagesDisponibles) {
            System.out.println("  Garage N°" + g.getNroDeGarage());
        }

        System.out.print("Ingrese número de garage para asignar el vehículo: ");
        int nroSel = input.nextInt();

        for (Garage g : garagesDisponibles) {
            if (g.getNroDeGarage() == nroSel) {
                g.asignarVehiculo(vehiculo, LocalDate.now());
                System.out.println("Vehículo asignado exitosamente al garage " + g.getNroDeGarage());
                return;
            }
        }

        System.out.println("Número de garage inválido o no disponible para este vehículo.");
    }
}
