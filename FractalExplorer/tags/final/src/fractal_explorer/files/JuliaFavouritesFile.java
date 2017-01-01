package fractal_explorer.files;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.ComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import fractal_explorer.util.ComplexNumber;

public class JuliaFavouritesFile {
	private static final String FILE_EXTENSION_FILTER = "julia";
	private static final String FILE_EXTENSION = "."+FILE_EXTENSION_FILTER;
	
	public static void saveFavourites(ComboBoxModel<ComplexNumber> comboBoxModel){
		String fileName = getFilePathFromFileChooser("saveJulia");

		//make sure we returned a value from the chooser
		if(fileName != null){
			fileName+=FILE_EXTENSION;
			//try-with-resources statement so no .close() needed
			try(FileOutputStream fileOut = new FileOutputStream(fileName);
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
			){
				out.writeObject(comboBoxModel);
				JOptionPane.showMessageDialog(null, "Successfully saved Julia Sets", "Julia Sets Saved", JOptionPane.INFORMATION_MESSAGE);
			}catch(IOException ioe){
				JOptionPane.showMessageDialog(null, "Unable to save file: "+fileName+"\n\n Error: "+ioe.getMessage(), "Fractal Explorer has encountered a problem", JOptionPane.INFORMATION_MESSAGE);
			
			}
		}
	}

	private static String getFilePathFromFileChooser(String type) {
		JFileChooser fc = new JFileChooser();
		fc.setMultiSelectionEnabled(false);
		

		FileFilter ff = new FileNameExtensionFilter("*"+FILE_EXTENSION, FILE_EXTENSION_FILTER);
		fc.setFileFilter(ff);

		//differences between saving and loading
		if(type.equals("saveJulia")){
			fc.setDialogTitle("Saving Julia Sets");
			fc.setApproveButtonText("Save");
			fc.setApproveButtonToolTipText("Save Julia Sets");
			
		}else if(type.equals("loadJulia")){
			fc.setDialogTitle("Load Julia Sets");
			fc.setApproveButtonText("Open");
			fc.setApproveButtonToolTipText("Load Julia Sets");
			
		}else{
			System.err.println("invalid JFileChooser type: "+type);
		}
		
		if( fc.showOpenDialog(null) == 	JFileChooser.APPROVE_OPTION){
			return fc.getSelectedFile().getAbsolutePath();
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	public static ComboBoxModel<ComplexNumber> loadFavourites() {
		String fileName = getFilePathFromFileChooser("loadJulia");

		if(fileName!=null){
			ComboBoxModel<ComplexNumber> favourites = null;

			try(FileInputStream fileIn = new FileInputStream(fileName);
				ObjectInputStream in = new ObjectInputStream(fileIn);
			){

				favourites = (ComboBoxModel<ComplexNumber>) in.readObject();

			} catch(IOException ioe){
				Boolean correctFileExtension = fileName.toLowerCase().endsWith(FILE_EXTENSION);
				JOptionPane.showMessageDialog(null, "Unable to open file: "+fileName+"\n\n Error: "+ioe.getLocalizedMessage()
					+ (correctFileExtension?"":"\n Wrong file Extension"), "Fractal Explorer has encountered a problem", JOptionPane.INFORMATION_MESSAGE);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			return favourites;

		}

		return null;
	}

}
