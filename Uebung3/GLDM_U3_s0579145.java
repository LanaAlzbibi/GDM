package Uebung3;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.ImageCanvas;
import ij.gui.ImageWindow;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 Opens an image window and adds a panel below the image
 @author Lana Alzbibi
 31/05/2021
 */



public class GLDM_U3_s0579145 implements PlugIn {

    ImagePlus imp; // ImagePlus object
    private int[] origPixels;
    private int width;
    private int height;

    String[] items = {"Original", "Rot-Kanal", "Graustufen", "Negativ", "BlackWhite", "Fehlerdiffusion", "Sepia-Färbung", "colored", "5 GreyScale"};


    public static void main(String args[]) {

        IJ.open("C:\\Users\\lana\\Downloads\\Bear.jpg");
        //IJ.open("Z:/Pictures/Beispielbilder/orchid.jpg");

        GLDM_U3_s0579145 pw = new GLDM_U3_s0579145();
        pw.imp = IJ.getImage();
        pw.run("");
    }

    public void run(String arg) {
        if (imp==null)
            imp = WindowManager.getCurrentImage();
        if (imp==null) {
            return;
        }
        CustomCanvas cc = new CustomCanvas(imp);

        storePixelValues(imp.getProcessor());

        new CustomWindow(imp, cc);
    }


    private void storePixelValues(ImageProcessor ip) {
        width = ip.getWidth();
        height = ip.getHeight();

        origPixels = ((int []) ip.getPixels()).clone();
    }


    class CustomCanvas extends ImageCanvas {

        CustomCanvas(ImagePlus imp) {
            super(imp);
        }

    } // CustomCanvas inner class


    class CustomWindow extends ImageWindow implements ItemListener {

        private String method;

        CustomWindow(ImagePlus imp, ImageCanvas ic) {
            super(imp, ic);
            addPanel();
        }

        void addPanel() {
            //JPanel panel = new JPanel();
            Panel panel = new Panel();

            JComboBox cb = new JComboBox(items);
            panel.add(cb);
            cb.addItemListener(this);

            add(panel);
            pack();
        }

        public void itemStateChanged(ItemEvent evt) {

            // Get the affected item
            Object item = evt.getItem();

            if (evt.getStateChange() == ItemEvent.SELECTED) {
                System.out.println("Selected: " + item.toString());
                method = item.toString();
                changePixelValues(imp.getProcessor());
                imp.updateAndDraw();
            }

        }


