package proyecto;

public class LavadoProfundo implements LavadoDeVehiculo {

    @Override
    public void lavarVehiculo() {
        System.out.println("1-Lavado Profundo");
    }

    @Override
    public void lavarVehiculo(Vehiculo vehiculo) {
        System.out.println("Lavado Profundo en el vehiculo: "+vehiculo.getMatricula());
        vehiculo.setEstadoDeLimpieza(0);
    }
    
}
