package indexingLucene;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import classes.Path;

public class MyIndexWriter {
	protected File dir;
	private Directory directory;
	private IndexWriter ixwriter;
	private FieldType type;
	
	public MyIndexWriter() throws IOException{
		//Need to delete all files in index folder first
		String folderPath = Path.IndexDir.substring(0, Path.IndexDir.length()-1);
		DeleteIndex di = new DeleteIndex();
		di.deleteAllFile(folderPath);
		
		
		directory = FSDirectory.open(Paths.get(Path.IndexDir));
		//directory = FSDirectory.open(Paths.get(classes.Path.IndexDir));
		IndexWriterConfig indexConfig=new IndexWriterConfig(new WhitespaceAnalyzer());
		indexConfig.setMaxBufferedDocs(10000);
		ixwriter = new IndexWriter( directory, indexConfig);
		type = new FieldType();
		type.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
		type.setStored(false);
		type.setStoreTermVectors(true);
	}
	
	public void index( String docno, String content) throws IOException{
		Document doc = new Document();
		doc.add(new StoredField("DOCNO", docno));		
		doc.add(new Field("CONTENT", content, type));
		ixwriter.addDocument(doc);
	}
	
	public void close() throws IOException{
		ixwriter.close();
		directory.close();
	}
}
