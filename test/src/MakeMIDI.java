import java.util.List;

import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class MakeMIDI {

	Sequence seq;
	public void toMIDI(List soundList) throws Exception {
    	Sequencer sequencer = null;
    	ShortMessage NoteArray[] = new ShortMessage[soundList.size()];

    	sequencer = MidiSystem.getSequencer();
        sequencer.open();

        seq = new Sequence(Sequence.PPQ, 240);
        Track track = seq.createTrack();

        int tem = 60;
        int l = 60*1000000/tem;
        MetaMessage tempo = new MetaMessage();
        tempo.setMessage(0x51, new byte[] {(byte)(l/65536), (byte)(l%65536/256), (byte)(l%256)}, 3);
        track.add(new MidiEvent(tempo, 0));
    	try {

            int Event = 0;
        	int Gate = 0;
        	int Step = 0;
        	int Tick = 0;
        	for(int a=0; a<soundList.size()/3-1; a++){

        		Event = Integer.parseInt(String.valueOf(soundList.get(a*3)));
        		Gate = Integer.parseInt(String.valueOf(soundList.get(a*3+1)));
            	Step = Integer.parseInt(String.valueOf(soundList.get(a*3+2)))+15;

            	NoteArray[a*2] = new ShortMessage();
            	NoteArray[a*2].setMessage(ShortMessage.NOTE_ON, Event, 80);
                track.add(new MidiEvent(NoteArray[a*2], Tick));

                Tick = Tick + Gate;

                NoteArray[a*2] = new ShortMessage();
                NoteArray[a*2].setMessage(ShortMessage.NOTE_OFF, Event, 80);
                track.add(new MidiEvent(NoteArray[a*2], Tick));

                Tick = Tick + Step;
        	}
        	sequencer.setSequence(seq);
        	sequencer.start();
            while (sequencer.isRunning()) Thread.sleep(100);

    	}finally {
    		if (sequencer != null && sequencer.isOpen()) sequencer.close();
    		MidiSystem.write(seq, 0,new java.io.File("sample.mid"));

    	}
	}

}
