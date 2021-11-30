import BST.BinaryTree;
import BST.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Programa {

    public static void main(String[] args) {

        final int LOTES_SIMULADOS = 2000;

        List lotesRecibidos = new ArrayList();
        BinaryTree bst = new BinaryTree();
        
        //Agrego registros a la lista
        for (int i = 0; i < LOTES_SIMULADOS; i++) {
            Lote lote = new Lote(i+1);
            lotesRecibidos.add(lote);

            bst.insert((int) lote.getPorcentajeRechazo()*100);
        }
        
        //Muestro lotes
        for (Object l: lotesRecibidos){
            ((Lote) l).evaluarRendimiento();
            //System.out.println(l.toString());
        }

        Proveedor prov = new Proveedor(2);
        System.out.println(prov.toString());


        bst.inOrden(bst.root);
    }





    public static void mostrarOrdenados(List lotesRecibidos){
        //Ordeno datos
        Collections.sort(lotesRecibidos, new Comparator<Lote>() {
            @Override
            public int compare(Lote z1, Lote z2) {
                if (z1.getPorcentajeRechazo() > z2.getPorcentajeRechazo())
                    return 1;
                if (z1.getPorcentajeRechazo() < z2.getPorcentajeRechazo())
                    return -1;
                return 0;
            }
        });

        for (Object l: lotesRecibidos){
            ((Lote) l).evaluarRendimiento();
            System.out.println(l.toString());
        }

        System.out.println();
    }
}




// Usamos: Árboles, Grafos, Tablas Hash, Num Aleatorios, Compresion