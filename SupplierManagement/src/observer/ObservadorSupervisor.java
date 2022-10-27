package observer;

import negocio.factura.Factura;

import javax.swing.*;

public class ObservadorSupervisor implements Observador{

    @Override
    public void actualizar(Sujeto sujeto) {

        if (sujeto instanceof Factura) {

            Factura factRecibida = ((Factura)sujeto);

            if (factRecibida.isAnulada()){
                notificarSupervisor(factRecibida);
                notificarGerente(factRecibida);
            } else {

                if (factRecibida.getSaldo() > 1000000) {
                    notificarGerente(factRecibida);
                } else {
                    if (factRecibida.getSaldo() > 50000) {
                        notificarSupervisor(factRecibida);
                    }
                }
            }
        }
    }

    private void notificarSupervisor(Factura factura){
        if (factura.isAnulada()) {
            String texto = "Se notificó al supervisor de sucursal que la factura fue anulada!";
            System.out.println(" (!) Atención: " + texto);
            JOptionPane.showMessageDialog(null, texto, "Atención!", JOptionPane.INFORMATION_MESSAGE);
        } else {
            if (factura.getSaldo() > 50000) {
                String texto = "Se notificó al supervisor de sucursal que usted cargó una factura que supera los $50.000";
                System.out.println(" (!) Atención: " + texto);
                JOptionPane.showMessageDialog(null, texto, "Atención!", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void notificarGerente(Factura factura){
        if (factura.isAnulada()) {
            String texto = "Se notificó al gerente de la empresa que la factura fue anulada!";
            System.out.println(" (!) Atención: " + texto);
            JOptionPane.showMessageDialog(null, texto, "Atención!", JOptionPane.INFORMATION_MESSAGE);
        } else {
            if (factura.getSaldo() > 1000000) {
                String texto = "Se notificó al gerente de la empresa que usted cargó una factura que supera el $1.000.000";
                System.out.println(" (!) Atención: " + texto);
                JOptionPane.showMessageDialog(null, texto, "Atención!", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }


}
