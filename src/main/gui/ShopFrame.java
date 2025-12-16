package src.main.gui;

import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class ShopFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Screens
    private SchermataNegozio schermataNegozio;
    private SchermataCarrello schermataCarrello;
    private SchermataOrdini schermataOrdini;

    // Dati
    private List<Prodotto> prodotti;
    private List<ItemCarrello> carrello;
    private List<Ordine> ordiniCompletati;

    public ShopFrame() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            System.err.println("Failed to initialize FlatLaf");
        }

        setTitle("ShopPro - Negozio Online");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        // Inizializza dati
        inizializzaDati();

        initComponents();
    }

    private void inizializzaDati() {
        prodotti = new ArrayList<>();
        carrello = new ArrayList<>();
        ordiniCompletati = new ArrayList<>();

        // Carica prodotti da CSV
        caricaProdottiDaCSV("data/prodotti.csv");
    }

    private void caricaProdottiDaCSV(String nomeFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(nomeFile))) {
            String linea;
            boolean primaLinea = true;

            while ((linea = br.readLine()) != null) {
                // Salta l'intestazione
                if (primaLinea) {
                    primaLinea = false;
                    continue;
                }

                // Dividi la linea per virgola
                String[] valori = linea.split(",");

                // Assicurati che ci siano almeno 5 campi
                if (valori.length >= 5) {
                    try {
                        String id = valori[0].trim();
                        String nome = valori[1].trim();
                        String categoria = valori[2].trim();
                        double prezzo = Double.parseDouble(valori[3].trim());
                        int disponibilita = Integer.parseInt(valori[4].trim());

                        prodotti.add(new Prodotto(id, nome, categoria, prezzo, disponibilita));
                    } catch (NumberFormatException e) {
                        System.err.println("Errore nel parsing della linea: " + linea);
                    }
                }
            }

            System.out.println("Caricati " + prodotti.size() + " prodotti dal CSV");

        } catch (IOException e) {
            System.err.println("Errore nella lettura del file CSV: " + e.getMessage());
            // Carica dati di esempio in caso di errore
        }
    }

    private void initComponents() {
        // Sidebar menu
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(0, 1, 5, 5));
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnNegozio = new JButton("📦 Catalogo Prodotti");
        JButton btnCarrello = new JButton("🛒 Carrello");
        JButton btnOrdini = new JButton("📋 I Miei Ordini");

        sidebar.add(btnNegozio);
        sidebar.add(btnCarrello);
        sidebar.add(btnOrdini);

        // CardLayout panel
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        schermataNegozio = new SchermataNegozio();
        schermataCarrello = new SchermataCarrello();
        schermataOrdini = new SchermataOrdini();

        mainPanel.add(schermataNegozio, "Negozio");
        mainPanel.add(schermataCarrello, "Carrello");
        mainPanel.add(schermataOrdini, "Ordini");

        // Button actions
        btnNegozio.addActionListener(e -> {
            schermataNegozio.aggiornaProdotti();
            cardLayout.show(mainPanel, "Negozio");
        });
        btnCarrello.addActionListener(e -> {
            schermataCarrello.aggiornaCarrello();
            btnCarrello.setText("🛒 Carrello (" + carrello.size() + ")");
            cardLayout.show(mainPanel, "Carrello");
        });
        btnOrdini.addActionListener(e -> {
            schermataOrdini.aggiornaOrdini();
            cardLayout.show(mainPanel, "Ordini");
        });

        // Layout
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(sidebar, BorderLayout.WEST);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    // ==================== SCHERMATA NEGOZIO ====================
    class SchermataNegozio extends JPanel {
        private JPanel prodottiPanel;
        private JScrollPane scrollPane;

        public SchermataNegozio() {
            setLayout(new BorderLayout(10, 10));
            setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // Header
            JLabel titleLabel = new JLabel("Catalogo Prodotti Disponibili");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            add(titleLabel, BorderLayout.NORTH);

            // Prodotti panel
            prodottiPanel = new JPanel();
            prodottiPanel.setLayout(new GridLayout(0, 3, 15, 15));

            scrollPane = new JScrollPane(prodottiPanel);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            add(scrollPane, BorderLayout.CENTER);

            aggiornaProdotti();
        }

        public void aggiornaProdotti() {
            prodottiPanel.removeAll();

            for (Prodotto p : prodotti) {
                if (p.disponibilita > 0) {
                    prodottiPanel.add(creaPannelloProdotto(p));
                }
            }

            prodottiPanel.revalidate();
            prodottiPanel.repaint();
        }

        private JPanel creaPannelloProdotto(Prodotto prodotto) {
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout(5, 5));
            panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));

            // Info prodotto
            JPanel infoPanel = new JPanel(new GridLayout(4, 1, 5, 5));
            JLabel nomeLabel = new JLabel(prodotto.nome);
            nomeLabel.setFont(new Font("Arial", Font.BOLD, 14));
            JLabel categoriaLabel = new JLabel(prodotto.categoria);
            categoriaLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            JLabel prezzoLabel = new JLabel("€" + String.format("%.2f", prodotto.prezzo));
            prezzoLabel.setFont(new Font("Arial", Font.BOLD, 16));
            prezzoLabel.setForeground(new Color(76, 175, 80));
            JLabel disponibilitaLabel = new JLabel("Disponibili: " + prodotto.disponibilita);
            disponibilitaLabel.setFont(new Font("Arial", Font.PLAIN, 11));

            infoPanel.add(nomeLabel);
            infoPanel.add(categoriaLabel);
            infoPanel.add(prezzoLabel);
            infoPanel.add(disponibilitaLabel);

            panel.add(infoPanel, BorderLayout.CENTER);

            // Controlli
            ItemCarrello itemEsistente = trovaInCarrello(prodotto.id);
            JPanel controlliPanel = new JPanel(new BorderLayout(5, 5));

            if (itemEsistente != null) {
                JPanel quantitaPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                JButton btnMeno = new JButton("-");
                JLabel lblQuantita = new JLabel(String.valueOf(itemEsistente.quantita));
                lblQuantita.setFont(new Font("Arial", Font.BOLD, 16));
                JButton btnPiu = new JButton("+");

                btnMeno.addActionListener(e -> {
                    diminuisciQuantita(prodotto.id);
                    aggiornaProdotti();
                });

                btnPiu.addActionListener(e -> {
                    aumentaQuantita(prodotto.id);
                    aggiornaProdotti();
                });

                quantitaPanel.add(btnMeno);
                quantitaPanel.add(lblQuantita);
                quantitaPanel.add(btnPiu);
                controlliPanel.add(quantitaPanel, BorderLayout.CENTER);
            } else {
                JButton btnAggiungi = new JButton("Aggiungi al Carrello");
                btnAggiungi.addActionListener(e -> {
                    aggiungiAlCarrello(prodotto);
                    aggiornaProdotti();
                });
                controlliPanel.add(btnAggiungi, BorderLayout.CENTER);
            }

            panel.add(controlliPanel, BorderLayout.SOUTH);

            return panel;
        }
    }

    // ==================== SCHERMATA CARRELLO ====================
    class SchermataCarrello extends JPanel {
        private DefaultTableModel tableModel;
        private JTable table;
        private JLabel totaleLabel;

        public SchermataCarrello() {
            setLayout(new BorderLayout(10, 10));
            setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // Header
            JLabel titleLabel = new JLabel("Il Mio Carrello");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            add(titleLabel, BorderLayout.NORTH);

            // Tabella
            String[] colonne = {"ID", "Prodotto", "Prezzo", "Quantità", "Subtotale", "Azioni"};
            tableModel = new DefaultTableModel(colonne, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 5;
                }
            };

            table = new JTable(tableModel);
            table.setRowHeight(40);
            table.getColumn("Azioni").setCellRenderer(new ButtonRenderer());
            table.getColumn("Azioni").setCellEditor(new ButtonEditor(new JCheckBox()));

            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);

            // Footer con totale
            JPanel footerPanel = new JPanel(new BorderLayout(10, 10));
            footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

            totaleLabel = new JLabel("Totale: €0.00");
            totaleLabel.setFont(new Font("Arial", Font.BOLD, 20));
            totaleLabel.setHorizontalAlignment(SwingConstants.RIGHT);

            JButton btnAcquista = new JButton("Completa Acquisto");
            btnAcquista.setFont(new Font("Arial", Font.BOLD, 16));
            btnAcquista.setPreferredSize(new Dimension(200, 50));
            btnAcquista.addActionListener(e -> completaAcquisto());

            footerPanel.add(totaleLabel, BorderLayout.CENTER);
            footerPanel.add(btnAcquista, BorderLayout.EAST);

            add(footerPanel, BorderLayout.SOUTH);

            aggiornaCarrello();
        }

        public void aggiornaCarrello() {
            tableModel.setRowCount(0);

            for (ItemCarrello item : carrello) {
                Prodotto p = trovaProdotto(item.idProdotto);
                if (p != null) {
                    Object[] row = {
                        p.id,
                        p.nome,
                        String.format("€%.2f", p.prezzo),
                        item.quantita,
                        String.format("€%.2f", p.prezzo * item.quantita),
                        "Gestisci"
                    };
                    tableModel.addRow(row);
                }
            }

            double totale = calcolaTotale();
            totaleLabel.setText("Totale: €" + String.format("%.2f", totale));
        }

        class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
            public ButtonRenderer() {
                setOpaque(true);
            }

            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                setText((value == null) ? "" : value.toString());
                return this;
            }
        }

        class ButtonEditor extends DefaultCellEditor {
            protected JButton button;
            private String label;
            private boolean isPushed;
            private int currentRow;

            public ButtonEditor(JCheckBox checkBox) {
                super(checkBox);
                button = new JButton();
                button.setOpaque(true);
                button.addActionListener(e -> {
                    fireEditingStopped();
                    mostraDialogoGestione(currentRow);
                });
            }

            public Component getTableCellEditorComponent(JTable table, Object value,
                    boolean isSelected, int row, int column) {
                currentRow = row;
                label = (value == null) ? "" : value.toString();
                button.setText(label);
                isPushed = true;
                return button;
            }

            public Object getCellEditorValue() {
                isPushed = false;
                return label;
            }

            private void mostraDialogoGestione(int row) {
                String idProdotto = (String) tableModel.getValueAt(row, 0);
                ItemCarrello item = trovaInCarrello(idProdotto);
                Prodotto p = trovaProdotto(idProdotto);

                if (item != null && p != null) {
                    String[] opzioni = {"+", "-", "Rimuovi", "Annulla"};
                    int scelta = JOptionPane.showOptionDialog(
                        SchermataCarrello.this,
                        "Quantità attuale: " + item.quantita + "\nDisponibilità: " + p.disponibilita,
                        "Gestisci " + p.nome,
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        opzioni,
                        opzioni[3]
                    );

                    switch (scelta) {
                        case 0: aumentaQuantita(idProdotto); break;
                        case 1: diminuisciQuantita(idProdotto); break;
                        case 2: rimuoviDalCarrello(idProdotto); break;
                    }

                    aggiornaCarrello();
                }
            }
        }
    }

    // ==================== SCHERMATA ORDINI ====================
    class SchermataOrdini extends JPanel {
        private JPanel ordiniPanel;

        public SchermataOrdini() {
            setLayout(new BorderLayout(10, 10));
            setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel titleLabel = new JLabel("I Miei Ordini");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            add(titleLabel, BorderLayout.NORTH);

            ordiniPanel = new JPanel();
            ordiniPanel.setLayout(new BoxLayout(ordiniPanel, BoxLayout.Y_AXIS));

            JScrollPane scrollPane = new JScrollPane(ordiniPanel);
            add(scrollPane, BorderLayout.CENTER);

            aggiornaOrdini();
        }

        public void aggiornaOrdini() {
            ordiniPanel.removeAll();

            if (ordiniCompletati.isEmpty()) {
                JLabel noOrdini = new JLabel("Nessun ordine completato");
                noOrdini.setFont(new Font("Arial", Font.PLAIN, 16));
                ordiniPanel.add(noOrdini);
            } else {
                for (Ordine ordine : ordiniCompletati) {
                    ordiniPanel.add(creaPannelloOrdine(ordine));
                    ordiniPanel.add(Box.createVerticalStrut(10));
                }
            }

            ordiniPanel.revalidate();
            ordiniPanel.repaint();
        }

        private JPanel creaPannelloOrdine(Ordine ordine) {
            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));
            panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

            // Header ordine
            JPanel headerPanel = new JPanel(new BorderLayout());
            JLabel idLabel = new JLabel(ordine.id);
            idLabel.setFont(new Font("Arial", Font.BOLD, 16));
            JLabel dataLabel = new JLabel(ordine.data);
            JLabel totaleLabel = new JLabel("€" + String.format("%.2f", ordine.totale));
            totaleLabel.setFont(new Font("Arial", Font.BOLD, 18));
            totaleLabel.setForeground(new Color(76, 175, 80));

            headerPanel.add(idLabel, BorderLayout.WEST);
            headerPanel.add(dataLabel, BorderLayout.CENTER);
            headerPanel.add(totaleLabel, BorderLayout.EAST);

            // Dettagli prodotti
            JTextArea dettagliArea = new JTextArea();
            dettagliArea.setEditable(false);
            StringBuilder sb = new StringBuilder();
            for (ItemCarrello item : ordine.prodotti) {
                Prodotto p = trovaProdotto(item.idProdotto);
                if (p != null) {
                    sb.append(String.format("%s - %dx €%.2f\n", 
                        p.nome, item.quantita, p.prezzo));
                }
            }
            dettagliArea.setText(sb.toString());

            panel.add(headerPanel, BorderLayout.NORTH);
            panel.add(new JScrollPane(dettagliArea), BorderLayout.CENTER);

            return panel;
        }
    }

    // ==================== METODI CARRELLO ====================
    private void aggiungiAlCarrello(Prodotto prodotto) {
        ItemCarrello item = trovaInCarrello(prodotto.id);
        if (item == null) {
            carrello.add(new ItemCarrello(prodotto.id, 1));
        }
    }

    private void aumentaQuantita(String idProdotto) {
        ItemCarrello item = trovaInCarrello(idProdotto);
        Prodotto p = trovaProdotto(idProdotto);
        if (item != null && p != null && item.quantita < p.disponibilita) {
            item.quantita++;
        }
    }

    private void diminuisciQuantita(String idProdotto) {
        ItemCarrello item = trovaInCarrello(idProdotto);
        if (item != null) {
            item.quantita--;
            if (item.quantita <= 0) {
                carrello.remove(item);
            }
        }
    }

    private void rimuoviDalCarrello(String idProdotto) {
        carrello.removeIf(item -> item.idProdotto.equals(idProdotto));
    }

    private ItemCarrello trovaInCarrello(String idProdotto) {
        return carrello.stream()
            .filter(item -> item.idProdotto.equals(idProdotto))
            .findFirst()
            .orElse(null);
    }

    private Prodotto trovaProdotto(String id) {
        return prodotti.stream()
            .filter(p -> p.id.equals(id))
            .findFirst()
            .orElse(null);
    }

    private double calcolaTotale() {
        double totale = 0;
        for (ItemCarrello item : carrello) {
            Prodotto p = trovaProdotto(item.idProdotto);
            if (p != null) {
                totale += p.prezzo * item.quantita;
            }
        }
        return totale;
    }

    private void completaAcquisto() {
        if (carrello.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Il carrello è vuoto!", 
                "Attenzione", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Ordine ordine = new Ordine(
            "ORD" + System.currentTimeMillis(),
            new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()),
            new ArrayList<>(carrello),
            calcolaTotale()
        );

        ordiniCompletati.add(ordine);
        carrello.clear();

        JOptionPane.showMessageDialog(this, 
            "Ordine completato con successo!", 
            "Successo", 
            JOptionPane.INFORMATION_MESSAGE);

        cardLayout.show(mainPanel, "Ordini");
        schermataOrdini.aggiornaOrdini();
    }

    // ==================== CLASSI DATI ====================
    static class Prodotto {
        String id, nome, categoria;
        double prezzo;
        int disponibilita;

        Prodotto(String id, String nome, String categoria, double prezzo, int disponibilita) {
            this.id = id;
            this.nome = nome;
            this.categoria = categoria;
            this.prezzo = prezzo;
            this.disponibilita = disponibilita;
        }
    }

    static class ItemCarrello {
        String idProdotto;
        int quantita;

        ItemCarrello(String idProdotto, int quantita) {
            this.idProdotto = idProdotto;
            this.quantita = quantita;
        }
    }

    static class Ordine {
        String id, data;
        List<ItemCarrello> prodotti;
        double totale;

        Ordine(String id, String data, List<ItemCarrello> prodotti, double totale) {
            this.id = id;
            this.data = data;
            this.prodotti = prodotti;
            this.totale = totale;
        }
    }

    // ==================== MAIN ====================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ShopFrame frame = new ShopFrame();
            frame.setVisible(true);
        });
    }
}