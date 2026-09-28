package AREA67.proyecto;

public class GestorExperiencia {
	private int nivelActual;
    private int xpActual;
    private int xpRequeridaActual;
    private int[] umbralesPorNivel;
    
    public GestorExperiencia() {
        this.nivelActual = 1;
        this.xpActual = 0;
        this.umbralesPorNivel = new int[]{5, 15, 35, 65, 105};
        this.xpRequeridaActual = umbralesPorNivel[0];
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
