package gh2;

import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

/**
 * A client that uses the synthesizer package to replicate a plucked guitar string sound
 */
public class GuitarHeroLite {
    private static final String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
    private static final double CONCERT_A = 440.0;
    private static final double CONCERT_C = CONCERT_A * Math.pow(2, 3.0 / 12.0);
    private static final int WIDTH = 512;
    private static final int HEIGHT = 512;

    public static void main(String[] args) {
        /* create two guitar strings, for concert A and C */
        int totalNotes = keyboard.length();
        GuitarString[] strings = new GuitarString[totalNotes];
        // 初始化十二平均律
        for (int i = 0; i < totalNotes; i++) {
            double frequency = 440.0 * Math.pow(2.0, (i - 24) / 12.0);
            strings[i] = new GuitarString(frequency);
        }
        // GuitarString stringA = new GuitarString(CONCERT_A);
        // GuitarString stringC = new GuitarString(CONCERT_C);
        StdDraw.setCanvasSize(WIDTH, HEIGHT);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.05);
        StdDraw.text(WIDTH / 2, (HEIGHT + 16) / 2, "Play the guitar!");
        StdDraw.text(WIDTH / 2, (HEIGHT - 32) / 2, "Type A or C");
        while (true) {

            /* check if the user has typed a key; if so, process it */
            /*
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (key == 'a') {
                    StdDraw.clear();
                    StdDraw.text(WIDTH / 2, HEIGHT / 2, "A");

                    StdDraw.show();
                    stringA.pluck();

                } else if (key == 'c') {
                    StdDraw.clear();
                    StdDraw.text(WIDTH / 2, HEIGHT / 2, "C");
                    StdDraw.show();

                    stringC.pluck();
                }
            }
            */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int index = keyboard.indexOf(key); // 寻找字符在字符串中的下标

                if (index != -1) { // 如果按键合法
                    strings[index].pluck(); // 拨动对应位置的琴弦
                }
            }

            /* compute the superposition of samples */
            // double sample = stringA.sample() + stringC.sample();

            /* play the sample on standard audio */
            // StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            // stringA.tic();
            // stringC.tic();

            double sample = 0.0;
            for (int i = 0; i < totalNotes; i++) {
                sample += strings[i].sample();
            }

            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            for (int i = 0; i < totalNotes; i++) {
                strings[i].tic();
            }
        }
    }
}

