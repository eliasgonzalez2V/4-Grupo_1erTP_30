package proyecto;

public class Vehiculo {

    private String matricula;
    private String nombre;
    private String tipo;
    private String dimensiones;
    private int estadoDeFrenos;
    private int estadoDeBateria;
    private int estadoDeLimpieza;

    public Vehiculo(String matricula, String nombre, String tipo, String dimensiones) {
        this.matricula = matricula;
        this.nombre = nombre;
        this.tipo = tipo;
        this.dimensiones = dimensiones;
        this.estadoDeFrenos = 0;
        this.estadoDeBateria = 0;
        this.estadoDeLimpieza = 0;
    }

    public Vehiculo(String matricula, boolean estadoDeFrenos, boolean estadoDeBateria, boolean estadoDeLimpieza) {
    
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

    public int isEstadoDeFrenos() {
        return estadoDeFrenos;
    }

    public void setEstadoDeFrenos(int estadoDeFrenos) {
        this.estadoDeFrenos = estadoDeFrenos;
    }

    public int isEstadoDeBateria() {
        return estadoDeBateria;
    }

    public void setEstadoDeBateria(int estadoDeBateria) {
        this.estadoDeBateria = estadoDeBateria;
    }

    public int isEstadoDeLimpieza() {
        return estadoDeLimpieza;
    }

    public void setEstadoDeLimpieza(int estadoDeLuces) {
        this.estadoDeLimpieza = estadoDeLuces;
    }


}
