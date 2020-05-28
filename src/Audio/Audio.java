package Audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioFormat.Encoding;

public class Audio {

    private Clip clip;

    public Audio(String path) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.getClass().getResourceAsStream(path));
            AudioFormat baseFormat = audioInputStream.getFormat();
            AudioFormat decodeFormat = new AudioFormat(Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
            AudioInputStream decodedAudioInputStream1 = AudioSystem.getAudioInputStream(decodeFormat, audioInputStream);
            this.clip = AudioSystem.getClip();
            this.clip.open(decodedAudioInputStream1);
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }

    public void play() {
        if (this.clip != null) {
            this.stop();
            this.clip.setFramePosition(0);
            this.clip.start();
        }
    }

    public void stop() {
        if (this.clip.isRunning()) {
            this.clip.stop();
        }

    }

    public void close() {
        this.stop();
        this.clip.close();
    }
}
