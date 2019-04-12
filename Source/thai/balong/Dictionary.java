package thai.balong;

import java.awt.EventQueue;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import javax.swing.UIManager;
import javax.swing.UIManager.*;

import javax.swing.JTextArea;
import javax.swing.JButton;


public class Dictionary {

	private JFrame frame;
	
	//private ArrayList<record> dictionary = new ArrayList<record>();
	private ArrayList<String> ArrMeaning= new ArrayList<String>();
	// create defaultListModel
	private DefaultListModel<String> model= new DefaultListModel<>();
	
	private ArrayList<String> ArrMeaningFavori= new ArrayList<String>();
	// create defaultListModel
	private DefaultListModel<String> modelFavori= new DefaultListModel<>();
	
	private JList<String> list;
	private JTextArea textArea;
	private JButton btnAnhViet;
	private JButton btnVietAnh;
	private JButton btnTuYeuThich;
	Boolean isAnhViet = true;
	Boolean isFavorite = false;
	private int numOfFavList = 0;
	private JButton btnAddFavorite;
	private JButton btnXoa;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					try {
					    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
					        if ("Nimbus".equals(info.getName())) {
					            UIManager.setLookAndFeel(info.getClassName());
					            break;
					        }
					    }
					} catch (Exception e) {
					    // If Nimbus is not available, you can set the GUI to another look and feel.
					}
					Dictionary window = new Dictionary();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Dictionary() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.setResizable(false);
		frame.getContentPane().setBackground(new Color(248, 248, 255));
		frame.getContentPane().setFont(new Font("Times New Roman", Font.PLAIN, 15));
		frame.setBounds(250, 60, 772, 559);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Dictionary");
		frame.getContentPane().setLayout(null);
		
		
		
		JLabel lblNewLabel = new JLabel("Danh s\u00E1ch m\u1EE5c t\u1EEB");
		lblNewLabel.setForeground(Color.BLUE);
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblNewLabel.setSize(new Dimension(177, 30));
		lblNewLabel.setLocation(10, 7);
		frame.getContentPane().add(lblNewLabel);
		
		list = new JList<String>();
		list.setFont(new Font("Tahoma", Font.PLAIN, 13));
		list.setBorder(new LineBorder(new Color(0, 0, 0)));
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		list.setSize(new Dimension(210, 334));
		list.setLocation(0, 2);
		list.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				// TODO Auto-generated method stub
				int iin = list.getSelectedIndex();
				//textArea.setText(dictionary.get(iin).getMeaning());
				
				if (iin >= 0) { 
					setEnableBtnFavTrue();
					if  (isFavorite == false) textArea.setText(ArrMeaning.get(iin).toString());
					else textArea.setText(ArrMeaningFavori.get(iin).toString());
				}
				System.out.println("Selected index is: "+iin);
			}
		});
		frame.getContentPane().add(list);
		
		JScrollPane scrollPane = new JScrollPane(list);
		//list.setLocation(10, 43);
		//scrollPane.setSize(new Dimension(210, 334));
		scrollPane.setBounds(10, 71, 249, 438);
		frame.getContentPane().add(scrollPane);
		
		
		
		textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
		textArea.setWrapStyleWord(true);
		textArea.setRows(20);
		textArea.setEditable(false);
		textArea.setBounds(117, -2, 489, 468);
		frame.getContentPane().add(textArea);
		
		JScrollPane scrPmean = new JScrollPane(textArea);
		scrPmean.setBounds(280, 71, 455, 438);
		frame.getContentPane().add(scrPmean);
		
		btnAddFavorite = new JButton("Add Favorite");
		btnAddFavorite.setBounds(615, 37, 120, 23);
		btnAddFavorite.setEnabled(false);
		btnAddFavorite.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
				int numF=getNumOfFavList();
				//số lượng từ yêu thích sẽ không vượt quá 20
				if (numF >=20) 
				{
					JOptionPane.showMessageDialog(null, "Số lượng từ yêu thích đã vượt quá giới hạn !!!\n(tối đa 20 từ)", "Thông báo", JOptionPane.WARNING_MESSAGE);
				}
				else
				{
					//Nếu từ đã có trong danh sách favorite thì không thêm nữa
					//so sánh xem từ có tồn tại trong listFavorite hay chưa, nếu chưa thì thêm, rồi thì bỏ qua
					addListFavorite();
					saveListFavorite();
					System.out.println("Thêm từ yêu thích !!!");
				}	
			}
		});
		frame.getContentPane().add(btnAddFavorite);
		
		btnAnhViet = new JButton("Anh - Việt");
		btnAnhViet.setBounds(10, 37, 79, 23);
		btnAnhViet.setEnabled(false);
		btnAnhViet.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String av_data_path = System.getProperty("user.dir")+"\\data\\Anh_Viet.xml";
				if (isAnhViet == false) getDataForListWord(av_data_path);
				setDataList();
				enableAnhViet();
				setEnableXoa(false);
				isAnhViet = true;
				isFavorite = false;
			}
		});
		frame.getContentPane().add(btnAnhViet);
		
		btnVietAnh = new JButton("Việt - Anh");
		btnVietAnh.setBounds(95, 37, 79, 23);
		btnVietAnh.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String av_data_path = System.getProperty("user.dir")+"\\data\\Viet_Anh.xml";
				if (isAnhViet == true) getDataForListWord(av_data_path);
				setDataList();
				enableVietAnh();
				setEnableXoa(false);
				isAnhViet = false;
				isFavorite = false;
			}
		});
		frame.getContentPane().add(btnVietAnh);
		
		btnTuYeuThich = new JButton("Từ yêu thích");
		btnTuYeuThich.setBounds(180, 37, 79, 23);
		btnTuYeuThich.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub	
				setDataListFavori();
				enableTuYeuThich();
				setEnableXoa(true);
				isFavorite = true;
				
			}
		});
		frame.getContentPane().add(btnTuYeuThich);
		
		btnXoa = new JButton("Xóa");
		btnXoa.setBounds(552, 37, 51, 23);
		btnXoa.setEnabled(false);
		btnXoa.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				xoaTu();
			}
		});
		frame.getContentPane().add(btnXoa);
		
		
		//lấy dữ liệu cho List
		String av_data_path = System.getProperty("user.dir")+"\\data\\Anh_Viet.xml";
		getDataForListWord(av_data_path);
		setDataList();
		//lấy dữ liệu cho danh sách từ yêu thích
		getListFavori();
		
		
		
	}
	public void setDataListFavori()
	{
		list.clearSelection();
		list.setModel(this.modelFavori);
		
		textArea.setText("");
	}
	public void setDataList()
	{
		list.clearSelection();
		list.setModel(this.model);
		
		textArea.setText("");
	}
	public void enableAnhViet()
	{
		btnVietAnh.setEnabled(true);
		btnAnhViet.setEnabled(false);
		btnTuYeuThich.setEnabled(true);
		btnAddFavorite.setEnabled(false);
	}
	public void enableVietAnh()
	{
		btnVietAnh.setEnabled(false);
		btnAnhViet.setEnabled(true);
		btnTuYeuThich.setEnabled(true);
		btnAddFavorite.setEnabled(false);
		
	}
	public void enableTuYeuThich()
	{
		btnVietAnh.setEnabled(true);
		btnAnhViet.setEnabled(true);
		btnTuYeuThich.setEnabled(false);
		btnAddFavorite.setEnabled(false);
	}
	//lấy data từ fileName (file xml) lên để hiển thị vô list
	private void getDataForListWord(String fileName)
	{
		try {		
			this.model = new DefaultListModel<String>();
			this.ArrMeaning = new ArrayList<String>(); 
			XMLInputFactory inputFac = XMLInputFactory.newInstance();
			InputStream in = new FileInputStream(fileName);
			XMLStreamReader streamReader = inputFac.createXMLStreamReader(in);
			streamReader.nextTag(); //bỏ qua tag Dictionary
			streamReader.nextTag(); //bỏ qua tag Recored
			int numWord = 0;
			while (streamReader.hasNext()) //nếu có phần tử tiếp theo thì vô code block
			{
				if (streamReader.isStartElement()) //nếu con trỏ đang trỏ đến một thẻ bắt đầu
				{
					switch (streamReader.getLocalName())
					{
					case "word": {
						this.model.addElement(streamReader.getElementText());
						}break;
					case "meaning": {
						this.ArrMeaning.add(streamReader.getElementText());
						}break;
					
					case "record":{
						numWord++;
						}
					}
				}
				streamReader.next();
				
			}
			System.out.println("Num of word in "+fileName+": "+ numWord);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public void getListFavori() {
		try {	
		this.modelFavori = new DefaultListModel<String>();
		this.ArrMeaningFavori = new ArrayList<String>(); 
		XMLInputFactory inputFac = XMLInputFactory.newInstance();
		String av_data_path = System.getProperty("user.dir")+"\\data\\List_Favorite.xml";
		InputStream in = new FileInputStream(av_data_path);
		XMLStreamReader streamReader = inputFac.createXMLStreamReader(in);
		streamReader.nextTag(); //bỏ qua tag Dictionary
		streamReader.nextTag(); //bỏ qua tag Recored
		int numWord = 0;
		while (streamReader.hasNext()) //nếu có phần tử tiếp theo thì vô code block
		{
			if (streamReader.isStartElement()) //nếu con trỏ đang trỏ đến một thẻ bắt đầu
			{
				switch (streamReader.getLocalName())
				{
				case "word": {
					this.modelFavori.addElement(streamReader.getElementText());
					}break;
				case "meaning": {
					this.ArrMeaningFavori.add(streamReader.getElementText());
					}break;
				
				case "record":{
					numWord++;
					}
				}
			}
			streamReader.next();
			
		}
		System.out.println("Num of favorite word in xml file: "+ numWord);
		this.numOfFavList = numWord;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
	}
	public void writeRecord(XMLStreamWriter streamWriter, String word, String mean) throws XMLStreamException
	{
		streamWriter.writeCharacters("  ");
		streamWriter.writeStartElement("record"); 	//start record
		streamWriter.writeCharacters("\n    ");
		streamWriter.writeStartElement("word");  	//start word
		streamWriter.writeCharacters(word);
		streamWriter.writeEndElement(); 			//end word
		streamWriter.writeCharacters("\n    ");
		streamWriter.writeStartElement("meaning"); //start meaning
		streamWriter.writeCharacters(mean);
		streamWriter.writeEndElement(); 		   //end meaning
		streamWriter.writeCharacters("\n  ");
		streamWriter.writeEndElement(); 		   //end record
			
		}
	public int getNumOfFavList()
	{
		this.numOfFavList = this.modelFavori.getSize();
		return this.numOfFavList;
	}
	public void saveData(String fileOut, ArrayList<String> arrMean, DefaultListModel<String> listWord)
	{
		//lưu danh sách các từ từ trong listWord cùng với nghĩa tương ứng trong arrMean vào file fileOut
		try {
		XMLOutputFactory outputFac = XMLOutputFactory.newInstance();
		OutputStream out = new FileOutputStream(fileOut);
		XMLStreamWriter streamWriter = null;
		try {
			streamWriter = outputFac.createXMLStreamWriter(out);
			streamWriter.writeStartDocument("utf-8", "1.0");
			streamWriter.writeCharacters("\n");
			streamWriter.writeStartElement("Dictionary");
			streamWriter.writeCharacters("\n");
		
			//Ghi các record
			for (int i=0;i<arrMean.size();i++)
			{
				writeRecord(streamWriter, listWord.get(i).toString(), arrMean.get(i).toString());
			}
			streamWriter.writeCharacters("\n");
			streamWriter.writeEndElement();
			streamWriter.writeEndDocument();
			} 
		finally {
				if (streamWriter != null)
					streamWriter.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void addListFavorite()
	{
		if (isFavorite == false) //nếu đang chọn xem danh sách Favorite thì bỏ qua luôn (không thể thêm từ trong ds fav vào ds fav được)
		{
			//lấy dữ liệu ra
			int in = list.getSelectedIndex();
			String wordFavorite = this.model.get(in).toString();
			String meanFavorite = this.ArrMeaning.get(in).toString();
			
			
			//kiểm tra tồn tại
			for (int i=0;i<this.modelFavori.getSize();i++)
			{
				if (modelFavori.get(i).toString().equals(wordFavorite)==true)
				{ System.out.println("Từ đã có trong DS yêu thích !!!");
					return;
				}
			}
			//Collections.sort(this.modelFavori);
			
			this.modelFavori.addElement(wordFavorite);
			this.ArrMeaningFavori.add(meanFavorite);
		}
	}
	public void setEnableBtnFavTrue()
	{
		this.btnAddFavorite.setEnabled(true);
	}
	public void setEnableBtnFavFalse()
	{
		this.btnAddFavorite.setEnabled(false);
	}
	public void setEnableXoa(Boolean b)
	{
		this.btnXoa.setEnabled(b);
	}
	public void saveListFavorite()
	{
		String av_data_path = System.getProperty("user.dir")+"\\data\\List_Favorite.xml";
		saveData(av_data_path, this.ArrMeaningFavori, this.modelFavori);
		return;
	}
	public void xoaTu()
	{
		int index = list.getSelectedIndex();
		if (index>=0)
		{
			this.ArrMeaningFavori.remove(index);
			this.modelFavori.remove(index);
			this.list.setModel(this.modelFavori);
			saveListFavorite();
		}
	}
}
