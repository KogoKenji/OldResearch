import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;

public class MIDItoText {
    static Sequence seq;
    static int currentTrack = 0;
    static ArrayList<Integer> nextMessageOf = new ArrayList<Integer>();

    public static String removeFileExtension(String filename) {
		int lastDotPos = filename.lastIndexOf('.');

	    if (lastDotPos == -1) {
	      return filename;
	    } else if (lastDotPos == 0) {
	      return filename;
	    } else {
	      return filename.substring(0, lastDotPos);
	    }
	}


	public void Midi2Text_main(int row){
		System.out.println("[ USE_MIDI_LIST ]�f�B���N�g�����̃t�@�C����͂��J�n");
		try{
			File dir = new File("./USE_MIDI_LIST");
			File[] files = dir.listFiles();

			// �f�B���N�g�����̃t�@�C���������؂Ď擾
			System.out.println();

			int number = 0;	// �f�B���N�g�����̃t�@�C����
			while(number < files.length) {	//path�Ŏw�肵���f�B���N�g�����̃t�@�C������S�Ď擾����
				File file = files[number];
				String name = file.getName();
				File Eventfile = new File("./EVENT/"+ removeFileExtension(name)+ ".txt");
				if(name.endsWith(".mid")){

					if(Eventfile.exists()){
						System.out.println("*****************************************");
						System.out.println("[" + name +"�͉�͍ς�");

					}else{
						int readTrackNo = 2;
						String MidiFileAdd = "";

			        	MidiFileAdd = "./USE_MIDI_LIST/" + name;

			        	System.out.println("*****************************************");
			            System.out.println("[ " + name + " ]��ǂݍ���ł��܂�");
			            System.out.println("*****************************************");
			            seq = MidiSystem.getSequence(new File(MidiFileAdd));
			            convertMidi2RealTime(seq, name, readTrackNo);	//readTrackNo�̓g���b�N�̎w��Ɏg�p

			            nextMessageOf.clear();	// nextMessageOf�z��̍폜

			            System.out.println("*****************************************");
						System.out.println("[ EVENT ]�f�B���N�g����Event��Tick���������݂܂�\n");
						getGate gg = new getGate();
						gg.toArray(removeFileExtension(name));

						System.out.println("*****************************************");
						System.out.println("[ STEP ]�f�B���N�g����Event��Step���������݂܂�\n");
						getStep gs = new getStep();
						gs.toArray(removeFileExtension(name));
					}
		            System.out.println("*****************************************");
				}
				number++;
			}

    	}catch(FileNotFoundException e){
			System.out.println(e);
		}catch(IOException e){
			System.out.println(e);
		}catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
		}

