package Audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioFormat.Encoding;
import java.util.HashMap;

public class Audio {

    private static HashMap<String, Clip> clips;
    private static int gap;
    private static boolean mute = false;

    public static void init() {
        clips = new HashMap<String, Clip>();
        gap = 0;
    }

    public static void load(String path, String name) {
        if(clips.get(name) != null) return;
        Clip clip;
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Audio.class.getResourceAsStream(path));
            AudioFormat baseFormat = audioInputStream.getFormat();
            AudioFormat decodeFormat = new AudioFormat(Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
            AudioInputStream decodedAudioInputStream1 = AudioSystem.getAudioInputStream(decodeFormat, audioInputStream);
            clip = AudioSystem.getClip();
            clip.open(decodedAudioInputStream1);
            clips.put(name, clip);
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }

    public static void play(String name) {
        play(name, gap);
    }

    public static void play(String name, int i) {
        if (mute) return;
        Clip clip = clips.get(name);
        if (clip == null) return;
        if (clip.isRunning()) clip.stop();
        clip.setFramePosition(i);
        while(!clip.isRunning()) clip.start();
    }

    public static void stop(String name) {
        if (clips.get(name) == null) return;
        if (clips.get(name).isRunning()) clips.get(name).stop();
    }

    public static void resume(String s) {
        if(mute) return;
        if(clips.get(s).isRunning()) return;
        clips.get(s).start();
    }

    public static void loop(String name, int start, int end) {
        loop(name, gap, start, end);
    }

    public static void loop(String name, int frame, int start, int end) {
        stop(name);
        if(mute) return;
        clips.get(name).setLoopPoints(start, end);
        clips.get(name).setFramePosition(frame);
        clips.get(name).loop(Clip.LOOP_CONTINUOUSLY);
    }

    public static void setPosition(String s, int frame) {
        clips.get(s).setFramePosition(frame);
    }

    public static int getFrames(String s) { return clips.get(s).getFrameLength(); }

    public static void close(String s) {
        stop(s);
        clips.get(s).close();
    }
}
