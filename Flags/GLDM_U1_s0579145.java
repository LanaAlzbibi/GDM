package Flags;


import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.gui.NewImage;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;

//erste Uebung (elementare Bilderzeugung)
//Author: Lana Alzbibi
//Matrikelnummer: s0579145

    public class GLDM_U1_s0579145 implements PlugIn {

        final static String[] choices = {
                "Schwarzes Bild",
                "Gelbes Bild",
                "Belgische Fahne",
                "USA Fahne",
                "Schwarz/Weiss Verlauf",
                "Horiz. Schwarz/Rot vert. Schwarz/Blau Verlauf",
                "tschechischen Fahne"
        };

        private String choice;

        public static void main(String args[]) {
            ImageJ ij = new ImageJ(); // neue ImageJ Instanz starten und anzeigen
            ij.exitWhenQuitting(true);

            GLDM_U1_s0579145 imageGeneration = new GLDM_U1_s0579145();
            imageGeneration.run("");
        }

        public void run(String arg) {

            int width = 566;  // Breite
            int height = 400;  // Hoehe

            // RGB-Bild erzeugen
            ImagePlus imagePlus = NewImage.createRGBImage("GLDM_U1", width, height, 1, NewImage.FILL_BLACK);
            ImageProcessor ip = imagePlus.getProcessor();

            // Arrays fuer den Zugriff auf die Pixelwerte
            int[] pixels = (int[]) ip.getPixels();

            dialog();

            ////////////////////////////////////////////////////////////////
            // Hier bitte Ihre Aenderungen / Erweiterungen

            if (choice.equals("Schwarzes Bild")) {
                generateBlackImage(width, height, pixels);
            }
            if (choice.equals("Gelbes Bild")) {
                generateYellowImage(width, height, pixels);
            }
            if (choice.equals("Belgische Fahne")) {
                generateBelgianFlag(width, height, pixels);
            }
            if (choice.equals("tschechischen Fahne")) {
                generateCzechFlag(width, height, pixels);
            }
            if (choice.equals("USA Fahne")) {
                generateUsaFlagFahne(width, height, pixels);
            }
            if (choice.equals("Schwarz/Weiss Verlauf")) {
                generateSWVerlauf(width, height, pixels);
            }
            if (choice.equals("Horiz. Schwarz/Rot vert. Schwarz/Blau Verlauf")) {
                generateUSRSBVerlauf(width, height, pixels);
            }


            ////////////////////////////////////////////////////////////////////

            // neues Bild anzeigen
            imagePlus.show();
            imagePlus.updateAndDraw();
        }



        private void generateBlackImage(int width, int height, int[] pixels) {
            // Schleife ueber die y-Werte
            for (int y = 0; y < height; y++) {
                // Schleife ueber die x-Werte
                for (int x = 0; x < width; x++) {
                    int pos = y * width + x; // Arrayposition bestimmen

                    int r = 0;
                    int g = 0;
                    int b = 0;

                    // Werte zurueckschreiben
                    pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                }
            }
        }

        private void generateYellowImage(int width, int height, int[] pixels) {
            // Schleife ueber die y-Werte
            for (int y = 0; y < height; y++) {
                // Schleife ueber die x-Werte
                for (int x = 0; x < width; x++) {
                    int pos = y * width + x; // Arrayposition bestimmen

                    int r = 255;
                    int g = 255;
                    int b = 0;

                    // Werte zurueckschreiben
                    pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                }
            }
        }
        // a method for printing the flag of belgium
        private void generateBelgianFlag(int width, int height, int[] pixels) {
        //determining the colors
            for ( int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    //black
                    if (x <= width / 3) {
                        int pos = y * width + x;
                        int r = 0;
                        int g = 0;
                        int b = 0;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    } //yewllow
                    else if (x <= width * 2 / 3) {
                        int pos = y * width + x;
                        int r = 255;
                        int g = 255;
                        int b = 0;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    } //red
                    else {
                        int pos = y * width + x;
                        int r = 255;
                        int g = 0;
                        int b = 0;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    }
                }
            }
        }
        // method for printing out Czech flag
        private void generateCzechFlag(int width, int height, int[] pixels) {
            //determining the colors
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    //upper blue triangle
                    if ( x <= width/2 && x < y  && y <= height/2 ){

                        int pos = y * width + x;
                        int r = 0;
                        int g = 0;
                        int b = 255;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    }
                     //white
                    else if (y <= height/2 ) {
                        int pos = y * width + x;
                        int r = 255;
                        int g = 255;
                        int b =255;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    }
                    //secHalf blue triangle
                     else if (  x  < height -y  ){

                        int pos = y * width + x;
                        int r = 0;
                        int g = 0;
                        int b = 255;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    }
                     //red
                    else {
                        int pos = y * width + x;
                        int r = 255;
                        int g = 0;
                        int b = 0;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    }
                }
            }
        }
        private void generateUsaFlagFahne(int width, int height, int[] pixels) {
            //determining the colors
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (x <= width / 2.75 && y <= height /1.85) {
                        int pos = y * width + x;
                        int r = 0;
                        int g = 0;
                        int b = 255;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    }
                 else if(y <= height / 13) {
                     int pos = y * width + x;
                     int r = 255;
                     int g = 0;
                     int b = 0;
                     pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                 }
                 else if(y <= height * 2 / 13){
                         int pos = y * width + x;
                         int r = 255;
                         int g = 255;
                         int b =255;
                         pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                     }
                  else if(y <= height * 3 / 13){
                        int pos = y * width + x;
                        int r = 255;
                        int g = 0;
                        int b = 0;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    }

                 else if(y <= height * 4 / 13){
                     int pos = y * width + x;
                     int r = 255;
                     int g = 255;
                     int b =255;
                     pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                 }
                 else if(y <= height * 5 / 13){
                     int pos = y * width + x;
                     int r = 255;
                     int g = 0;
                     int b = 0;
                     pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                 }

                 else if(y <= height * 6 / 13){
                     int pos = y * width + x;
                     int r = 255;
                     int g = 255;
                     int b =255;
                     pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                 }
                 else if(y <= height * 7 / 13){
                     int pos = y * width + x;
                     int r = 255;
                     int g = 0;
                     int b = 0;
                     pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                 }

                 else if(y <= height * 8 / 13){
                     int pos = y * width + x;
                     int r = 255;
                     int g = 255;
                     int b =255;
                     pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                 }
                 else if(y <= height * 9 / 13){
                     int pos = y * width + x;
                     int r = 255;
                     int g = 0;
                     int b = 0;
                     pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                 }
                 else if(y <= height * 10 / 13){
                     int pos = y * width + x;
                     int r = 255;
                     int g = 255;
                     int b =255;
                     pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                 }
                 else if(y <= height * 11 / 13){
                     int pos = y * width + x;
                     int r = 255;
                     int g = 0;
                     int b = 0;
                     pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                 }
                 else if(y <= height * 12 / 13){
                     int pos = y * width + x;
                     int r = 255;
                     int g = 255;
                     int b =255;
                     pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                 }
                 else if(y <= height * 13 / 13){
                     int pos = y * width + x;
                     int r = 255;
                     int g = 0;
                     int b = 0;
                     pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                 }

                }
            }
        }



        private void myGenerateSWVerlauf(int width, int height, int[] pixels) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (y <= height / 33) {
                        int pos = y * width + x;
                        int r = 0;
                        int g = 0;
                        int b = 0;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    } else if (y <= height * 2 / 33) {
                        int pos = y * width + x;
                        int r = 15;
                        int g = 15;
                        int b = 15;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    } else if (y <= height * 3 / 33) {
                        int pos = y * width + x;
                        int r = 20;
                        int g = 20;
                        int b = 20;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    } else if (y <= height *4 / 33) {
                        int pos = y * width + x;
                        int r = 30;
                        int g = 30;
                        int b = 30;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    } else if (y <= height * 5 / 33) {
                        int pos = y * width + x;
                        int r = 40;
                        int g = 40;
                        int b = 40;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    } else if (y <= height * 6 / 33) {
                        int pos = y * width + x;
                        int r = 50;
                        int g = 50;
                        int b = 50;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    } else if (y <= height * 7 / 33) {
                        int pos = y * width + x;
                        int r = 60;
                        int g = 60;
                        int b = 60;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    } else if (y <= height * 8/ 33) {
                        int pos = y * width + x;
                        int r = 70;
                        int g = 70;
                        int b = 70;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    } else if (y <= height * 9 / 33) {
                        int pos = y * width + x;
                        int r = 80;
                        int g = 80;
                        int b = 80;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    } else if (y <= height * 10/ 33) {
                        int pos = y * width + x;
                        int r = 90;
                        int g = 90;
                        int b = 90;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    } else if (y <= height * 11 / 33) {
                        int pos = y * width + x;
                        int r = 100;
                        int g = 100;
                        int b = 100;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    } else if (y <= height * 12 / 33) {
                        int pos = y * width + x;
                        int r = 110;
                        int g = 110;
                        int b = 110;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    } else if (y <= height * 13 / 33) {
                        int pos = y * width + x;
                        int r = 120;
                        int g = 120;
                        int b = 120;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    } else if (y <= height * 14 / 33) {
                        int pos = y * width + x;
                        int r = 130;
                        int g = 130;
                        int b = 130;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    } else if (y <= height * 15 / 33) {
                        int pos = y * width + x;
                        int r = 140;
                        int g = 140;
                        int b = 140;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    } else if (y <= height * 16 / 33) {
                        int pos = y * width + x;
                        int r = 150;
                        int g = 150;
                        int b = 150;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    } else if (y <= height * 17 / 33) {
                        int pos = y * width + x;
                        int r = 160;
                        int g = 160;
                        int b = 160;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    } else if (y <= height * 18 / 33) {
                        int pos = y * width + x;
                        int r = 170;
                        int g = 170;
                        int b = 170;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    } else if (y <= height * 19 / 33) {
                        int pos = y * width + x;
                        int r = 180;
                        int g = 180;
                        int b = 180;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    } else if (y <= height * 20 / 33) {
                        int pos = y * width + x;
                        int r = 190;
                        int g = 190;
                        int b = 190;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    } else if (y <= height * 21 / 33) {
                        int pos = y * width + x;
                        int r = 200;
                        int g = 200;
                        int b = 200;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    } else if (y <= height * 22 / 33) {
                        int pos = y * width + x;
                        int r = 210;
                        int g = 210;
                        int b = 210;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    }   else if (y <= height * 23 / 33) {
                        int pos = y * width + x;
                        int r = 215;
                        int g = 215;
                        int b = 215;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    } else if (y <= height * 24 / 33) {
                        int pos = y * width + x;
                        int r = 220;
                        int g = 220;
                        int b = 220;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    } else if (y <= height * 25 / 33) {
                        int pos = y * width + x;
                        int r = 225;
                        int g = 225;
                        int b = 225;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    } else if (y <= height * 26 / 33) {
                        int pos = y * width + x;
                        int r = 230;
                        int g = 230;
                        int b = 230;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    } else if (y <= height * 27 / 33) {
                        int pos = y * width + x;
                        int r = 235;
                        int g = 235;
                        int b = 235;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;

                    } else if (y <= height * 28 / 33) {
                        int pos = y * width + x;
                        int r = 240;
                        int g = 240;
                        int b = 240;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;

                    } else if (y <= height * 29 / 33) {
                        int pos = y * width + x;
                        int r = 245;
                        int g = 245;
                        int b = 245;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    } else if (y <= height * 30 / 33) {
                        int pos = y * width + x;
                        int r = 250;
                        int g = 250;
                        int b = 250;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    } else if (y <= height * 31 / 33) {
                        int pos = y * width + x;
                        int r = 252;
                        int g = 252;
                        int b = 252;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    }
                    else if (y <= height * 32 / 33) {
                        int pos = y * width + x;
                        int r = 255;
                        int g = 255;
                        int b = 255;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    }

                    else if (y <= height * 33 / 33) {
                        int pos = y * width + x;
                        int r = 255;
                        int g = 255;
                        int b = 255;
                        pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
                    }

                }
            }
        }

        private void generateSWVerlauf(int width, int height, int[] pixels) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pos = y * width + x; // Arrayposition bestimmen

                    int r = (int) (x/(width-1.) * 255);
                    int g = (int) (x/(width-1.) * 255);
                    int b = (int) (x/(width-1.) * 255);

                    // Werte zurueckschreiben
                    pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;


                }
            }
     }

        private void  generateUSRSBVerlauf(int width, int height, int[] pixels) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pos = y * width + x; // Arrayposition bestimmen

                    int r = (int) (x/(width-1.) * 255);
                    int g = 0;
                    int b = (int) (y/(width-1.) * 255);

                    // Werte zurueckschreiben
                    pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;


                }
            }
        }




        private void dialog() {
            // Dialog fuer Auswahl der Bilderzeugung
            GenericDialog gd = new GenericDialog("Bildart");

            gd.addChoice("Bildtyp", choices, choices[0]);


            gd.showDialog();    // generiere Eingabefenster

            choice = gd.getNextChoice(); // Auswahl uebernehmen

            if (gd.wasCanceled())
                System.exit(0);
        }
    }


