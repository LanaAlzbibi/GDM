package Uebung4;
import ij.*;
import ij.io.*;
import ij.process.*;
import ij.gui.*;
import ij.plugin.filter.*;

//Author: Lana Alzbibi
//Matrikelnummer: s0579145
public class GLDM_U4_s0579145 implements PlugInFilter {

    protected ImagePlus imp;
    final static String[] choices = {"Wischen", "Wischen-vertikal","Weiche Blende","multiply","screen" ,"Chroma-Keying", " eigene Überblendung" ,"neg-screen-Ineinander" };

    public int setup(String arg, ImagePlus imp) {
        this.imp = imp;
        return DOES_RGB+STACK_REQUIRED;
    }

    public static void main(String args[]) {
        ImageJ ij = new ImageJ(); // neue ImageJ Instanz starten und anzeigen
        ij.exitWhenQuitting(true);

        IJ.open("C:\\Users\\lana\\Downloads\\StackB.zip");

        GLDM_U4_s0579145 sd = new GLDM_U4_s0579145();
        sd.imp = IJ.getImage();
        ImageProcessor B_ip = sd.imp.getProcessor();
        sd.run(B_ip);
    }

    public void run(ImageProcessor B_ip) {
        // Film B wird uebergeben
        ImageStack stack_B = imp.getStack();

        int length = stack_B.getSize();
        int width = B_ip.getWidth();
        int height = B_ip.getHeight();

        // ermoeglicht das Laden eines Bildes / Films
        Opener o = new Opener();
        OpenDialog od_A = new OpenDialog("Auswählen des 2. Filmes ...", "");

        // Film A wird dazugeladen
        String dateiA = od_A.getFileName();
        if (dateiA == null) return; // Abbruch
        String pfadA = od_A.getDirectory();
        ImagePlus A = o.openImage(pfadA, dateiA);
        if (A == null) return; // Abbruch

        ImageProcessor A_ip = A.getProcessor();
        ImageStack stack_A = A.getStack();

        if (A_ip.getWidth() != width || A_ip.getHeight() != height) {
            IJ.showMessage("Fehler", "Bildgrößen passen nicht zusammen");
            return;
        }

        // Neuen Film (Stack) "Erg" mit der kleineren Laenge von beiden erzeugen
        length = Math.min(length, stack_A.getSize());

        ImagePlus Erg = NewImage.createRGBImage("Ergebnis", width, height, length, NewImage.FILL_BLACK);
        ImageStack stack_Erg = Erg.getStack();

        // Dialog fuer Auswahl des Ueberlagerungsmodus
        GenericDialog gd = new GenericDialog("Überlagerung");
        gd.addChoice("Methode", choices, "");
        gd.showDialog();

        int methode = 0;
        String s = gd.getNextChoice();
        if (s.equals("Wischen")) methode = 1;
        if (s.equals("Wischen-vertikal")) methode = 2;
        if (s.equals("Weiche Blende")) methode = 3;
        if (s.equals("multiply")) methode = 4;
        if (s.equals("screen")) methode = 5;
        if (s.equals("Chroma-Keying")) methode = 6;
        if (s.equals("neg-screen-Ineinander")) methode = 7;
        if (s.equals(" eigene Überblendung")) methode = 8;

        //TODO : NICHT GESCHAFFT
        if (s.equals("Schieb-Blende")) methode = 9;


        // Arrays fuer die einzelnen Bilder
        int[] pixels_B;
        int[] pixels_A;
        int[] pixels_Erg;

        // Schleife ueber alle Bilder
        for (int z = 1; z <= length; z++) {
            pixels_B = (int[]) stack_B.getPixels(z);
            pixels_A = (int[]) stack_A.getPixels(z);
            pixels_Erg = (int[]) stack_Erg.getPixels(z);

            int pos = 0;
            for (int y = 0; y < height; y++)
                for (int x = 0; x < width; x++, pos++) {
                    int cA = pixels_A[pos];
                    int rA = (cA & 0xff0000) >> 16;
                    int gA = (cA & 0x00ff00) >> 8;
                    int bA = (cA & 0x0000ff);

                    int cB = pixels_B[pos];
                    int rB = (cB & 0xff0000) >> 16;
                    int gB = (cB & 0x00ff00) >> 8;
                    int bB = (cB & 0x0000ff);

                    //FORMS ARE USED FROM THE LECTURE

                    if (methode == 1) {
                        if (x + 1 > (z - 1) * (double) width / (length - 1))
                            pixels_Erg[pos] = pixels_B[pos];
                        else
                            pixels_Erg[pos] = pixels_A[pos];
                    }

					/*
					if (methode == 2)
					{
					// ...

					int r = ...
					int g = ...
					int b = ...

					pixels_Erg[pos] = 0xFF000000 + ((r & 0xff) << 16) + ((g & 0xff) << 8) + ( b & 0xff);
					}
					*/
                    if (methode == 2) {
                        if (y + 1 > (z - 1) * (double) width / (length - 1))
                            pixels_Erg[pos] = pixels_B[pos];
                        else
                            pixels_Erg[pos] = pixels_A[pos];
                    }
                    //weiche B
                    if (methode == 3) {

                        //mask should be a grey-scale
                        //int mask = (int) (x/(width-1.) * 255);
                        //math.form. from lecture:
                        //(c) = alpha*pic1 +maxPix-alpha*pic2/maxPix
                        int mask = (int) (z / (length / 255F));
                        int max = 255;
                        int r = (int) ((mask * rA + (max - mask) * rB) / max);
                        int g = (int) ((mask * gA + (max - mask) * gB) / max);
                        int b = (int) ((mask * bA + (max - mask) * bB) / max);


                        pixels_Erg[pos] = 0xFF000000 + ((r & 0xff) << 16) + ((g & 0xff) << 8) + (b & 0xff);
                    }

                    //overlay multiply
                    if (methode == 4) {
                        // ...
                        int max = 255;
                        int r = (rA * rB) / max;
                        int g = (gA * gB) / max;
                        int b = (bA * bB) / max;

                        pixels_Erg[pos] = 0xFF000000 + ((r & 0xff) << 16) + ((g & 0xff) << 8) + (b & 0xff);
                    }
                    //overlay screen
                    if (methode == 5) {
                        int max = 255;
                        int r = (max - (max - rA) * (max - rB) / max);
                        int g = (max - (max - gA) * (max - gB) / max);
                        int b = (max - (max - bA) * (max - bB) / max);

                        pixels_Erg[pos] = 0xFF000000 + ((r & 0xff) << 16) + ((g & 0xff) << 8) + (b & 0xff);
                    }


                    if (methode == 6) {
                        //: get rid of the orange-background
                        //"Chroma-Keying"

                        // the following mathematical form was explained by a college <Nermin>


                        //Wurzel aus ( rfarbWertVonPixel - rfarbe ) ^2 + (gP - gF)^2 + (bP - bF)^2
                        //orange 	(255,165,0)
                        //pixel  : wenn Pixel weit Weg von <orange> ist, wird es nicht weggeloescht
                        //pixel2 : wenn Pixel nah an der Wert ist, wird es geloescht, pixel von anderem Bild wird genommen

                        int oR = 255;
                        int oG = 165;
                        int oB = 0;
                        int ditanz = 210;
                        double orange = Math.sqrt((Math.pow(oR - rA, 2) + Math.pow(oG - gA, 2) + Math.pow(oB - bA, 2)));
                        if (orange < ditanz) {

                            pixels_Erg[pos] = pixels_B[pos];
                        } else {
                            pixels_Erg[pos] = pixels_A[pos];
                        }

                    }


                    if (methode == 7) {
                        //screen + negative
                        //For Some reason it sill does not fully work
                        int r;
                        int g;
                        int b;

                        int maxValue = 128;
                        int max = 255;

                        //rot
                        if (rB <= maxValue) {
                            r = (rA * rB) / maxValue;
                        } else {
                            r = max - ((max - rA) * (max - rB) / maxValue);
                        }

                        //grun

                        if (gB <= maxValue) {
                            g = (gA * gB) / maxValue;
                        } else {
                            g = max - ((max - gA) * (max - gB) / maxValue);
                        }

                        //blau

                        if (bB <= maxValue) {
                            b = (bA * bB) / maxValue;
                        } else {
                            b = max - ((max - bA) * (max - bB) / maxValue);
                        }

                        pixels_Erg[pos] = 0xFF000000 + ((r & 0xff) << 16) + ((g & 0xff) << 8) + (b & 0xff);
                    }

                    if (methode == 8) {
                        //"eigene Überblendung"

                        if (y <= height / 2 && x > y && x >= width / 2 && x <= width) {
                            pixels_Erg[pos] = pixels_A[pos];
                        } else {
                            pixels_Erg[pos] = pixels_B[pos];
                        }
                    }
                }
          }



        // neues Bild anzeigen
        Erg.show();
        Erg.updateAndDraw();

    }

}