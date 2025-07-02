package proyecto;

public class CambioLiquidoDeFrenos implements MantenimientoDeFrenos {

    @Override
    public void mantenimientoFrenos() {
        System.out.println("2-Cambio de liquido de frenos");
    }

    @Override
    public void mantenimientoFrenos(Vehiculo vehiculo) {
        System.out.println("Cambio de liquido de frenos en el vehiculo: "+vehiculo.getMatricula());
        vehiculo.setEstadoDeFrenos(1);
    }
}
