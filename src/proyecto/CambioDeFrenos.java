package proyecto;

public class CambioDeFrenos implements MantenimientoDeFrenos {

    @Override
    public void mantenimientoFrenos() {
        System.out.println("1-Cambiando frenos");
    }

    @Override
    public void mantenimientoFrenos(Vehiculo vehiculo) {
        System.out.println("Cambiando frenos en el vehiculo: "+vehiculo.getMatricula());
        vehiculo.setEstadoDeFrenos(0);
    }
    
}
