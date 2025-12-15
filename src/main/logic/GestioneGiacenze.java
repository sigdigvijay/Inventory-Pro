package src.main.logic;

import src.main.dao.ProductsDAO;
import src.main.dao.OrdineDAO;// ✅

import src.main.dao.MovementsDAO;
import java.time.LocalDateTime;

public class GestioneGiacenze {

    /**
     * Apply a movement (carico or scarico) to a product
     *
     * @param productId ID of the product
     * @param tipo "carico" or "scarico"
     * @param quantita Quantity to add or subtract
     * @param causale Description or reason for movement
     */
    public static void registraMovimento(String productId, String tipo, int quantita, String causale) {
        int currentStock = ProductsDAO.getStock(productId);

        if(tipo.equalsIgnoreCase("carico")) {
            currentStock += quantita;
        } else if(tipo.equalsIgnoreCase("scarico")) {
            currentStock -= quantita;
            if(currentStock < 0) currentStock = 0;
        } else {
            throw new IllegalArgumentException("Tipo movimento non valido: " + tipo);
        }

        // Update stock in ProductsDAO
        ProductsDAO.updateStock(productId, currentStock);

        // Add movement record in CSV
        String dataMovimento = LocalDateTime.now().toString();
        MovementsDAO.addMovement(productId, tipo, quantita, causale, dataMovimento);
    }

    /**
     * Automatically load products from an order (used when order is received)
     *
     * @param orderId ID of the order
     */
    public static void caricoDaOrdine(String orderId) {
        var righe = OrdineDAO.getLinesByOrderId(orderId);
        for(var riga : righe) {
            String productId = riga[0];
            int quantita = Integer.parseInt(riga[1]);
            String causale = "Carico da ordine #" + orderId;
            registraMovimento(productId, "carico", quantita, causale);
        }
    }

    /**
     * Check if a product is below minimum stock
     *
     * @param productId ID of the product
     * @return true if stock < min stock
     */
    public static boolean isLowStock(String productId) {
        int stock = ProductsDAO.getStock(productId);
        int minStock = ProductsDAO.getMinStock(productId);
        return stock < minStock;
    }
}
