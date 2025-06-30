package proyecto;

public abstract class Persona {
    private String DNI;
    private String nombre;
    private String direccion;
    private double telefono;
    private String contrasenia;
    private Menu persMenu;

    public Persona(String DNI, String nombre, String direccion, double telefono, String contrasenia) {
        this.DNI = DNI;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.contrasenia = contrasenia;
    }
    
    public void setpersMenu(Menu persMenu){
        this.persMenu=persMenu;
    }
    public String getDNI() {
        return DNI;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public double getTelefono() {
        return telefono;
    }

    public String getContrasenia() {
        return contrasenia;
    }
}

