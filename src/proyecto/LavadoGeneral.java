package proyecto;

public class LavadoGeneral implements LavadoDeVehiculo {

    @Override
    public void lavarVehiculo() {
        System.out.println("2-Lavado general");
    }

    @Override
    public void lavarVehiculo(Vehiculo vehiculo) {
        System.out.println("Lavado general en el vehiculo: " + vehiculo.getMatricula());
        vehiculo.setEstadoDeLimpieza(1);
    }
}
