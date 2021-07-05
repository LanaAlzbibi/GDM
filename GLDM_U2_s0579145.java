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

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 Opens an image window and adds a panel below the image
 */


//Author: Lana Alzbibi
//Matrikelnummer: s0579145

    //code mostly work but not 100% :(
public class GLDM_U2_s0579145 implements PlugIn {

    ImagePlus imp; // ImagePlus object
    private int[] origPixels;
    private int width;
    private int height;


    public static void main(String args[]) {
        //new ImageJ();
        //IJ.open("/users/barthel/applications/ImageJ/_images/orchid.jpg");
        IJ.open("C:\\Users\\lana\\Downloads\\orchid.jpg");

        GLDM_U2_s0579145 pw = new GLDM_U2_s0579145();
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


    class CustomWindow extends ImageWindow implements ChangeListener {

        private JSlider jSliderBrightness;
        private JSlider jSliderContrast;
        private JSlider jSliderSaturation;
        private JSlider jSliderHue;
        private JSlider jSliderGray;

        private double brightness;
        private double contrast;
        private double saturation;
        private double hue;
        private double gray;

        CustomWindow(ImagePlus imp, ImageCanvas ic) {
            super(imp, ic);
            addPanel();
        }

        void addPanel() {
            //JPanel panel = new JPanel();
            Panel panel = new Panel();

            panel.setLayout(new GridLayout(4, 1));
            jSliderBrightness = makeTitledSilder("Helligkeit", 0, 200, 100);
            jSliderGray = makeTitledSilder("Gray", 0, 200, 100);
            jSliderContrast = makeTitledSilder("constrast", 0, 10, 5);
            jSliderSaturation = makeTitledSilder("saturation", 0, 6, 3);
            jSliderHue = makeTitledSilder("Hue", 0, 360, 180);

            panel.add(jSliderBrightness);
            panel.add(jSliderContrast);
            panel.add(jSliderSaturation);
            panel.add(jSliderHue);
            panel.add(jSliderGray);
            add(panel);

            pack();
        }

        private JSlider makeTitledSilder(String string, int minVal, int maxVal, int val) {

            JSlider slider = new JSlider(JSlider.HORIZONTAL, minVal, maxVal, val);
            Dimension preferredSize = new Dimension(width, 50);
            slider.setPreferredSize(preferredSize);
            TitledBorder tb = new TitledBorder(BorderFactory.createEtchedBorder(),
                    string, TitledBorder.LEFT, TitledBorder.ABOVE_BOTTOM,
                    new Font("Sans", Font.PLAIN, 11));
            slider.setBorder(tb);
            slider.setMajorTickSpacing((maxVal - minVal) / 10);
            slider.setPaintTicks(true);
            slider.addChangeListener(this);

            return slider;
        }

        private void setSliderTitle(JSlider slider, String str) {
            TitledBorder tb = new TitledBorder(BorderFactory.createEtchedBorder(),
                    str, TitledBorder.LEFT, TitledBorder.ABOVE_BOTTOM,
                    new Font("Sans", Font.PLAIN, 11));
            slider.setBorder(tb);
        }

        public void stateChanged(ChangeEvent e) {
            JSlider slider = (JSlider) e.getSource();

            if (slider == jSliderBrightness) {
                //brightness = slider.getValue()-100;
                brightness = slider.getValue() - 100;
                String str = "Helligkeit " + brightness;
                setSliderTitle(jSliderBrightness, str);
                changePixelValues(imp.getProcessor());
            }

            if (slider == jSliderContrast) {
                contrast = slider.getValue();
                String str = "contrast " + contrast;
                setSliderTitle(jSliderContrast, str);
                contrast(imp.getProcessor());
            }
            if (slider == jSliderSaturation) {
                saturation = slider.getValue();
                String str = "saturation  " + saturation;
                setSliderTitle(jSliderSaturation, str);
                saturation(imp.getProcessor());
            }

            if (slider == jSliderHue) {
                hue = slider.getValue();
                String str = "hue" + hue;
                setSliderTitle(jSliderHue, str);
                hue(imp.getProcessor());
            }

            if (slider == jSliderGray) {
                gray = slider.getValue();
                String str = "gray" + gray;
                setSliderTitle(jSliderGray, str);
                gray(imp.getProcessor());
            }



            imp.updateAndDraw();
        }


        private void contrast(ImageProcessor ip) {
            int[] pixels = (int[]) ip.getPixels();

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pos = y * width + x;
                    int argb = origPixels[pos]; // Lesen der Originalwerte

                    int r = (argb >> 16) & 0xff;
                    int g = (argb >> 8) & 0xff;
                    int b = argb & 0xff;

                    //transformation
                    double ly = 0.299 * r + 0.587 * g + 0.114 * b;
                    double u = (b - ly) * 0.493;
                    double v = (r - ly) * 0.877;


                    double theValue = jSliderContrast.getValue();

                   /*if(ly <= 150 ) {
                        ly = ly - theValue * 2;
                    }
                    else if(ly >= 150) {
                        ly = ly + theValue * 2  ;
                    }*/


                    //another way of solving //form from the lectures
                    ly = theValue * (ly - 127.5) + 127.5 + jSliderBrightness.getValue();
                    u = theValue * u + jSliderBrightness.getValue();
                    ;
                    v = theValue * v + jSliderBrightness.getValue();
                    ;

                    int rn = (int) (ly + v / 0.877);
                    int gn = (int) (1 / 0.587 * ly - 0.299 / 0.587 * r - 0.114 / 0.587 * b);
                    int bn = (int) (ly + u / 0.493);


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

                    pixels[pos] = (0xFF << 24) | (rn << 16) | (gn << 8) | bn;


                }
            }
        }

