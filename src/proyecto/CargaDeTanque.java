package proyecto;

public class CargaDeTanque implements CargaDeVehiculo {

    @Override
    public void cargarVehiculo() {
        System.out.println("2-Carga de tanque");
    }

    @Override
    public void cargarVehiculo(Vehiculo vehiculo) {
      System.out.println("Carga de tanque en el vehiculo: "+vehiculo.getMatricula());
        vehiculo.setEstadoDeBateria(1);
    }

}
