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

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public double getTelefono() {
        return telefono;
    }

    public void setTelefono(double telefono) {
        this.telefono = telefono;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

}
