package codigo.adapter;

public class AdaptadorPagosBancario implements ServicioPagos {

    @Override
    public boolean cobrarFianza(String idEstudiante, double monto) {
        // Traduce a la firma del banco: POST /cobros { "cuenta": id, "importe": monto, "moneda": "MXN" }
        System.out.println("Cobro REST enviado al banco: " + idEstudiante + " — $" + monto);
        return true;
    }
}