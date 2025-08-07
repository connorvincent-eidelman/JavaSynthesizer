package Synth;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;

import Synth.utils.Utils;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;

import java.util.function.Supplier;

public class AudioThread extends Thread {
    static final int BUFFER_SIZE = 512;
    static final int BUFFER_COUNT = 8;

    private final Supplier<short[]> bufferSupplier;
    private final int[] buffers = new int[BUFFER_COUNT];
    private final long device = alcOpenDevice(alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER)); // creates device
    private final long context = alcCreateContext(device, new int[1]);
    private final int source;

    private int bufferIndex;
    private boolean closed;
    private boolean running;

    AudioThread(Supplier<short[]> bufferSupplier) {
        this.bufferSupplier = bufferSupplier;

        alcMakeContextCurrent(context); // tells device to use our context?
        AL.createCapabilities(ALC.createCapabilities(device)); // must be written before AL stuff can be called.
        source = alGenSources(); // init sources
        for (int i = 0; i < BUFFER_COUNT; i++) {
            // buffer samples method
            bufferSamples(new short[0]);
        }
        alSourcePlay(source); // source plays buffer
        catchInternalException();
        start();

    }

    boolean isRunning() {
        return running;
    }

    @Override
    public synchronized void run() {
        while (!closed) {
            while (!running) {
                Utils.handleProcedure(this::wait, false);

            }
            int processedBufs = alGetSourcei(source, AL_BUFFERS_PROCESSED);
            for (int i = 0; i < processedBufs; ++i) {
                short[] samples = bufferSupplier.get();
                if (samples == null) {
                    running = false;
                    break;
                }

                alDeleteBuffers(alSourceUnqueueBuffers(source));
                buffers[bufferIndex] = alGenBuffers();
                bufferSamples(samples);

            }
            if (alGetSourcei(source, AL_SOURCE_STATE) != AL_PLAYING) {
                alSourcePlay(source);
            }
            catchInternalException();
        }
        alDeleteSources(source);
        alDeleteBuffers(buffers);
        alcDestroyContext(context);
        alcCloseDevice(device);

    }

    synchronized void triggerPlayback() {
        running = true;
        notify();
    }

    void close() {
        closed = true;
        // break out of loop
        triggerPlayback();
    }

    private void bufferSamples(short[] samples) {
        int buf = buffers[bufferIndex++];
        alBufferData(buf, AL_FORMAT_MONO16, samples, SynthGUI.AudioInfo.SAMPLE_RATE); // buffer properties
        alSourceQueueBuffers(source, buf);
        bufferIndex %= BUFFER_COUNT;

    }

    private void catchInternalException() {
        int err = alcGetError(device);
        if (err != ALC_NO_ERROR) {
            throw new OpenALRuntimeException(err);

        }
    }

}

