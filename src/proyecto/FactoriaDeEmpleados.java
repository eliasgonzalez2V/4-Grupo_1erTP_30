package proyecto;

import java.util.ArrayList;

public class FactoriaDeEmpleados {

    private FactoriaDeEmpleados() {
    }

    private static FactoriaDeEmpleados f = null;

    public static FactoriaDeEmpleados getInstance() {
        if (f == null) {
            f = new FactoriaDeEmpleados();
        }
        return f;
    }

    public Empleado crearEmpleado(String DNI, String nombre, String direccion, double telefono, String contrasenia, int codigo, String especialidad) {
        Empleado e = null;
        try {
            e = (Empleado) Class.forName(f.getClass().getPackage().getName() + "."
                    + especialidad).getDeclaredConstructor(String.class, String.class, String.class, double.class, String.class, int.class, String.class)
                    .newInstance(DNI, nombre, direccion, telefono, contrasenia, codigo, especialidad);
        } catch (Exception ex) {
            System.err.println(ex);
        }
        if (e == null) {
            throw new IllegalArgumentException(especialidad);
        }
        return e;
    }
}
