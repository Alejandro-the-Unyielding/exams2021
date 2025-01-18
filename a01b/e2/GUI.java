package a01b.e2;

import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GUI extends JFrame {
    
    private static final long serialVersionUID = -6218820567019985015L;
    private final Map<JButton, Pair<Integer,Integer>> cells = new HashMap<>();
    
    public GUI(int size) {
        Logic logics = new LogicsImple(size);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(50*size, 50*size);
        
        JPanel panel = new JPanel(new GridLayout(size,size));
        this.getContentPane().add(panel);
        
        ActionListener al = e -> {
        	var button = (JButton)e.getSource();
            var index = this.cells.get(button);
            var turn = logics.hit(index.getX(),index.getY());
            switch (turn) {
                case FIRST: button.setText("1"); break;
            
                case SECOND: button.setText("2");   break;
                
                case THIRD: 
                        for (var entry : this.cells.entrySet()) {
                            if(logics.isSelected(entry.getValue().getX(), entry.getValue().getY())){
                                entry.getKey().setText("*");
                            }
                            if(logics.isOver()){
                                entry.getKey().setEnabled(false);
                            }
                            
                        }    break;

                case NOT_VALID: break;
            }

        };
                
        for (int i=0; i<size; i++){
            for (int j=0; j<size; j++){
                final JButton jb = new JButton(" ");
                this.cells.put(jb, new Pair<Integer,Integer>(i, j));
                jb.addActionListener(al);
                panel.add(jb);
            }
        }
        this.setVisible(true);
    }
    
}
