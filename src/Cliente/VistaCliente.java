package Cliente;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;

public class VistaCliente extends javax.swing.JFrame implements Observer{
    //CONSTRUCTOR
    public VistaCliente(ControladorCliente controlador) {
        initComponents();
        tablero = new JPanel[20][20];
        addKeyListener(controlador);
        jSalir.addMouseListener(controlador);
        this.setLayout(new GridLayout(20,20));
        for(int i=0;i<20;i++){
            for(int j=0;j<20;j++){
                JPanel panel = new JPanel();
                panel.setBackground(Color.white);
                //panel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                panel.setPreferredSize( new Dimension(this.getWidth()/20,this.getHeight()/20));
                this.add(panel);
                tablero[i][j] = panel;
            }
        }
        controlador.getConector().addObserver(this);
        tablero[10][10].setBackground(Color.green);
        tablero[11][10].setBackground(Color.green);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jSalir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jSalir.setText("SALIR");
        jMenuBar1.add(jSalir);
        setJMenuBar(jMenuBar1);

        jSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salirActionPerformed(evt);
            }

            private void salirActionPerformed(ActionEvent evt) {
                VistaGeneral vg = new VistaGeneral();
                vg.setVisible(true);
                dispose();
            }
        });
       
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 556, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 379, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>                        
    // Variables declaration - do not modify                     
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JButton jSalir;
    // End of variables declaration                   
    private JPanel[][] tablero;
    
    @Override
    public void update(Observable o, Object o1) {
        if(o1.equals(2)){
            ControladorCliente.Conector con = (ControladorCliente.Conector) o;
            tablero[con.getCay()][con.getCax()].setBackground(Color.green);
            if(con.getCoy()!=20) tablero[con.getCoy()][con.getCox()].setBackground(Color.white);
        }
        if(o1.equals(3)){
            ControladorCliente.Conector con = (ControladorCliente.Conector) o;
            tablero[con.getTy()][con.getTx()].setBackground(Color.red);
        }
    }
}