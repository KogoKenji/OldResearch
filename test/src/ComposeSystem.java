import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;


public class ComposeSystem extends JFrame implements ActionListener{

	/**
	 * @param args
	 */
	JLabel label;
	JTextField text;
	JProgressBar bar;
	JPanel free;

	private String[] columnNames2 = {"Event", "Gate", "Step"};

	DefaultListModel listModel;
	DefaultTableModel tableModel2;
	JList list;	// 挿入されたMIDIファイル名を表示するリスト
	JTable table2;	// 作曲に使用する音情報を表示するテーブル

	ComposeSystem cs;
	public static void main(String[] args){
		File newdir = new File("USE_MIDI_LIST");
		newdir.mkdir();	// USE_MIDI_LISTディレクトリの生成
		System.out.println("made [USE_MIDI_LIST]");

		newdir = new File("EVENT");
		newdir.mkdir();	// EVENTディレクトリの生成
		System.out.println("made [EVENT]");

		newdir = new File("STEP");
		newdir.mkdir();	// STEPディレクトリの生成
		System.out.println("made [STEP]");

		newdir = new File("OnOff_EVENT");
		newdir.mkdir();	// OnOff_EVENTディレクトリの生成
		System.out.println("made [OnOff_EVENT]");

		ComposeSystem frame = new ComposeSystem();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(740, 600);
		frame.setMinimumSize(new Dimension(740, 600));
		frame.setLocationRelativeTo(null);
		frame.setTitle("TrigramComposeSystem");
		frame.setVisible(true);

	}


	JButton button_insert;
	JButton button_compose;
	JButton button_remove;
	JButton play;

	public ComposeSystem(){
		free = new JPanel();
		free.setLayout(null);

		button_insert = new JButton("ファイル挿入");
		button_insert.setBounds(20, 10, 120, 30);
		button_insert.addActionListener(this);

		button_compose = new JButton("作曲開始");
		button_compose.setBounds(300, 150, 120, 30);
		button_compose.addActionListener(this);

		button_remove = new JButton("削除");
		button_remove.setBounds(150, 10, 120, 30);
		button_remove.addActionListener(this);

		free.add(button_insert);
		free.add(button_compose);
		free.add(button_remove);


		JLabel label2 = new JLabel("作曲に使用する音列情報");
		label2.setBounds(510, 30, 200, 20);
		free.add(label2);

		label = new JLabel();

		JPanel labelPanel = new JPanel();
		labelPanel.add(label);

		listModel = new DefaultListModel();
		tableModel2 = new DefaultTableModel(columnNames2, 0);
		list = new JList(listModel);
		table2 = new JTable(tableModel2);


		LineBorder inborder = new LineBorder(Color.white, 0);
		TitledBorder border = new TitledBorder(inborder, "ファイル名", TitledBorder.CENTER, TitledBorder.TOP);
		list.setBorder(border);

	    play = new JButton("再生");
		play.setBounds(450, 450, 80, 30);
		play.addActionListener(this);

		free.add(play);

	    text = new JTextField("50",2);
	    text.setBounds(380,120, 40, 20);
	    free.add(text);

	    JLabel label4 = new JLabel("フレーズ数：");
	    label4.setBounds(300, 120, 100,20);
	    free.add(label4);


		// 挿入されたMIDIファイル名を表示するリスト
		JScrollPane sp = new JScrollPane(list);
		sp.setBounds(20, 50, 250, 400);

		// 作曲に使用する音情報を表示するテーブル
		JScrollPane sp2 = new JScrollPane(table2);
		sp2.setBounds(450, 50, 250, 400);

		free.add(sp);
		free.add(sp2);



		getContentPane().add(free, BorderLayout.CENTER);
		getContentPane().add(label, BorderLayout.PAGE_END);

		addWindowListener(new WinAdapter());
		//setVisible(true);
	}

