package Uebung5;


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
     //Matrikelnummer: s0579145
     */
    public class GLDM_U5_s0579145 {

        ImagePlus imp; // ImagePlus object
        private int[] origPixels;
        private int[] pixelkernel;
        private int width;
        private int height;

        String[] items = {"Original", "Filter 1","Filter 2", "Filter 3","Filter 4"};


        public static void main(String args[]) {

            IJ.open("C:\\Users\\lana\\Downloads\\sail.jpg");
            //IJ.open("Z:/Pictures/Beispielbilder/orchid.jpg");

            GLDM_U5_s0579145  pw = new GLDM_U5_s0579145();

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
                int[] pixels = (int[]) ip.getPixels();

                if (method.equals("Original")) {

                    for (int y = 0; y < height; y++) {
                        for (int x = 0; x < width; x++) {
                            int pos = y * width + x;

                            pixels[pos] = origPixels[pos];
                        }
                    }
                }

                if (method.equals("Filter 1")) {

                    for (int y = 0; y < height; y++) {
                        for (int x = 0; x < width; x++) {
                            int pos = y * width + x;
                            int argb = origPixels[pos];  // Lesen der Originalwerte

                            int r = (argb >> 16) & 0xff;
                            int g = (argb >> 8) & 0xff;
                            int b = argb & 0xff;

                            int rn = r / 2;
                            int gn = g / 2;
                            int bn = b / 2;

                            pixels[pos] = (0xFF << 24) | (rn << 16) | (gn << 8) | bn;
                        }
                    }

                }
                //from the book : Burger-Burge2015_Book_DigitaleBildverarbeitung
                //I(u, v) ← 1/9
                //· [ I(u−1, v−1) + I(u, v−1) + I(u+1, v−1) +
                //I(u−1, v) +I(u, v) +I(u+1, v) +
                //I(u−1, v + 1) + I(u, v+1) + I(u+1, v+1) ] ,

                if (method.equals("Filter 2")) {

                    for (int y = 0; y < height; y++) {
                        for (int x = 0; x < width; x++) {
                            int pos = y * width + x;
//                            int argb = origPixels[pos];  // Lesen der Originalwerte
//                            int r = (argb >> 16) & 0xff;
//                            int g = (argb >> 8) & 0xff;
//                            int b = argb & 0xff;

                            int rn=0;
                            int gn=0;
                            int bn=0;

                            //grenze fuer das Bild
                            if (y>0 && x>0 && y<height-1 && x<width-1) {
                                //array zum erstellen des kernels
                                pixelkernel =new int[] {
                                        y*width + (x-1),
                                        (y+1)*width + (x-1),
                                        (y+1)*width + x,
                                        (y+1)*width + (x+1),
                                        y*width + (x+1),
                                        (y-1)*width + (x+1),
                                        (y-1)*width + x,
                                        (y-1)*width + (x-1),

                                };

                                for (int i =0 ; i <pixelkernel.length; i++) {
                                    //Lesen der Originalwerte mit dem kernel
                                    int argb = origPixels[pixelkernel[i]];
                                    int r = (argb >> 16) & 0xff;
                                    int g = (argb >> 8) & 0xff;
                                    int b = argb & 0xff;

                                    rn = rn + (r * 1/9);
                                    gn = gn+ (g * 1/9);
                                    bn = bn +( b * 1/9);


                                }
                                //helligkeitGrenze
                                if (rn > 255)
                                    rn = 255;
                                if (gn > 255)
                                    gn = 255;
                                if (bn > 255)
                                    bn = 255;
                                //dunkelheitGrenze
                                if (rn < 0)
                                    rn = 0;
                                if (gn < 0)
                                    gn = 0;
                                if (bn < 0)
                                    bn = 0;

                                pixels[pos] = (0xFF<<24) | (rn<<16) | (gn << 8) | bn;
                            }


                        }
                    }

                }
                if (method.equals("Filter 3")) {

                    for (int y = 0; y < height; y++) {
                        for (int x = 0; x < width; x++) {
                            int pos = y * width + x;
                            int argbo = origPixels[pos];  // Lesen der Originalwerte
                            //orginal
                            int ro = (argbo >> 16) & 0xff;
                            int go = (argbo >> 8) & 0xff;
                            int bo = argbo & 0xff;
                            //fuer hochpass
                            int rn=0;
                            int gn=0;
                            int bn=0;
                            //fuer tiefpass
                            int rnt=0;
                            int gnt=0;
                            int bnt=0;


                            //grenze fuer das Bild
                            if (y>0 && x>0 && y<height-1 && x<width-1) {
                                //array zum erstellen des kernels
                                pixelkernel =new int[] {
                                        y*width + (x-1),
                                        (y+1)*width + (x-1),
                                        (y+1)*width + x,
                                        (y+1)*width + (x+1),
                                        y*width + (x+1),
                                        (y-1)*width + (x+1),
                                        (y-1)*width + x,
                                        (y-1)*width + (x-1),

                                };
                                for (int i =0 ; i <pixelkernel.length; i++) {
                                    //Lesen der Originalwerte mit dem kernel
                                    int argbt = origPixels[pixelkernel[i]];
                                    int rt = (argbt >> 16) & 0xff;
                                    int gt = (argbt >> 8) & 0xff;
                                    int bt = argbt & 0xff;

                                    rnt = rnt + (rt * 1/9);
                                    gnt = gnt+ (gt * 1/9);
                                    bnt =bnt +( bt * 1/9);
                                    //Tiefpass - orginal = hochpass
                                    rn = ro -rnt;
                                    gn = go - gnt;
                                    bn = bo - bnt;




                                }
                                int grenze=128;
                                rn = rn + grenze;
                                gn = gn + grenze;
                                bn = bn +grenze;
                                //helligkeitGrenze
                                if (rn > 255)
                                    rn = 255;
                                if (gn > 255)
                                    gn = 255;
                                if (bn > 255)
                                    bn = 255;
                                //dunkelheitGrenze
                                if (rn < 0)
                                    rn = 0;
                                if (gn < 0)
                                    gn = 0;
                                if (bn < 0)
                                    bn = 0;
                                pixels[pos] = (0xFF<<24) | (rn<<16) | (gn << 8) | bn;
                            }


                        }
                    }

                }
                if (method.equals("Filter 4")) {

                    for (int y = 0; y < height; y++) {
                        for (int x = 0; x < width; x++) {
                            int pos = y * width + x;
                            int argbo = origPixels[pos];  // Lesen der Originalwerte
                            //orginal
                            int ro = (argbo >> 16) & 0xff;
                            int go = (argbo >> 8) & 0xff;
                            int bo = argbo & 0xff;
                            //fuer hochpass
                            int rn=0;
                            int gn=0;
                            int bn=0;
                            //fuer tiefpass
                            int rnt=0;
                            int gnt=0;
                            int bnt=0;
                            //fuer scharfesBild
                            int rnS=0;
                            int gnS=0;
                            int bnS=0;


                            //grenze fuer das Bild
                            if (y>0 && x>0 && y<height-1 && x<width-1) {
                                //array zum erstellen des kernels
                                pixelkernel =new int[] {
                                        y*width + (x-1),
                                        (y+1)*width + (x-1),
                                        (y+1)*width + x,
                                        (y+1)*width + (x+1),
                                        y*width + (x+1),
                                        (y-1)*width + (x+1),
                                        (y-1)*width + x,
                                        (y-1)*width + (x-1),

                                };
                                for (int i =0 ; i <pixelkernel.length; i++) {
                                    //Lesen der Originalwerte mit dem kernel
                                    int argbt = origPixels[pixelkernel[i]];
                                    int rt = (argbt >> 16) & 0xff;
                                    int gt = (argbt >> 8) & 0xff;
                                    int bt = argbt & 0xff;

                                    rnt = rnt + (rt * 1/9);
                                    gnt = gnt+ (gt * 1/9);
                                    bnt =bnt +( bt * 1/9);
                                    //Tiefpass - orginal = hochpass
                                    rn = ro -rnt;
                                    gn = go - gnt;
                                    bn = bo - bnt;
                                    //hochpass + orginal = scharfesBild
                                    rnS = rn + ro;
                                    gnS = gn + go;
                                    bnS = bn + bo;


                                }
                                //helligkeitGrenze
                                if (rnS > 255)
                                    rnS = 255;
                                if (gnS > 255)
                                    gnS = 255;
                                if (bnS > 255)
                                    bnS = 255;
                                //dunkelheitGrenze
                                if (rnS < 0)
                                    rnS = 0;
                                if (gnS < 0)
                                    gnS = 0;
                                if (bnS < 0)
                                    bnS = 0;
                                pixels[pos] = (0xFF<<24) | (rnS<<16) | (gnS << 8) | bnS;
                            }


                        }
                    }

                }

            }
        }


        } // CustomWindow inner class

