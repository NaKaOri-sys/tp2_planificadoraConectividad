package view.components;

import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import java.awt.*;
import java.util.List;

public class AristaConCosto extends MapPolygonImpl {
    private double costo;

    public AristaConCosto(List<Coordinate> puntos, double costo) {
        super(puntos); 
        this.costo = costo;
    }

    @Override
    public void paint(Graphics g, List<Point> points) {
        super.paint(g, points);
        if (points.size() >= 2) {
            Point p1 = points.get(0);
            Point p2 = points.get(1);
            
            int x = (p1.x + p2.x) / 2;
            int y = (p1.y + p2.y) / 2;

            String label = String.format("$%.2f", costo);
            g.setColor(new Color(255, 255, 255, 220));
            FontMetrics fm = g.getFontMetrics();
            int width = fm.stringWidth(label);
            g.fillRect(x - (width/2) - 2, y - 12, width + 4, 15);
            g.setColor(Color.RED);
            g.setFont(new Font("SansSerif", Font.BOLD, 11));
            g.drawString(label, x - (width/2), y);
        }
    }
}