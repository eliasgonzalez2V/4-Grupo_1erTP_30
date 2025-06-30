package proyecto;

public class Vehiculo {
    private String matricula;
    private String nombre;
    private String tipo;
    private String dimensiones;
    private boolean estadoDeFrenos;
    private boolean estadoDeBateria;
    private boolean estadoDeLuces;

    public Vehiculo(String matricula, String nombre, String tipo, String dimensiones) {
        this.matricula = matricula;
        this.nombre = nombre;
        this.tipo = tipo;
        this.dimensiones = dimensiones;
        this.estadoDeFrenos = true;
        this.estadoDeBateria = true;
        this.estadoDeLuces = true;
    }

    public String getMatricula() { 
        return matricula; 
    }
    
    public String getNombre() { 
        return nombre; 
    }
    
    public String getTipo() { 
        return tipo; 
    }
    
    public String getDimensiones() { 
        return dimensiones; 
    }

    public boolean isEstadoDeFrenos() {
        return estadoDeFrenos;
    }

    public void setEstadoDeFrenos(boolean estadoDeFrenos) {
        this.estadoDeFrenos = estadoDeFrenos;
    }

    public boolean isEstadoDeBateria() {
        return estadoDeBateria;
    }

    public void setEstadoDeBateria(boolean estadoDeBateria) {
        this.estadoDeBateria = estadoDeBateria;
    }

    public boolean isEstadoDeLuces() {
        return estadoDeLuces;
    }

    public void setEstadoDeLuces(boolean estadoDeLuces) {
        this.estadoDeLuces = estadoDeLuces;
    }

    @Override
    public String toString() {
        return "Vehículo: " + nombre +
               " [" + matricula + "] | Tipo: " + tipo +
               " | Dimensiones: " + dimensiones +
               " | Frenos: " + (estadoDeFrenos ? "OK" : "Mal") +
               " | Batería: " + (estadoDeBateria ? "OK" : "Mal") +
               " | Luces: " + (estadoDeLuces ? "OK" : "Mal");
    }
}