		System.out.println("\n>>>>>>>>>> ��͏I�� <<<<<<<<<<\n");
	}


    // MIDI�t�@�C���̃e�L�X�g��
    public static void convertMidi2RealTime(Sequence seq, String midiFile, int trackNo) {
        double currentTempo = 500000;
        int tickOfTempoChange = 0;
        double msb4 = 0;

        for (int track = 0; track < seq.getTracks().length; track ++) nextMessageOf.add(0);
        System.out.println();

        MidiEvent nextEvent;
    	MIDItoText m2t;
    	String midiFileName = "";

    	System.out.println("�w�肳�ꂽTrack>> " + trackNo);
		while ((nextEvent = getNextEvent()) != null) {
			int tick = (int)nextEvent.getTick();

			if( currentTrack == trackNo){
				if (noteIsOff(nextEvent)) {
//					double time = (msb4+(((currentTempo/seq.getResolution())/1000)*(tick-tickOfTempoChange)));
//					System.out.println("track="+currentTrack+" tick="+tick+" time="+(int)(time+0.5)+"ms "
//						+" note "+((int)nextEvent.getMessage().getMessage()[1] & 0xFF)+" off");


	    			int noteNomber = ((int)nextEvent.getMessage().getMessage()[1] & 0xFF);
					midiFileName = "off_" + removeFileExtension(midiFile);
            		m2t = new MIDItoText();
					m2t.createFile(noteNomber, tick, midiFileName);	//note���e�L�X�g�ɏ�������

				} else if (noteIsOn(nextEvent)) {
//                	double time = (msb4+(((currentTempo/seq.getResolution())/1000)*(tick-tickOfTempoChange)));
//               	System.out.println("track="+currentTrack+" tick="+tick+" time="+(int)(time+0.5)+"ms "
//                  	+" note "+((int)nextEvent.getMessage().getMessage()[1] & 0xFF)+" on");

            		int noteNomber = ((int)nextEvent.getMessage().getMessage()[1] & 0xFF);
					midiFileName = "on_" + removeFileExtension(midiFile);
            		m2t = new MIDItoText();
            		m2t.createFile(noteNomber, tick, midiFileName);	//note���e�L�X�g�ɏ�������

				} else if (changeTemp(nextEvent)) {
                	String a = (Integer.toHexString((int)nextEvent.getMessage().getMessage()[3] & 0xFF));
                	String b = (Integer.toHexString((int)nextEvent.getMessage().getMessage()[4] & 0xFF));
                	String c = (Integer.toHexString((int)nextEvent.getMessage().getMessage()[5] & 0xFF));
                	if (a.length() == 1) a = ("0"+a);
                	if (b.length() == 1) b = ("0"+b);
                	if (c.length() == 1) c = ("0"+c);
                	String whole = a+b+c;
                	int newTempo = Integer.parseInt(whole,16);
                	double newTime = (currentTempo/seq.getResolution())*(tick-tickOfTempoChange);
                	msb4 += (newTime/1000);
                	tickOfTempoChange = tick;
                	currentTempo = newTempo;
				}
			}
		}

		System.out.println("[ OnOff_EVENT ]�f�B���N�g���Ɉȉ��̃t�@�C�����쐬���܂�");
    	System.out.println(">>[ on_"+ removeFileExtension(midiFile) +".txt ]��NoteOn��Event��Tick���������݂܂���");
    	System.out.println(">>[ off_"+ removeFileExtension(midiFile) +".txt ]��NoteOff��Event��Tick���������݂܂���");
	}

    public static MidiEvent getNextEvent() {
        ArrayList<MidiEvent> nextEvent = new ArrayList<MidiEvent>();
        ArrayList<Integer> trackOfNextEvent = new ArrayList<Integer>();
        for (int track = 0; track < seq.getTracks().length; track ++) {
            if (seq.getTracks()[track].size()-1 > (nextMessageOf.get(track))) {
                nextEvent.add(seq.getTracks()[track].get(nextMessageOf.get(track)));
                trackOfNextEvent.add(track);
            }
        }
        if (nextEvent.size() == 0) return null;
        int closestMessage = 0;
        int smallestTick = (int)nextEvent.get(0).getTick();
        for (int trialMessage = 1; trialMessage < nextEvent.size(); trialMessage ++) {
            if ((int)nextEvent.get(trialMessage).getTick() < smallestTick) {
                smallestTick = (int)nextEvent.get(trialMessage).getTick();
                closestMessage = trialMessage;
            }
        }

        currentTrack = trackOfNextEvent.get(closestMessage);
        nextMessageOf.set(currentTrack,(nextMessageOf.get(currentTrack)+1));
        return nextEvent.get(closestMessage);
    }

    public static boolean noteIsOff(MidiEvent event) {
        if (Integer.toString((int)event.getMessage().getStatus(), 16).toUpperCase().charAt(0) == '8' ||
         (noteIsOn(event) && event.getMessage().getLength() >= 3 && ((int)event.getMessage().getMessage()[2] & 0xFF) == 0)) return true;
        return false;
    }

    public static boolean noteIsOn(MidiEvent event) {
        if (Integer.toString(event.getMessage().getStatus(), 16).toUpperCase().charAt(0) == '9') return true;
        return false;
    }

    public static boolean changeTemp(MidiEvent event) {
        if ((int)Integer.valueOf((""+Integer.toString((int)event.getMessage().getStatus(), 16).toUpperCase().charAt(0)), 16) == 15
         && (int)Integer.valueOf((""+((String)(Integer.toString((int)event.getMessage().getStatus(), 16).toUpperCase())).charAt(1)), 16) == 15
         && Integer.toString((int)event.getMessage().getMessage()[1],16).toUpperCase().length() == 2
         && Integer.toString((int)event.getMessage().getMessage()[1],16).toUpperCase().equals("51")
         && Integer.toString((int)event.getMessage().getMessage()[2],16).toUpperCase().equals("3")) return true;
        return false;
    }


	//�e�L�X�g�t�@�C���̍폜
	public void deleteFile(String fname){
		// onEvent�̃t�@�C�����폜
		File file_on = new File("./OnOff_EVENT/on_" + fname + ".txt");
		if (file_on.exists()){	//�����̃t�@�C�������݂���ꍇ�A�t�@�C�����폜����
			file_on.delete();
		}

		//offEvent�̃t�@�C�����폜
		File file_off = new File("./OnOff_EVENT/off_" + fname + ".txt");
		if (file_off.exists()){	//�����̃t�@�C�������݂���ꍇ�A�t�@�C�����폜����
			file_off.delete();
		}
	}


	//�e�L�X�g�t�@�C���̐���
	public void createFile(int noteNo, int tick, String fileName){
		try{
			File file = new File("./OnOff_EVENT/" + fileName +".txt");
			//System.out.println(fileName + ".txt���쐬���܂�")

			file.createNewFile();

			if (checkBeforeWritefile(file)){
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

				pw.println(noteNo);
				pw.println(tick);

				pw.close();
			}else{
				System.out.println("�t�@�C���ɏ������߂܂���");
			}
		}catch(IOException e){
			System.out.println(e);
		}
	}

	private static boolean checkBeforeWritefile(File file){
		if (file.exists()){
			if (file.isFile() && file.canWrite()){
				return true;
			}
		}
		return false;
	}

}
