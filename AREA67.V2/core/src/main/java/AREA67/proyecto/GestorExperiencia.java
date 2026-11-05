package AREA67.proyecto;

public class GestorExperiencia {
    private int nivelActual;
    private int xpActual;
    private int xpRequeridaActual;
    private int[] umbralesPorNivel;
    private int nivelesPendientes; // <-- NUEVO: Para rastrear las mejoras sin reclamar
    
    public GestorExperiencia() {
        this.nivelActual = 1;
        this.xpActual = 0;
        this.umbralesPorNivel = new int[]{5, 15, 35, 65, 105};
        this.xpRequeridaActual = umbralesPorNivel[0];
        this.nivelesPendientes = 0;
    }
    
    public void sumarExperiencia(int cantidad) {
        if (cantidad <= 0) {
            return; 
        }

        xpActual += cantidad;

        while (xpActual >= xpRequeridaActual) {
            procesarSubidaDeNivel();
        }
    }
    
    private void procesarSubidaDeNivel() {
        xpActual -= xpRequeridaActual;
        
        nivelActual++;
        nivelesPendientes++; // <-- NUEVO: Sumamos 1 nivel pendiente al marcador
        
        actualizarXpRequerida();

        System.out.println("¡Nivel " + nivelActual + " alcanzado!");
    }
    
    private void actualizarXpRequerida() {
        if (nivelActual <= umbralesPorNivel.length) {
            xpRequeridaActual = umbralesPorNivel[nivelActual - 1];
        } else {
            xpRequeridaActual = (int) (xpRequeridaActual * 1.20);
        }
    }
    
    // --- NUEVOS MÉTODOS PARA EL MENÚ DE MEJORA ---
    public boolean tieneNivelesPendientes() {
        return nivelesPendientes > 0;
    }

    public void consumirNivelPendiente() {
        if (nivelesPendientes > 0) {
            nivelesPendientes--;
        }
    }
    // ---------------------------------------------
    
    public int getNivelActual() {
        return nivelActual;
    }

    public int getXpActual() {
        return xpActual;
    }

    public int getXpRequeridaActual() {
        return xpRequeridaActual;
    }
}