        private void saturation(ImageProcessor ip) {
            int[] pixels = (int[]) ip.getPixels();

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pos = y * width + x;
                    int argb = origPixels[pos]; // Lesen der Originalwerte

                    int r = (argb >> 16) & 0xff;
                    int g = (argb >> 8) & 0xff;
                    int b = argb & 0xff;

                    //transformation
                    double ly = 0.299 * r + 0.587 * g + 0.114 * b;
                    double u = (b - ly) * 0.493;
                    double v = (r - ly) * 0.877;

                    float value = jSliderSaturation.getValue();

                    u = u * value;
                    v = v * value;


                    int rn = (int) (ly + v / 0.877);
                    int gn = (int) (1 / 0.587 * ly - 0.299 / 0.587 * r - 0.114 / 0.587 * b);
                    int bn = (int) (ly + u / 0.493);

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

                    pixels[pos] = (0xFF << 24) | (rn << 16) | (gn << 8) | bn;

                }
            }
        }

        private void hue(ImageProcessor ip) {
            int[] pixels = (int[]) ip.getPixels();

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pos = y * width + x;
                    int argb = origPixels[pos]; // Lesen der Originalwerte

                    int r = (argb >> 16) & 0xff;
                    int g = (argb >> 8) & 0xff;
                    int b = argb & 0xff;

                    //transformation
                    double ly = 0.299 * r + 0.587 * g + 0.114 * b;
                    double u = (b - ly) * 0.493;
                    double v = (r - ly) * 0.877;

                    int value = jSliderHue.getValue();

                    double hueu = Math.cos(value)
                            - (Math.sin(Math.toRadians(value)) * u);

                    double huev = Math.sin(value)
                            + Math.cos(Math.toRadians(value)) * v;

                    u = hueu;
                    v = huev;
                    int rn = (int) (ly + v / 0.877);
                    int gn = (int) (1 / 0.587 * ly - 0.299 / 0.587 * r - 0.114 / 0.587 * b);
                    int bn = (int) (ly + u / 0.493);

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

                    pixels[pos] = (0xFF << 24) | (rn << 16) | (gn << 8) | bn;
                }
            }
        }

        private void changePixelValues(ImageProcessor ip) {

            // Array fuer den Zugriff auf die Pixelwerte
            int[] pixels = (int[]) ip.getPixels();

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pos = y * width + x;
                    int argb = origPixels[pos];  // Lesen der Originalwerte

                    int r = (argb >> 16) & 0xff;
                    int g = (argb >> 8) & 0xff;
                    int b = argb & 0xff;

                    //Korrigieren Sie den Überlauf, der bei der Helligkeitsänderung auftritt.
                    double ly = 0.299 * r + 0.587 * g + 0.114 * b;
                    double u = (b - ly) * 0.493;
                    double v = (r - ly) * 0.877;

                    int rn = (int) ((ly + v / 0.877) + brightness);
                    int gn = (int) ((1 / 0.587 * ly - 0.299 / 0.587 * r - 0.114 / 0.587 * b) + brightness);
                    int bn = (int) ((ly + u / 0.493) + brightness);


                    // int rn = (int) (r + brightness);
                    // int gn = (int) (g + brightness);
                    // int bn = (int) (b + brightness);
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

                    // anstelle dieser drei Zeilen später hier die Farbtransformation durchführen,
                    // die Y Cb Cr -Werte verändern und dann wieder zurücktransformieren
                    //Als nächster Schritt soll eine Farbtransformation von RGB nach YUV programmiert werden.




                    /*//helligkeit mit YUV

                    int rn = (int) (transformToR( yn , un , vn) + brightness);
                    int gn = (int) (transformToG(yn , r , b ) + brightness);
                    int bn = (int) (transformationToB(yn , vn) + brightness);
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
                         */
                    // Hier muessen die neuen RGB-Werte wieder auf den Bereich von 0 bis 255 begrenzt werden


                    pixels[pos] = (0xFF << 24) | (rn << 16) | (gn << 8) | bn;
                }
            }
        }

        private void gray(ImageProcessor ip) {

            // Array fuer den Zugriff auf die Pixelwerte
            int[] pixels = (int[]) ip.getPixels();

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pos = y * width + x;
                    int argb = origPixels[pos];  // Lesen der Originalwerte

                    int r = (argb >> 16) & 0xff;
                    int g = (argb >> 8) & 0xff;
                    int b = argb & 0xff;


                    int gray = jSliderGray.getValue();
                    //Korrigieren Sie den Überlauf, der bei der Helligkeitsänderung auftritt.
                    double ly = 0.299 * r + 0.587 * g + 0.114 * b;
                    double u = (b - ly) * 0.493;
                    double v = (r - ly) * 0.877;

                    /*double ly = (r+g+b)/3;
                    double u = (b - ly) * 0.493;
                    double v = (r - ly) * 0.877;*/

                    int rn = (int) (ly + gray);
                    int gn = (int) (rn);
                    int bn = (int) (rn);


                    // int rn = (int) (r + brightness);
                    // int gn = (int) (g + brightness);
                    // int bn = (int) (b + brightness);
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


                    pixels[pos] = (0xFF << 24) | (rn << 16) | (gn << 8) | bn;
                }
            }
        }


    } // CustomWindow inner class
}
