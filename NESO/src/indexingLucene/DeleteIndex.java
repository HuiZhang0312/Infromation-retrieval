package indexingLucene;

import java.io.File;

public class DeleteIndex {
	
	public static void main(String[] args){
		DeleteIndex di = new DeleteIndex();
		di.deleteAllFile("E:/Workspaces/MyEclipse 2016 CI/.metadata/.me_tcat7/webapps/IR_Model_Web/WEB-INF/classes/data/index");
	}
	
	public void deleteFolder(String folderPath){
		deleteAllFile(folderPath);
		String filePath = folderPath;
		filePath = filePath.toString();
		File myFilePath = new File(filePath);
		myFilePath.delete();	    
	}
	
	public boolean deleteAllFile(String path){
		boolean flag = false;
	       File file = new File(path);
	       if (!file.exists()) {
	         return flag;
	       }
	       if (!file.isDirectory()) {
	         return flag;
	       }
	       String[] tempList = file.list();
	       File temp = null;
	       for (int i = 0; i < tempList.length; i++) {
	          if (path.endsWith(File.separator)) {
	             temp = new File(path + tempList[i]);
	          } else {
	              temp = new File(path + File.separator + tempList[i]);
	          }
	          if (temp.isFile()) {
	             temp.delete();
	          }
	          if (temp.isDirectory()) {
	             deleteAllFile(path + "/" + tempList[i]);
	             deleteFolder(path + "/" + tempList[i]);
	             flag = true;
	          }
	       }
	       return flag;
	     }

}
