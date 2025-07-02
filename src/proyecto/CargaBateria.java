package proyecto;

public class CargaBateria implements CargaDeVehiculo {

    @Override
    public void cargarVehiculo() {
        System.out.println("1-Carga de bateria");
    }

    @Override
    public void cargarVehiculo(Vehiculo vehiculo) {
        System.out.println("Carga de bateria en el vehiculo: "+vehiculo.getMatricula());
        vehiculo.setEstadoDeBateria(0);
    }

}