	int insertFlg = 0;
	ArrayList<Integer> SoundList = new ArrayList<Integer>();
	ArrayList<String> UseFileName = new ArrayList<String>();
	public void actionPerformed(ActionEvent e){
		if(e.getSource().equals(button_insert)){	// ファイル挿入ボタンが押されたら
			File dir = new File("./MIDI_FILES");
			JFileChooser filechooser = new JFileChooser(dir);

			int selected = filechooser.showOpenDialog(this);
			if (selected == JFileChooser.APPROVE_OPTION){
				File file = filechooser.getSelectedFile();
				label.setText(file.getName()+"を追加しました");

				String FileName = file.getName();	// 選択されたファイル名をFileNameに代入
				System.out.println("Input >> "+ file.getName());

				String From = "./MIDI_FILES/" + file.getName();	// ファイルのコピー元
				String To = "./USE_MIDI_LIST/" + file.getName();	// ファイルのコピー先

				cs = new ComposeSystem();
				cs.FileCopy(From, To);

				listModel.addElement(FileName);
				UseFileName.add(FileName);
				insertFlg++;

			}else if (selected == JFileChooser.CANCEL_OPTION){
				label.setText("キャンセルされました");
			}else if (selected == JFileChooser.ERROR_OPTION){
				label.setText("エラー又は取消しがありました");
			}

		}else if (e.getSource().equals(button_remove)) {	// 削除ボタンが押されたら
			// 選択行を削除
			if (!list.isSelectionEmpty()) {
				System.out.println("Remove >> "+ String.valueOf(list.getSelectedValue()));
				// USE_MIDI_LISTディレクトリからファイルを削除する
				cs.deleteFile(list.getSelectedValue());
				UseFileName.remove(UseFileName.indexOf(list.getSelectedValue()));

				// 選択されたファイル名をリストから削除
				label.setText(list.getSelectedValue()+"を削除しました");
				listModel.remove(list.getSelectedIndex());



				insertFlg--;

			} else {
				JOptionPane.showMessageDialog(null, "MIDIファイルを選択してください","警告", JOptionPane.ERROR_MESSAGE);
			}


		}else if (e.getSource().equals(button_compose)) {	// 作曲開始ボタンが押されたら
			SoundList.clear();	// SoundListの初期化
			while(true){	// テーブルの要素を全削除
				if(tableModel2.getRowCount() == 0){
					break;
				}
				tableModel2.removeRow(0);

			}


			if(insertFlg == 0){
				JOptionPane.showMessageDialog(null, "MIDIファイルを挿入してください","警告", JOptionPane.ERROR_MESSAGE);

			}else if(text.getText() == ""){
				JOptionPane.showMessageDialog(null, "フレーズ数を指定してください","警告", JOptionPane.ERROR_MESSAGE);

			}else if(Integer.parseInt(text.getText()) == 0){
				JOptionPane.showMessageDialog(null, "フレーズ数は1以上にしてください","警告", JOptionPane.ERROR_MESSAGE);

			}else if(Integer.parseInt(text.getText()) != 0){
				label.setText(text.getText()+"フレーズの作曲中です");
				System.out.println("フレーズ数>>"+text.getText());

				MIDItoText m2t = new MIDItoText();
				m2t.Midi2Text_main(insertFlg);

				MakeMusic mm = new MakeMusic();
				mm.MakeMusic_main(SoundList, insertFlg, Integer.parseInt(text.getText()), UseFileName);

				for(int a=0; a<SoundList.size()/3; a++){
					String[] SoundNum = {String.valueOf(SoundList.get(3*a)),String.valueOf(SoundList.get(3*a+1)),String.valueOf(SoundList.get(3*a+2))};
					tableModel2.addRow(SoundNum);
				}
				label.setText("作曲が終わりました");

			}

		}else if (e.getSource().equals(play)){

			if(insertFlg <= 0){
				JOptionPane.showMessageDialog(null, "作曲が完了していません","警告", JOptionPane.ERROR_MESSAGE);
			}else{
				// 完成した曲の生成
				MakeMIDI mMidi = new MakeMIDI();
				try {
					mMidi.toMIDI(SoundList);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				label.setText("生成された曲を sample.mid に書き出しました");
			}
		}
	}


//	 Windowアダプタクラス
	class WinAdapter extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.out.println("Window Closing");
			AllFileDelete("./USE_MIDI_LIST");
			DeleteDir("USE_MIDI_LIST");
			dispose();
		}

		public void windowClosed(WindowEvent e) {
			System.exit(0);
		}
	}


	public void FileCopy(String from, String to){
		BufferedOutputStream out = null;
		BufferedInputStream in = null;

		try {
			in = new BufferedInputStream(new FileInputStream(from));
			out = new BufferedOutputStream(new FileOutputStream(to));

			byte[] buff = new byte[4096];
			int len = 0;
			while ((len = in.read(buff, 0, buff.length)) >= 0) {
				out.write(buff, 0, len);
			}

		}catch(IOException e){
			System.out.println(e);

		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
	}

	// ディレクトリ内のファイルを全て削除
	public void AllFileDelete(String address){
		File dir = new File(address);
		File[] files = dir.listFiles();

		// ディレクトリ内のファイル名をすぺて取得
		System.out.println();
		int n = 0;	// ディレクトリ内のファイル数
		while(n < files.length) {	//pathで指定したディレクトリ内のファイル名を全て取得する
			File file = files[n];
			file.delete();
			n++;
		}
	}

	// ディレクトリの削除
	public void DeleteDir(String name){
		File dir = new File(name);
		dir.delete();
	}


	// ファイルの削除
	public void deleteFile(Object fname){
		File file = new File("./USE_MIDI_LIST/" + fname);
		if (file.exists()){	//同名のファイルが存在する場合、ファイルを削除する
			file.delete();

		}else{
			System.out.println("["+ fname + "] is nothing");
		}
	}

}