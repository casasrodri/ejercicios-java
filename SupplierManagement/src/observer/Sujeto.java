package observer;

public interface Sujeto {
    void agregarObservador(Observador obs);
    void notificarObservadores();
}
