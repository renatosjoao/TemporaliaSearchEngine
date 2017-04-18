package de.l3s.similarities;
/**
 * @author Renato Stoffalette Joao
 * @mail renatosjoao@gmail.com
 * @version 1.0
 * @date 02.2017
 * 
 */
import org.apache.lucene.search.similarities.LMJelinekMercerSimilarity;

public class MyLMJelinekMercerSimilarity extends LMJelinekMercerSimilarity{

	public MyLMJelinekMercerSimilarity(float lambda) {
		super(lambda);
	}

}