        private void changePixelValues(ImageProcessor ip) {

            // Array zum Zurückschreiben der Pixelwerte
            int[] pixels = (int[])ip.getPixels();

            if (method.equals("Original")) {

                for (int y=0; y<height; y++) {
                    for (int x=0; x<width; x++) {
                        int pos = y*width + x;

                        pixels[pos] = origPixels[pos];
                    }
                }
            }

            if (method.equals("Rot-Kanal")) {

                for (int y=0; y<height; y++) {
                    for (int x=0; x<width; x++) {
                        int pos = y*width + x;
                        int argb = origPixels[pos];  // Lesen der Originalwerte

                        int r = (argb >> 16) & 0xff;
                        //int g = (argb >>  8) & 0xff;
                        //int b =  argb        & 0xff;

                        int rn = r;
                        int gn = 0;
                        int bn = 0;

                        // Hier muessen die neuen RGB-Werte wieder auf den Bereich von 0 bis 255 begrenzt werden

                        //helligkeitGrenze
                        if ( rn >255)
                            rn = 255;
                        if ( gn >255)
                            gn = 255;
                        if ( bn >255)
                            bn = 255;
                        //dunkelheitGrenze
                        if ( rn < 0)
                            rn = 0;
                        if ( gn < 0)
                            gn = 0;
                        if ( bn < 0)
                            bn = 0;

                        pixels[pos] = (0xFF<<24) | (rn<<16) | (gn<<8) | bn;
                    }
                }
            }
            if (method.equals("Graustufen")) {

                for (int y=0; y<height; y++) {
                    for (int x=0; x<width; x++) {
                        int pos = y*width + x;
                        int argb = origPixels[pos];  // Lesen der Originalwerte

                        int r = (argb >> 16) & 0xff;
                        int g = (argb >>  8) & 0xff;
                        int b =  argb        & 0xff;

                        int bw = ( r + g + b)/3;
                        int rn = bw;
                        int gn = bw;
                        int bn = bw;

                        // Hier muessen die neuen RGB-Werte wieder auf den Bereich von 0 bis 255 begrenzt werden

                        //helligkeitGrenze
                        if ( rn >255)
                            rn = 255;
                        if ( gn >255)
                            gn = 255;
                        if ( bn >255)
                            bn = 255;
                        //dunkelheitGrenze
                        if ( rn < 0)
                            rn = 0;
                        if ( gn < 0)
                            gn = 0;
                        if ( bn < 0)
                            bn = 0;

                        pixels[pos] = (0xFF<<24) | (rn<<16) | (gn<<8) | bn;
                    }
                }
            }

            if (method.equals("Negativ")) {

                for (int y=0; y<height; y++) {
                    for (int x=0; x<width; x++) {
                        int pos = y*width + x;
                        int argb = origPixels[pos];  // Lesen der Originalwerte

                        int r = (argb >> 16) & 0xff;
                        int g = (argb >>  8) & 0xff;
                        int b =  argb        & 0xff;

                        int n = 255;
                        int rn = n-r;
                        int gn = n-g;
                        int bn = n-b;

                        // Hier muessen die neuen RGB-Werte wieder auf den Bereich von 0 bis 255 begrenzt werden

                        //helligkeitGrenze
                        if ( rn >255)
                            rn = 255;
                        if ( gn >255)
                            gn = 255;
                        if ( bn >255)
                            bn = 255;
                        //dunkelheitGrenze
                        if ( rn < 0)
                            rn = 0;
                        if ( gn < 0)
                            gn = 0;
                        if ( bn < 0)
                            bn = 0;

                        pixels[pos] = (0xFF<<24) | (rn<<16) | (gn<<8) | bn;
                    }
                }
            }

            if (method.equals("BlackWhite")) {

                for (int y=0; y<height; y++) {
                    for (int x=0; x<width; x++) {
                        int pos = y*width + x;
                        int argb = origPixels[pos];  // Lesen der Originalwerte

                        int r = (argb >> 16) & 0xff;
                        int g = (argb >>  8) & 0xff;
                        int b =  argb        & 0xff;
                        //Grenzwert
                        int gw = 128;

                        int bw = ( r + g + b)/3;
                        int rn = bw;
                        int gn = bw;
                        int bn = bw;

                        //rot
                        if (rn < gw){
                            rn =0;
                        } else rn = 255;
                        //grun
                        if (gn < gw){
                            gn =0;
                        } else gn = 255;
                        //blau
                        if (bn < gw){
                            bn =0;
                        } else bn = 255;

                        // Hier muessen die neuen RGB-Werte wieder auf den Bereich von 0 bis 255 begrenzt werden

                        //helligkeitGrenze
                        if ( rn >255)
                            rn = 255;
                        if ( gn >255)
                            gn = 255;
                        if ( bn >255)
                            bn = 255;
                        //dunkelheitGrenze
                        if ( rn < 0)
                            rn = 0;
                        if ( gn < 0)
                            gn = 0;
                        if ( bn < 0)
                            bn = 0;

                        pixels[pos] = (0xFF<<24) | (rn<<16) | (gn<<8) | bn;
                    }
                }
            }
            if (method.equals("Fehlerdiffusion")) {

                for (int y = 0; y < height; y++) {
                    int f = 0;

                    for (int x = 0; x < width; x++) {
                        int pos = y * width + x;
                        int argb = origPixels[pos];  // Lesen der Originalwerte

                        int r = (argb >> 16) & 0xff;
                        int g = (argb >> 8) & 0xff;
                        int b = argb & 0xff;
                        //Grenzwert
                        int gw = 255 / 2;



                        int bw = (r + g + b) / 3;
                        int rn = bw;
                        int gn = bw;
                        int bn = bw;

                        int greyValue = bw + f;

                        if (greyValue > gw) {
                            f = (255 - greyValue) * -1;



                        } else {
                            f = 0 + greyValue;
                                greyValue = 0;

                        }
                        // Hier muessen die neuen RGB-Werte wieder auf den Bereich von 0 bis 255 begrenzt werden


                        pixels[pos] = (0xFF << 24) | (greyValue << 16) | (greyValue << 8)
                                | greyValue;

                    }
                }
            }

            if (method.equals( "Sepia-Färbung")) {

                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int pos = y * width + x;
                        int argb = origPixels[pos];  // Lesen der Originalwerte

                        int r = (argb >> 16) & 0xff;
                        int g = (argb >> 8) & 0xff;
                        int b = argb & 0xff;
                        //Grenzwert
                        int gw = (r + g + b) / 3;

                        int rn = gw + 50;
                        int gn = gw + 30;
                        int bn = gw;

                        // Hier muessen die neuen RGB-Werte wieder auf den Bereich von 0 bis 255 begrenzt werden

                        //helligkeitGrenze
                        if ( rn >255)
                            rn = 255;
                        if ( gn >255)
                            gn = 255;
                        if ( bn >255)
                            bn = 255;
                        //dunkelheitGrenze
                        if ( rn < 0)
                            rn = 0;
                        if ( gn < 0)
                            gn = 0;
                        if ( bn < 0)
                            bn = 0;

                        pixels[pos] = (0xFF<<24) | (rn<<16) | (gn<<8) | bn;

                    }
                }
            }


            if (method.equals("5 GreyScale")) {

                for (int y=0; y<height; y++) {
                    for (int x=0; x<width; x++) {
                        int pos = y*width + x;
                        int argb = origPixels[pos];  // Lesen der Originalwerte

                        int r = (argb >> 16) & 0xff;
                        int g = (argb >>  8) & 0xff;
                        int b =  argb        & 0xff;
                        //Grenzwert
                        int gw = 255/4;

                        int bw = ( r + g + b)/3;
                        int rn = bw;
                        int gn = bw;
                        int bn = bw;

                        int wertR = rn / gw;
                        int wertG = gn / gw;
                        int wertB = bn / gw;

                        rn = wertR * gw;

                        gn = wertG * gw;

                        bn = wertB * gw;
                        // Hier muessen die neuen RGB-Werte wieder auf den Bereich von 0 bis 255 begrenzt werden

                        //helligkeitGrenze
                        if ( rn >255)
                            rn = 255;
                        if ( gn >255)
                            gn = 255;
                        if ( bn >255)
                            bn = 255;
                        //dunkelheitGrenze
                        if ( rn < 0)
                            rn = 0;
                        if ( gn < 0)
                            gn = 0;
                        if ( bn < 0)
                            bn = 0;

                        pixels[pos] = (0xFF<<24) | (rn<<16) | (gn<<8) | bn;
                    }
                }
            }


        }



        }


    } // CustomWindow inner class
